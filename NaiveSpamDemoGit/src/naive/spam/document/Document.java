package naive.spam.document;

/* This class represents a document of ? type */
public class Document {

    private String emailType;
    private String body;
    private String[] bodyArray = null;
    private double spamProb = 0d;
    private double hamProb = 0d;

    public Document(String type, String text) {
        this.emailType = type;
        this.body = text;
    }

    public void makeBodyArray() {
        if (!this.body.isEmpty()) {
            this.bodyArray = this.body.split(System.getProperty("line.separator"));
        }
    }

    public void addSpamProb(double spamP) {
        //this.spamProb = (double) Math.abs(this.spamProb + spamP);
        this.spamProb = (double) this.spamProb + spamP;
    }

    public void addHamProb(double hamP) {
        //this.hamProb = (double) Math.abs(this.hamProb + hamP);
        this.hamProb = (double) this.hamProb + hamP;
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

    public double getSpamProb() {
        return this.spamProb;
    }

    public double getHamProb() {
        return this.hamProb;
    }

}
