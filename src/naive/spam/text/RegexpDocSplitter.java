package naive.spam.text;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import naive.spam.document.Document;

/* Splitting testing file and creating document objects */
public class RegexpDocSplitter {
    private String testData = null;
    
    public RegexpDocSplitter(String text) {
        this.testData = text;
    }
    
    /* Searching the test data for email types and creating Document
    objects with the text body and respective types */
    public Set<Document> getDocObjects() {
        String emailBody = null;
        String emailType = null;
        Pattern pType = Pattern.compile("#\\*#\\*# [a-z]+");
        Set<Document> testingSet = new HashSet<Document>();
        
        /* splitting line but retaining the delimiter 
        (delimiter is put before the chunk) */
        for (String chunk: testData.split("(?=#\\*#\\*# spam)|(?=#\\*#\\*# ham)")) {
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
