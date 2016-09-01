/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package run;

import be.ac.ulg.montefiore.run.jahmm.ObservationDiscrete;
import entities.BOWFeatureVector;
import entities.HMMFeatureVector;
import entities.HMMSequence;
import entities.NGGFeatureVector;
import entities.SequenceInstance;
import entities.WekaBOWFeatureVector;
import entities.WekaHMMFeatureVector;
import entities.WekaNGGFeatureVector;
import gr.demokritos.iit.jinsect.documentModel.representations.DocumentNGramGraph;
import io.FAFileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import static java.util.Collections.rotate;
import java.util.List;
import representation.BOWHandler;
import representation.BOW_SequenceAnalyst;
import representation.BagOfWords;
import representation.GenomicSequenceAnalyst;
import representation.GenomicSequenceRepresentationHandler;
import representation.HMM_SequenceAnalyst;
import representation.HmmHandler;
import representation.NGGHandler;
import representation.NGG_SequenceAnalyst;
import statistics.BinaryStatisticsEvaluator;
import weka.core.Instances;

/**
 *
 * @author nikos
 */
public class RunHandler {
    public void run(String NFR_pathfile, String NBS_pathfile, String folds, String representation_type, String classifier_type) throws IOException {
        FAFileReader reader = new FAFileReader();
        ArrayList<SequenceInstance> NFR_instances = reader.getSequencesFromFile(NFR_pathfile);
        ArrayList<SequenceInstance> NBS_instances = reader.getSequencesFromFile(NBS_pathfile);        
        
       // ArrayList<SequenceInstance> NBS_instances = new ArrayList<>(temp_NBS_instances.subList(0, NFR_instances.size()));
        
        GenomicSequenceAnalyst<List<ObservationDiscrete<HMMSequence.Packet>>> hmm_analyst = new HMM_SequenceAnalyst();
        NGG_SequenceAnalyst ngg_analyst = new NGG_SequenceAnalyst();
        BOW_SequenceAnalyst bow_analyst = new BOW_SequenceAnalyst();
        
        int nfolds = Integer.parseInt(folds);
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
            
            if("HMM".equals(representation_type)) {
                /* Representing the sequences as HMMs */
                List<List<ObservationDiscrete<HMMSequence.Packet>>> NFRTrainingHMM = hmm_analyst.represent(NFR_trainingSeqs);
                List<List<ObservationDiscrete<HMMSequence.Packet>>> NBSTrainingHMM = hmm_analyst.represent(NBS_trainingSeqs);

                /* The same with the testing sequences */
                List<List<ObservationDiscrete<HMMSequence.Packet>>> NFRTestingHMM = hmm_analyst.represent(NFR_testingSeqs);
                List<List<ObservationDiscrete<HMMSequence.Packet>>> NBSTestingHMM = hmm_analyst.represent(NBS_testingSeqs);
                
                /* We train the two HMMs only by using the training HMM sequences */
            
                GenomicSequenceRepresentationHandler<List<ObservationDiscrete<HMMSequence.Packet>>> handler = new HmmHandler();
                handler.train(NFRTrainingHMM, "Nucleosome Free Region");
                handler.train(NBSTrainingHMM, "Nucleosome Binding Site");
                
                /* Initializing the vectors we want to store */
            
                ArrayList<HMMFeatureVector> NFRTrainingVectors = new ArrayList<HMMFeatureVector>();
                ArrayList<HMMFeatureVector> NBSTrainingVectors = new ArrayList<HMMFeatureVector>();

                ArrayList<HMMFeatureVector> NFRTestingVectors = new ArrayList<HMMFeatureVector>();
                ArrayList<HMMFeatureVector> NBSTestingVectors = new ArrayList<HMMFeatureVector>();
                
                 /* Getting the feature vectors for each of our sequence lists */
            
                for(List<ObservationDiscrete<HMMSequence.Packet>> NFRinstanceRepresentation : NFRTrainingHMM) {
                    NFRTrainingVectors.add((HMMFeatureVector) handler.getFeatureVector(NFRinstanceRepresentation, "Nucleosome Free Region"));
                }  
                for(List<ObservationDiscrete<HMMSequence.Packet>> NBSinstanceRepresentation : NBSTrainingHMM) {
                    NBSTrainingVectors.add((HMMFeatureVector) handler.getFeatureVector(NBSinstanceRepresentation, "Nucleosome Binding Site"));
                }   
                for(List<ObservationDiscrete<HMMSequence.Packet>> NFRinstanceRepresentation : NFRTestingHMM) {
                    NFRTestingVectors.add((HMMFeatureVector) handler.getFeatureVector(NFRinstanceRepresentation, "Nucleosome Free Region"));
                }
                for(List<ObservationDiscrete<HMMSequence.Packet>> NBSinstanceRepresentation : NBSTestingHMM) {
                    NBSTestingVectors.add((HMMFeatureVector) handler.getFeatureVector(NBSinstanceRepresentation, "Nucleosome Binding Site"));
                }
                
                //Get the Weka feature vectors
                WekaHMMFeatureVector HMMfv= new WekaHMMFeatureVector();
                Instances Training_Instances = HMMfv.fillInstanceSet(NFRTrainingVectors, NBSTrainingVectors);
                Instances Testing_Instances = HMMfv.fillInstanceSet(NFRTestingVectors, NBSTestingVectors);
                
                // Perform classification and get Confusion Matrix
                BinaryStatisticsEvaluator ev = new BinaryStatisticsEvaluator();
                double[][] ConfMatrix = ev.getConfusionMatrix(Training_Instances, Testing_Instances, classifier_type);

                // Print results
                System.out.println("Precision of model :" + ev.getPrecision(ConfMatrix));
                System.out.println("Accuracy of model :" + ev.getAccuracy(ConfMatrix));
                System.out.println("AUC of model :" + ev.getAUC(ConfMatrix));
                System.out.println("Recall of model :" + ev.getRecall(ConfMatrix));
                System.out.println("Specificity of model :" + ev.getSpecificity(ConfMatrix));
                System.out.println("F-score of model :" + ev.getfScore(ConfMatrix));

                rotate(NFR_Seqs, NFRpartitionSize);
                rotate(NBS_Seqs, NBSpartitionSize);

                NFR_trainingSeqs.clear();
                NBS_trainingSeqs.clear();
                NFR_testingSeqs.clear();
                NBS_testingSeqs.clear();
            }
            
            if("NGG".equals(representation_type)) {
                /* Representing the sequences as NGGs */
                List<List<DocumentNGramGraph>> NFRTrainingNGG = ngg_analyst.represent(NFR_trainingSeqs);
                List<List<DocumentNGramGraph>> NBSTrainingNGG = ngg_analyst.represent(NBS_trainingSeqs);

                /* The same with the testing sequences */
                List<List<DocumentNGramGraph>> NFRTestingNGG = ngg_analyst.represent(NFR_testingSeqs);
                List<List<DocumentNGramGraph>> NBSTestingNGG = ngg_analyst.represent(NBS_testingSeqs);
                
                /* We train the two NGGs only by using the training NGG sequences */
            
                NGGHandler handler = new NGGHandler();
                handler.train(NFRTrainingNGG, "Nucleosome Free Region");
                handler.train(NBSTrainingNGG, "Nucleosome Binding Site");
                                
                /* Initializing the vectors we want to store */
                
                ArrayList<NGGFeatureVector> NFRTrainingVectors = new ArrayList<NGGFeatureVector>();
                ArrayList<NGGFeatureVector> NBSTrainingVectors = new ArrayList<NGGFeatureVector>();

                ArrayList<NGGFeatureVector> NFRTestingVectors = new ArrayList<NGGFeatureVector>();
                ArrayList<NGGFeatureVector> NBSTestingVectors = new ArrayList<NGGFeatureVector>();
                
                /* Getting the feature vectors for each of our sequence lists */
                
                for(List<DocumentNGramGraph> NFRinstanceRepresentation : NFRTrainingNGG) {
                    NFRTrainingVectors.add((NGGFeatureVector) handler.getFeatureVector(NFRinstanceRepresentation, "Nucleosome Free Region"));
                }  
                for(List<DocumentNGramGraph> NBSinstanceRepresentation : NBSTrainingNGG) {
                    NBSTrainingVectors.add((NGGFeatureVector) handler.getFeatureVector(NBSinstanceRepresentation, "Nucleosome Binding Site"));
                }   
                for(List<DocumentNGramGraph> NFRinstanceRepresentation : NFRTestingNGG) {
                    NFRTestingVectors.add((NGGFeatureVector) handler.getFeatureVector(NFRinstanceRepresentation, "Nucleosome Free Region"));
                }
                for(List<DocumentNGramGraph> NBSinstanceRepresentation : NBSTestingNGG) {
                    NBSTestingVectors.add((NGGFeatureVector) handler.getFeatureVector(NBSinstanceRepresentation, "Nucleosome Binding Site"));
                }
                
                WekaNGGFeatureVector NGGfv= new WekaNGGFeatureVector();
                Instances Training_Instances = NGGfv.fillInstanceSet(NFRTrainingVectors, NBSTrainingVectors, "training");
                Instances Testing_Instances = NGGfv.fillInstanceSet(NFRTestingVectors, NBSTestingVectors, "testing");
                
                // Perform classification and get Confusion Matrix
                BinaryStatisticsEvaluator ev = new BinaryStatisticsEvaluator();
                Collections.shuffle(Training_Instances);
                Collections.shuffle(Testing_Instances);
                double[][] ConfMatrix = ev.getConfusionMatrix(Training_Instances, Testing_Instances, classifier_type);

                // Print results
                System.out.println("Precision of model :" + ev.getPrecision(ConfMatrix));
                System.out.println("Accuracy of model :" + ev.getAccuracy(ConfMatrix));;
                System.out.println("AUC of model :" + ev.getAUC(ConfMatrix));
                System.out.println("Recall of model :" + ev.getRecall(ConfMatrix));
                System.out.println("Specificity of model :" + ev.getSpecificity(ConfMatrix));
                System.out.println("F-score of model :" + ev.getfScore(ConfMatrix));

                rotate(NFR_Seqs, NFRpartitionSize);
                rotate(NBS_Seqs, NBSpartitionSize);

                NFR_trainingSeqs.clear();
                NBS_trainingSeqs.clear();
                NFR_testingSeqs.clear();
                NBS_testingSeqs.clear();

            }
            
            if("BOW".equals(representation_type)) {
                /* Representing the sequences as BOWs */
                List<List<BagOfWords>> NFRTrainingBOW = bow_analyst.represent(NFR_trainingSeqs);
                List<List<BagOfWords>> NBSTrainingBOW = bow_analyst.represent(NBS_trainingSeqs);

                /* The same with the testing sequences */
                List<List<BagOfWords>> NFRTestingBOW = bow_analyst.represent(NFR_testingSeqs);
                List<List<BagOfWords>> NBSTestingBOW = bow_analyst.represent(NBS_testingSeqs);
                
                /* We train the two BOWs only by using the training BOW sequences */
                BOWHandler handler = new BOWHandler();
                handler.train(NFRTrainingBOW, "Nucleosome Free Region");
                handler.train(NBSTrainingBOW, "Nucleosome Binding Site");
                
                /* Initializing the vectors we want to store */
                
                ArrayList<BOWFeatureVector> NFRTrainingVectors = new ArrayList<BOWFeatureVector>();
                ArrayList<BOWFeatureVector> NBSTrainingVectors = new ArrayList<BOWFeatureVector>();

                ArrayList<BOWFeatureVector> NFRTestingVectors = new ArrayList<BOWFeatureVector>();
                ArrayList<BOWFeatureVector> NBSTestingVectors = new ArrayList<BOWFeatureVector>();
                
                for(List<BagOfWords> NFRinstanceRepresentation : NFRTrainingBOW) {
                    NFRTrainingVectors.add((BOWFeatureVector) handler.getFeatureVector(NFRinstanceRepresentation, "Nucleosome Free Region"));
                }  
                for(List<BagOfWords> NBSinstanceRepresentation : NBSTrainingBOW) {
                    NBSTrainingVectors.add((BOWFeatureVector) handler.getFeatureVector(NBSinstanceRepresentation, "Nucleosome Binding Site"));
                }   
                for(List<BagOfWords> NFRinstanceRepresentation : NFRTestingBOW) {
                    NFRTestingVectors.add((BOWFeatureVector) handler.getFeatureVector(NFRinstanceRepresentation, "Nucleosome Free Region"));
                }
                for(List<BagOfWords> NBSinstanceRepresentation : NBSTestingBOW) {
                    NBSTestingVectors.add((BOWFeatureVector) handler.getFeatureVector(NBSinstanceRepresentation, "Nucleosome Binding Site"));
                }
                
                 WekaBOWFeatureVector NGGfv= new WekaBOWFeatureVector();
                Instances Training_Instances = NGGfv.fillInstanceSet(NFRTrainingVectors, NBSTrainingVectors);
                Instances Testing_Instances = NGGfv.fillInstanceSet(NFRTestingVectors, NBSTestingVectors);
                
                // Perform classification and get Confusion Matrix
                BinaryStatisticsEvaluator ev = new BinaryStatisticsEvaluator();
                double[][] ConfMatrix = ev.getConfusionMatrix(Training_Instances, Testing_Instances, classifier_type);

                // Print results
                System.out.println("Precision of model :" + ev.getPrecision(ConfMatrix));
                System.out.println("Accuracy of model :" + ev.getAccuracy(ConfMatrix));;
                System.out.println("AUC of model :" + ev.getAUC(ConfMatrix));
                System.out.println("Recall of model :" + ev.getRecall(ConfMatrix));
                System.out.println("Specificity of model :" + ev.getSpecificity(ConfMatrix));
                System.out.println("F-score of model :" + ev.getfScore(ConfMatrix));


                rotate(NFR_Seqs, NFRpartitionSize);
                rotate(NBS_Seqs, NBSpartitionSize);

                NFR_trainingSeqs.clear();
                NBS_trainingSeqs.clear();
                NFR_testingSeqs.clear();
                NBS_testingSeqs.clear();
            }
        }
    }
}
