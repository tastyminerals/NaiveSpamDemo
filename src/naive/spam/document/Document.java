package naive.spam.document;

/* This class represents a document of ? type */
public class Document {
    private String emailType;
    private String body;
    private String[] bodyArray = null;
    
    public Document (String type, String text) {
        this.emailType = type;
        this.body = text;
    }
    
    public void makeBodyArray() {
        if (!this.body.isEmpty()) {
            this.bodyArray = this.body.split(System.getProperty("line.separator"));
        }
    }
    
     /* Retrieving all words in the email body */
    public String[] getBodyWords() {
        return body.split("\n");
    }
    
    public String getEmailType() {
        return emailType;
    }

    public String getBody() {
        return body;
    }

    public String[] getBodyArray() {
        return bodyArray;
    }
}
