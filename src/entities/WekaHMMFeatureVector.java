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
    public ArrayList<Attribute> initializeWekaFeatureVector() {
        //Declaration of the numeric value dMaxProb
        Attribute Attribute1 = new Attribute("Prob1");
        Attribute Attribute2 = new Attribute("Prob2");

        //Declare the class attribute along with its values
        ArrayList<String> fvClassVal = new ArrayList<>();
        fvClassVal.add("Nucleosome Free Region");
        fvClassVal.add("Nucleosome Binding Site");
        Attribute ClassAttribute = new Attribute("theClass", fvClassVal);

        ArrayList<Attribute> fvWekaAttributesHmm;
        fvWekaAttributesHmm = new ArrayList<>();
        fvWekaAttributesHmm.add(Attribute1);
        fvWekaAttributesHmm.add(Attribute2);
        fvWekaAttributesHmm.add(ClassAttribute);

        return fvWekaAttributesHmm;
    }

    public Instance fillFeatureVector(HMMFeatureVector vSource, Instances data) {
        double[] values = new double[data.numAttributes()];
        
        values[0] = vSource.getProbArrayAtIndex(0);
        values[1] = vSource.getProbArrayAtIndex(1);
        values[2] = data.attribute(2).indexOfValue(vSource.getLabel());
        
        Instance inst = new DenseInstance(1.0, values);

       // i.setValue((Attribute) vTarget.elementAt(0), vSource.getProbArrayAtIndex(0));
        //i.setValue((Attribute) vTarget.elementAt(1), vSource.getProbArrayAtIndex(1));

        //i.setValue((Attribute) vTarget.elementAt(2), vSource.getLabel());

        return inst;
    }

    public Instances fillInstanceSet(ArrayList<HMMFeatureVector> vList, ArrayList<HMMFeatureVector> vList2) {

        //FastVector fvWekaAttributesHmm = new FastVector(3);

        ArrayList<Attribute> attributes = initializeWekaFeatureVector();
        Instances isSet = new Instances(vList.get(0).getLabel(), attributes, vList.size());

        isSet.setClassIndex(isSet.numAttributes() - 1);

        for (HMMFeatureVector HMMv : vList) {

            Instance i = fillFeatureVector(HMMv, isSet);

            isSet.add(i);
        }
        
        for (HMMFeatureVector HMMv : vList2) {

            Instance i = fillFeatureVector(HMMv, isSet);

            isSet.add(i);
        }

        return isSet;
    }

}
