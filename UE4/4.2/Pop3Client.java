import java.io.*;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Pop3Client {

    private Socket socket;

    private boolean debug = false;

    private BufferedReader reader;
    private BufferedWriter writer;

    private static final int DEFAUL_PORT = 110;

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

    /**
     * Connects to the default TCP-Port -> 110
     *
     * @param host the host
     * @throws IOException
     */
    public void connect(String host) throws IOException {
        connect(host, DEFAUL_PORT);
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
        reader = null;
        writer = null;

        if (debug)
            System.out.println("Diconneted from the host!!!");
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
     * @return
     *          true -> Client is connected to a Server
     *          false -> Client isn't connected to a Server
     */
    private boolean isConnected() {

        return socket != null && socket.isConnected();
    }

    /**
     * Reads one line of data sent by the server.
     *
     * @return the read line
     *
     * @throws IOException server returned an error.
     */
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

    /**
     * Sends data (commands) to the server.
     *
     * @param command the command, which would be processed by the server
     *
     * @return one line from server response
     *
     * @throws IOException if command couldn't be sent to the server
     *
     */
    protected String sendCommand(String command) throws IOException {

        if (debug) {
            System.out.println("DEBUG [out]: " + command);
        }

        writer.write(command + "\n");
        writer.flush();

        return readResponseLine();
    }


    /**
     * Login user to server.
     *
     * @param username the username
     * @param password the users password
     *
     * @throws IOException if sendCommand() failed
     */
    public void login(String username, String password) throws IOException {

        sendCommand("USER " + username);
        sendCommand("PASSW " + password);

    }

    /**
     * Logout user from server.
     *
     * @throws IOException if sendCommand() failed.
     */
    public void logout() throws IOException {
        sendCommand("QUIT");
    }


    /**
     * Used to check for number of messages on the server.
     *
     * @return number of messages
     *
     * @throws IOException sendCommand() failed.
     */
    public int getNumberOfNewMessages() throws IOException {

        String response = sendCommand("LIST");
        String[] values = response.split(" ");
        return Integer.parseInt(values[1]);

    }

    /**
     * Gets a message from the server.
     * This method uses RETR to get a message from the server.
     * After sending the command, the server starts sending a message.
     * Firstly the headers (terminated by \0) followed by body (terminated by ".").
     *
     * First loop:
     *              - goes through all headers and saves it to the map "headers" -> Format : headerName:headerValue
     *
     * Second loop:
     *              - goes through all body response lines until the termination character is found
     *
     * @param i message index
     *
     * @return message from server
     *
     * @throws IOException if sendCommand() failed
     */
    protected Message getMessage(int i) throws IOException {

        String response = sendCommand("RETR " + i);

        Map<String, List<String>> headers = new HashMap<String, List<String>>();
        String headerName = null;

        //process headers
        while ((response = readResponseLine()).length() != 0) {
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

        while (!(response = readResponseLine()).equals(".")) {
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
