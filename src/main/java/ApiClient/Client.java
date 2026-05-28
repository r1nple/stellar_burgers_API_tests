package ApiClient;

import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;

import static io.restassured.RestAssured.given;

public class Client {

    public static final String BASE_URL = "https://stellarburgers.education-services.ru";
    public static final String BASE_PATH = "/api";

    public RequestSpecification specification() {
        return given()
                .contentType(ContentType.JSON)
                .baseUri(BASE_URL)
                .basePath(BASE_PATH);
    }

}
