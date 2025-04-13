package scooter.parameters;

public class LoginCourier {
    private String login;
    private String password;

    public LoginCourier(String login, String password) {
        this.login = login;
        this.password = password;
    }
    public LoginCourier() {
    }

    public String getLogin() {
        return login;
    }
    public void setName(String login) {
        this.login = login;
    }

    public String getPassword() {
        return password;
    }
    public void setPassword(String password) {
        this.password = password;
    }
}