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
public class BaselineBOWFeatureVector {
    String label;
    ArrayList<Integer> frequencyArray;
    ArrayList<String> Bag;

    public BaselineBOWFeatureVector() {
       frequencyArray = new ArrayList<>();
       Bag = new ArrayList<>();
    }

    public void setLabel(String classLabel) {
        label = classLabel;
    }

    public String getLabel() {
        return label;
    }

    public int getFrequencyArrayAtIndex(int index) {
        return frequencyArray.get(index);
    }

    public void setFrequencyArrayAtIndex(int element) {
        this.frequencyArray.add(element);
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
