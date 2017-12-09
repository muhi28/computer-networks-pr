import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.util.Formatter;

/** Plain class for UE1_1. **/
public class Support {
    private Formatter x;

    public void initEmptyTestFile() throws IOException {
        x = new Formatter("emptyFile.txt"); // Uses standardpath (UE1 ~...)
        x.format("%s", "");

        System.out.println(">>emptyFile.txt has been updated.");
        x.close();
    }

    public void initWrittenTestFile() throws IOException{
        x = new Formatter("writtenFile.txt");
        x.format("%s%s%s%s","First.\n", "Second\n", "lorem Ipsum\n", "4.Line\n");

        System.out.println(">>writtenFile.txt has been updated.");
        x.close();
    }
}
