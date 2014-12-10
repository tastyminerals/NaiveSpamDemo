package naive.spam.test;

import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;
import naive.spam.document.Document;

/* This class classifies test doc(email) according to their assigned
 probability */
public class TestClassifier {

    private final double defaultSpam;
    private final double defaultHam;
    private final Set<Document> docs;
    private Set<Document> spam = new HashSet<>();
    private Set<Document> ham = new HashSet<>();
    private Set<String> filter = new HashSet<String>();

    public TestClassifier(Set<Document> ds, double defS, double defH, String v) {
        this.docs = ds;
        this.defaultSpam = defS;
        this.defaultHam = defH;
        filter = new HashSet(Arrays.asList(v.split(System.getProperty("line.separator"))));
    }

    public void classify(HashMap<String, HashMap> trainedProbs) {
        //calculating probability for each doc
        for (Document d : getDocs()) {
            for (String w : d.getBodyWords()) {
                // getting the probability of a test word
                if (trainedProbs.get(w) != null) {
                    HashMap<String, Double> trainedProb = trainedProbs.get(w);
                    d.addSpamProb(trainedProb.get("spam"));
                    d.addHamProb(trainedProb.get("ham"));
                } else {
                    if (filter.contains(w)) {
                        d.addSpamProb(defaultSpam);
                        d.addHamProb(defaultHam);
                    }
                }
                //System.out.println(w + " " + d.getSpamProb() + " " + d.getHamProb());
            }
//            System.out.println(d.getEmailType());
//            System.out.println(d.getSpamProb() + " " + d.getHamProb());
        }

        //assigning docs to predicted types
        for (Document d : getDocs()) {
            if (d.getEmailType() == null) {
                continue;
            }
            if (d.getSpamProb() > d.getHamProb()) {
                getSpam().add(d);
            } else if (d.getSpamProb() < d.getHamProb()) {
                getHam().add(d);
            } else {
                getHam().add(d);
            }
        }
    }

    public void checkPrecision() {
        //caclulating misclassified docs
        int allDocs = 0;
        int spamErr = 0;
        int hamErr = 0;
        float prec = 0;

        //calc spam errors
        for (Document d : getSpam()) {
            if (d.getEmailType() == null) {
                continue;
            }
            if (d.getEmailType().equals("#*#*# ham")) {
                spamErr++;
            }
            allDocs++;
        }
        //calc ham errors
        for (Document d : getHam()) {
            if (d.getEmailType() == null) {
                continue;
            }
            if (d.getEmailType().equals("#*#*# spam")) {
                hamErr++;
            }
            allDocs++;
        }
        prec = (float) (spamErr + hamErr) / allDocs;
        System.out.println("Spam errors: " + spamErr + ", Ham errors: " + hamErr + ", All docs: " + allDocs);
        System.out.println("Prec: " + (1 - prec));
    }

    public double getDefaultSpam() {
        return defaultSpam;
    }

    public double getDefaultHam() {
        return defaultHam;
    }

    public Set<Document> getDocs() {
        return docs;
    }

    public Set<Document> getSpam() {
        return spam;
    }

    public Set<Document> getHam() {
        return ham;
    }

}
