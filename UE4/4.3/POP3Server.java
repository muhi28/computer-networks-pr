import java.io.*;
import java.net.*;
import java.util.List;

/** Server-Architecture
 * @method POP3Server - Constructor
 * @method run() - Server Startup
 */
public class POP3Server implements Runnable {
    private String response;    // To Client
    private String request;     // From Client
    private int port;
    private int clientCount = 0;
    private Socket cs;
    private ServerSocket ss;

    public POP3Server(int port) {
        this.port = port;
    }

    /**
     * Runs Server
     */
    public void run(){
        try{
            // Setup Socket & Acceptor
            ss = new ServerSocket(this.port);
            cs = ss.accept();
        }catch(Exception e){
            e.printStackTrace();
        }
        while(true){
            try{
                // Setup Writer & Reader-Pipes
                BufferedReader in = new BufferedReader(new InputStreamReader(cs.getInputStream())); // Setup Listener
                BufferedWriter out = new BufferedWriter(new OutputStreamWriter(cs.getOutputStream()));

                // Display whenever somebody connects to Server
                if(cs.isConnected()){
                    System.out.printf("Client %d - Connected.\n", ++clientCount);
                }
                // Handshake by Server
                response = "Hello From Server.";
                out.write(response);
                out.flush();

                // Expect Input
                System.out.println("Expecting Input.");
                request = in.readLine();
                System.out.println(request + response);
                // Cases for Input
                if(request.equalsIgnoreCase("quit")){
                    response = "+OK :: Quitting connection.";
                    out.write(response);
                    out.flush();
                    cs.close();
                }else if(request.equalsIgnoreCase("list")){
                    // TODO
                    response = "-ERR: 511";
                    out.write(response);
                    out.flush();
                }else if(request.equalsIgnoreCase("retr")){
                    // TODO
                    response = "-ERR: 512";
                    out.write(response);
                    out.flush();
                }else{
                    response = "-ERR: Misread Command. Enter again.";
                    System.out.println(response);
                    out.write(response);
                    out.flush();
                }

                System.out.println("End of loop - Repeating");
            }catch(Exception e){
                e.printStackTrace();
                System.out.println("Terminating Server-Setup.");
                break;
            }
        }
    }
}