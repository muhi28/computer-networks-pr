package src.Client;

import javax.net.ssl.SSLSocketFactory;
import java.io.*;
import java.net.Socket;

public class POP3Client {

    private static final int DEFAULT_PORT = 110;

    private Socket socket;
    private BufferedReader reader;
    private BufferedWriter writer;


    public void connect(String host, int port) throws IOException {

        SSLSocketFactory ssl = (SSLSocketFactory) SSLSocketFactory.getDefault();
        socket = ssl.createSocket(host, port);

        reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        writer = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));

        if (socket.isConnected()) {
            System.out.println("Client connected");

        }

        singleresponse();
    }

    private String singleresponse() {

        String response = null;

        try {
            response = reader.readLine();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return response;
    }

    private String multiLineResponse() {

        StringBuilder sb = new StringBuilder();
        String line;

        while (!(line = singleresponse()).equals(".")) {
            sb.append(line);
            sb.append("\n");
        }
        sb.deleteCharAt(sb.lastIndexOf("\n"));
        return sb.toString();
    }

    private void sendData(String data) {
        try {

            writer.write(data + "\n");
            writer.flush();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private boolean isConnected() {
        return socket != null && socket.isConnected();
    }


}
