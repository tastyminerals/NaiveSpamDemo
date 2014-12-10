package naive.spam.train;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/* Creating a HashMap of words probabilities with absolute discounting */
public class Coach {
    /*counts has the following structure:
     {word1 = {spam = 2, ham = 5}, word2 = {spam = 1, ham = 0}, ...} */

    private HashMap<String, HashMap> counts = new HashMap<String, HashMap>();

    //default probability for unknown words
    private double defaultSpamProb;
    private double defaultHamProb;

    private double getBayesProb(int typeFreq, float smoothing,
            int allTypeFreq, int typeCounts) {
        double p1 = 0;
        if (typeFreq != 0) {
            p1 = (double) ((typeFreq - smoothing) / (double) allTypeFreq);
        }
        double p2 = ((((double) smoothing * (double) typeCounts)
                / (double) allTypeFreq) * (1d / 100000d));
        return (double) Math.log(p1 + p2);
    }

    public Coach(HashMap distr) {
        this.counts = distr;
    }

    /* Don't use it for data that uses other types than "spam" and "ham" */
    public HashMap<String, HashMap> trainSpamHam(HashMap spamDists, HashMap hamDists, float smoothing) {
        Iterator it = counts.entrySet().iterator();
        HashMap<String, HashMap> typeWordProbs = new HashMap<>();
        HashMap<String, Double> wordProbs;

        // calculating "spam" word frequencies
        int allSpamFreq = 0;
        int allHamFreq = 0;

        while (it.hasNext()) {
            Map.Entry pairs = (Map.Entry) it.next();
            HashMap vals = (HashMap) pairs.getValue();
            //E N(xi, wk)
            allSpamFreq = allSpamFreq + (int) vals.get("spam");
            allHamFreq = allHamFreq + (int) vals.get("ham");
        }

        //n+
        int spamCounts = ((HashMap) spamDists.get("#*#*# spam")).size();
        int hamCounts = ((HashMap) hamDists.get("#*#*# ham")).size();

        // calculating probabilities for every word in "spam" and "ham"
        for (Map.Entry hm : counts.entrySet()) {
            wordProbs = new HashMap<>();

            //word
            String kw = (String) hm.getKey();

            //{spam=5, ham=1}
            HashMap vals = (HashMap) hm.getValue();

            //N (wi, wk)
            int spamFreq = (int) vals.get("spam");
            int hamFreq = (int) vals.get("ham");

            //calculating probabilities
            double wordProbPerSpam = getBayesProb(spamFreq, smoothing, allSpamFreq, spamCounts);
            double wordProbPerHam = getBayesProb(hamFreq, smoothing, allHamFreq, hamCounts);
            wordProbs.put("spam", wordProbPerSpam);
            wordProbs.put("ham", wordProbPerHam);
            typeWordProbs.put(kw, wordProbs);
//            if (kw.equals("BIRDYDK@aol.com")) {
//                System.out.println(hm + " " + wordProbPerSpam + " " + wordProbPerHam);
//            }
        }

        setDefaultSpamProb(getBayesProb(0, smoothing, allSpamFreq, spamCounts));
        setDefaultHamProb(getBayesProb(0, smoothing, allHamFreq, hamCounts));
        return typeWordProbs;
    }

    public double getDefaultSpamProb() {
        return defaultSpamProb;
    }

    public void setDefaultSpamProb(double defaultProb) {
        this.defaultSpamProb = defaultProb;
    }

    public double getDefaultHamProb() {
        return defaultHamProb;
    }

    public void setDefaultHamProb(double defaultHamProb) {
        this.defaultHamProb = defaultHamProb;
    }
}
