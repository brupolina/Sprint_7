import io.restassured.RestAssured;
import io.restassured.specification.RequestSpecification;
import org.junit.Before;
import scooter.parameters.URL;

public class BaseTest {
    protected RequestSpecification requestSpecification;

    @Before
    public void setUp() {
        requestSpecification = RestAssured.given()
                .baseUri(URL.API_ENDPOINT)
                .header("Content-Type", "application/json");
    }
}