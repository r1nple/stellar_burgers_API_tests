package ApiClient;

import constants.Endpoints;
import io.restassured.response.Response;

public class IngredientsClient extends Client {

    public Response allIngredientsFetch() {
        return specification()
                .get(Endpoints.INGREDIENTS);
    }

}
