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
public class WekaNGGFeatureVector implements WekaFeatureVector{

    @Override
    public ArrayList<Attribute> initializeWekaFeatureVector() {
        //Declaration of the numeric value dMaxProb
        Attribute Attribute1 = new Attribute("containmentSimilarity1");
        Attribute Attribute2 = new Attribute("sizeSimilarity1");
        Attribute Attribute3 = new Attribute("valueSimilarity1");
        Attribute Attribute4 = new Attribute("containmentSimilarity2");
        Attribute Attribute5 = new Attribute("sizeSimilarity2");
        Attribute Attribute6 = new Attribute("valueSimilarity2");
        //Declare the class attribute along with its values
        ArrayList<String> fvClassVal = new ArrayList<>();
        fvClassVal.add("Nucleosome Free Region");
        fvClassVal.add("Nucleosome Binding Site");
        Attribute ClassAttribute = new Attribute("theClass", fvClassVal);
        
        //Declare the feature vector
        ArrayList<Attribute> fvWekaAttributesNgg;
        fvWekaAttributesNgg = new ArrayList<>();
        fvWekaAttributesNgg.add(Attribute1);
        fvWekaAttributesNgg.add(Attribute2);
        fvWekaAttributesNgg.add(Attribute3);
        fvWekaAttributesNgg.add(Attribute4);
        fvWekaAttributesNgg.add(Attribute5);
        fvWekaAttributesNgg.add(Attribute6);
        fvWekaAttributesNgg.add(ClassAttribute); 
        
        return fvWekaAttributesNgg;
    }
    
    public Instance fillFeatureVector(NGGFeatureVector vSource, Instances data) {
        
        double[] values = new double[data.numAttributes()];
        
        values[0] = vSource.getContainmentSimilarityArrayAtIndex(0);
        values[1] = vSource.getContainmentSimilarityArrayAtIndex(1);
        values[2] = vSource.getSizeSimilarityArrayAtIndex(0);
        values[3] = vSource.getSizeSimilarityArrayAtIndex(1);
        values[4] = vSource.getValueSimilarityArrayAtIndex(0);
        values[5] = vSource.getValueSimilarityArrayAtIndex(1);
        values[6] = data.attribute(6).indexOfValue(vSource.getLabel());
        
        Instance inst = new DenseInstance(1.0, values);
        
        return inst;
    }
     
    public Instances fillInstanceSet(ArrayList<NGGFeatureVector> vList, ArrayList<NGGFeatureVector> vList2) {
        FastVector fvWekaAttributesBow = new FastVector(7);
        
        Instances isSet = new Instances(vList.get(0).getLabel(), fvWekaAttributesBow, vList.size());
        
        if("NFR".equals(vList.get(0).label)) {
            isSet.setClassIndex(0);
        }
        else
            isSet.setClassIndex(1);
        
        for (NGGFeatureVector NGGv : vList) {
            
            Instance i = fillFeatureVector(NGGv, isSet);
            
            isSet.add(i); 
        }
        
        for (NGGFeatureVector NGGv : vList2) {

            Instance i = fillFeatureVector(NGGv, isSet);

            isSet.add(i);
        }
       
        return isSet;
    }
    
}
