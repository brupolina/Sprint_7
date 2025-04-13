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

public class CourierLoginTest extends CourierSteps {
    Faker faker = new Faker();
    String login = faker.name().username();
    String password = faker.internet().password();
    String firstName = faker.name().firstName();

    @After
    @Step("Постусловие: очистка базы данных - удаление созданного курьера.")
    public  void cleanData() {
        LoginCourier courierLogin = new LoginCourier(login, password);
        Response response = sendPostRequestForLoginCourier(courierLogin);
        int status = response.then().extract().statusCode();
        if (status == 200) {
            String courierId = response.then().extract().body().path("id").toString();
            deleteCourier(courierId);
        }
    }

    @Test
    @DisplayName("Авторизация курьера.")
    @Description("Курьер авторизируется с валидными данными.")
    public void checkLoginCourier() {
        CreateCourier courierCreate = new CreateCourier(login, password, firstName);
        LoginCourier courierLogin = new LoginCourier(login, password);
        sendPostRequestForCreatingCourier(courierCreate);
        Response response = sendPostRequestForLoginCourier(courierLogin);
        checkStatus200ForLogin(response);
    }

    @Test
    @DisplayName("Авторизация курьера без логина.")
    @Description("Невозможно авторизироваться без данных в поле login.")
    public void checkLoginCourierWithoutLoginField() {
        CreateCourier courierCreate = new CreateCourier(login, password, firstName);
        LoginCourier courierLogin = new LoginCourier("", password);
        sendPostRequestForCreatingCourier(courierCreate);
        Response response = sendPostRequestForLoginCourier(courierLogin);
        checkStatus400ForLogin(response);
    }

    @Test
    @DisplayName("Авторизация курьера без пароля.")
    @Description("Невозможно авторизироваться без данных в поле password.")
    public void checkLoginCourierWithoutPasswordField() {
        CreateCourier courierCreate = new CreateCourier(login, password, firstName);
        LoginCourier courierLogin = new LoginCourier(login, "");
        sendPostRequestForCreatingCourier(courierCreate);
        Response response = sendPostRequestForLoginCourier(courierLogin);
        checkStatus400ForLogin(response);
    }

    @Test
    @DisplayName("Авторизация курьера с несуществующим паролем.")
    @Description("Невозможно авторизироваться с несуществующими данными.")
    public void checkLoginCourierWithNonexistentPassword() {
        CreateCourier courierCreate = new CreateCourier(login, password, firstName);
        String fakePassword = faker.internet().password();
        LoginCourier courierLogin = new LoginCourier(login, fakePassword);
        Response response = sendPostRequestForLoginCourier(courierLogin);
        checkStatus404ForLogin(response);
    }

    @Test
    @DisplayName("Авторизация курьера с несуществующим логином.")
    @Description("Невозможно авторизироваться с несуществующими данными.")
    public void checkLoginCourierWithNonexistentLogin() {
        CreateCourier courierCreate = new CreateCourier(login, password, firstName);
        String fakeLogin = faker.name().username();
        LoginCourier courierLogin = new LoginCourier(fakeLogin, password);
        Response response = sendPostRequestForLoginCourier(courierLogin);
        checkStatus404ForLogin(response);
    }
}