import java.io.*;
import java.util.Random;

/** Future ideas:
 *  - Would need some Mutex/Sem between writeAndShowFile() & run().
 *  ->> Reading from that data while something else writes to it will cause 'Dirty Read'
 */
public class TheRealThing extends Thread {
    private static float result = 0;
    private String filename;
    private int start;
    private int end;

    /**
     * Creates a new TheRealThing thread which operates
     * on the indexes start to end.
     */
    public TheRealThing(String filename, int start, int end) {
        this.filename = filename;
        this.start = start;
        this.end = end;
    }

    /**
     * Performs "eine komplizierte Berechnung" on array and
     * returns the result
     */
    private float eine_komplizierte_Berechnung(float[] array) {
        // Set synchronized here, in order to set some threadcontrolling somewhere.
        // TODO 'Optional: Make this interesting
        float value = 0;

        for(int i = 0; i < array.length; i++){
            value += array[i];
        }
        System.out.println(Thread.currentThread().getName() + " has calculated: " + value);

        return value;
    }

    synchronized public void run() {
        System.out.println("* Echo from " + Thread.currentThread().getName());
        int delta = this.end-this.start;                    // AB = B-A, vector-wise
        float[] subsetData = new float[delta];                  // -1 Indexoffset

        // Fetch arraydata
        /**FIXME
         * Still reading too much data.
         * - Problem being start & end
         * - Could probably mark entries with a certain delimiter in order to saferead
         */
        try{
            BufferedReader br = new BufferedReader(new FileReader(this.filename));
            System.out.println(Thread.currentThread().getName() + " is fetching Data.");
            int realReads;
            int lineCount = 0;
            for(realReads = 0; realReads < subsetData.length; ){
                String in = br.readLine();
                // Check if we're really reading a float
                if (in.matches("([0-9]*\\.[0-9]*)|([0-9]*)")) {   // Float = 'value'.'value'; 'value';
                    // Only read in the floats wanted, from Position a to b
                    lineCount++;
                    System.out.println("\t\"" + in + "\"  was read");
                    if ((lineCount <= this.end) && (lineCount >= this.start)) {
                        System.out.println("\t\t... and will be considered for calculation.");
                        subsetData[realReads] = Float.parseFloat(in);
                        realReads++;
                    } else {
                        System.out.println("\t\t... but remains ignored.");
                    }
                }else{
                    // Ignore that entry in .dat.
                }
            }

        }catch(Exception e){
            e.printStackTrace();
        }
        //synchronized(this){
            result = result + eine_komplizierte_Berechnung(subsetData);
        //}
        System.out.println("** Farewell from " + Thread.currentThread().getName());
    }

    /** Invoke the file
     */
    private static void writeAndShowFile(String path, int arraySize){
        File f = new File(path);
        Random r = new Random();

        System.out.println("-- Executing File creation.");
        try {
            // Create file
            PrintWriter pw = new PrintWriter(f);                                  // || DataOutputStream dos = new DataOutputStream(new FileOutputStream(f));
            pw.println("UE1_4 - Datafile used for Calcs");                        // || dos.writeUTF("UE1_4 - Datafile used for Calcs.\n");
            pw.print("ID: " + 42);                                                       // || dos.writeUTF(String.valueOf("42"));
            pw.println(" ||| Size: " + arraySize);                                              // || dos.writeUTF(String.valueOf(arraySize));

            for (int i = 0; i < arraySize; i++) {
                pw.println(r.nextFloat()*100);                                    // ||dos.writeUTF(String.valueOf(r.nextFloat()*100));                         // Float-allocs
            }
            pw.close(); /** IMPORTANT TO CLOSE/FLUSH, otherwise won't execute write file **/
        } catch (IOException e) {
            e.printStackTrace();
        }

        System.out.println("-- Human-Readable of myArrayData.dat");
        try{
            BufferedReader br = new BufferedReader(new FileReader(f));
            for(String line; (line = br.readLine()) != null;){
                System.out.println(line);
            }

        }catch(FileNotFoundException e){
            e.printStackTrace();
        }catch(IOException d){
            d.printStackTrace();
        }
    }

    public static void main(String[] args) {
        String pathToFile = "./myArrayData.dat";
        int numThreads = 2;
        int arraySize = 6;

        if(arraySize % numThreads != 0){
            System.out.println("Uneven values may cause malfunction" +
                    "\n\tStill processing.");
        }

        // Invoke the datafile to read/write from
        writeAndShowFile(pathToFile, arraySize);

        System.out.println("\n-- Executing program\n" +
                "\t#Threads: " + numThreads + "\n\t#Floats: " + arraySize);
        try{
            int execPerT = arraySize / numThreads;
            int start = 0;                           // Any array starts with 0
            int end = execPerT;

            Thread[] arrThread = new Thread[numThreads];
            for(int i = 0; i < arrThread.length; i++){
                    TheRealThing th = new TheRealThing(pathToFile, start, end);
                    arrThread[i] = new Thread(th);
                    arrThread[i].start();       /** For fucks sake dont use .run() **/

                    // Increment start/end
                    start += execPerT;
                    end += execPerT;
            }

            for(int j = 0; j < arrThread.length; j++){
                arrThread[j].join();
            }

        }catch(Exception e){
            e.printStackTrace();
        }
        System.out.println("\tResult of calculations: " + result);
    }
}