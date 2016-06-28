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
    public FastVector initializeWekaFeatureVector() {
        //Declaration of the numeric value dMaxProb
        Attribute Attribute1 = new Attribute("containmentSimilarity1");
        Attribute Attribute2 = new Attribute("sizeSimilarity1");
        Attribute Attribute3 = new Attribute("valueSimilarity1");
        Attribute Attribute4 = new Attribute("containmentSimilarity2");
        Attribute Attribute5 = new Attribute("sizeSimilarity2");
        Attribute Attribute6 = new Attribute("valueSimilarity2");
        //Declare the class attribute along with its values
        FastVector fvClassVal = new FastVector(2);
        fvClassVal.addElement("NFR");
        fvClassVal.addElement("NBS");
        Attribute ClassAttribute = new Attribute("theClass", fvClassVal);
        
        //Declare the feature vector
        FastVector fvWekaAttributesNgg = new FastVector(7);
        fvWekaAttributesNgg.addElement(Attribute1);
        fvWekaAttributesNgg.addElement(Attribute2);
        fvWekaAttributesNgg.addElement(Attribute3);
        fvWekaAttributesNgg.addElement(Attribute4);
        fvWekaAttributesNgg.addElement(Attribute5);
        fvWekaAttributesNgg.addElement(Attribute6);
        fvWekaAttributesNgg.addElement(ClassAttribute); 
        
        return fvWekaAttributesNgg;
    }
    
    public Instance fillFeatureVector(NGGFeatureVector vSource) {
        Instance i = new DenseInstance(7);
        FastVector vTarget;
        
        WekaHMMFeatureVector v = new WekaHMMFeatureVector();
        vTarget = v.initializeWekaFeatureVector();
        
        i.setValue((Attribute)vTarget.elementAt(0), vSource.getContainmentSimilarityArrayAtIndex(0) );
        i.setValue((Attribute)vTarget.elementAt(1), vSource.getContainmentSimilarityArrayAtIndex(0) );
        i.setValue((Attribute)vTarget.elementAt(2), vSource.getSizeSimilarityArrayAtIndex(0) );
        i.setValue((Attribute)vTarget.elementAt(3), vSource.getSizeSimilarityArrayAtIndex(1) );
        i.setValue((Attribute)vTarget.elementAt(4), vSource.getValueSimilarityArrayAtIndex(1) );
        i.setValue((Attribute)vTarget.elementAt(5), vSource.getValueSimilarityArrayAtIndex(1) );
        
        i.setValue((Attribute)vTarget.elementAt(6), vSource.getLabel() );
        
        return i;
    }
     
    public Instances fillInstanceSet(ArrayList<NGGFeatureVector> vList) {
        FastVector fvWekaAttributesBow = new FastVector(7);
        
        Instances isSet = new Instances(vList.get(0).getLabel(), fvWekaAttributesBow, vList.size());
        
        if("NFR".equals(vList.get(0).label)) {
            isSet.setClassIndex(0);
        }
        else
            isSet.setClassIndex(1);
        
        for (NGGFeatureVector NGGv : vList) {
            
            Instance i = fillFeatureVector(NGGv);
            
            isSet.add(i); 
        }
       
        return isSet;
    }
    
}
