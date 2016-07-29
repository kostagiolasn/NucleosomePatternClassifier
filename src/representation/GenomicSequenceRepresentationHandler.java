/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package representation;

import entities.HMMFeatureVector;
import java.util.List;

/**
 *
 * @author nikos
 */
public interface GenomicSequenceRepresentationHandler<T> {
    public void train(List<T> representation, String label);
        
    public Object getClassModel();
    
    public Object getFeatureVector(T representation, String className);
}
