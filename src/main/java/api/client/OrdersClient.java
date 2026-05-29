package api.client;

import constants.Endpoints;
import io.qameta.allure.Step;
import io.restassured.response.Response;

import java.util.List;
import java.util.Map;

public class OrdersClient extends Client {

    private static final String HEADER_PATH = "Authorization";

    @Step("Создание заказа без авторизации")
    public Response sendOrderWithoutLogin(Map<String, List<String>> ingredientsData) {
        return specification()
                .body(ingredientsData)
                .post(Endpoints.ORDERS);
    }

    @Step("Создание заказа с авторизацией")
    public Response sendOrderWithLogin(Map<String, List<String>> ingredientsData, String token) {
        return specification()
                .header(HEADER_PATH, token)
                .body(ingredientsData)
                .post(Endpoints.ORDERS);
    }

    @Step("Получение заказов с авторизацией")
    public Response getUserOrders(String token) {
        return specification()
                .header(HEADER_PATH, token)
                .get(Endpoints.ORDERS);
    }

    @Step("Получение заказов без авторизации")
    public Response getAnonymousOrders() {
        return specification()
                .get(Endpoints.ORDERS);
    }

}
