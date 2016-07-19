/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package nucleosomepatternclassifier;

import be.ac.ulg.montefiore.run.jahmm.ObservationDiscrete;
import entities.HMMFeatureVector;
import representation.HMM_SequenceAnalyst;
import entities.HMMSequence;
import entities.NGGFeatureVector;
import entities.RepresentationFeatureVector;
import entities.SequenceInstance;
import entities.WekaHMMFeatureVector;
import entities.WekaNGGFeatureVector;
import gr.demokritos.iit.jinsect.documentModel.representations.DocumentNGramGraph;
import io.FAFileReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import static java.util.Collections.rotate;
import java.util.List;
import representation.GenomicSequenceAnalyst;
import representation.GenomicSequenceRepresentationHandler;
import representation.HmmHandler;
import representation.NGGHandler;
import representation.NGG_SequenceAnalyst;
import statistics.BinaryStatisticsEvaluator;
import weka.core.Instance;
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
        
        //GenomicSequenceAnalyst<List<ObservationDiscrete<HMMSequence.Packet>>> analyst = new HMM_SequenceAnalyst();
        NGG_SequenceAnalyst analyst = new NGG_SequenceAnalyst();
        
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
        
        List<SequenceInstance> NFR_testingSeqs = null;
        NFR_testingSeqs = new ArrayList();
         List<SequenceInstance> NBS_testingSeqs = null;
        NBS_testingSeqs = new ArrayList();

        //for(int i = 0; i < evaluations; i++) {
        for (int i = 0; i < nfolds; i++) {
            /* Initializing the training sequences */
            for(int j = 0; j < (nfolds-1)*NFRpartitionSize; j++) {
                NFR_trainingSeqs.add(NFR_Seqs.get(j));
            }
            
            for(int j = 0; j < (nfolds-1)*NBSpartitionSize; j++) {
                NBS_trainingSeqs.add(NBS_Seqs.get(j));
            }
            
            /* Initializing the testing sequences */
            for(int j = (nfolds-1)*NFRpartitionSize; j < NFR_Seqs.size(); j++) {
                NFR_testingSeqs.add(NBS_Seqs.get(j));
            }
            
            for(int j = (nfolds-1)*NBSpartitionSize; j < NBS_Seqs.size(); j++) {
                NBS_testingSeqs.add(NBS_Seqs.get(j));
            }
            
            /* Representing the sequences as HMMs */
            //List<List<ObservationDiscrete<HMMSequence.Packet>>> NFRTrainingHMM = analyst.represent(NFR_trainingSeqs);
            //List<List<ObservationDiscrete<HMMSequence.Packet>>> NBSTrainingHMM = analyst.represent(NBS_trainingSeqs);
            
            /* The same with the testing sequences */
            //List<List<ObservationDiscrete<HMMSequence.Packet>>> NFRTestingHMM = analyst.represent(NFR_testingSeqs);
            //List<List<ObservationDiscrete<HMMSequence.Packet>>> NBSTestingHMM = analyst.represent(NBS_testingSeqs);
            
            /* Representing the sequences as NGGs */
            List<List<DocumentNGramGraph>> NFRTrainingNGG = analyst.represent(NFR_trainingSeqs);
            List<List<DocumentNGramGraph>> NBSTrainingNGG = analyst.represent(NBS_trainingSeqs);
            
            /* The same with the testing sequences */
            List<List<DocumentNGramGraph>> NFRTestingNGG = analyst.represent(NFR_testingSeqs);
            List<List<DocumentNGramGraph>> NBSTestingNGG = analyst.represent(NBS_testingSeqs);
            
            /* We train the two HMMs only by using the training HMM sequences */
            
            /*GenomicSequenceRepresentationHandler<List<ObservationDiscrete<HMMSequence.Packet>>> handler = new HmmHandler();
            handler.train(NFRTrainingHMM, "Nucleosome Free Region");
            handler.train(NBSTrainingHMM, "Nucleosome Binding Site");*/
            
            /* We train the two NGGs only by using the training NGG sequences */
            NGGHandler handler = new NGGHandler();
            handler.train(NFRTrainingNGG, "Nucleosome Free Region");
            handler.train(NBSTrainingNGG, "Nucleosome Binding Site");
            
            /* Initializing the vectors we want to store */
            
            /*ArrayList<HMMFeatureVector> NFRTrainingVectors = new ArrayList<HMMFeatureVector>();
            ArrayList<HMMFeatureVector> NBSTrainingVectors = new ArrayList<HMMFeatureVector>();
            
            ArrayList<HMMFeatureVector> NFRTestingVectors = new ArrayList<HMMFeatureVector>();
            ArrayList<HMMFeatureVector> NBSTestingVectors = new ArrayList<HMMFeatureVector>();*/
            ArrayList<NGGFeatureVector> NFRTrainingVectors = new ArrayList<NGGFeatureVector>();
            ArrayList<NGGFeatureVector> NBSTrainingVectors = new ArrayList<NGGFeatureVector>();
            
            ArrayList<NGGFeatureVector> NFRTestingVectors = new ArrayList<NGGFeatureVector>();
            ArrayList<NGGFeatureVector> NBSTestingVectors = new ArrayList<NGGFeatureVector>();
            
            /* Getting the feature vectors for each of our sequence lists */
            
            /*for(List<ObservationDiscrete<HMMSequence.Packet>> NFRinstanceRepresentation : NFRTrainingHMM) {
                NFRTrainingVectors.add((HMMFeatureVector) handler.getFeatureVector(NFRinstanceRepresentation));
            }  
            for(List<ObservationDiscrete<HMMSequence.Packet>> NBSinstanceRepresentation : NBSTrainingHMM) {
                NBSTrainingVectors.add((HMMFeatureVector) handler.getFeatureVector(NBSinstanceRepresentation));
            }   
            for(List<ObservationDiscrete<HMMSequence.Packet>> NFRinstanceRepresentation : NFRTestingHMM) {
                NFRTestingVectors.add((HMMFeatureVector) handler.getFeatureVector(NFRinstanceRepresentation));
            }
            for(List<ObservationDiscrete<HMMSequence.Packet>> NBSinstanceRepresentation : NBSTestingHMM) {
                NBSTestingVectors.add((HMMFeatureVector) handler.getFeatureVector(NBSinstanceRepresentation));
            }*/
            
            for(List<DocumentNGramGraph> NFRinstanceRepresentation : NFRTrainingNGG) {
                NFRTrainingVectors.add((NGGFeatureVector) handler.getFeatureVector(NFRinstanceRepresentation));
            }  
            for(List<DocumentNGramGraph> NBSinstanceRepresentation : NBSTrainingNGG) {
                NBSTrainingVectors.add((NGGFeatureVector) handler.getFeatureVector(NBSinstanceRepresentation));
            }   
            for(List<DocumentNGramGraph> NFRinstanceRepresentation : NFRTestingNGG) {
                NFRTestingVectors.add((NGGFeatureVector) handler.getFeatureVector(NFRinstanceRepresentation));
            }
            for(List<DocumentNGramGraph> NBSinstanceRepresentation : NBSTestingNGG) {
                NBSTestingVectors.add((NGGFeatureVector) handler.getFeatureVector(NBSinstanceRepresentation));
            }
            
            //Get the Weka feature vectors
            /*WekaHMMFeatureVector HMMfv= new WekaHMMFeatureVector();
            Instances Training_Instances = HMMfv.fillInstanceSet(NFRTrainingVectors, NBSTrainingVectors);
            Instances Testing_Instances = HMMfv.fillInstanceSet(NFRTestingVectors, NBSTestingVectors);*/
            
            WekaNGGFeatureVector NGGfv= new WekaNGGFeatureVector();
            Instances Training_Instances = NGGfv.fillInstanceSet(NFRTrainingVectors, NBSTrainingVectors);
            Instances Testing_Instances = NGGfv.fillInstanceSet(NFRTestingVectors, NBSTestingVectors);
            
            // Perform classification and get Confusion Matrix
            BinaryStatisticsEvaluator ev = new BinaryStatisticsEvaluator();
            double[][] ConfMatrix = ev.getConfusionMatrix(Training_Instances, Testing_Instances);
            
            // Print results
            System.out.println("Precision of model :" + ev.getPrecision(ConfMatrix));
            System.out.println("Accuracy of model :" + ev.getAccuracy(ConfMatrix));;
            System.out.println("AUC of model :" + ev.getAUC(ConfMatrix));
            System.out.println("Recall of model :" + ev.getRecall(ConfMatrix));
            System.out.println("Specificity of model :" + ev.getSpecificity(ConfMatrix));
            
            rotate(NFR_Seqs, NFRpartitionSize);
            rotate(NBS_Seqs, NBSpartitionSize);
            
            NFR_trainingSeqs.clear();
            NBS_trainingSeqs.clear();
            NFR_testingSeqs.clear();
            NBS_testingSeqs.clear();
        }
        
       
    }
    
}
