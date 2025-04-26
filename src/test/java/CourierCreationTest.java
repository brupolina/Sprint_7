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

import static org.apache.http.HttpStatus.SC_OK;

public class CourierCreationTest extends BaseTest {
    private CourierSteps courierSteps;
    private Faker faker = new Faker();
    private String login;
    private String password;
    private String firstName;

    @Before
    public void setUp() {
        super.setUp();
        courierSteps = new CourierSteps();
    }

    @After
    @Step("Постусловие: очистка базы данных - удаление созданного курьера")
    public void cleanData() {
        if (login != null && password != null) {
            LoginCourier courierLogin = new LoginCourier(login, password);
            Response response = courierSteps.sendPostRequestForLoginCourier(courierLogin);
            int status = response.then().extract().statusCode();
            if (status == SC_OK) {
                String courierId = response.then().extract().body().path("id").toString();
                courierSteps.deleteCourier(courierId);
            }
        }
    }

    @Test
    @DisplayName("Создание курьера")
    @Description("Можно создать курьера с валидными данными")
    public void checkCreatingCourierTest() {
        login = faker.name().username();
        password = faker.internet().password();
        firstName = faker.name().firstName();
        CreateCourier courierCreate = new CreateCourier(login, password, firstName);
        Response response = courierSteps.sendPostRequestForCreatingCourier(courierCreate);
        courierSteps.checkStatus201ForCreatingCourier(response);
    }

    @Test
    @DisplayName("Создание курьера без имени")
    @Description("Можно создать курьера без указания имени (firstName)")
    public void checkCreatingCourierWithoutFirstNameTest() {
        login = faker.name().username();
        password = faker.internet().password();
        CreateCourier courierCreate = new CreateCourier(login, password, null);
        Response response = courierSteps.sendPostRequestForCreatingCourier(courierCreate);
        courierSteps.checkStatus201ForCreatingCourier(response);
    }

    @Test
    @DisplayName("Создание двух одинаковых курьеров")
    @Description("Невозможно создать двух курьеров с одинаковыми валидными данными")
    public void checkCreatingIdenticalCouriersTest() {
        // Создание курьера
        login = faker.name().username();
        password = faker.internet().password();
        firstName = faker.name().firstName();
        CreateCourier courierCreate = new CreateCourier(login, password, firstName);
        Response responseFirst = courierSteps.sendPostRequestForCreatingCourier(courierCreate);
        courierSteps.checkStatus201ForCreatingCourier(responseFirst);
        // Повторное создание курьера
        Response responseSecond = courierSteps.sendPostRequestForCreatingCourier(courierCreate);
        courierSteps.checkStatus409ForCreatingCourier(responseSecond);
    }

    @Test
    @DisplayName("Создание курьера без логина")
    @Description("Невозможно создать курьера без данных в поле login")
    public void checkCreatingCourierWithoutLoginFieldTest() {
        password = faker.internet().password();
        firstName = faker.name().firstName();
        CreateCourier courierCreate = new CreateCourier("", password, firstName);
        Response response = courierSteps.sendPostRequestForCreatingCourier(courierCreate);
        courierSteps.checkStatus400ForCreatingCourier(response);
    }

    @Test
    @DisplayName("Создание курьера без пароля")
    @Description("Невозможно создать курьера без данных в поле password")
    public void checkCreatingCourierWithoutPasswordFieldTest() {
        login = faker.name().username();
        firstName = faker.name().firstName();
        CreateCourier courierCreate = new CreateCourier(login, "", firstName);
        Response response = courierSteps.sendPostRequestForCreatingCourier(courierCreate);
        courierSteps.checkStatus400ForCreatingCourier(response);
    }
}