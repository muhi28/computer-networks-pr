import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

public class HttpServer{
    private int port;
    private String request, response;
    private ServerSocket ss;
    private Socket cs;
    private BufferedReader in;
    private FileOutputStream fos;

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
//                    String answer = "Mai boi";
  //                  response = "HTTP/1.1 200 OK\r\n\r\n" + answer;
    //                cs.getOutputStream().write(response.getBytes("UTF-8"));

                    // Fetch the data
                    String path = request.substring(request.indexOf('/'), request.indexOf('H')-1);
                    File data = fetchFile(path);

                    // Transfer to client
                byte[] mybyteArray = new byte[(int) data.length()];
                FileInputStream fis = new FileInputStream(data);
                BufferedInputStream bis = new BufferedInputStream(fis);
                bis.read(mybyteArray,0,mybyteArray.length);
                OutputStream os = cs.getOutputStream();
                System.out.println("Sending: " + data);
                os.write("HTTP/1.1 200 OK\r\n\r\n".getBytes("UTF-8"));
                os.write(mybyteArray, 0, mybyteArray.length);
                os.flush();
                System.out.println("C4yourself");
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
        String pwd = System.getProperty("user.dir"); // Path up to working dir
        String psd = "/UE5/5.1";                     // Path up to source dir -- FIXME: needs a better solution
        String pathToFile = pwd + psd + path;             // Concat Paths for full path to file


        if(pathToFile.equals("/home/thompson/Uni/WS2017/RN-UE/Computer-Networks/UE5/5.1/documentRoot")){
            System.out.println("Matching paths");
        }

        File requestedFile = new File(pathToFile);
        System.out.println("Pathing:\t\t\t"+  requestedFile.getPath());
        System.out.println("Absolute path: \t\t/home/thompson/Uni/WS2017/RN-UE/Computer-Networks/UE5/5.1/documentRoot");
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