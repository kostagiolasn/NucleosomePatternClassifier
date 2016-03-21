/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package classifier;

import java.util.List;

/**
 *
 * @author nikos
 * @param <T>
 */
public interface GenomicSequenceClassifier<T> {
    public String classify(T representation);
    public void train(List<T> representation, String label);
}
