package authentication.api;

import com.assessment.authentication.exception.ExceptionHandler;
import com.assessment.authentication.presentation.api.CreateCredentialsEndpoint;
import com.assessment.authentication.service.AuthenticationService;
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
import static org.mockito.BDDMockito.willDoNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(MockitoJUnitRunner.class)
public class CreateCredentialsEndpointTest {

    @Mock
    AuthenticationService authenticationService;

    @InjectMocks
    CreateCredentialsEndpoint createCredentialsEndpoint;

    private MockMvc mockMvc;

    @Before
    public void setup() {
        this.mockMvc = MockMvcBuilders.standaloneSetup(createCredentialsEndpoint).setControllerAdvice(new ExceptionHandler())
                .alwaysDo(print()).build();
    }

    @Test
    public void testCreateCredentials() throws Exception {
        given(authenticationService.verifyIfAccountExists(any(String.class))).willReturn(true);
        given(authenticationService.verifyIfUserNameExists(any(String.class))).willReturn(false);
        willDoNothing().given(authenticationService).create(any(String.class), any(String.class));

        mockMvc.perform(post(CREATE_ENDPOINT)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .content(CREATE_ENDPOINT_JSON))
                .andExpect(status().isCreated());

        verify(authenticationService, times(1)).create(any(String.class), any(String.class));
    }

    @Test
    public void testCreateCredentialsWithInvalidAccount() throws Exception {
        given(authenticationService.verifyIfAccountExists(any(String.class))).willReturn(false);

        mockMvc.perform(post(CREATE_ENDPOINT)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .content(CREATE_ENDPOINT_JSON))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("message").value("No Account found for the given Account number"))
                .andExpect(jsonPath("code").value("404"));
    }

    @Test
    public void testCreateCredentialsWithDuplicateUserName() throws Exception {
        given(authenticationService.verifyIfAccountExists(any(String.class))).willReturn(true);
        given(authenticationService.verifyIfUserNameExists(any(String.class))).willReturn(true);

        mockMvc.perform(post(CREATE_ENDPOINT)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .content(CREATE_ENDPOINT_JSON))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("message").value("A UserName already exists in the system. Please try with new UserName"))
                .andExpect(jsonPath("code").value("409"));
    }

    @Test
    public void testCreateCredentialsWithInvalidPasswordFormat() throws Exception {
        given(authenticationService.verifyIfAccountExists(any(String.class))).willReturn(true);
        given(authenticationService.verifyIfUserNameExists(any(String.class))).willReturn(false);

        mockMvc.perform(post(CREATE_ENDPOINT)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .content(CREATE_ENDPOINT_INVALID_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("message").value("The password should have minimum length of 6"))
                .andExpect(jsonPath("code").value("400"));
    }

}
