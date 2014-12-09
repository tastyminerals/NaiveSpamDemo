package naive.spam.train;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/* Creating a HashMap of words probabilities with absolute discounting */
public class Coach {
    /*counts has the following structure:
     {word1 = {spam = 2, ham = 5}, word2 = {spam = 1, ham = 0}, ...} */
    private HashMap<String, HashMap> counts = new HashMap<String, HashMap>();

    private double getBayesProb(int spamFreq, float smoothing,
            int allSpamFreq, int spamCounts) {
        double p1 = 0;
        if (spamFreq != 0) {
            p1 = (double) ((spamFreq - smoothing) / (double) allSpamFreq);
        }
        double p2 = ((((double) smoothing * (double) spamCounts)
                / (double) allSpamFreq) * (1d / 100000d));
        return (double) Math.log(p1 + p2);
    }

    public Coach(HashMap distr) {
        this.counts = distr;
    }

    /* Don't use it for data that uses other types than "spam" and "ham" */
    public HashMap<String, HashMap> trainSpamHam(HashMap spamDists, HashMap hamDists, float smoothing) {
        Iterator it = counts.entrySet().iterator();
        HashMap<String, HashMap> typeWordProbs = new HashMap<>();

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
        double wProb = 0;
        for (Map.Entry hm : counts.entrySet()) {
            HashMap<String, Double> wordProbs = new HashMap<String, Double>();
            
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
            //System.out.println(typeWordProbs);
        }
        return typeWordProbs;
    }
}
