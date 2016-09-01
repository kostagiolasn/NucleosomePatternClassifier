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
import weka.core.Instance;
import weka.core.Instances;
import weka.core.converters.ArffSaver;

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
        
        values[0] = vSource.getCosSimilarityArrayAtIndex(0);//((vSource.getCosSimilarityArrayAtIndex(0) + vSource.getCosSimilarityArrayAtIndex(1)));
        values[1] = vSource.getCosSimilarityArrayAtIndex(1);//((vSource.getCosSimilarityArrayAtIndex(0) + vSource.getCosSimilarityArrayAtIndex(1)));
        values[2] = data.attribute(2).indexOfValue(vSource.getLabel());
        
        Instance inst = new DenseInstance(1.0, values);
        
        return inst;
    }
     
    public Instances fillInstanceSet(ArrayList<BOWFeatureVector> vList, ArrayList<BOWFeatureVector> vList2) throws IOException {
        
        ArrayList<Attribute> attributes = initializeWekaFeatureVector();
        Instances isSet = new Instances(vList.get(0).getLabel(), attributes, vList.size());
        
        isSet.setClassIndex(isSet.numAttributes() - 1);
        
        for (BOWFeatureVector BOWv : vList) {
            
            Instance i = fillFeatureVector(BOWv, isSet);
            
            isSet.add(i); 
        }
        
        for (BOWFeatureVector BOWv : vList2) {
            
            Instance i = fillFeatureVector(BOWv, isSet);
            
            isSet.add(i); 
        }
        
        ArffSaver saver = new ArffSaver();
        saver.setInstances(isSet);
        saver.setFile(new File("./data/test.arff"));
        saver.writeBatch();
       
        return isSet;
    }
    
}
