/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package representation;

import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author nikos
 */
class BagOfWords {
    
    private final HashMap<String, Integer> BowMap;

    public BagOfWords() {
        BowMap = new HashMap<String, Integer>();
    }
    
    
    public BagOfWords(String sequence) {
        
        BowMap = new HashMap<String, Integer>();
        
        for(int i = 0; i < sequence.length()-2; i++) {
            String key = sequence.substring(i, i+3);
            if(!BowMap.containsKey(key))
                BowMap.put(key, 1);
            else
                BowMap.put(key, BowMap.get(key)+1);
        }
    }

    public HashMap<String, Integer> getBowMap() {
        return BowMap;
    }
    
     
}
