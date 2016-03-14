/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package nucleosomepatternclassifier;

import representation.ARFFLoaderAndAnalyzer;
import representation.CRF_Trainer;
import representation.HMM_Trainer;
import cc.mallet.types.CrossValidationIterator;
import cc.mallet.types.InstanceList;
import entities.SequenceInstance;
import gr.demokritos.iit.jinsect.documentModel.representations.DocumentNGramGraph;
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
import representation.NGGTrainer;

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
           
           /* Here we take each set of instances and we train its Hidden Markov Model
           using the HMM_Trainer constructor.
           */
           
           HMM_Trainer NFRtrainer = new HMM_Trainer(NFR_training_instances, NFR_testing_instances);
           HMM_Trainer NBStrainer = new HMM_Trainer(NBS_training_instances, NBS_testing_instances);
           
           /* Here we take each set of instances and we create its n-gram graph using the
           NGGTrainer constructor.
           */
           
           DocumentNGramGraph NFR_training_ngGraph = new DocumentNGramGraph();
           DocumentNGramGraph NBS_training_ngGraph = new DocumentNGramGraph();
           
           NGGTrainer NGGtrainer = new NGGTrainer(NFR_training_instances, NBS_training_instances, NFR_training_ngGraph, NBS_training_ngGraph);
           
           int NFR_TruePositives = 0, NFR_FalseNegatives = 0, NFR_FalsePositives = 0, NFR_TrueNegatives = 0;
           int NBS_TruePositives = 0, NBS_FalseNegatives = 0, NBS_FalsePositives = 0, NBS_TrueNegatives = 0;
           
           NGGtrainer.NGGClassify(NFR_training_ngGraph, NBS_training_ngGraph, NFR_testing_instances, NFR_TruePositives, NFR_FalsePositives);
           NBS_FalsePositives = NFR_FalseNegatives;
           NBS_TrueNegatives = NFR_TruePositives;
           NGGtrainer.NGGClassify(NFR_training_ngGraph, NBS_training_ngGraph, NBS_testing_instances, NBS_TruePositives, NBS_FalsePositives);
           NFR_FalsePositives = NBS_FalseNegatives;
           NFR_TrueNegatives = NBS_TruePositives;

       }
       //CRF_Trainer NFRtrainer = new CRF_Trainer(NFR_training_instances, NFR_testing_instances);
       //CRF_Trainer NBStrainer = new CRF_Trainer(NBS_training_instances, NBS_testing_instances);
       
    }
    
}
