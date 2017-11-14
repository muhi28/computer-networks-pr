import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.Socket;

public class POP3Client {
    private String host;
    private int port;
    private Socket cs;
    private String request, response;

    POP3Client(String host, int port){
        this.host = host;
        this.port = port;
        // Trivial setups
        this.request = "";
        this.response = "";
    }

    public void run() {
        try{
            // Setup Scanner
            BufferedReader read = new BufferedReader(new InputStreamReader(System.in));

            // Setup Socket
            cs = new Socket(host, port);
            BufferedReader in = new BufferedReader(new InputStreamReader(cs.getInputStream()));
            DataOutputStream out = new DataOutputStream(cs.getOutputStream());

            // Await Handshake
            System.out.println("Expecting Handshake:\n\t");
            System.out.println("Handshake:\t" + response);

            // Now execute code
            while(true){
                request = read.readLine();
                if(request.equalsIgnoreCase("Quit")){
                    break;
                }
                out.writeBytes(request + "\n");

                response = in.readLine();
                System.out.println("FROM SERVER::\n\t"+response);
            }
            // Once Quit is hit
            System.out.println("Terminating.");
        }catch(Exception e){
            e.printStackTrace();
        }
    }
}
