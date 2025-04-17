import com.github.javafaker.Faker;
import scooter.parameters.CreateOrder;
import scooter.steps.OrderSteps;

import io.qameta.allure.Description;
import io.qameta.allure.Step;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.Response;

import org.junit.After;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.util.List;
import java.util.Random;

@RunWith(Parameterized.class)
public class OrderCreationTest extends OrderSteps {
    Faker faker = new Faker();
    private final String firstName = faker.name().firstName();
    private final String lastName = faker.name().lastName();
    String address = faker.address().streetAddress();
    Integer metroStation = new Random().nextInt(10);
    String phone = faker.phoneNumber().cellPhone();
    Integer rentTime = new Random().nextInt(10);
    String deliveryDate = String.format("2025-01-%d",new Random().nextInt(29));
    String comment = faker.name().fullName();
    private final List<String> colour;
    private String trackId = null;

    public OrderCreationTest(List<String> colour) {
        this.colour = colour;
    }

    @After
    @Step("Постусловие: очистка данных - отмена созданного заказа.")
    public  void cleanData() {
        if (trackId != null) {
            cancelOrder(trackId);
        }
    }

    @Parameterized.Parameters(name = "Цвет самоката: {0}")
    public static Object[][] getData() {
        return new Object[][] {
                {List.of("BLACK", "GREY")},
                {List.of("BLACK")},
                {List.of("GREY")},
                {List.of()}
        };
    }

    @Test
    @DisplayName("Создание заказа.")
    @Description("Создание заказа самоката в двух цветовых решениях.")
    public  void checkDifferentColoursForOrderTest() {
        CreateOrder createOrder = new CreateOrder(firstName, lastName, address, metroStation, phone, rentTime, deliveryDate, comment, colour);
        Response response = sendPostRequestForCreatingOrder(createOrder);
        trackId = checkStatus201ForCreatingOrder(response);
    }
}