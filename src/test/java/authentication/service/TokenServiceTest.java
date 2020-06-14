package authentication.service;

import com.assessment.authentication.service.TokenService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(MockitoJUnitRunner.class)
public class TokenServiceTest {

    TokenService tokenService;

    @Before
    public void setup() {
        this.tokenService = new TokenService();
    }

    @Test
    public void testJwtToken() {
        String jwtToken = tokenService.generateJWTToken("user");
        assertThat(jwtToken).isNotNull();
    }
}
