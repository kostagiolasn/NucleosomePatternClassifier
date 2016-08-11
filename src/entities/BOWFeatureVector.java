/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entities;

import java.util.ArrayList;

/**
 *
 * @author nikos
 */
public class BOWFeatureVector implements RepresentationFeatureVector {

    String label;
    ArrayList<Double> cosSimilarityArray;
    ArrayList<String> Bag;

    public BOWFeatureVector() {
       cosSimilarityArray = new ArrayList<>();
       Bag = new ArrayList<>();
    }

    @Override
    public void setLabel(String classLabel) {
        label = classLabel;
    }

    @Override
    public String getLabel() {
        return label;
    }

    public double getCosSimilarityArrayAtIndex(int index) {
        return cosSimilarityArray.get(index);
    }

    public void setCosSimilarityArrayAtIndex(double element, int index) {
        this.cosSimilarityArray.add(index, element);
    }
    
    public ArrayList<String> getBag() {
        return Bag;
    }

    public void setBag(ArrayList<String> TBag) {
        Bag = new ArrayList<>(TBag);
    }
    
    public void addToBag(String string) {
        if(!(Bag.contains(string)))
            Bag.add(string);
    }

}
