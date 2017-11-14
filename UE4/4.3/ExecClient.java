import java.util.Scanner;

public class ExecClient {
    public static void main(String []args){
        int port = 6789;
        String host;

        System.out.println("Enter Host for Client - read above.");
        Scanner s = new Scanner(System.in);
        host = s.nextLine();

        POP3Client p3c = new POP3Client(host, port);
        p3c.run();
    }
}
