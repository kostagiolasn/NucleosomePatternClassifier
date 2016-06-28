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
    public FastVector initializeWekaFeatureVector() {
        
        //Declaration of the numeric value cosSimilarity
        Attribute Attribute1 = new Attribute("cosSimilarity1");
        Attribute Attribute2 = new Attribute("cosSimilarity2");
        //Declare the class attribute along with its values
        FastVector fvClassVal = new FastVector(2);
        fvClassVal.addElement("NFR");
        fvClassVal.addElement("NBS");
        Attribute ClassAttribute = new Attribute("theClass", fvClassVal);
        
        //Declare the feature vector
        FastVector fvWekaAttributesBow = new FastVector(3);
        fvWekaAttributesBow.addElement(Attribute1);
        fvWekaAttributesBow.addElement(Attribute2);
        fvWekaAttributesBow.addElement(ClassAttribute); 
        
        return fvWekaAttributesBow;
    }
    
     public Instance fillFeatureVector(BOWFeatureVector vSource) {
        Instance i = new DenseInstance(3);
        FastVector vTarget;
        
        WekaHMMFeatureVector v = new WekaHMMFeatureVector();
        vTarget = v.initializeWekaFeatureVector();
        
        i.setValue((Attribute)vTarget.elementAt(0), vSource.getCosSimilarityArrayAtIndex(0) );
        i.setValue((Attribute)vTarget.elementAt(1), vSource.getCosSimilarityArrayAtIndex(1) );
        
        i.setValue((Attribute)vTarget.elementAt(2), vSource.getLabel() );
        
        return i;
    }
     
    public Instances fillInstanceSet(ArrayList<BOWFeatureVector> vList) {
        FastVector fvWekaAttributesBow = new FastVector(3);
        
        Instances isSet = new Instances(vList.get(0).getLabel(), fvWekaAttributesBow, vList.size());
        
        for (BOWFeatureVector BOWv : vList) {
            
            Instance i = fillFeatureVector(BOWv);
            
            isSet.add(i); 
        }
       
        return isSet;
    }
    
}
