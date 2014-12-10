package naive.spam.runner;

import java.io.IOException;
import java.util.HashMap;
import java.util.Set;
import naive.spam.document.Document;
import naive.spam.feature.SimpleFeatureCalculator;
import naive.spam.input.InternalResReader;
import naive.spam.input.TextFileReader;
import naive.spam.test.TestClassifier;
import naive.spam.text.RegexpDocSplitter;
import naive.spam.train.Coach;

public class NaiveSpamDemo {

    public static void main(String[] args) throws IOException {
        float smoothD = 0.0f;
        TextFileReader tr = new TextFileReader();
        InternalResReader irr = new InternalResReader();
        String fdata1 = null;
        String fdata2 = null;
        String fdata3 = null;
        String fdata4 = null;
        
        if (args.length == 0) {
            smoothD = 0.7f;
            fdata4 = irr.getResource("/data/ham_spam_testing");
            System.out.println("Using default smoothing: 0.7f");
        } else if (args[0].contains("help")) {
            System.out.println("\"NaiveSpam classifier\"\n"
                    + "Training and test sets are already included in the jar "
                    + "file, but you can supply yours as arguments.\n"
                    + "USAGE 1: java -jar NaiveSpamDemo.jar 0.5f testfile -- "
                    + "first argument is smoothing parameter, second is the test file.\n"
                    + "USAGE 2: java -jar NaiveSpamDemo.jar -- "
                    + "default 0.7f will be used with default test file");
            return;
        } else {
            smoothD = Float.valueOf(args[0]);
            fdata4 = tr.readFile(String.valueOf(args[1]));
            System.out.println("Picked up smoothing: " + smoothD + 
                    ", filename: " + String.valueOf(args[1]));
        }
        
        //TextFileReader tfr = new TextFileReader();
        fdata1 = irr.getResource("/data/spam_training");
        fdata2 = irr.getResource("/data/ham_training");
        fdata3 = irr.getResource("/data/vocab_100000.wl");

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

        // testing
        TestClassifier tc = new TestClassifier(testDocsObjs,
                c.getDefaultSpamProb(), c.getDefaultHamProb(), fdata3);
        tc.classify(trainWordsProbs);
        tc.checkPrecision();
    }

}
