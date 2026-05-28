package ApiClient;

import constants.Endpoints;
import io.qameta.allure.Step;
import io.restassured.response.Response;
import models.User;

public class UserClient extends Client {

    private static final String HEADER_AUTH = "Authorization";

    @Step("Регистрация пользователя(null-поля включены)")
    public Response register(User user) {
        return specification()
                .body(user)
                .post(Endpoints.REGISTER);
    }

    @Step("Авторизация пользователя")
    public Response login(User user) {
        return specification()
                .body(user)
                .post(Endpoints.LOGIN);
    }

    @Step("Получение данных о пользователе")
    public Response getUserInfo(String accessToken) {
        return specification()
                .header(HEADER_AUTH, accessToken)
                .get(Endpoints.USER);
    }

    @Step("Обновление данных пользователя с токеном")
    public Response userUpdateWithToken(User user, String accessToken) {
        return specification()
                .header(HEADER_AUTH, accessToken)
                .body(user)
                .patch(Endpoints.USER);
    }

    @Step("Обновление данных пользователя без токена")
    public Response userUpdateWithoutToken(User user) {
        return specification()
                .body(user)
                .patch(Endpoints.USER);
    }

    @Step("Удаление пользователя")
    public Response userDelete(User user, String accessToken) {
        return specification()
                .header(HEADER_AUTH, accessToken)
                .body(user)
                .delete(Endpoints.USER);
    }

}
