/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entities;

import gr.demokritos.iit.jinsect.structs.GraphSimilarity;
import gr.demokritos.iit.jinsect.structs.ISimilarity;
import java.util.ArrayList;

/**
 *
 * @author nikos
 */
public class NGGFeatureVector implements RepresentationFeatureVector{

    String label;
    ArrayList <Double> containmentSimilarityArray;
    ArrayList <Double> sizeSimilarityArray;
    ArrayList <Double> valueSimilarityArray;
    ArrayList <Double> nvsArray;

    public NGGFeatureVector() {
        containmentSimilarityArray = new ArrayList<>();
        sizeSimilarityArray = new ArrayList<>();
        valueSimilarityArray = new ArrayList<>();
        nvsArray = new ArrayList<>();
    }
    
    
    @Override
    public void setLabel(String classLabel) {
        label = classLabel;
    }

    @Override
    public String getLabel() {
        return label;
    }
    
    public double getContainmentSimilarityArrayAtIndex(int index) {
        return containmentSimilarityArray.get(index);
    }
    
    public void setContainmentSimilarityArrayAtIndex(double element, int index) {
        this.containmentSimilarityArray.add(index, element);
    }
    
    public double getSizeSimilarityArrayAtIndex(int index) {
        return sizeSimilarityArray.get(index);
    }
    
    public void setSizeSimilarityArrayAtIndex(double element, int index) {
        this.sizeSimilarityArray.add(index, element);
    }
    
    public double getValueSimilarityArrayAtIndex(int index) {
        return valueSimilarityArray.get(index);
    }
    
    public void setValueSimilarityArrayAtIndex(double element, int index) {
        this.valueSimilarityArray.add(index, element);
    }
    
     public double getNVSArrayAtIndex(int index) {
        return nvsArray.get(index);
    }
    
    public void setNVSArrayAtIndex(double element, int index) {
        this.nvsArray.add(index, element);
    }
    
}
