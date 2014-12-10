package naive.spam.input;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

/* Simple text file reader class that uses BufferedReader and StringBuilder */
public class TextFileReader {

    private String line;
    private StringBuilder content;

    public String readFile(String filepath) throws IOException {
        FileReader fr = new FileReader(filepath);
        BufferedReader bf = new BufferedReader(fr);
        content = new StringBuilder();
        while ((line = bf.readLine()) != null) {
            content.append(line);
            content.append(System.getProperty("line.separator"));
        }
        return content.toString();
    }
}
