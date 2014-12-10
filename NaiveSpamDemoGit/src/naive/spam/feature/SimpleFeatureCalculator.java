package naive.spam.feature;

import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/* This class calculates spam types and word frequencies given the document and
 returns a dictionaries of various types */
public class SimpleFeatureCalculator {

    private HashMap<String, HashMap> featClass;
    private HashMap<String, Integer> featCnt;
    private Set<String> filter = new HashSet<String>();
    
    /* initializing here so it would accumulate classes as new training files
        are read in */
    private HashMap<String, Integer> classCnt = new HashMap<>();
    
    public SimpleFeatureCalculator(String voc) {
        filter = new HashSet(Arrays.asList(voc.split(System.getProperty("line.separator"))));
        filter.add("#*#*# spam");
        filter.add("#*#*# ham");
    }

    /* This method calculates word occurences within each class and returns a 
    dictionary: {class1 :{ word1: freq, word2: freq, ...}, class2: {...}} */
    public HashMap calcFeatures(String fdata) {
        Pattern rclass = Pattern.compile("#\\*#\\*# [a-z]+");
        String currentClass = null;
        featClass = new HashMap<>();
        featCnt = new HashMap<>();
        
        //filtering out the words with vocabulary
        String[] dataArr = fdata.split(System.getProperty("line.separator"));
        LinkedList<String> items = new LinkedList(Arrays.asList(dataArr));
        items.retainAll(filter);

        //calculating occurences
        for (String line : items) {
            Matcher match = rclass.matcher(line);
            if (match.find()) {
                currentClass = match.group();
                featClass.put(currentClass, featCnt);
                continue;
            }
            // assuming each training file has /#*#*# [a-z]+/ at 0 line //FIXIT
            HashMap inner = featClass.get(currentClass);
            inner.put(line, (inner.get(line) == null) ? 1: (int) inner.get(line) + 1);
        }
        return featClass;
    }

    /* This method calculates classes number and returns a dictionary
    {class1: frequency, class2: frequency, class3: frequency, ...} */
    public HashMap calcClasses(String fdata) {
        Pattern rclass = Pattern.compile("#\\*#\\*# [a-z]+");
        String currentClass = null;
        int n = 1;

        for (String line : fdata.split(System.getProperty("line.separator"))) {
            Matcher match = rclass.matcher(line);
            
            if (match.find()) {
                currentClass = match.group();
                classCnt.put(currentClass, n++);
            }
        }
        return classCnt;
    }
    
    /* This method calculates word distributions over a class and returns a 
    dictionary: {word: {spam: frequency, ham: frequency} */
    public HashMap calcClassDistribution(HashMap f1, HashMap f2) {
        HashMap<String, HashMap> wordClassDistr = new HashMap<>();
        HashMap spamVals = (HashMap) f1.get("#*#*# spam");
        HashMap hamVals = (HashMap) f2.get("#*#*# ham");
        
        // Making a set of all words from training sets
        Set<String> combined = new HashSet<String>();
        combined.addAll(spamVals.keySet());
        combined.addAll(hamVals.keySet());
        
        //(inner.get(line) == null) ? 1: (int) inner.get(line) + 1
        for (String word: combined) {
            HashMap classDistr = new HashMap();
            int sv = (spamVals.get(word) == null) ? 0: (int) spamVals.get(word);
            int hv = (hamVals.get(word) == null) ? 0: (int) hamVals.get(word);
            classDistr.put("spam", sv);
            classDistr.put("ham", hv);
            wordClassDistr.put(word, classDistr);
        }
        return wordClassDistr;
    }
}
    
    
