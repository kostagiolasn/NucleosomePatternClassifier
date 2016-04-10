/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entities;

/**
 *
 * @author nikos
 */
public class HMMFeatureVector implements RepresentationFeatureVector {
    
    String label;
    double probArray[];

    @Override
    public void setLabel(String classLabel) {
        
        label = classLabel;
    }   

    @Override
    public String getLabel() {
        return label;
    }

    public double getProbArrayAtIndex(int index) {
        return probArray[index];
    }

    public void setProbArrayAtIndex(double element, int index) {
        this.probArray[index] = element;
    }


    
}
