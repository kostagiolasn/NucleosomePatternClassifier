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
public class WekaBaselineBOWFeatureVector implements WekaFeatureVector{

    @Override
    public ArrayList<Attribute> initializeWekaFeatureVector() {
        
        //Declaration of the numeric value cosSimilarity
        String s = "triNucleotide";
        
        //Declare the feature vector
        ArrayList<Attribute> fvWekaAttributesBow;
        fvWekaAttributesBow = new ArrayList<>();
        
        for(int i = 0; i < 64; i++) {
            Attribute tempAttribute = new Attribute(s + " " + Integer.toString(i));
            fvWekaAttributesBow.add(tempAttribute);
        }
        //Attribute Attribute2 = new Attribute("cosSimilarity2");
        //Declare the class attribute along with its values
        ArrayList<String> fvClassVal = new ArrayList<>();
        fvClassVal.add("Nucleosome Free Region");
        fvClassVal.add("Nucleosome Binding Site");
        Attribute ClassAttribute = new Attribute("theClass", fvClassVal);
       
        //for(int i = 0; i < 64; i++)
        //    fvWekaAttributesBow.add(Attribute1);
        //fvWekaAttributesBow.add(Attribute2);
        fvWekaAttributesBow.add(ClassAttribute); 
        
        return fvWekaAttributesBow;
    }
    
     public Instance fillFeatureVector(BaselineBOWFeatureVector vSource, Instances data) {
         double[] values = new double[data.numAttributes()];
        
        //values[0] = vSource.getCosSimilarityArrayAtIndex(0);//((vSource.getCosSimilarityArrayAtIndex(0) + vSource.getCosSimilarityArrayAtIndex(1)));
        //values[1] = vSource.getCosSimilarityArrayAtIndex(1);//((vSource.getCosSimilarityArrayAtIndex(0) + vSource.getCosSimilarityArrayAtIndex(1)));
        for(int i = 0; i < 64; i++)
            values[i] = vSource.getFrequencyArrayAtIndex(i);
        values[64] = data.attribute(64).indexOfValue(vSource.getLabel());
        
        Instance inst = new DenseInstance(1.0, values);
        
        return inst;
    }
     
    public Instances fillInstanceSet(ArrayList<BaselineBOWFeatureVector> vList, ArrayList<BaselineBOWFeatureVector> vList2) throws IOException {
        
        ArrayList<Attribute> attributes = initializeWekaFeatureVector();
        Instances isSet = new Instances(vList.get(0).getLabel(), attributes, vList.size());
        
        isSet.setClassIndex(isSet.numAttributes() - 1);
        
        for (BaselineBOWFeatureVector BOWv : vList) {
            
            Instance i = fillFeatureVector(BOWv, isSet);
            
            isSet.add(i); 
        }
        
        for (BaselineBOWFeatureVector BOWv : vList2) {
            
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
