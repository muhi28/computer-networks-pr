import java.io.*;
import java.nio.charset.Charset;
import java.util.Scanner;

import static java.lang.Thread.sleep;

/**
 * Invoked using main of control. Some meths have not beed implemented to it's full completion yet.
 */
public class Ue1_1 {
    Support sup = new Support();

    // a) Erklären Sie die Klassen OutputStream und InputStream.
    public void a_inputStream(){
        /**Subclasses: BufferedInputStream, ByteArrayInputStream,
         * DataInputStream, FilterInputStream, read(), OutputStream, PushbackInputStream
         */
        /**Implements: Closeable
         */
        /**What it does: This abstract class is the superclass of all classes
         * representing an input stream of bytes.
         * e.g.: Der InputStream liest einen Stream ein, etwa stdin und kann diesen
         * via Methoden zwischenspeichern.
         */
        /**Methoden:
         * available() >> Gibt die Anzahl an Bytes wieder die man noch schreiben kann.
         * close() >> Schließt Stream & befreit belegten Speicher
         * mark(int readLimit) >> markiert die derzeitige Position
         * markSupported() >> Probt ob mark(..) auf dem jew. Stream funktioniert
         * read() >> Liest das nächste Byte aus dem Stream
         * read(byte[] b) >> Liest Bytes aus dem InputStream & speichert diese
         * in dem Array b
         * read(byte[] b, int off, int len) >> Liest #len Bytes aus dem Inputstream
         * und speichert sie an der Position+Offset #len ins Array b.
         * reset() >> Setzt die Streamposition zurück zur gesetzten mark(..)
         * skip(long n) >> Überspringt & löscht n bytes aus dem InputStream
         */
        System.out.println("The inputstream is used in order to read data from a specific object, using the type byte.");
        try {
            sup.initWrittenTestFile();
        } catch (IOException e) {
            e.printStackTrace();
        }

        System.out.println("We will now read from that written File.\n" +
                "Using an 'int'-Buffer, we read 1 symbol at a time and convert it to the corresponding char.");
        try{
            InputStream is = new FileInputStream("writtenFile.txt");

            sleep(5000);
            int dataValue = 0;
            while(dataValue != -1){
                dataValue = is.read();
                System.out.println("Value =\t" + dataValue + "\tChar = " + (char) dataValue);
                sleep(600);
            }

            is.close();
            System.out.println("\nEnd of InputStream.\n");
        }catch(Exception e){
            e.printStackTrace();
        }
    }

    public void a_outputStream() {
        /**Subclasses: ByteArrayOutputStream, FileOutputStream, FilterOutputStream,
         * ObjectOutputStream, OutputStream, PipedOutputStream
         */
        /**Implements: Closeable, Flushable
         */
        /**What it does: This abstract class is the superclass of all classes
         * representing an output stream of bytes. An output stream accepts output
         * bytes and sends them to some sink.
         * e.g.: Der OutputStream nimmt Bytes entgegen und beschreibt eine
         * spezifische Stelle.
         */
        /**Methoden:
         * close() >> Schließt & befreit besetzten Speicher des Streams
         * flush() >> Löscht den Inhalt des Streams
         * write(byte[] b) >> Schreibt Byte aus dem Array in den Stream
         * write(byte[] b, int off, int len) >> Schreibt #len Bytes aus dem Array
         *   mit einem Indexoffset #off in den Stream
         * write(int b) >> Schreibt 'b' in den Stream
         */

        System.out.println("The Outputstream is the counterpart of the Inputstream. " +
                "Using it, we can write data from a certain buffer into a specific object.");
        try {
            sup.initEmptyTestFile();
        } catch (Exception e) {
            e.printStackTrace();
        }

        try {
            OutputStream os = new FileOutputStream("emptyFile.txt");    // Set onto correct file

            System.out.println("Enter something to write into 'emptyFile.txt'");
            Scanner s = new Scanner(System.in);
            String entry = "The entry was: " + s.nextLine();
            System.out.println(entry + " -- This needs to be properly handled, as OS requires Byte.\n");

            os.write(entry.getBytes(Charset.forName("UTF-8")));     // Execute writing
            System.out.println("Now check whether it has correctly written into 'emptyFile.txt'\n" +
                    "As a reminder: Try not to use a raw string.getBytes. It might encode wrong.");
            sleep(5000);
        } catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println("\nEnd of Outputstream.\n");
    }

    public void b_FilterInputStream(){
        System.out.println("The FilterInputStream uses any other Stream as source. It may transform data " +
                "along the way or provides other functionalities. The rest is analogue to the " +
                "usual InputStream");
        try {
            FileInputStream fis = new FileInputStream("writtenFile.txt");
            FilterInputStream finS = new BufferedInputStream(fis);

            sup.initWrittenTestFile();

            int r = 0;
            while(r != -1){
                r = finS.read();
                System.out.printf("%c", (char) r);
                sleep(400);
            }


        } catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println("\nEnd of FilterInputStream.\n");
    }


    public void b_FilterOutputStream(){
        System.out.println("The FilterOutputStream is the Out-Version of the FilterInputStream.\n " +
                "It uses some OutputStream as a source which can be modified using various" +
                "functionalities.\n We will now update the EmptyFile.txt and write \"Hello World.\" " +
                "into it.");

        try {
            FileOutputStream fos = new FileOutputStream("emptyFile.txt");
            FilterOutputStream filterOS = new FilterOutputStream(fos);
            sup.initEmptyTestFile();

            String s = "Hello World.";
            byte[] b = s.getBytes();
            for(int i = 0; i < b.length; i++){
                filterOS.write(b[i]);
            }
            sleep(5000);
            fos.close();
            System.out.println("The empty file now contains \""+ s + "\"");

        } catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println("\nEnd of FilterOutputStream.\n");

    }

    public void c_classDescriptions(){
        System.out.println("--- 1. BufferedInputStream\n ---" +
                "The BufferedInputStream is a in-stream which uses a buffer and" +
                "supports mark() and reset().\nThis gives you the ability to jump" +
                "from sign to sign within the buffer & to modify the input.");
        System.out.println("--- 2. DataOutputStream ---\n" +
                "The DataOutputStream uses primitive datatypes to write into some Out-Stream.\n");
        System.out.println("--- 3. CipherInputStream ---\n " +
                "A CipherInputStream is the combination of some In-Stream and a cipher\n" +
                "Data that comes through the InputStream will be directly used by the cipher\n" +
                "in order to decrypt it.");
        System.out.println("--- 4.ZipOutputStream ---\n" +
                "The ZipOutputStream uses any Out-Stream to write files using the .zip format.\n" +
                "It includes support for compressed and uncompressed entries.");
        System.out.println("--- 5. PushbackInputStream ---\n" +
                "The PushbackInputStream uses an In-Stream to read data.\n" +
                "Using unread() will allow you to put data back into the buffer to read it again later on.\n\n");

    }

    public void d_readerAndWriter(){
        System.out.println("--- Reader and Writer ---");
        System.out.println("A Reader 'reads' from character stream. It uses read(char[]. int, int) and close().");
        System.out.println("A Writer 'writes' to character streams. Using write(char[], int, int), flush() and close().");
    }

    public void init(){
        Scanner sc = new Scanner(System.in);
        int choice = 0;

        do{
            System.out.println("####################\n### Subsection 1 ###");
            System.out.println("Enter a value to play the spec. subsection.\n" +
                    "Options: 1, 2, 3, 4, otherwise press 5 to exit. Enter 6 to return to Main.");
            choice = sc.nextInt();
            switch(choice){
                case 1:
                    System.out.println("Which part do you wish to execute?\n1 - Inputstream || 2 - Outputstream");
                    choice = sc.nextInt();
                    if(choice == 1){
                        a_inputStream();
                    }

                    if(choice == 2){
                        a_outputStream();
                    }

                    break;
                case 2:
                    System.out.println("Which part do you wish to execute?\n1 - FilterInputStream || 2 - FilterOutputStream");
                    choice = sc.nextInt();
                    if(choice == 1){
                        b_FilterInputStream();
                    }

                    if(choice == 2){
                        b_FilterOutputStream();
                    }

                    break;
                case 3:
                    c_classDescriptions();
                    break;
                case 4:
                    d_readerAndWriter();
                    break;
                case 5:
                    System.exit(0);
                default:
                    if(choice != 6){
                        System.out.println("Unable to determine exersice. Try again.");
                    }
                    break;
            }
        }while(choice != 6); // Return to mainprogramm
    }
}