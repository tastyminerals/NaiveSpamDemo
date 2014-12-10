package naive.spam.runner;

import java.util.HashMap;
import java.util.Set;
import naive.spam.document.Document;
import naive.spam.feature.SimpleFeatureCalculator;
import naive.spam.input.InternalResReader;
import naive.spam.test.TestClassifier;
import naive.spam.text.RegexpDocSplitter;
import naive.spam.train.Coach;

public class NaiveSpamDemo {

    public static void main(String[] args) {
        float smoothD = 0.0f;
        if (args.length == 0) {
            System.out.println("default smoothing: 0.7f");
            smoothD = 0.7f;
        } else {
            smoothD = Float.valueOf(args[0]);
        }
        
        
        String fdata1 = null;
        String fdata2 = null;
        String fdata3 = null;
        String fdata4 = null;
        InternalResReader irr = new InternalResReader();
        //TextFileReader tfr = new TextFileReader();
        fdata1 = irr.getResource("/data/spam_training");
        fdata2 = irr.getResource("/data/ham_training");
        fdata3 = irr.getResource("/data/vocab_100000.wl");
        fdata4 = irr.getResource("/data/ham_spam_testing");

        // calculating features
        SimpleFeatureCalculator sfc = new SimpleFeatureCalculator(fdata3);
        HashMap classes = sfc.calcClasses(fdata1);
        classes = sfc.calcClasses(fdata2);
        HashMap spamFeats = sfc.calcFeatures(fdata1);
        HashMap hamFeats = sfc.calcFeatures(fdata2);
        HashMap distr = sfc.calcClassDistribution(spamFeats, hamFeats);
        HashMap<String, HashMap> trainWordsProbs = new HashMap<>();

        // training
        Coach c = new Coach(distr);
        trainWordsProbs = c.trainSpamHam(spamFeats, hamFeats, smoothD);

        RegexpDocSplitter rds = new RegexpDocSplitter(fdata4);
        Set<Document> testDocsObjs = rds.getDocObjects();

//        for (Document d : testDocsObjs) {
//            System.out.println(d.getEmailType() + "\n" + d.getBodyWords());
//        }
        // testing
        TestClassifier tc = new TestClassifier(testDocsObjs,
                c.getDefaultSpamProb(), c.getDefaultHamProb(), fdata3);
        tc.classify(trainWordsProbs);
        tc.checkPrecision();
    }

}
