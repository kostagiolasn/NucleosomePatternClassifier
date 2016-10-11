/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entities;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import weka.core.Attribute;
import weka.core.DenseInstance;
import weka.core.FastVector;
import weka.core.Instance;
import weka.core.Instances;
import weka.core.converters.ArffSaver;

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
        double min_prob;
        
        values[0] = vSource.getProbArrayAtIndex(0)/(vSource.getProbArrayAtIndex(0) + vSource.getProbArrayAtIndex(1));
        values[1] = vSource.getProbArrayAtIndex(1)/(vSource.getProbArrayAtIndex(0) + vSource.getProbArrayAtIndex(1));
        values[2] = data.attribute(2).indexOfValue(vSource.getLabel());
        
        Instance inst = new DenseInstance(1.0, values);
        return inst;
    }

    public Instances fillInstanceSet(ArrayList<HMMFeatureVector> vList, ArrayList<HMMFeatureVector> vList2) throws IOException {

        //FastVector fvWekaAttributesHmm = new FastVector(3);

        ArrayList<Attribute> attributes = initializeWekaFeatureVector();
        Instances isSet = new Instances("dataset", attributes, vList.size());

        isSet.setClassIndex(isSet.numAttributes() - 1);

        for (HMMFeatureVector HMMv : vList) {

            Instance i = fillFeatureVector(HMMv, isSet);

            isSet.add(i);
        }
        
        for (HMMFeatureVector HMMv : vList2) {

            Instance i = fillFeatureVector(HMMv, isSet);

            isSet.add(i);
        }
        
        ArffSaver saver = new ArffSaver();
        saver.setInstances(isSet);
        saver.setFile(new File("./data/test.arff"));
        saver.writeBatch();

        return isSet;
    }

}
