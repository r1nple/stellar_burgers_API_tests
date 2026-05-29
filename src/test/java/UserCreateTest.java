import api.client.UserClient;
import generators.UserGenerators;
import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.Response;
import models.User;
import org.apache.http.HttpStatus;
import org.junit.After;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.equalTo;

public class UserCreateTest {

    private final UserClient userClient = new UserClient();
    private User user;
    private String accessToken;

    @After
    public void tearDown() {
        if (accessToken != null) {
            userClient.userDelete(user, accessToken);
        }
    }

    @Test
    @DisplayName("Успешная регистрация нового пользователя")
    @Description("Проверка, что новый пользователь регистрируется успешно")
    public void shouldRegisterUserSuccessfullyTest() {
        user = UserGenerators.generateUser();
        Response response = userClient.register(user);
        accessToken = response.body().path("accessToken");
        response.then()
                .statusCode(HttpStatus.SC_OK)
                .body("success", equalTo(true))
                .body("user.email", equalTo(user.getEmail()));
    }

    @Test
    @DisplayName("Регистрация без email")
    @Description("Проверка, что появляется ошибка при регистрации без email")
    public void shouldNotRegisterUserWithoutEmailTest() {
        user = UserGenerators.generateUser();
        user.setEmail(null);
        Response response = userClient.register(user);
        response.then()
                .statusCode(HttpStatus.SC_FORBIDDEN)
                .body("message", equalTo("Email, password and name are required fields"));
    }

    @Test
    @DisplayName("Регистрация без name")
    @Description("Проверка, что появляется ошибка при регистрации без имени")
    public void shouldNotRegisterUserWithoutNameTest() {
        user = UserGenerators.generateUser();
        user.setName(null);
        Response response = userClient.register(user);
        response.then()
                .statusCode(HttpStatus.SC_FORBIDDEN)
                .body("message", equalTo("Email, password and name are required fields"));
    }

    @Test
    @DisplayName("Регистрация без password")
    @Description("Проверка, что появляется ошибка при регистрации без пароля")
    public void shouldNotRegisterUserWithoutPasswordTest() {
        user = UserGenerators.generateUser();
        user.setPassword(null);
        Response response = userClient.register(user);
        response.then()
                .statusCode(HttpStatus.SC_FORBIDDEN)
                .body("message", equalTo("Email, password and name are required fields"));
    }

    @Test
    @DisplayName("Регистрация существующего пользователя")
    @Description("Проверка, что при повторной регистрации возвращается ошибка")
    public void shouldNotRegisterExistingUserTest() {
        user = UserGenerators.generateUser();
        userClient.register(user).then().statusCode(HttpStatus.SC_OK);
        Response response = userClient.register(user);
        response.then()
                .statusCode(HttpStatus.SC_FORBIDDEN)
                .body("message", equalTo("User already exists"));
    }

}
