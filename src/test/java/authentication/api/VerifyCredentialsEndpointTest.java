package authentication.api;

import com.assessment.authentication.exception.ExceptionHandler;
import com.assessment.authentication.presentation.api.VerifyCredentialsEndpoint;
import com.assessment.authentication.service.AuthenticationService;
import com.assessment.authentication.service.TokenService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static authentication.common.AuthenticationApiTestConstants.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(MockitoJUnitRunner.class)
public class VerifyCredentialsEndpointTest {

    @Mock
    AuthenticationService authenticationService;

    @Mock
    TokenService tokenService;

    @InjectMocks
    VerifyCredentialsEndpoint verifyCredentialsEndpointTest;

    private MockMvc mockMvc;

    @Before
    public void setup() {
        this.mockMvc = MockMvcBuilders.standaloneSetup(verifyCredentialsEndpointTest).setControllerAdvice(new ExceptionHandler())
                .alwaysDo(print()).build();
    }

    @Test
    public void testCreateCredentials() throws Exception {
        given(authenticationService.verifyIfUserNameExists(any(String.class))).willReturn(true);
        given(authenticationService.verifyIfUserBlocked(any(String.class))).willReturn(false);
        given(authenticationService.verifyIfValidCredentials(any(String.class), any(String.class))).willReturn(true);
        given(tokenService.generateJWTToken(any(String.class))).willReturn(TOKEN);

        mockMvc.perform(post(VERIFY_ENDPOINT)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .content(VERIFY_ENDPOINT_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("jwtToken").value("abcde"));
    }

    @Test
    public void testCreateCredentialsWithNoUserName() throws Exception {
        given(authenticationService.verifyIfUserNameExists(any(String.class))).willReturn(false);

        mockMvc.perform(post(VERIFY_ENDPOINT)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .content(VERIFY_ENDPOINT_JSON))
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("message").value("Authentication Failed. Reason: UserName doesn't exist"));
    }

    @Test
    public void testCreateCredentialsNoUserBlocked() throws Exception {
        given(authenticationService.verifyIfUserNameExists(any(String.class))).willReturn(true);
        given(authenticationService.verifyIfUserBlocked(any(String.class))).willReturn(true);

        mockMvc.perform(post(VERIFY_ENDPOINT)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .content(VERIFY_ENDPOINT_JSON))
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("message").value("Authentication Failed. Reason: UserName blocked due to incorrect login attempts"));
    }

    @Test
    public void testCreateCredentialsBadCredentials() throws Exception {
        given(authenticationService.verifyIfUserNameExists(any(String.class))).willReturn(true);
        given(authenticationService.verifyIfUserBlocked(any(String.class))).willReturn(false);
        given(authenticationService.verifyIfValidCredentials(any(String.class), any(String.class))).willReturn(false);

        mockMvc.perform(post(VERIFY_ENDPOINT)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .content(VERIFY_ENDPOINT_JSON))
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("message").value("Authentication Failed. Reason: Invalid UserName and password"));
    }

}
