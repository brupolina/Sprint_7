import com.github.javafaker.Faker;
import scooter.parameters.CreateCourier;
import scooter.parameters.LoginCourier;
import scooter.steps.CourierSteps;

import io.qameta.allure.Description;
import io.qameta.allure.Step;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.Response;

import org.junit.Before;
import org.junit.After;
import org.junit.Test;

public class CourierLoginTest extends BaseTest {
    private CourierSteps courierSteps;
    private Faker faker = new Faker();
    private String login;
    private String password;
    private String firstName;
    private String courierId;

    @Before
    @Step("Предварительное условие: создание курьера")
    public void setUp() {
        super.setUp();
        courierSteps = new CourierSteps();

        if (courierSteps == null) {
            throw new IllegalStateException("CourierSteps is not initialized");
        }

        login = faker.name().username();
        password = faker.internet().password();
        firstName = faker.name().firstName();
        CreateCourier courierCreate = new CreateCourier(login, password, firstName);
        Response response = courierSteps.sendPostRequestForCreatingCourier(courierCreate);
        courierId = response.then().extract().body().path("id").toString();
    }

    @After
    @Step("Постусловие: очистка базы данных - удаление созданного курьера")
    public void cleanData() {
        if (courierId != null) {
            courierSteps.deleteCourier(courierId);
        }
    }

    @Test
    @DisplayName("Авторизация курьера")
    @Description("Курьер авторизируется с валидными данными")
    public void checkLoginCourierTest() {
        LoginCourier courierLogin = new LoginCourier(login, password);
        Response response = courierSteps.sendPostRequestForLoginCourier(courierLogin);
        courierSteps.checkStatus200ForLogin(response);
    }

    @Test
    @DisplayName("Авторизация курьера без логина")
    @Description("Невозможно авторизироваться без данных в поле login")
    public void checkLoginCourierWithoutLoginFieldTest() {
        LoginCourier courierLogin = new LoginCourier("", password);
        Response response = courierSteps.sendPostRequestForLoginCourier(courierLogin);
        courierSteps.checkStatus400ForLogin(response);
    }

    @Test
    @DisplayName("Авторизация курьера без пароля")
    @Description("Невозможно авторизироваться без данных в поле password")
    public void checkLoginCourierWithoutPasswordFieldTest() {
        LoginCourier courierLogin = new LoginCourier(login, "");
        Response response = courierSteps.sendPostRequestForLoginCourier(courierLogin);
        courierSteps.checkStatus400ForLogin(response);
    }

    @Test
    @DisplayName("Авторизация курьера с несуществующим паролем")
    @Description("Невозможно авторизироваться с несуществующими данными")
    public void checkLoginCourierWithNonexistentPasswordTest() {
        String fakePassword = faker.internet().password();
        LoginCourier courierLogin = new LoginCourier(login, fakePassword);
        Response response = courierSteps.sendPostRequestForLoginCourier(courierLogin);
        courierSteps.checkStatus404ForLogin(response);
    }

    @Test
    @DisplayName("Авторизация курьера с несуществующим логином")
    @Description("Невозможно авторизироваться с несуществующими данными")
    public void checkLoginCourierWithNonexistentLoginTest() {
        String fakeLogin = faker.name().username();
        LoginCourier courierLogin = new LoginCourier(fakeLogin, password);
        Response response = courierSteps.sendPostRequestForLoginCourier(courierLogin);
        courierSteps.checkStatus404ForLogin(response);
    }
}