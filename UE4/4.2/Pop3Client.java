import java.io.*;
import java.net.InetSocketAddress;
import java.net.Socket;

public class Pop3Client {

    private Socket socket;

    private boolean debug = false;

    private BufferedReader reader;
    private BufferedWriter writer;

    private static final int DEFAUL_PORT = 110;

    public boolean isDebug() {
        return debug;
    }

    public void connect(String host, int port) throws IOException {
        socket = new Socket();

        socket.connect(new InetSocketAddress(host, port));

        if (isConnected()) {

            reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            writer = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));

            if (debug) {
                System.out.println("Client is connected to host !!");
            }
        } else {
            System.out.println("Client isn't connected!!");
        }

        readResponseLine();

    }

    public void connect(String host) throws IOException {
        connect(host, DEFAUL_PORT);
    }

    public void disconnect() throws IOException {

        if (!isConnected())
            throw new IllegalStateException("Not connected to a host!!");


        socket.close();
        reader = null;
        writer = null;

        if (debug)
            System.out.println("Diconneted from the host!!!");
    }

    public void setDebug(boolean debug) {

        this.debug = debug;
    }


    private boolean isConnected() {

        return socket != null && socket.isConnected();
    }

    protected String readResponseLine() throws IOException {

        String response = reader.readLine();

        if (debug) {
            System.out.println("DEBUG [in]: " + response);
        }

        if (response.startsWith("-ERR")) {
            throw new RuntimeException("Server has returned error: " + response.replaceFirst("-ERR", ""));

        }

        return response;
    }

    protected String sendCommand(String command) throws IOException {

        if (debug) {
            System.out.println("DEBUG [out]: " + command);
        }

        writer.write(command + "\n");
        writer.flush();

        return readResponseLine();
    }


    public void login(String username, String password) throws IOException {

        sendCommand("USER " + username);
        sendCommand("PASSW " + password);

    }

    public void logout() throws IOException {
        sendCommand("QUIT");
    }


    public int getNumberOfNewMessages() throws IOException {

        String response = sendCommand("LIST");
        String[] values = response.split(" ");
        return Integer.parseInt(values[1]);

    }


}
