/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package representation;

import entities.BOWFeatureVector;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 *
 * @author nikos
 */
public class BOWHandler implements GenomicSequenceRepresentationHandler<List<BagOfWords>> {
    
    private final Map<String, BagOfWords> classModel;
    
    public BOWHandler() {
        this.classModel = new HashMap<>();
    }

    @Override
    public void train(List<List<BagOfWords>> representation, String label) {
        
        BagOfWords classBoW = new BagOfWords();
            
        for(int i = 0; i < representation.size(); i++) {
            BagOfWords tempBoW = representation.get(i).get(i);
            
            for(String a : tempBoW.getBowMap().keySet()) {

                if(!classBoW.getBowMap().containsKey(a)) {
                    classBoW.getBowMap().put(a, tempBoW.getBowMap().get(a));
                }
                else
                    classBoW.getBowMap().put(a, classBoW.getBowMap().get(a) + tempBoW.getBowMap().get(a));
            }

        }
        
        classModel.put(label, null);
    }

    @Override
    public Object getClassModel() {
        return classModel;
    }


    @Override
    public Object getFeatureVector(List<BagOfWords> representation) {
        
        BOWFeatureVector v = new BOWFeatureVector();
        
        int count = 0;
        
        for(String className : classModel.keySet()) {
            BagOfWords curClassModel = classModel.get(className);
            
            double tempCosSimilarity = 0.0;
            
            for(String a : curClassModel.getBowMap().keySet()) {
                for (BagOfWords temp : representation) {
                    if(temp.getBowMap().containsKey(a)) {
                        tempCosSimilarity += temp.getBowMap().get(a) *  curClassModel.getBowMap().get(a);
                    }
                }
            }
            
            v.setCosSimilarityArrayAtIndex(tempCosSimilarity, count);
            
            if(count == 0)
                v.setLabel(className);
            else {
                if(v.getCosSimilarityArrayAtIndex(0) < v.getCosSimilarityArrayAtIndex(1))
                    v.setLabel(className);
            }
            count++;
        }
        return v;
    }
    
}
