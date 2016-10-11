/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package representation;

import entities.BaselineBOWFeatureVector;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *
 * @author nikos
 */
public class BaselineBOWHandler implements GenomicSequenceRepresentationHandler<List<BaselineBagOfWords>> {
    
    private final Map<String, BaselineBagOfWords> classModel;
    
    public BaselineBOWHandler() {
        this.classModel = new HashMap<>();
    }

    @Override
    public void train(List<List<BaselineBagOfWords>> representation, String label) {
        
        BaselineBagOfWords classBoW = new BaselineBagOfWords();
            
        for (List<BaselineBagOfWords> representation1 : representation) {
            for (BaselineBagOfWords tempBoW : representation1) {
                for(String a : tempBoW.getBowMap().keySet()) {

                    if(!classBoW.getBowMap().containsKey(a)) {
                        classBoW.getBowMap().put(a, tempBoW.getBowMap().get(a));
                    }
                    else
                        classBoW.getBowMap().put(a, classBoW.getBowMap().get(a) + tempBoW.getBowMap().get(a));
                }
            }
        }
        
        classModel.put(label, classBoW);
    }

    @Override
    public Object getClassModel() {
        return classModel;
    }


    @Override
    public Object getFeatureVector(List<BaselineBagOfWords> representation, String label) {
        
        BaselineBOWFeatureVector v = new BaselineBOWFeatureVector();
        
        
        for(BaselineBagOfWords temp : representation){
            for(String a : temp.getBaselineBowMap().keySet()) {
                    v.setFrequencyArrayAtIndex(temp.getBaselineBowMap().get(a));
            }
        }

        v.setLabel(label);
        return v;
    }
    
}
