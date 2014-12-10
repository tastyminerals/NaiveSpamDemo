package naive.spam.input;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class InternalResReader {

    /* Reading data from file and returning a String */
    public String getResource(String filename) {
        String line = null;
        StringBuilder content = new StringBuilder();
        InputStream stream = InternalResReader.class.getResourceAsStream(filename);
        try (BufferedReader bf = new BufferedReader(new InputStreamReader(
                stream))) {

            while ((line = bf.readLine()) != null) {
                content.append(line);
                content.append(System.getProperty("line.separator"));
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
        return content.toString();
    }

}
