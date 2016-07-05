/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entities;

import java.util.ArrayList;
import weka.core.Attribute;
import weka.core.DenseInstance;
import weka.core.FastVector;
import weka.core.Instance;
import weka.core.Instances;

/**
 *
 * @author nikos
 */
public class WekaBOWFeatureVector implements WekaFeatureVector{

    @Override
    public ArrayList<Attribute> initializeWekaFeatureVector() {
        
        //Declaration of the numeric value cosSimilarity
        Attribute Attribute1 = new Attribute("cosSimilarity1");
        Attribute Attribute2 = new Attribute("cosSimilarity2");
        //Declare the class attribute along with its values
        ArrayList<String> fvClassVal = new ArrayList<>();
        fvClassVal.add("Nucleosome Free Region");
        fvClassVal.add("Nucleosome Binding Site");
        Attribute ClassAttribute = new Attribute("theClass", fvClassVal);
        
        //Declare the feature vector
        ArrayList<Attribute> fvWekaAttributesBow;
        fvWekaAttributesBow = new ArrayList<>();
        fvWekaAttributesBow.add(Attribute1);
        fvWekaAttributesBow.add(Attribute2);
        fvWekaAttributesBow.add(ClassAttribute); 
        
        return fvWekaAttributesBow;
    }
    
     public Instance fillFeatureVector(BOWFeatureVector vSource, Instances data) {
         double[] values = new double[data.numAttributes()];
        
        values[0] = vSource.getCosSimilarityArrayAtIndex(0);
        values[1] = vSource.getCosSimilarityArrayAtIndex(1);
        values[2] = data.attribute(2).indexOfValue(vSource.getLabel());
        
        Instance inst = new DenseInstance(1.0, values);
        
        return inst;
    }
     
    public Instances fillInstanceSet(ArrayList<BOWFeatureVector> vList, ArrayList<BOWFeatureVector> vList2) {
        FastVector fvWekaAttributesBow = new FastVector(3);
        
        Instances isSet = new Instances(vList.get(0).getLabel(), fvWekaAttributesBow, vList.size());
        
        for (BOWFeatureVector BOWv : vList) {
            
            Instance i = fillFeatureVector(BOWv, isSet);
            
            isSet.add(i); 
        }
        
        for (BOWFeatureVector BOWv : vList2) {
            
            Instance i = fillFeatureVector(BOWv, isSet);
            
            isSet.add(i); 
        }
       
        return isSet;
    }
    
}
