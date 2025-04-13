package scooter.steps;
import scooter.steps.BaseHttpClient;
import scooter.parameters.URL;
import scooter.parameters.CreateCourier;
import scooter.parameters.LoginCourier;

import io.qameta.allure.Step;
import io.restassured.response.Response;
import static io.restassured.RestAssured.given;

import static org.apache.http.HttpStatus.*;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;

public class CourierSteps extends BaseHttpClient {
    @Step("Запрос POST на /api/v1/courier для создания курьера.")
    public Response sendPostRequestForCreatingCourier(CreateCourier courier) {
        return requestSpecification
                .and()
                .body(courier)
                .when()
                .post(URL.COURIER_CREATE_POST);
    }

    @Step("Статус ответа: 201, поле 'ok': true.")
    public void checkStatus201ForCreatingCourier(Response response) {
        response.then()
                .statusCode(SC_CREATED)
                .assertThat()
                .body("ok",equalTo(true));
    }

    @Step("Статус ответа: 409, поле 'message': 'Этот логин уже используется. Попробуйте другой.'")
    public void checkStatus409ForCreatingCourier(Response response) {
        response.then()
                .statusCode(SC_CONFLICT)
                .assertThat()
                .body("message",equalTo("Этот логин уже используется. Попробуйте другой."));
    }

    @Step("Статус ответа: 400, поле 'message': 'Недостаточно данных для создания учетной записи.'")
    public void checkStatus400ForCreatingCourier(Response response) {
        response.then()
                .statusCode(SC_BAD_REQUEST)
                .assertThat()
                .body("message",equalTo("Недостаточно данных для создания учетной записи."));
    }

    @Step("Запрос POST на /api/v1/courier/login для авторизации курьера.")
    public  Response sendPostRequestForLoginCourier(LoginCourier courier) {
        return requestSpecification
                .and()
                .body(courier)
                .post(URL.COURIER_LOGIN_POST);
    }

    @Step("Статус ответа: 200, поле 'id' со значением.")
    public void checkStatus200ForLogin(Response response) {
        response.then()
                .statusCode(SC_OK)
                .assertThat()
                .body("id",notNullValue());
    }

    @Step("Статус ответа: 400, поле поле 'message': 'Недостаточно данных для входа.'")
    public void checkStatus400ForLogin(Response response) {
        response.then()
                .statusCode(SC_BAD_REQUEST)
                .assertThat()
                .body("message", equalTo("Недостаточно данных для входа."));
    }

    @Step("Статус ответа: 404, поле 'message': 'Учетная запись не найдена.'")
    public void checkStatus404ForLogin(Response response) {
        response.then()
                .statusCode(SC_NOT_FOUND)
                .assertThat()
                .body("message", equalTo("Учетная запись не найдена."));
    }

    @Step("Курьер удален.")
    public void deleteCourier(String id) {
        requestSpecification
                .delete(URL.COURIER_DELETE, id)
                .then()
                .statusCode(SC_OK);
    }

}