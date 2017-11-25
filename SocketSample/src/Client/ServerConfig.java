package src.Client;

public class ServerConfig {

    private static final String USER_MAIL = "asawd1245@gmail.com";
    private static final String USER_PASS = "asdfe1234";
    private static final int POP3_SSL_PORT = 995;
    private static final String POP_SERVERHOST = "pop.gmail.com";

    public String getUserMail() {
        return USER_MAIL;
    }

    public String getUserPass() {
        return USER_PASS;
    }

    public int getPop3SslPort() {
        return POP3_SSL_PORT;
    }

    public String getPopServerhost() {
        return POP_SERVERHOST;
    }

}
