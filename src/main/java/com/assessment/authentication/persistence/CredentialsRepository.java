package com.assessment.authentication.persistence;

import com.assessment.authentication.service.dto.Password;
import com.assessment.authentication.service.dto.UserStatus;
import org.springframework.stereotype.Repository;

import java.util.concurrent.ConcurrentHashMap;

@Repository
public class CredentialsRepository {

    private static ConcurrentHashMap<String, Password> credentials;
    private static ConcurrentHashMap<String, UserStatus> userStatus;

    public CredentialsRepository() {
        credentials = new ConcurrentHashMap<>();
        userStatus = new ConcurrentHashMap<>();
    }

    public void addUserCredentials(String userName, Password password) {
        credentials.put(userName, password);
    }

    public boolean userNameExists(String userName) {
        return credentials.containsKey(userName);
    }

    public Password getPassword(String userName) {
        return credentials.get(userName);
    }

    public UserStatus getUserStatus(String userName) {
        return userStatus.get(userName);
    }

    public void addOrUpdateStatus(String userName, UserStatus status) {
        userStatus.put(userName, status);
    }

}
