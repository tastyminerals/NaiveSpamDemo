package naive.spam.text;

import java.util.Arrays;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import naive.spam.document.Document;

/* Splitting testing file and creating document objects */
public class RegexpDocSplitter {

    private String testData = null;
    private String voc = null;

    public RegexpDocSplitter(String text) {
        this.testData = text;
    }

    /* Searching the test data for email types and creating Document
     objects with the text body and respective types */
    public Set<Document> getDocObjects() {
        
        String emailBody = null;
        String emailType = null;
        Pattern pType = Pattern.compile("#\\*#\\*# [a-z]+");
        Set<Document> testingSet = new HashSet<>();

        /* splitting line but retaining the delimiter
         (delimiter is put before the chunk) */
        for (String chunk : testData.split("(?=#\\*#\\*# spam)|(?=#\\*#\\*# ham)")) {
            Matcher match = pType.matcher(chunk);
            if (match.find()) {
                emailType = match.group();
            }
            emailBody = chunk.replaceFirst("#\\*#\\*# [a-z]+\n", "");
            testingSet.add(new Document(emailType, emailBody));
        }
        return testingSet;
    }

}
