import ApiClient.UserClient;
import generators.UserGenerators;
import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import models.User;
import org.apache.http.HttpStatus;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.equalTo;

public class UserLoginTest {

    private final UserClient userClient = new UserClient();
    private User userTest;
    private String accessToken;

    @Before
    public void setUp() {
        userTest = UserGenerators.generateUser();
        accessToken = userClient.register(userTest).body().path("accessToken");
    }

    @After
    public void tearDown() {
        if (accessToken != null) {
            userClient.userDelete(userTest, accessToken);
        }
    }

    @Test
    @DisplayName("Успешная авторизация пользователя")
    @Description("Проверка, что пользователь с корректными данными может войти")
    public void shouldLoginSuccessfullyTest() {
        userClient.login(userTest)
                .then()
                .statusCode(HttpStatus.SC_OK)
                .body("success", equalTo(true));
    }

    @Test
    @DisplayName("Авторизация с некорректным паролем")
    @Description("Проверка, что появляется текст ошибки при неверном пароле")
    public void shouldNotLoginWithWrongPasswordTest() {
        userTest.setPassword("wrongPassword");
        userClient.login(userTest)
                .then()
                .statusCode(HttpStatus.SC_UNAUTHORIZED)
                .body("success", equalTo(false))
                .body("message", equalTo("email or password are incorrect"));
    }

    @Test
    @DisplayName("Авторизация без email")
    @Description("Проверка, что появляется текст ошибки при пустом email")
    public void shouldNotLoginWithoutEmailTest() {
        userTest.setEmail(null);
        userClient.login(userTest)
                .then()
                .statusCode(HttpStatus.SC_UNAUTHORIZED)
                .body("success", equalTo(false))
                .body("message", equalTo("email or password are incorrect"));
    }

    @Test
    @DisplayName("Авторизация без пароля")
    @Description("Проверка, что появляется текст ошибки при пустом пароле")
    public void shouldNotLoginWithoutPasswordTest() {
        userTest.setPassword(null);
        userClient.login(userTest)
                .then()
                .statusCode(HttpStatus.SC_UNAUTHORIZED)
                .body("success", equalTo(false))
                .body("message", equalTo("email or password are incorrect"));
    }

}
