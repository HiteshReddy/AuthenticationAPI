package authentication.service;

import com.assessment.authentication.persistence.CredentialsRepository;
import com.assessment.authentication.service.AuthenticationService;
import com.assessment.authentication.service.dto.UserStatus;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(MockitoJUnitRunner.class)
public class AuthenticationServiceTest {

    AuthenticationService authenticationService;

    @Before
    public void setUp() {
        this.authenticationService = new AuthenticationService();
        this.authenticationService.setCredentialsRepository(new CredentialsRepository());
    }

    @Test
    public void testCreate() {
        String user = "user", password = "password";
        authenticationService.create(user, password);

        boolean userExists = authenticationService.verifyIfUserNameExists(user);
        assertThat(userExists).isTrue();

        UserStatus userStatus = authenticationService.getCredentialsRepository().getUserStatus(user);
        assertThat(userStatus).isNotNull();
        assertThat(userStatus.getLastInvalidAccess()).isNull();
        assertThat(userStatus.getInvalidAttempts()).isEqualTo(0);
        assertThat(userStatus.getStatus()).isEqualTo(UserStatus.Status.NOT_BLOCKED);
    }

    @Test
    public void testVerifyUser() {
        String user = "user", password = "password";
        authenticationService.create(user, password);

        boolean userExists = authenticationService.verifyIfUserNameExists(user);
        assertThat(userExists).isTrue();

        UserStatus userStatus = authenticationService.getCredentialsRepository().getUserStatus(user);
        assertThat(userStatus).isNotNull();
        assertThat(userStatus.getLastInvalidAccess()).isNull();
        assertThat(userStatus.getInvalidAttempts()).isEqualTo(0);
        assertThat(userStatus.getStatus()).isEqualTo(UserStatus.Status.NOT_BLOCKED);
    }

    @Test
    public void testUserBlocksAfter3InvalidAttempts() {
        String user = "user", password = "password";
        authenticationService.create(user, password);

        String invalidPassword = "invalid";
        authenticationService.verifyIfValidCredentials(user, invalidPassword);
        authenticationService.verifyIfValidCredentials(user, invalidPassword);
        authenticationService.verifyIfValidCredentials(user, invalidPassword);

        boolean isUserBlocked = authenticationService.verifyIfUserBlocked(user);

        assertThat(isUserBlocked).isTrue();
    }

    @Test
    public void verifyIfUserStatusResetsAfterValidAttempt() {
        String user = "user", password = "password";
        authenticationService.create(user, password);

        String invalidPassword = "invalid";
        authenticationService.verifyIfValidCredentials(user, invalidPassword);
        authenticationService.verifyIfValidCredentials(user, invalidPassword);

        authenticationService.verifyIfValidCredentials(user, password);
        boolean isUserBlocked = authenticationService.verifyIfUserBlocked(user);

        assertThat(isUserBlocked).isFalse();

        UserStatus userStatus = authenticationService.getCredentialsRepository().getUserStatus(user);

        assertThat(userStatus.getLastInvalidAccess()).isNull();
        assertThat(userStatus.getInvalidAttempts()).isEqualTo(0);
        assertThat(userStatus.getStatus()).isEqualTo(UserStatus.Status.NOT_BLOCKED);
    }

}
