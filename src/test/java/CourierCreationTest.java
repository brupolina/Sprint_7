import com.github.javafaker.Faker;
import scooter.parameters.CreateCourier;
import scooter.parameters.LoginCourier;
import scooter.steps.CourierSteps;

import io.qameta.allure.Description;
import io.qameta.allure.Step;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.Response;

import org.junit.After;
import org.junit.Test;

import static org.apache.http.HttpStatus.SC_OK;

public class CourierCreationTest extends CourierSteps {
    Faker faker = new Faker();
    String login = faker.name().username();
    String password = faker.internet().password();
    String firstName = faker.name().firstName();
    @After
    @Step("Постусловие: очистка базы данных - удаление созданного курьера")
    public  void cleanData() {
        LoginCourier courierLogin = new LoginCourier(login, password);
        Response response = sendPostRequestForLoginCourier(courierLogin);
        int status = response.then().extract().statusCode();
        if (status == SC_OK) {
            String courierId = response.then().extract().body().path("id").toString();
            deleteCourier(courierId);
        }
    }

    @Test
    @DisplayName("Создание курьера")
    @Description("Можно создать курьера с валидными данными")
    public void checkCreatingCourierTest() {
        CreateCourier courierCreate = new CreateCourier(login, password, firstName);
        Response response = sendPostRequestForCreatingCourier(courierCreate);
        checkStatus201ForCreatingCourier(response);
    }

    @Test
    @DisplayName("Создание курьера без имени")
    @Description("Можно создать курьера без указания имени (firstName)")
    public void checkCreatingCourierWithoutFirstNameTest() {
        CreateCourier courierCreate = new CreateCourier(login, password, null);
        Response response = sendPostRequestForCreatingCourier(courierCreate);
        checkStatus201ForCreatingCourier(response);
    }

    @Test
    @DisplayName("Создание двух одинаковых курьеров")
    @Description("Невозможно создать двух курьеров с одинаковыми валидными данными")
    public void checkCreatingIdenticalCouriersTest() {
        // Создание курьера
        CreateCourier courierCreate = new CreateCourier(login, password, firstName);
        Response responseFirst = sendPostRequestForCreatingCourier(courierCreate);
        checkStatus201ForCreatingCourier(responseFirst);
        // Повторное создание курьера
        Response responseSecond = sendPostRequestForCreatingCourier(courierCreate);
        checkStatus409ForCreatingCourier(responseSecond);
    }

    @Test
    @DisplayName("Создание курьера без логина")
    @Description("Невозможно создать курьера без данных в поле login")
    public void checkCreatingCourierWithoutLoginFieldTest() {
        CreateCourier courierCreate = new CreateCourier("", password, firstName);
        Response response = sendPostRequestForCreatingCourier(courierCreate);
        checkStatus400ForCreatingCourier(response);
    }

    @Test
    @DisplayName("Создание курьера без пароля")
    @Description("Невозможно создать курьера без данных в поле password")
    public void checkCreatingCourierWithoutPasswordFieldTest() {
        CreateCourier courierCreate = new CreateCourier(login, "", firstName);
        Response response = sendPostRequestForCreatingCourier(courierCreate);
        checkStatus400ForCreatingCourier(response);
    }
}