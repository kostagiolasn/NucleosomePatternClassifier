/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package nucleosomepatternclassifier;

import analysis.ARFFLoaderAndAnalyzer;
import analysis.CRF_Trainer;
import analysis.HMM_Trainer;
import cc.mallet.types.CrossValidationIterator;
import cc.mallet.types.InstanceList;
import entities.SequenceInstance;
import io.MalletDataImporter;
import io.FAFileReader;
import io.GenomicSequenceFileReader;
import io.ManyInstancesPerFileConverter;
import io.OneInstancePerFileConverter;
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
        
        /* Here we import the data stored in the two FASTA files. Then, we pass them, along with the 
        nfolds variable to the CrossValidationIterator objects (one for each class), in order to 
        implement the n-fold cross-fold validation. We may include the two file paths and the nfolds variable
        in the args parameter -> FUTURE ADDONS
        The CrossValidationIterator is an iterator which splits an InstanceList into n-folds and iterates 
        over the folds for use in n-fold cross-validation. For each iteration, list[0] contains an InstanceList
        with n-1 filds typically used for training and list[1] contains an InstanceList with 1 folds typically
        used for validation.
        */
        
       //ManyInstancesPerFileConverter input = new ManyInstancesPerFileConverter(args);
       
       //TextToArffConverter ttac = new TextToArffConverter("/home/nikos/NetBeansProjects/NucleosomePatternClassifier/Datasets");
       
       //ARFFLoaderAndAnalyzer analyzer = new ARFFLoaderAndAnalyzer();
       
       /*HMM_Trainer NFRtrainer = new HMM_Trainer("/home/nikos/NetBeansProjects/NucleosomePatternClassifier/Datasets/NFR/NFR_Instances",
               "/home/nikos/NetBeansProjects/NucleosomePatternClassifier/Datasets/NFR/NFR_Instances");
       HMM_Trainer NBStrainer = new HMM_Trainer("/home/nikos/NetBeansProjects/NucleosomePatternClassifier/Datasets/NBS/NBS_Instances",
               "/home/nikos/NetBeansProjects/NucleosomePatternClassifier/Datasets/NBS/NBS_Instances");
       */
        
       int nfolds = 10;
       
       MalletDataImporter importer = new MalletDataImporter();
       
       InstanceList NFR_instances = importer.readDirectory(new File("/home/nikos/NetBeansProjects/NucleosomePatternClassifier/Datasets/NFR/"));
       CrossValidationIterator NFR_iterator = new CrossValidationIterator(NFR_instances, nfolds);
       
       InstanceList NBS_instances = importer.readDirectory(new File("/home/nikos/NetBeansProjects/NucleosomePatternClassifier/Datasets/NBS/"));
       CrossValidationIterator NBS_iterator = new CrossValidationIterator(NBS_instances, nfolds);
       
       while(NFR_iterator.hasNext() && NBS_iterator.hasNext()) {
           InstanceList NFR_training_instances = NFR_iterator.nextSplit()[0];
           InstanceList NFR_testing_instances = NFR_iterator.nextSplit()[1];
           
           InstanceList NBS_training_instances = NBS_iterator.nextSplit()[0];
           InstanceList NBS_testing_instances = NBS_iterator.nextSplit()[1];
       }
       
       //CRF_Trainer NFRtrainer = new CRF_Trainer(NFR_training_instances, NFR_testing_instances);
       //CRF_Trainer NBStrainer = new CRF_Trainer(NBS_training_instances, NBS_testing_instances);
       
       
       
    }
    
}
