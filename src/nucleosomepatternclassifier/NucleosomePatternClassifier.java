/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package nucleosomepatternclassifier;

import be.ac.ulg.montefiore.run.jahmm.ObservationDiscrete;
import representation.HMM_SequenceAnalyst;
import cc.mallet.types.CrossValidationIterator;
import cc.mallet.types.Instance;
import cc.mallet.types.InstanceList;
import entities.GenomicSequence;
import entities.HMMSequence;
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
import static java.util.Collections.rotate;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import representation.GenomicSequenceAnalyst;
import representation.NGG_SequenceAnalyst;
import weka.core.Instances;

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
        
        FAFileReader reader = new FAFileReader();
        ArrayList<SequenceInstance> NFR_instances = reader.getSequencesFromFile("/home/nikos/NetBeansProjects/NucleosomePatternClassifier/Datasets/1099_consistent_NFR.fa");
        ArrayList<SequenceInstance> NBS_instances = reader.getSequencesFromFile("/home/nikos/NetBeansProjects/NucleosomePatternClassifier/Datasets/3061_consistent_nucleosomes.fa");        
        
        GenomicSequenceAnalyst<List<ObservationDiscrete<HMMSequence.Packet>>> analyst = new HMM_SequenceAnalyst();
        
        int nfolds = 10;
        int evaluations = 10;
        int NFRpartitionSize = NFR_instances.size() / nfolds;
        int NBSpartitionSize = NBS_instances.size() / nfolds;
        
        List<SequenceInstance> NFR_Seqs = new ArrayList<>(NFR_instances);
        List<SequenceInstance> NBS_Seqs = new ArrayList<>(NBS_instances);
        
        List<SequenceInstance> NFR_trainingSeqs;
        NFR_trainingSeqs = new ArrayList();
         List<SequenceInstance> NBS_trainingSeqs;
        NBS_trainingSeqs = new ArrayList();

        for(int i = 0; i < evaluations; i++) {
            
            for(int j = 0; j < (nfolds-1)*NFRpartitionSize; j++) {
                NFR_trainingSeqs.add(NFR_Seqs.get(j));
            }
            
            for(int j = 0; j < (nfolds-1)*NBSpartitionSize; j++) {
                NBS_trainingSeqs.add(NBS_Seqs.get(j));
            }
            
            rotate(NFR_Seqs, NFRpartitionSize);
            rotate(NBS_Seqs, NBSpartitionSize);
        }
        
        List<List<ObservationDiscrete<HMMSequence.Packet>>> NFRTrainingHMM = analyst.represent(NFR_trainingSeqs);
        List<List<ObservationDiscrete<HMMSequence.Packet>>> NBSTrainingHMM = analyst.represent(NBS_trainingSeqs);

        
       //ManyInstancesPerFileConverter input = new ManyInstancesPerFileConverter(args);
       
       //TextToArffConverter ttac = new TextToArffConverter("/home/nikos/NetBeansProjects/NucleosomePatternClassifier/Datasets");
       
       //ARFFLoaderAndAnalyzer analyzer = new ARFFLoaderAndAnalyzer();
        
       /*int nfolds = 10;
       
       MalletDataImporter importer = new MalletDataImporter();
       
       InstanceList NFR_instances = importer.readDirectory(new File("/home/nikos/NetBeansProjects/NucleosomePatternClassifier/Datasets/NFR/"));
       for (Iterator<Instance> it = NFR_instances.iterator(); it.hasNext();) {
            Instance n = it.next();
            System.out.println(n);
        }
       CrossValidationIterator NFR_iterator = new CrossValidationIterator(NFR_instances, nfolds);
       
       InstanceList NBS_instances = importer.readDirectory(new File("/home/nikos/NetBeansProjects/NucleosomePatternClassifier/Datasets/NBS/"));
        for (Iterator<Instance> it = NBS_instances.iterator(); it.hasNext();) {
            Instance n = it.next();
            System.out.println(n);
        }
       CrossValidationIterator NBS_iterator = new CrossValidationIterator(NBS_instances, nfolds);
       
       while(NFR_iterator.hasNext() && NBS_iterator.hasNext()) {
           
           InstanceList NFR_training_instances = NFR_iterator.nextSplit()[0];
           InstanceList NFR_testing_instances = NFR_iterator.nextSplit()[1];
           
           InstanceList NBS_training_instances = NBS_iterator.nextSplit()[0];
           InstanceList NBS_testing_instances = NBS_iterator.nextSplit()[1];*/
           
           /* Here we take each set of instances and we train its Hidden Markov Model
           using the HMM_SequenceAnalyst constructor.
           */
           
           //HMM_FeatureAnalyst NFRtrainer = new HMM_SequenceAnalyst(NFR_training_instances, NFR_testing_instances);
           //HMM_FeatureAnalyst NBStrainer = new HMM_SequenceAnalyst(NBS_training_instances, NBS_testing_instances);
           
           /* Here we take each set of instances and we create its n-gram graph using the
           NGG_SequenceAnalyst constructor.
           */
           
           /*DocumentNGramGraph NFR_training_ngGraph = new DocumentNGramGraph();
           DocumentNGramGraph NBS_training_ngGraph = new DocumentNGramGraph();
           
           NGG_SequenceAnalyst NGGtrainer = new NGG_SequenceAnalyst(NFR_training_instances, NBS_training_instances, NFR_training_ngGraph, NBS_training_ngGraph);
           
           int NFR_TruePositives = 0, NFR_FalseNegatives = 0, NFR_FalsePositives = 0, NFR_TrueNegatives;
           int NBS_TruePositives = 0, NBS_FalseNegatives = 0, NBS_FalsePositives, NBS_TrueNegatives;*/
           
           /* Here we classify the testing instances both for the NFR and NBS classes and 
           we take the false negative, the false positives, the true negatives and the true positives
           in order to use some statistical evaluations in the future.
           */
           
           /*NGGtrainer.NGGClassify(NFR_training_ngGraph, NBS_training_ngGraph, NFR_testing_instances, NFR_TruePositives, NFR_FalsePositives);
           NBS_FalsePositives = NFR_FalseNegatives;
           NBS_TrueNegatives = NFR_TruePositives;
           NGGtrainer.NGGClassify(NBS_training_ngGraph, NFR_training_ngGraph, NBS_testing_instances, NBS_TruePositives, NBS_FalsePositives);
           NFR_FalsePositives = NBS_FalseNegatives;
           NFR_TrueNegatives = NBS_TruePositives;

       }*/
       
    }
    
}
