package authentication.upstream;

import com.assessment.authentication.exception.ApiException;
import com.assessment.authentication.service.dto.Account;
import com.assessment.authentication.upstream.AccountsApi;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

@RunWith(MockitoJUnitRunner.class)
public class AccountsApiTest {

    @Mock
    RestTemplate restTemplate;

    @InjectMocks
    AccountsApi accountsApi;

    private MockMvc mockMvc;

    @Before
    public void setup() {
        this.mockMvc = MockMvcBuilders.standaloneSetup(accountsApi).build();
    }

    @Test
    public void testFetchAccountDetails() {
        given(restTemplate.getForObject(any(String.class), any())).willReturn(getDummyAccount());
        final Account account = accountsApi.fetchAccountDetails("user");

        assertThat(account).isNotNull();
        assertThat(account.getIban()).isEqualTo("NL24INGB7785344909");
        assertThat(account.getOwnerId()).isEqualTo("c3629d83-95f7-4966-9b67-76b13fe2cd5a");
    }

    @Test
    public void testFetchAccountDetailsThrowsIOException() {
        given(restTemplate.getForObject(any(String.class), any())).willThrow(new ResourceAccessException("I/O Eception"));
        try {
            final Account account = accountsApi.fetchAccountDetails("user");
        } catch (ApiException.UpstreamApiException e) {
            assertThat(e).isNotNull();
            assertThat(e.getMessage()).isEqualTo("Accounts API service is unavailable. Please start the Accounts API and try again");
        }
    }

    @Test
    public void testFetchAccountDetailsThrowsNotFoundException() {
        given(restTemplate.getForObject(any(String.class), any())).willThrow(new RestClientException("No Account Found"));
        try {
            final Account account = accountsApi.fetchAccountDetails("user");
        } catch (ApiException.AccountApiException e) {
            assertThat(e).isNotNull();
            assertThat(e.getMessage()).isEqualTo("No Account available for the given Account Number");
        }
    }

    private final Account getDummyAccount() {
        return new Account("NL24INGB7785344909", "c3629d83-95f7-4966-9b67-76b13fe2cd5a");
    }

}
