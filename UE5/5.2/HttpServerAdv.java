import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

public class HttpServerAdv {
    private int port;
    private int clientCount = 0;

    public static void main(String[]args) throws IOException {
        int port = 6789;
        HttpServerAdv hsa = new HttpServerAdv(port);
        hsa.startHttpsServer();
    }

    public HttpServerAdv(int port){
        this.port = port;
    }

    public void startHttpsServer() throws IOException{
        ServerSocket ss = new ServerSocket(this.port);
        while(true){
            Socket cs = ss.accept();
            new Thread(new Runnable(){
                public void run(){
                    System.out.println(Thread.currentThread().getId() + " working on " + (++clientCount));
                    startCom(cs);
                }
            }).start();
        }
    }
    private void startCom(Socket cs){
        try{
            // Setup reader
            BufferedReader inFromClient = new BufferedReader(new InputStreamReader(cs.getInputStream()));
            String request = inFromClient.readLine();
            System.out.println("Incomming Request:\t" + request);

            if(request.matches("((\\/\\.\\.)+\\/)")){
                System.out.println("Malicious command caught. Terminating.");
                Thread.currentThread().interrupt(); // Dont use stop()
            }

            if(request.contains("GET")){
                // Fetch datapath
                String path = request.substring(request.indexOf('/'), request.indexOf('H')-1);
                File data = fetchFile(path);

                /**Data ONLY contains some index.html, otherwise ignored.**/
                // TODO -- Send file to client
                if(data != null && data.exists()){

                }
            }

            // Exit
            System.out.println(Thread.currentThread().getId() + " is terminating.");
        }catch(Exception e){
            System.out.println("\tError in startCom() :: ");e.printStackTrace();
        }
    }

    private File fetchFile(String path) {
        // Create the path to our file(s)
        String pwd = System.getProperty("user.dir"); // Path up to working dir
        String psd = "/UE5/5.1/documentRoot";        // Path up to source dir -- FIXME: needs a better solution
        String pathToFile = pwd + psd + path;        // Concat Paths for full path to file

        if(!new File(pathToFile).exists()){
            System.out.println("\tNonexistant File");
        }
        File f = new File(pathToFile);
        if(f.isFile()){
            System.out.println("\t\tReturning valid File: " + f.getPath());
            return f;
        }
        if(f.isDirectory()){
            System.out.println("\t\tDetected valid Dir: " + f.getPath());
            File[] subFiles = f.listFiles();
            for(File subFile : subFiles){
                System.out.println("File: " + subFile.getPath());
                if(subFile.isFile() && subFile.getPath().contains("index.html")){
                    return subFile;
                }
            }
        }
        return null;
    }
}
