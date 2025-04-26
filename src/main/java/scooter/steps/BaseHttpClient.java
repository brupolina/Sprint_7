package scooter.steps;

import io.restassured.specification.RequestSpecification;
import scooter.parameters.URL;

import static io.restassured.RestAssured.given;

public class BaseHttpClient {
    protected final RequestSpecification requestSpecification = given()
            .baseUri(URL.API_ENDPOINT)
            .header("Content-type", "application/json");
}