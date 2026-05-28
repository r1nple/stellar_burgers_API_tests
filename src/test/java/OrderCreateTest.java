import ApiClient.OrdersClient;
import ApiClient.UserClient;
import generators.IngredientGenerators;
import generators.UserGenerators;
import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.Response;
import models.User;
import org.apache.http.HttpStatus;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.List;
import java.util.Map;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.notNullValue;

public class OrderCreateTest {

    private final UserClient userClient = new UserClient();
    private final OrdersClient ordersClient = new OrdersClient();
    private final IngredientGenerators ingredientGenerators = new IngredientGenerators();
    private String accessToken;
    private User user;

    @Before
    public void setUp() {
        user = UserGenerators.generateUser();
        Response resp = userClient.register(user);
        accessToken = resp.body().path("accessToken");
    }

    @After
    public void cleanUp() {
        if (accessToken != null) {
            userClient.userDelete(user, accessToken);
        }
    }

    @Test
    @DisplayName("Создание заказа с авторизацией и валидными ингредиентами")
    @Description("Проверка, что авторизованный пользователь может создать заказ")
    public void createOrderWithLoginTest() {
        Map<String, List<String>> data = ingredientGenerators.getValidIngredients();
        Response response = ordersClient.sendOrderWithLogin(data, accessToken);
        response.then()
                .statusCode(HttpStatus.SC_OK)
                .body("success", equalTo(true))
                .body("order.number", notNullValue());
    }

    @Test
    @DisplayName("Создание заказа без авторизации")
    @Description("Проверка, что неавторизованный пользователь может создать заказ")
    public void createOrderWithoutLoginTest() {
        Map<String, List<String>> data = ingredientGenerators.getValidIngredients();
        Response response = ordersClient.sendOrderWithoutLogin(data);
        response.then()
                .statusCode(HttpStatus.SC_OK)
                .body("success", equalTo(true))
                .body("order.number", notNullValue());
    }

    @Test
    @DisplayName("Создание заказа без ингредиентов")
    @Description("Проверка,что появится ошибка при попытке создать заказ без ингредиентов")
    public void createOrderWithoutIngredientsTest() {
        Map<String, List<String>> data = ingredientGenerators.getEmptyIngredients();
        Response response = ordersClient.sendOrderWithLogin(data, accessToken);
        response.then()
                .statusCode(HttpStatus.SC_BAD_REQUEST)
                .body("success", equalTo(false))
                .body("message", equalTo("Ingredient ids must be provided"));
    }

    @Test
    @DisplayName("Создание заказа с невалидными ингредиентами")
    @Description("Проверка, что появится ошибка при передаче несуществующих ингредиентов")
    public void createOrderWithInvalidIngredientsTest() {
        Map<String, List<String>> data = ingredientGenerators.getInvalidIngredients();
        Response response = ordersClient.sendOrderWithLogin(data, accessToken);
        response.then()
                .statusCode(HttpStatus.SC_INTERNAL_SERVER_ERROR);
    }

}
