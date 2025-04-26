package scooter.parameters;

public class URL {
    public static final String API_ENDPOINT = "http://qa-scooter.praktikum-services.ru";
    public static final String COURIER_CREATE_POST = "/api/v1/courier";
    public static final String COURIER_LOGIN_POST = "/api/v1/courier/login";
    public static final String COURIER_DELETE = "/api/v1/courier/{id}";
    public static final String ORDER_CREATE_POST = "/api/v1/orders";
    public static final String ORDER_LIST_GET = "/v1/orders";
    public static final String ORDER_CANCEL_PUT = "/api/v1/orders/cancel";
}