/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package representation;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author nikos
 */
public class BaselineBagOfWords {
    
    private final HashMap<String, Integer> BowMap;
    private final HashMap<String, Integer> BaselineBowMap;
    private ArrayList<String> possibleSequences;

    public BaselineBagOfWords() {
        BowMap = new HashMap<String, Integer>();
        
        String alpha = "ACGT";
        char[] seq = alpha.toCharArray();

        int length = 3;
        possibleSequences = generatePossibleStrings(length, alpha, seq);
        BaselineBowMap = new HashMap<String, Integer>();
    }
    
    
    public BaselineBagOfWords(String sequence) {
        
        BowMap = new HashMap<String, Integer>();
        BaselineBowMap = new HashMap<String, Integer>();
        
        String alpha = "ACGT";
        char[] seq = alpha.toCharArray();

        int length = 3;
        possibleSequences = generatePossibleStrings(length, alpha, seq);
        
        for(int i = 0; i < sequence.length()-2; i++) {
            String key = sequence.substring(i, i+3);
            if(!BowMap.containsKey(key))
                BowMap.put(key, 1);
            else
                BowMap.put(key, BowMap.get(key)+1);
        }
        
        for(String s : possibleSequences) {
            if(!BowMap.containsKey(s))
                BaselineBowMap.put(s, 0);
            else
                BaselineBowMap.put(s, BowMap.get(s));
        }
    }

    public HashMap<String, Integer> getBaselineBowMap() {
        return BaselineBowMap;
    }

    private ArrayList<String> generatePossibleStrings(int length, String alphabet, char[] sequence) {
        StringBuilder builder = new StringBuilder("   ");
        
        possibleSequences = new ArrayList<>();

        int[] pos = new int[length];
        int total = (int) Math.pow(alphabet.length(), length);
        for (int i = 0; i < total; i++) {
            for (int x = 0; x < length; x++) {
                if (pos[x] == sequence.length) {
                    pos[x] = 0;
                    if (x + 1 < length) {
                        pos[x + 1]++;
                    }
                }
                builder.setCharAt(x, sequence[pos[x]]);
            }
            pos[0]++;

            possibleSequences.add(builder.toString());
        }
        
        return possibleSequences;
    }

    HashMap<String, Integer> getBowMap() {
        return BowMap;
    }
    
     
}
