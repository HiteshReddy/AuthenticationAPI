package com.assessment.authentication.service;

import com.assessment.authentication.persistence.CredentialsRepository;
import com.assessment.authentication.service.dto.Account;
import com.assessment.authentication.service.dto.Password;
import com.assessment.authentication.service.dto.UserStatus;
import com.assessment.authentication.upstream.AccountsApi;
import com.assessment.authentication.util.DateUtil;
import com.assessment.authentication.util.PasswordUtil;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

/**
 * Authentication service to create, validate and fetch the user credentials
 */
@Getter
@Setter
@Service
public class AuthenticationService {

    @Autowired
    AccountsApi accountsApi;

    @Autowired
    CredentialsRepository credentialsRepository;

    @Value("${app.pepper}")
    String pepper;

    /**
     * create the user credentials and update the userStatus in the repository
     *
     * @param userName
     * @param password
     */
    public void create(String userName, String password) {
        Password hashed = getHashedPassword(password);
        credentialsRepository.addUserCredentials(userName, hashed);
        updateUserStatus(userName, null, true);
    }

    /**
     * verify if an account exists in the Accounts API
     *
     * @param accountNumber
     * @return boolean
     */
    public boolean verifyIfAccountExists(String accountNumber) {
        Account account = accountsApi.fetchAccountDetails(accountNumber);
        return account != null;
    }

    /**
     * verify if the username already exists in the repository
     *
     * @param userName
     * @return
     */
    public boolean verifyIfUserNameExists(String userName) {
        return credentialsRepository.userNameExists(userName);
    }

    /**
     * verify if the user is BLOCKED or NON_BLOCKED
     * If the user is BLOCKED and the last invalid attempt is more than 24 hours,
     * unblocks the user
     *
     * @param userName
     * @return boolean
     */
    public boolean verifyIfUserBlocked(String userName) {
        UserStatus userStatus = credentialsRepository.getUserStatus(userName);
        if (userStatus.getLastInvalidAccess() != null) {
            if (DateUtil.findIfDifferenceIsGreaterThan24Hours(userStatus.getLastInvalidAccess(), DateUtil.getCurrentDateAndTime())) {
                resetUserStatus(userStatus);
                return true;
            }
        }
        return userStatus != null && userStatus.getStatus() == UserStatus.Status.BLOCKED;
    }

    /**
     * maches the username and the password sent by the user
     *
     * @param userName
     * @param userPassword
     * @return boolean
     */
    public boolean verifyIfValidCredentials(String userName, String userPassword) {
        Password password = credentialsRepository.getPassword(userName);
        UserStatus userStatus = credentialsRepository.getUserStatus(userName);
        String hashedPassword = PasswordUtil.hashPassword(userPassword, password.getSalt(), pepper);
        if (hashedPassword.equals(password.getPassword())) {
            updateUserStatus(userName, userStatus, true);
            return true;
        }
        updateUserStatus(userName, userStatus, false);
        return false;
    }

    /**
     * updates the user status based on the whether it is a valid attempt or invalid attempt
     * In case of first time user creates the credentials in the system, a new default user status is created
     *
     * @param userName
     * @param userStatus
     * @param isValidCredential
     */
    private void updateUserStatus(String userName, UserStatus userStatus, boolean isValidCredential) {
        if (userStatus == null) {
            userStatus = new UserStatus();
        }
        if (isValidCredential) {
            resetUserStatus(userStatus);
        } else {
            int invalidAttempts = userStatus.getInvalidAttempts();
            // do not update the last invalid access time if the total attempts are more than 3
            // this is to unblock the user after 24 hours
            if (invalidAttempts < 3) {
                userStatus.setLastInvalidAccess(DateUtil.getCurrentDateAndTime());
            }
            userStatus.setInvalidAttempts(invalidAttempts + 1);
            userStatus.setStatus(invalidAttempts >= 2 ? UserStatus.Status.BLOCKED : UserStatus.Status.NOT_BLOCKED);
        }
        credentialsRepository.addOrUpdateStatus(userName, userStatus);
    }

    /**
     * reset the user status with default values
     *
     * @param userStatus
     */
    private void resetUserStatus(UserStatus userStatus) {
        userStatus.setLastInvalidAccess(null);
        userStatus.setInvalidAttempts(0);
        userStatus.setStatus(UserStatus.Status.NOT_BLOCKED);
    }

    /**
     * returns the hashed value of password and the salt
     *
     * @param password
     * @return Password
     */
    private Password getHashedPassword(String password) {
        String salt = PasswordUtil.generateSalt(12);
        String hashedPassword = PasswordUtil.hashPassword(password, salt, pepper);
        return new Password(hashedPassword, salt);
    }

}
