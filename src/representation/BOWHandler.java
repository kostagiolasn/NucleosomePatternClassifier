/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package representation;

import entities.BOWFeatureVector;
import static java.lang.Math.sqrt;
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
            
        for (List<BagOfWords> representation1 : representation) {
            for (BagOfWords tempBoW : representation1) {
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
    public Object getFeatureVector(List<BagOfWords> representation, String label) {
        
        BOWFeatureVector v = new BOWFeatureVector();
        
        int count = 0;
        
        for(String className : classModel.keySet()) {
            BagOfWords curClassModel = classModel.get(className);

            
            double tempCosSimilarity = 0.0;
            double sumA = 0.0;
            double sumB = 0.0;
            
            for(String a : curClassModel.getBowMap().keySet()) {
                sumA += curClassModel.getBowMap().get(a);
            }
            
            for (BagOfWords temp : representation) {
                for(String a : temp.getBowMap().keySet()) {
                    sumB += temp.getBowMap().get(a);
                    v.addToBag(a);
                }
            }
            
            for(String a : curClassModel.getBowMap().keySet()) {
                for (BagOfWords temp : representation) {
                    if(temp.getBowMap().containsKey(a)) {
                        tempCosSimilarity += temp.getBowMap().get(a) *  curClassModel.getBowMap().get(a);
                    }
                }
            }
            
            tempCosSimilarity = tempCosSimilarity / (sqrt(sumA) * sqrt(sumB));
            v.setCosSimilarityArrayAtIndex(tempCosSimilarity, count);
            
            /*if(count == 0)
                v.setLabel(className);
            else {
                if(v.getCosSimilarityArrayAtIndex(0) < v.getCosSimilarityArrayAtIndex(1))
                    v.setLabel(className);
            }*/
            count++;
        }
        v.setLabel(label);
        return v;
    }
    
}
