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
public class WekaHMMFeatureVector implements WekaFeatureVector {
    
    @Override
    public FastVector initializeWekaFeatureVector() {
        //Declaration of the numeric value dMaxProb
        Attribute Attribute1 = new Attribute("Prob1");
        Attribute Attribute2 = new Attribute("Prob2");

        //Declare the class attribute along with its values
        FastVector fvClassVal = new FastVector(2);
        fvClassVal.addElement("NFR");
        fvClassVal.addElement("NBS");
        Attribute ClassAttribute = new Attribute("theClass", fvClassVal);

        //Declare the feature vector
        FastVector fvWekaAttributesHmm = new FastVector(3);
        fvWekaAttributesHmm.addElement(Attribute1);
        fvWekaAttributesHmm.addElement(Attribute2);
        fvWekaAttributesHmm.addElement(ClassAttribute);
        
        return fvWekaAttributesHmm;
    }
    
    public Instance fillFeatureVector(HMMFeatureVector vSource) {
        Instance i = new DenseInstance(3);
        FastVector vTarget;
        
        WekaHMMFeatureVector v = new WekaHMMFeatureVector();
        vTarget = v.initializeWekaFeatureVector();
        
        i.setValue((Attribute) vTarget.elementAt(0), vSource.getProbArrayAtIndex(0));
        i.setValue((Attribute) vTarget.elementAt(1), vSource.getProbArrayAtIndex(1));
        
        i.setValue((Attribute) vTarget.elementAt(2), vSource.getLabel());
        
        return i;
    }
    
    public Instances fillInstanceSet(ArrayList<HMMFeatureVector> vList) {
        
        FastVector fvWekaAttributesHmm = new FastVector(3);
        
        Instances isSet = new Instances(vList.get(0).getLabel(), fvWekaAttributesHmm, vList.size());

        /*if("NFR".equals(vList.get(0).label)) {
         isSet.setClassIndex(0);
         }
         else
         isSet.setClassIndex(1);*/
        isSet.setClassIndex(isSet.numAttributes() - 1);
        
        for (HMMFeatureVector HMMv : vList) {
            
            Instance i = fillFeatureVector(HMMv);
            
            isSet.add(i);            
        }
        
        return isSet;
    }
    
}
