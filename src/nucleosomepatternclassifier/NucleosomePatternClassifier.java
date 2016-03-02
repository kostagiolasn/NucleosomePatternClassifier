/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package nucleosomepatternclassifier;

import analysis.ARFFLoadAndAnalyze;
import analysis.CRF_Training;
import analysis.HMM_Training;
import cc.mallet.types.InstanceList;
import entities.SequenceInstance;
import io.DataImport;
import io.FAFileReader;
import io.GenomicSequenceFileReader;
import io.InstancesToFiles;
import io.InstancesToFiles2;
import io.TextToArffConverter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author nikos
 */
public class NucleosomePatternClassifier {

    /**
     * @param args the command line arguments
     * @throws java.io.FileNotFoundException
     */
    public static void main(String[] args) throws FileNotFoundException, IOException, Exception {
        
       //InstancesToFiles2 itof = new InstancesToFiles2(args);
       
       //TextToArffConverter ttac = new TextToArffConverter("/home/nikos/NetBeansProjects/NucleosomePatternClassifier/Datasets");
       
       ARFFLoadAndAnalyze analyzer = new ARFFLoadAndAnalyze();
       
       /*HMM_Training NFRtrainer = new HMM_Training("/home/nikos/NetBeansProjects/NucleosomePatternClassifier/Datasets/NFR/NFR_Instances",
               "/home/nikos/NetBeansProjects/NucleosomePatternClassifier/Datasets/NFR/NFR_Instances");
       HMM_Training NBStrainer = new HMM_Training("/home/nikos/NetBeansProjects/NucleosomePatternClassifier/Datasets/NBS/NBS_Instances",
               "/home/nikos/NetBeansProjects/NucleosomePatternClassifier/Datasets/NBS/NBS_Instances");
       */
       
       /*DataImport importer = new DataImport();
       InstanceList NFR_training_instances = importer.readDirectory(new File("/home/nikos/NetBeansProjects/NucleosomePatternClassifier/Datasets/NFR/"));
       InstanceList NBS_training_instances = importer.readDirectory(new File("/home/nikos/NetBeansProjects/NucleosomePatternClassifier/Datasets/NBS/"));
       
       InstanceList NFR_testing_instances = importer.readDirectory(new File("/home/nikos/NetBeansProjects/NucleosomePatternClassifier/Datasets/NFR/"));
       InstanceList NBS_testing_instances = importer.readDirectory(new File("/home/nikos/NetBeansProjects/NucleosomePatternClassifier/Datasets/NBS/"));
       
       CRF_Training NFRtrainer = new CRF_Training(NFR_training_instances, NFR_testing_instances);
       CRF_Training NBStrainer = new CRF_Training(NBS_training_instances, NBS_testing_instances);
       */
       
       
    }
    
}
