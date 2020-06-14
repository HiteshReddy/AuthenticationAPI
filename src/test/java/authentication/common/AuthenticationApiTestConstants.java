package authentication.common;

public final class AuthenticationApiTestConstants {

    public static final String CREATE_ENDPOINT = "/api/create";
    public static final String CREATE_ENDPOINT_JSON = "{ \"accountNumber\":\"77853449\",\"userName\":\"nathanlouis\",\"password\":\"1234567\" }";
    public static final String CREATE_ENDPOINT_INVALID_JSON = "{ \"accountNumber\":\"77853449\",\"userName\":\"nathanlouis\",\"password\":\"12345\" }";

    public static final String VERIFY_ENDPOINT = "/api/authenticate";
    public static final String VERIFY_ENDPOINT_JSON = "{ \"userName\":\"nathanlouis\",\"password\":\"1234567\" }";
    public static final String TOKEN = "abcde";
}
