import java.io.*;
import java.nio.charset.Charset;
import java.nio.charset.CharsetDecoder;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Scanner;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;


/** TODO
 * Problem invoking the issued charset in BufferedWriter below.
 * >> FIXME below
 *
 * Invoked using main of Control
 * */
public class Ue1_2 {
    private File fIn, fOut;
    private String outName;

    public void init(){
        System.out.println("Executing UE1_2.");
        fetchFilePathsFromCmd();
        executeZipTransformation();
        System.out.println("End of UE1_2");
    }

    private void fetchFilePathsFromCmd(){
        System.out.println("Enter File-Path.\n" +
                "Loc.: /home/thompson/Downloads/TODO/RNP-UE/UE1/utf8files\n" +
                "\tFile.: file1.txt || file2.txt\n");
        Scanner s = new Scanner(System.in);
        Path p = Paths.get(s.nextLine());

        System.out.printf("\nPath entered: " + p.toString() + "\n\tChecking validity: ");
        fIn = new File(String.valueOf(p));
        if(!fIn.exists()){
            System.out.printf("File not existant.\n");
        }
        else{
            System.out.printf("Existing file found.\n");
        }
        System.out.printf("Enter an output name. >>");
        outName = ( s.nextLine()+".zip");
    }

    private void executeZipTransformation(){
        fOut = new File(outName);
        ZipEntry zE = new ZipEntry("CompressedData.txt"); //  Data within Zip

        try {
            BufferedReader br = new BufferedReader(new FileReader(fIn));

            ZipOutputStream zos = new ZipOutputStream(new FileOutputStream(fOut));
            // FIXME: Encoding remains as System.getProperty("file.encoding") => UTF-8
            BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(zos, Charset.forName("ISO-8859-1")));
            zos.putNextEntry(zE);

            String line = null;
            while((line = br.readLine()) != null){
                bw.append(line).append('\n');
            }
            bw.flush(); // According to stackoverflow :: Good convention

            zos.closeEntry();
            zos.finish();

            br.close();
            bw.close();

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
