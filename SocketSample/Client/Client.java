package src.Client;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.InetAddress;
import java.net.Socket;

public class Client {
    public static void main(String []args) throws Exception{
        String sentence;
        String modifiedSentence;
        String host = InetAddress.getLocalHost().getHostAddress();
        int port = 6789;

        BufferedReader inFromUser = new BufferedReader(new InputStreamReader(System.in));

        Socket clientSocket = new Socket(host, port);

        DataOutputStream outToServer = new DataOutputStream(clientSocket.getOutputStream());

        BufferedReader inFromServer = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));

        sentence = inFromUser.readLine();

        outToServer.writeBytes(sentence + "\n");

        modifiedSentence = inFromServer.readLine();

        System.out.println("FROM SERVER::"+modifiedSentence);

        clientSocket.close();
    }
}
