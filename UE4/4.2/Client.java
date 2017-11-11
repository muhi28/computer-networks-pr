import java.io.IOException;
import java.util.List;
import java.util.Scanner;

public class Client {

    private static Scanner sc;

    public static void main(String[] args) throws IOException {

        System.out.print("Insert the host: ");
        sc = new Scanner(System.in);

        String host = sc.next();

        Pop3Client client = new Pop3Client();

        client.setDebug(true);

        client.connect(host, 6789);

        client.login("nam@yserver.com", "password");

        System.out.println("Number of emails: " + client.getNumberOfNewMessages());


        client.sendCommand("RETR");
        List<Message> messages = client.getMessages();

        for (int i = 0; i < messages.size(); i++) {

            System.out.println("--- Message num. " + i + " ---");
            System.out.println(messages.get(i).getBody());

        }



        client.logout();
        client.disconnect();
    }
}
