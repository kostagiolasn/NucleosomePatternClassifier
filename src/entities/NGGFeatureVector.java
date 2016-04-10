/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entities;

import gr.demokritos.iit.jinsect.structs.GraphSimilarity;
import gr.demokritos.iit.jinsect.structs.ISimilarity;

/**
 *
 * @author nikos
 */
public class NGGFeatureVector implements RepresentationFeatureVector{

    String label;
    double containmentSimilarityArray[];
    double sizeSimilarityArray[];
    double valueSimilarityArray[];
    @Override
    public void setLabel(String classLabel) {
        label = classLabel;
    }

    @Override
    public String getLabel() {
        return label;
    }
    
    public double getContainmentSimilarityArrayAtIndex(int index) {
        return containmentSimilarityArray[index];
    }
    
    public void setContainmentSimilarityArrayAtIndex(double element, int index) {
        this.containmentSimilarityArray[index] = element;
    }
    
    public double getSizeSimilarityArrayAtIndex(int index) {
        return sizeSimilarityArray[index];
    }
    
    public void setSizeSimilarityArrayAtIndex(double element, int index) {
        this.sizeSimilarityArray[index] = element;
    }
    
    public double getValueSimilarityArrayAtIndex(int index) {
        return valueSimilarityArray[index];
    }
    
    public void setValueSimilarityArrayAtIndex(double element, int index) {
        this.valueSimilarityArray[index] = element;
    }
    
}
