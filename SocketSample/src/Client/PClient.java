package src.Client;

import javax.net.ssl.SSLSocketFactory;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.SocketHandler;

public class PClient {


    private final String HOST;
    private final int PORT;


    private Socket socket;

    private boolean debug = false;

    private BufferedReader reader;
    private DataOutputStream writer;

    public PClient(String host, int port) {

        HOST = host;
        PORT = port;

    }

    /**
     * POP3 Client Debug Feature
     *
     * @return the debug mode state
     */
    public boolean isDebug() {
        return debug;
    }

    /**
     * Handles connecting to a specific server and initializes needed fields.
     *
     * @param host the host
     * @param port the port
     * @throws IOException reader or writer initialization failed
     */
    public void connect() {

        try {
            SSLSocketFactory ssf = (SSLSocketFactory) SSLSocketFactory.getDefault();

            socket = ssf.createSocket(HOST, PORT);
        } catch (IOException e) {
            e.printStackTrace();
        }


        initStreams();


        if (isConnected()) {
            if (debug) {
                System.out.println("Client is connected to host !!");
            }
        } else {
            System.out.println("Client isn't connected!!");
        }


    }


    private void initStreams() {

        try {
            writer = new DataOutputStream(socket.getOutputStream());
            reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Used to disconnect the client.
     *
     * @throws IOException if POP3-Client isn't connected
     */
    public void disconnect() throws IOException {

        if (!isConnected())
            throw new IllegalStateException("Not connected to a host!!");


        socket.close();
        reader.close();
        writer.close();

        if (debug)
            System.out.println("Disconnected from host!!!");
    }

    /**
     * Used to set the debug mode.
     *
     * @param debug debug mode
     */
    public void setDebug(boolean debug) {

        this.debug = debug;
    }


    /**
     * Check whether the client is connected or not.
     *
     * @return true -> Client is connected to a Server
     * false -> Client isn't connected to a Server
     */
    private boolean isConnected() {

        return socket != null && socket.isConnected();
    }

    /**
     * Reads one line of data sent by the server.
     *
     * @return the read line
     * @throws IOException server returned an error.
     */
    protected String singleLineResponse() {

        String response = null;

        try {
            response = reader.readLine();
        } catch (IOException e) {
            e.printStackTrace();
        }


        if (debug) {
            System.out.println("DEBUG [in]: " + response);
        }


        return response;
    }

    protected String multiLineResponse() {

        StringBuilder sb = new StringBuilder();

        String line;

        while (!(line = singleLineResponse()).equals(".")) {
            sb.append(line);
            sb.append("\n");
        }

        return sb.toString();
    }

    /**
     * Sends data (commands) to the server.
     *
     * @param command the command, which would be processed by the server
     * @return one line from server response
     * @throws IOException if command couldn't be sent to the server
     */
    protected void sendData(String command) throws IOException {

        if (debug) {
            System.out.println("DEBUG [out]: " + command);
        }

        writer.writeBytes(command);
        writer.flush();

    }


    /**
     * Login user to server.
     *
     * @param username the username
     * @throws IOException if sendCommand() failed
     */
    public String user(String username) {

        try {
            sendData("USER " + username);
        } catch (IOException e) {
            e.printStackTrace();
        }


        return singleLineResponse();

    }

    public String password(String password) {

        try {
            sendData("PASS" + password);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return singleLineResponse();
    }

    public String quit() {
        try {
            sendData("QUIT");
        } catch (IOException e) {
            e.printStackTrace();
        }

        return singleLineResponse();
    }

    public String retr(int num) {

        try {
            sendData("RETR" + num);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return multiLineResponse();
    }

    public String list() {

        try {
            sendData("LIST");
        } catch (IOException e) {
            e.printStackTrace();
        }
        return multiLineResponse();
    }

    /**
     * Used to check for number of messages on the server.
     *
     * @return number of messages
     * @throws IOException sendCommand() failed.
     */
    public int getNumberOfNewMessages() throws IOException {


        String amount = list().split(" ")[1];

        return Integer.parseInt(amount);

    }

    /**
     * Gets a message from the server.
     * This method uses RETR to get a message from the server.
     * After sending the command, the server starts sending a message.
     * Firstly the headers (terminated by \0) followed by body (terminated by ".").
     * <p>
     * First loop:
     * - goes through all headers and saves it to the map "headers" -> Format : headerName:headerValue
     * <p>
     * Second loop:
     * - goes through all body response lines until the termination character is found
     *
     * @param i message index
     * @return message from server
     * @throws IOException if sendCommand() failed
     */
    protected Message getMessage(int i) throws IOException {

        String response = retr(i);

        Map<String, List<String>> headers = new HashMap<String, List<String>>();
        String headerName = null;

        //process headers
        while ((response = singleLineResponse()).length() != 0) {
            if (response.startsWith("\t")) {
                continue;
            }


            int colonPosition = response.indexOf(":");

            headerName = response.substring(0, colonPosition);
            String headerValue;

            if (headerName.length() > colonPosition) {
                headerValue = response.substring(colonPosition + 2);
            } else {
                headerValue = "";
            }

            List<String> headerValues = headers.get(headerName);

            if (headerValues == null) {

                headers.put(headerName, headerValues);

            }

            headerValues.add(headerValue);

        }

        StringBuilder bodyBuilder = new StringBuilder();

        while (!(response = singleLineResponse()).equals(".")) {
            bodyBuilder.append(response + "\n");
        }

        return new Message(headers, bodyBuilder.toString());

    }

    /**
     * Get the list of messages from the server.
     *
     * @return list of messages
     * @throws IOException if getNumberofNewMessages() failed
     */
    public List<Message> getMessages() throws IOException {

        int num = getNumberOfNewMessages();
        List<Message> messageList = new ArrayList<Message>();

        for (int i = 1; i <= num; i++) {

            messageList.add(getMessage(i));
        }

        return messageList;
    }
}
