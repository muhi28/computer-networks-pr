import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

public class HttpServer{
    private int port;
    private String request;
    private File data;
    private ServerSocket ss;
    private Socket cs;
    private BufferedReader inFromClient;
    private BufferedInputStream bufferToClient;
    private OutputStream os;
    private byte[] byteBuffer;

    private final String CLRF = "\r\n\r\n";
    private final String httpResponseHeader = "HTTP/0.9 200 OK" + CLRF;

    private boolean debug_printWholeRequest = false;
    private boolean debug_fetchFile = true;

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
                inFromClient = new BufferedReader(new InputStreamReader(cs.getInputStream()));

                // Expect input
                request = inFromClient.readLine();
                System.out.println("Incomming Request:\t" + request);

                // For Debugging: Print Request
                if(debug_printWholeRequest){ printGetRequest(); }

                // Check for correct GET-Request
                /** Example: http://localhost:6789/documentRoot/index.html**/
                if(request.contains("GET")){
                    // Fetch the data
                    String path = request.substring(request.indexOf('/'), request.indexOf('H')-1);

                    // TODO:
                    /*if(){
                        // Path matches a directory
                    }else{
                        // Path matches a simple File
                        data = fetchFile(path);
                    }*/

                    //Setup Datatransfer
                    setupTransfer();
                    // Send Data
                    sendData();
                }
                // After Transfer, terminate data
                cs.close();
            }
        }catch(Exception e){
            e.printStackTrace();

        }
    }

    private void sendData() throws IOException {
        // Send Data to client
        os = cs.getOutputStream();
        os.write(httpResponseHeader.getBytes("UTF-8"));
        os.write(byteBuffer, 0, byteBuffer.length);
        os.flush();
    }

    private void setupTransfer() throws IOException{
        // Transfer to client -- Buffer the datafile to send
        byteBuffer = new byte[(int) data.length()];
        bufferToClient = new BufferedInputStream(new FileInputStream(data));
        bufferToClient.read(byteBuffer,0,byteBuffer.length);
    }

    /**
     * @param path of the file requested
     * @return file of requested path
     */
    private File fetchFile(String path) {
        // Create the path to our file(s)
        String pwd = System.getProperty("user.dir"); // Path up to working dir
        String psd = "/UE5/5.1";                     // Path up to source dir -- FIXME: needs a better solution
        String pathToFile = pwd + psd + path;        // Concat Paths for full path to file

        if(debug_fetchFile){
            System.out.println("Working Dir:\n\t" + pwd);
            System.out.println("Source Dir:\n\t" + psd);
            System.out.println("WD + SD:\n\t" + pathToFile);
            System.out.println("Absolute path: \n\t/home/thompson/Uni/WS2017/RN-UE/Computer-Networks/UE5/5.1/documentRoot");
        }

        // Now link the file for real.
        File requestedFile = new File(pathToFile);
        // Check existance
        if(!requestedFile.exists()){
            System.out.println("Non-Existant File.");
        }
        return requestedFile;
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
            request = inFromClient.readLine();
        }
        System.out.println("--------- End of printGetRequest ----------");
    }

    public static void main(String[]args){
        int port = 6789;
        HttpServer hs = new HttpServer(port);
        hs.start();
    }
}