package com.assessment.authentication.upstream;

import com.assessment.authentication.exception.ApiException;
import com.assessment.authentication.service.dto.Account;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestTemplate;

@Component
public class AccountsApi {

    @Autowired
    RestTemplate restTemplate;

    @Value("${accounts.api.endpoint}")
    private String ACCOUNTS_ENDPOINT;

    /**
     * Invokes the upstream API to fetch the Account details
     *
     * @param accountNumber
     * @return Account
     */
    public Account fetchAccountDetails(String accountNumber) {
        Account account = null;
        try {
            account = restTemplate.getForObject(
                    ACCOUNTS_ENDPOINT + accountNumber, Account.class);
        } catch (Exception e) {
            if (e instanceof ResourceAccessException) {
                throw new ApiException.UpstreamApiException("Accounts API service is unavailable. Please start the Accounts API and try again");
            } else {
                throw new ApiException.AccountApiException("No Account available for the given Account Number");
            }
        }
        return account;
    }

}
