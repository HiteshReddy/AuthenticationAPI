package com.assessment.authentication.presentation.api;

import com.assessment.authentication.exception.ApiException;
import com.assessment.authentication.presentation.model.Credentials;
import com.assessment.authentication.presentation.model.TokenResponse;
import com.assessment.authentication.service.AuthenticationService;
import com.assessment.authentication.service.TokenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

/**
 * API to authenticate user credentials
 */
@RestController
@RequestMapping("/api")
public class VerifyCredentialsEndpoint {

    @Autowired
    AuthenticationService authenticationService;

    @Autowired
    TokenService tokenService;

    /**
     * validates the username and password, it matches returns the JWT Token
     *
     * @param credentials
     * @return ResponseEntity
     */
    @PostMapping(value = "/authenticate", consumes = APPLICATION_JSON_VALUE, produces = APPLICATION_JSON_VALUE)
    public ResponseEntity verifyCredentials(@RequestBody Credentials credentials) {
        // verify if account is unique
        boolean userNameExists = authenticationService.verifyIfUserNameExists(credentials.getUserName());
        if (!userNameExists) {
            throw new ApiException.AuthenticationException("Authentication Failed. Reason: UserName doesn't exist");
        }
        // verify if user is blocked
        boolean isUserBlocked = authenticationService.verifyIfUserBlocked(credentials.getUserName());
        if (isUserBlocked) {
            throw new ApiException.AuthenticationException("Authentication Failed. Reason: UserName blocked due to incorrect login attempts");
        }
        //  verify if credentials are valid
        boolean isValidCredential = authenticationService.verifyIfValidCredentials(credentials.getUserName(), credentials.getPassword());
        if (!isValidCredential) {
            throw new ApiException.AuthenticationException("Authentication Failed. Reason: Invalid UserName and password");
        }
        // get the JWT token
        String jwtToken = tokenService.generateJWTToken(credentials.getUserName());
        return ResponseEntity.status(HttpStatus.OK).body(TokenResponse.of(jwtToken));
    }

}
