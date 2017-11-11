import java.io.*;
import java.net.*;
import java.util.List;

public class POP3Server implements Runnable {
    String response;    // To Client
    String request;     // From Client
    int port;

    ServerSocket ss;

    public POP3Server(int port) {
        this.port = port;
    }

    public void run(){
        while(true){
            try{
                ss = new ServerSocket(this.port);
                Socket cs = ss.accept();

                BufferedReader in = new BufferedReader(new InputStreamReader(cs.getInputStream())); // Setup Listener
                DataOutputStream out = new DataOutputStream(cs.getOutputStream());                  // Setup Reader

                request = in.readLine();
                if(request.equalsIgnoreCase("quit")){
                    response = "+OK :: Quitting connection.";
                    out.writeBytes(response);
                    cs.close();
                }
                if(request.equalsIgnoreCase("list")){
                    response = "ERROR 511";
                    out.writeBytes(response);
                }
                if(request.equalsIgnoreCase("retr")){
                    response = "ERROR 512";
                    out.writeBytes(response);
                }

            }catch(Exception e){
                e.printStackTrace();
                System.out.println("Terminating Server-Setup.");
                break;
            }
        }
    }
}