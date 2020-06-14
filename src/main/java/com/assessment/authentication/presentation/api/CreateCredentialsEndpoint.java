package com.assessment.authentication.presentation.api;

import com.assessment.authentication.presentation.model.AccountDetails;
import com.assessment.authentication.service.AuthenticationService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.function.Function;
import java.util.function.Predicate;

import static com.assessment.authentication.exception.ApiException.*;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

/**
 * API to create the credentials in the system
 */
@RestController
@Slf4j
@RequestMapping("/api")
public class CreateCredentialsEndpoint {

    @Autowired
    AuthenticationService authenticationService;

    /**
     * validates the input and creates credentials in the system
     *
     * @param account
     * @return ResponseEntity
     */
    @PostMapping(value = "/create", consumes = APPLICATION_JSON_VALUE)
    public ResponseEntity createCredentials(@RequestBody AccountDetails account) {
        // verify if account exists in the systemaz
        boolean accountExist = authenticationService.verifyIfAccountExists(account.getAccountNumber());
        if (!accountExist) {
            throw new NoAccountAvailableException("No Account found for the given Account number");
        }
        // verify if the username is not null or empty
        if (account.getUserName() == null || account.getUserName().isEmpty()) {
            throw new InvalidInputException("Invalid UserName. Please send the valid UserName");
        }
        // verify if account is unique
        boolean userNameExists = authenticationService.verifyIfUserNameExists(account.getUserName());
        if (userNameExists) {
            throw new UsernameExistsException("A UserName already exists in the system. Please try with new UserName");
        }
        // verify if the password is minimum 6 digits length
        if (account.getPassword() == null || !(isValidPassword().apply(account.getPassword()).test(6))) {
            throw new InvalidInputException("The password should have minimum length of 6");
        }
        authenticationService.create(account.getUserName(), account.getPassword());
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    /**
     * returns a Function that takes String as an input and returns the
     * Predicate(for checking the string length)
     *
     * @return Function<String, Predicate < Integer>>
     */
    private Function<String, Predicate<Integer>> isValidPassword() {
        return (password) -> n -> password.length() >= n;
    }
}
