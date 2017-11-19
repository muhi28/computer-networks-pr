import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

public class HttpServer{
    private int port;
    private String request;
    private BufferedReader inFromClient;

    private boolean debug_printWholeRequest = false;
    private boolean debug_fetchFile = true;

    public HttpServer(int port){
        this.port = port;

    }

    public void start(){
        try{
            // Setup SS
            ServerSocket ss = new ServerSocket(this.port);

            // Await requests
            while(true){
                // Setup CS
                Socket cs = ss.accept();

                // Setup readable for GET request
                inFromClient = new BufferedReader(new InputStreamReader(cs.getInputStream()));

                // Expect input
                request = inFromClient.readLine();
                System.out.printf("\nIncomming Request:\t" + request);

                // For Debugging: Print Request
                if(debug_printWholeRequest){ printGetRequest(); }

                // Check for correct GET-Request
                /** Example: http://localhost:6789**/
                if(request.contains("GET")){
                    // Fetch the data
                    String path = request.substring(request.indexOf('/'), request.indexOf('H')-1);
                    File data = fetchFile(path);
                    if((data != null && data.exists())) {
                        //Setup Datatransfer
                        byte[] byteBuffer = new byte[(int) data.length()];
                        BufferedInputStream bufferToClient = new BufferedInputStream(new FileInputStream(data));
                        bufferToClient.read(byteBuffer,0, byteBuffer.length);
                        // Send Data
                        OutputStream os = cs.getOutputStream();
                        String CLRF = "\r\n\r\n";
                        String httpResponseHeader = "HTTP/0.9 200 OK" + CLRF;
                        os.write(httpResponseHeader.getBytes("UTF-8"));
                        os.write(byteBuffer, 0, byteBuffer.length);
                        os.flush();
                    }
                }
                // After Transfer, terminate data
                cs.close();
            }
        }catch(Exception e){
            e.printStackTrace();

        }
    }

    /**
     * @param path of the file requested
     * @return file of requested path
     */
    private File fetchFile(String path) {
        // Create the path to our file(s)
        String pwd = System.getProperty("user.dir"); // Path up to working dir
        String psd = "/UE5/5.1/documentRoot";                   // Path up to source dir -- FIXME: needs a better solution
        String pathToFile = pwd + psd + path;        // Concat Paths for full path to file

        if(debug_fetchFile){
            System.out.println("\nPath:\t\t\t" + path);
            System.out.println("Working Dir:\t" + pwd);
            System.out.println("Source Dir:\t\t" + psd);
            System.out.println("WD + SD:\t\t" + pathToFile);
            System.out.println("Absolute path: \t/home/thompson/Uni/WS2017/RN-UE/Computer-Networks/UE5/5.1/");
        }

        if(!new File(pathToFile).exists()){
            System.out.println("\tNonexistant File");
        }

        File requestedFile = new File(pathToFile);
        if(requestedFile.isDirectory()){
            System.out.println("\tDetected valid dir.");
            File[] subFiles = requestedFile.listFiles();
            for (File f : subFiles) {
                System.out.println("File exists:\t" + f.exists());
                System.out.println("File Path: \t" + f.getPath());
                if(f.isFile() && f.getPath().contains("index.html")){
                    return f;
                }
            }
        }//else: return file
        else if(requestedFile.isFile()){
            System.out.println("\tReturning valid file.");
            return requestedFile;
        }
        return null;
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