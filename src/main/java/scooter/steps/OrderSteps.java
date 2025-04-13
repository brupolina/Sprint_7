package scooter.steps;
import io.restassured.specification.RequestSpecification;
import scooter.steps.BaseHttpClient;
import scooter.parameters.URL;
import scooter.parameters.CreateOrder;

import io.qameta.allure.Step;
import io.restassured.response.Response;
import static io.restassured.RestAssured.given;

import static org.apache.http.HttpStatus.SC_CREATED;
import static org.apache.http.HttpStatus.SC_OK;
import static org.hamcrest.Matchers.notNullValue;

public class OrderSteps extends BaseHttpClient {
    @Step("Запрос POST на /api/v1/orders для создания заказа.")
    public Response sendPostRequestForCreatingOrder(CreateOrder order) {
        return requestSpecification
                .and()
                .body(order)
                .when()
                .post(URL.ORDER_CREATE_POST);
    }

    @Step("Статус ответа: 201, поле 'track' со значением.")
    public String checkStatus201ForCreatingOrder(Response response) {
        response.then()
                .statusCode(SC_CREATED)
                .assertThat()
                .body("track",notNullValue());
        return response.then().extract().body().path("track").toString();

    }

    @Step("Заказ отменен.")
    public void cancelOrder(String track) {
        requestSpecification
                .queryParam("track", track)
                .put(URL.ORDER_CANCEL_PUT)
                .then().statusCode(SC_OK);
    }

    @Step("Отправка запроса GET на /api/v1/orders для получения списков заказов.")
    public Response sendGetRequestForCreatingOrder() {
        return requestSpecification
                .get(URL.ORDER_LIST_GET);

    }

    @Step("Статус ответа: 200, ответ содержит заказ.")
    public void checkStatus200AndBody(Response response) {
        response.then()
                .statusCode(SC_OK)
                .assertThat()
                .body("orders.id", notNullValue());
    }
}