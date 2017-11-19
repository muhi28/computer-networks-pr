import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;

public class HttpServer{
    private int port;
    private String request, response;
    private ServerSocket ss;
    private Socket cs;
    private BufferedReader in;


    private boolean debug_printWholeRequest = false;

    public HttpServer(int port){
        this.port = port;

    }

    public void start(){
        try{
            // Setup SS
            ss = new ServerSocket(this.port);

            // Await requests
            while(true){
                // Setup CS
                cs = ss.accept();

                // Setup readable for GET/POST request
                in = new BufferedReader(new InputStreamReader(cs.getInputStream()));

                // Expect input
                request = in.readLine();
                System.out.println("Incomming Request:\t" + request);

                // For Debugging: Print Request
                if(debug_printWholeRequest){ printGetRequest(); }

                // Check for correct GET-Request
                /** Example: http://localhost:6789/documentRoot/index.html**/
                // TODO: Send the index.html aswell as its pictures
                if(request.contains("GET")){
                    String answer = "Mai boi";
                    response = "HTTP/1.1 200 OK\r\n\r\n" + answer;
                    cs.getOutputStream().write(response.getBytes("UTF-8"));
                }

                // After Transfer, terminate data
                cs.close();
            }
        }catch(Exception e){
            e.printStackTrace();

        }
    }

    /**
     * Method entirely for debugging on fetched request
     * @throws Exception
     */
    private void printGetRequest() throws Exception{
        if(request.contains("GET")){    System.out.println("- Request contains GET");  }
        if(request.contains("/")){      System.out.println("- Request contains some Path"); }
        if(request.contains("HTTP")){   System.out.println("- Request contains HTTP-Format"); }
        System.out.println("--------- Full request ----------");
        while(!request.isEmpty()){
            System.out.println("\t"+request);
            request = in.readLine();
        }
        System.out.println("--------- End of printGetRequest ----------");
    }

    public static void main(String[]args){
        int port = 6789;
        HttpServer hs = new HttpServer(port);
        hs.start();
    }
}