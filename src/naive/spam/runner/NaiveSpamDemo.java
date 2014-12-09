package naive.spam.runner;

import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Set;
import naive.spam.document.Document;
import naive.spam.feature.SimpleFeatureCalculator;
import naive.spam.input.TextFileReader;
import naive.spam.text.RegexpDocSplitter;
import naive.spam.train.Coach;

public class NaiveSpamDemo {

    public static void main(String[] args) {
        //final float smoothD = Float.valueOf(args[1]);
        final float smoothD = 0.7f;
        String fdata1 = null;
        String fdata2 = null;
        String fdata3 = null;
        String fdata4 = null;
        TextFileReader tfr = new TextFileReader();

        try {
            fdata1 = tfr.readFile("src/data/spam_training");
            fdata2 = tfr.readFile("src/data/ham_training");
            fdata3 = tfr.readFile("src/data/vocab_100000.wl");
            fdata4 = tfr.readFile("src/data/ham_spam_testing");
        } catch (IOException e) {
            System.out.println(e);
        }

        // calculating features
        SimpleFeatureCalculator sfc = new SimpleFeatureCalculator(fdata3);
        HashMap classes = sfc.calcClasses(fdata1);
        classes = sfc.calcClasses(fdata2);
        HashMap spamFeats = sfc.calcFeatures(fdata1);
        HashMap hamFeats = sfc.calcFeatures(fdata2);
        HashMap distr = sfc.calcClassDistribution(spamFeats, hamFeats);
        HashMap<String, HashMap> trainWordsProbs = new HashMap<>();

        // training
        Coach coach = new Coach(distr);
        trainWordsProbs = coach.trainSpamHam(spamFeats, hamFeats, smoothD);
        
        RegexpDocSplitter rds = new RegexpDocSplitter(fdata4);
        Set<Document> testDocsObjs = rds.getDocObjects();
        System.out.println(classes);
//        for (Object o: trainWordsProbs.entrySet()) {
//            System.out.println(o);
//        }
        
        
//        for (Document d: testDocsObjs) {
//            for (String w: d.getBodyWords()){
//                // getting the probability of a test word
//                System.out.println(w + " prob: " + trainWordsProbs.get(w));
//            }
//        }
        
        
    }

}
