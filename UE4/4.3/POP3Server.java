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
                BufferedWriter out = new BufferedWriter(new OutputStreamWriter(cs.getOutputStream()));

                if(cs.isConnected()){
                    System.out.println("Client Connected.");
                }
                response = "Hello From Server.";
                out.write(response);

                System.out.println("Expecting Input.");
                request = in.readLine();
                if(request.equalsIgnoreCase("quit")){
                    response = "+OK :: Quitting connection.";
                    out.write(response);
                    cs.close();
                }
                if(request.equalsIgnoreCase("list")){
                    response = "ERROR 511";
                    out.write(response);
                }
                if(request.equalsIgnoreCase("retr")){
                    response = "ERROR 512";
                    out.write(response);
                }

            }catch(Exception e){
                e.printStackTrace();
                System.out.println("Terminating Server-Setup.");
                break;
            }
        }
    }
}