import scooter.steps.OrderSteps;

import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.Response;

import org.junit.Test;

public class GetOrderListTest extends OrderSteps {

    @Test
    @DisplayName("Получение списка заказов")
    @Description("Проверяем наличие содержимого в полученном списке")
    public void checkOrderListHasBodyTest() {
        Response response = sendGetRequestForCreatingOrder();
        checkStatus200AndBody(response);
    }
}