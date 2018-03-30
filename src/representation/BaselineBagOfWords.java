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
    private final HashMap<String, Double> BaselineBowMap;
    private ArrayList<String> possibleSequences;

    public BaselineBagOfWords() {
        BowMap = new HashMap<String, Integer>();
        
        String alpha = "ACGT";
        char[] seq = alpha.toCharArray();

        int length = 3;
        possibleSequences = generatePossibleStrings(length, alpha, seq);
        BaselineBowMap = new HashMap<String, Double>();
    }
    
    
    public BaselineBagOfWords(String sequence) {
        
        double A_count = 14317.0;
        double T_count = 14320.0;
        double C_count = 7436.0;
        double G_count = 7545.0;
        double sum_count = A_count + T_count + C_count + G_count;
        
        BowMap = new HashMap<String, Integer>();
        BaselineBowMap = new HashMap<String, Double>();
        
        String alpha = "ACGT";
        char[] seq = alpha.toCharArray();

        int length = 3;
        possibleSequences = generatePossibleStrings(length, alpha, seq);
        
        for(int i = 0; i < sequence.length()-2; i++) {
            String key = sequence.substring(i, i+3);
            //System.out.println(key);
            if(!BowMap.containsKey(key))
                BowMap.put(key, 1);
            else
                BowMap.put(key, BowMap.get(key)+1);
        }
        
        
        for(String s : possibleSequences) {
            //System.out.println(s);
            //System.out.println(BowMap.toString());
            //System.out.println(BowMap.containsKey(s));
            
            if(BowMap.containsKey(s)) {
                double freq = (double)(BowMap.get(s) / 64.0);
                for(int i = 0; i < s.length(); i++) {
                    if(s.charAt(i) == 'A')
                        freq /= A_count / sum_count;
                    else if(s.charAt(i) == 'T')
                        freq /= T_count / sum_count;
                    else if(s.charAt(i) == 'C')
                        freq /= C_count / sum_count;
                    else
                        freq /= G_count / sum_count;
                }
                BaselineBowMap.put(s, freq);
            } else BaselineBowMap.put(s, 0.0);
        }
    }

    public HashMap<String, Double> getBaselineBowMap() {
        
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

