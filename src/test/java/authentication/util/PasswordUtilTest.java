package authentication.util;

import com.assessment.authentication.util.PasswordUtil;
import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class PasswordUtilTest {

    @Test
    public void testGenerateRandom() {
        String salt = PasswordUtil.generateSalt(12);
        assertThat(salt).isNotNull();
    }

    @Test
    public void testPasswordHash() {
        String password = "123456";
        String salt = PasswordUtil.generateSalt(12);
        String pepper = "pepper";
        String hashed = PasswordUtil.hashPassword(password, salt, pepper);

        assertThat(hashed).isNotNull();
        assertThat(hashed).isNotEqualTo(password);
    }

    @Test
    public void testPasswordVerify() {
        String salt = PasswordUtil.generateSalt(12);
        String pepper = "pepper";

        String password1 = "123456";
        String hashed1 = PasswordUtil.hashPassword(password1, salt, pepper);

        String password2 = "123456";
        String hashed2 = PasswordUtil.hashPassword(password1, salt, pepper);

        assertThat(hashed1).isEqualTo(hashed2);
    }

}
