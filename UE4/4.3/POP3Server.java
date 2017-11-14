import java.io.*;
import java.net.*;

/** ExecServer-Architecture
 */
public class POP3Server implements Runnable {
    private String response;    // To POP3Client
    private String request;     // From POP3Client
    private int port;
    private int clientCount = 0;
    private Socket cs;
    private ServerSocket ss;
    private boolean hs_sem = false;

    public POP3Server(int port) {
        this.port = port;
    }

    /**
     * Runs ExecServer
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
                BufferedReader in = new BufferedReader(new InputStreamReader(cs.getInputStream()));
                DataOutputStream out = new DataOutputStream(cs.getOutputStream());

                // Display whenever somebody connects to ExecServer
                if(cs.isConnected()){
                    System.out.printf("POP3Client %d - Connected.\n", ++clientCount);
                }
                // TODO: Redirect to unique thread
                if(!hs_sem){
                    // Handshake by ExecServer
                    response = "Hello from POP3Server.";
                    out.writeBytes(response);
                    out.flush();
                }
                // Expect Input
                System.out.println("Expecting Input.");
                request = in.readLine();
                System.out.println("Ordered: " + request);
                // Cases for Input
                if(request.equalsIgnoreCase("quit")){
                    response = "+OK :: Quitting connection.";
                    out.writeBytes(response);
                    out.flush();
                    cs.close();
                }else if(request.equalsIgnoreCase("list")){
                    // TODO
                    response = "-ERR: 511";
                    out.writeBytes(response);
                    out.flush();
                }else if(request.equalsIgnoreCase("retr")){
                    // TODO
                    response = "-ERR: 512";
                    out.writeBytes(response);
                    out.flush();
                }else{
                    response = "-ERR: Misread Command. Enter again.";
                    System.out.println(response);
                    out.writeBytes(response);
                    out.flush();
                }

                System.out.println("End of loop - Repeating");
            }catch(Exception e){
                e.printStackTrace();
                System.out.println("Terminating ExecServer-Setup.");
                break;
            }
        }
    }
}