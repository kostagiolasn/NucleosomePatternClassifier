/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package run;

import be.ac.ulg.montefiore.run.jahmm.ObservationDiscrete;
import entities.BOWFeatureVector;
import entities.BaselineBOWFeatureVector;
import entities.HMMFeatureVector;
import entities.HMMSequence;
import entities.NGGFeatureVector;
import entities.SequenceInstance;
import entities.WekaBOWFeatureVector;
import entities.WekaBaselineBOWFeatureVector;
import entities.WekaHMMFeatureVector;
import entities.WekaNGGFeatureVector;
import gr.demokritos.iit.jinsect.documentModel.representations.DocumentNGramGraph;
import io.FAFileReader;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import static java.util.Collections.rotate;
import java.util.List;
import representation.BOWHandler;
import representation.BOW_SequenceAnalyst;
import representation.BagOfWords;
import representation.BaselineBOWHandler;
import representation.BaselineBOW_SequenceAnalyst;
import representation.BaselineBagOfWords;
import representation.GenomicSequenceAnalyst;
import representation.GenomicSequenceRepresentationHandler;
import representation.HMM_SequenceAnalyst;
import representation.HmmHandler;
import representation.NGGHandler;
import representation.NGG_SequenceAnalyst;
import representation.NormalizedHmmHandler;
import statistics.BinaryStatisticsEvaluator;
import weka.core.Instances;
import weka.core.converters.ArffSaver;

/**
 *
 * @author nikos
 */
public class RunHandler {
    public void run(String NFR_pathfile, String NBS_pathfile, String folds, String representation_type, String classifier_type, boolean testAll) throws IOException {
        FAFileReader reader = new FAFileReader();
//        ManyInstancesPerFileConverter m = new ManyInstancesPerFileConverter();
        ArrayList<SequenceInstance> NFR_instances = reader.getSequencesFromFile(NFR_pathfile);
        ArrayList<SequenceInstance> NBS_instances = reader.getSequencesFromFile(NBS_pathfile);        
        
       // ArrayList<SequenceInstance> NBS_instances = new ArrayList<>(temp_NBS_instances.subList(0, NFR_instances.size()));
        
        GenomicSequenceAnalyst<List<ObservationDiscrete<HMMSequence.Packet>>> hmm_analyst = new HMM_SequenceAnalyst();
        NGG_SequenceAnalyst ngg_analyst = new NGG_SequenceAnalyst();
        BOW_SequenceAnalyst bow_analyst = new BOW_SequenceAnalyst();
        BaselineBOW_SequenceAnalyst baselinebow_analyst = new BaselineBOW_SequenceAnalyst();
        
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

        double total_score = 0.0d;
        for (int i = 0; i < nfolds; i++) {
            initTrainingSeqs(nfolds, NFRpartitionSize, NFR_trainingSeqs, NFR_Seqs, 
                    NBSpartitionSize, NBS_trainingSeqs, NBS_Seqs);
            
            initTestSeqs(nfolds, NFRpartitionSize, NFR_Seqs, NFR_testingSeqs, 
                    NBSpartitionSize, NBS_Seqs, NBS_testingSeqs);
            
            if("HMM".equals(representation_type)) {
                /* Representing the sequences as HMMs */
                List<List<ObservationDiscrete<HMMSequence.Packet>>> NFRTrainingHMM = hmm_analyst.represent(NFR_trainingSeqs);
                List<List<ObservationDiscrete<HMMSequence.Packet>>> NBSTrainingHMM = hmm_analyst.represent(NBS_trainingSeqs);

                /* The same with the testing sequences */
                List<List<ObservationDiscrete<HMMSequence.Packet>>> NFRTestingHMM = hmm_analyst.represent(NFR_testingSeqs);
                List<List<ObservationDiscrete<HMMSequence.Packet>>> NBSTestingHMM = hmm_analyst.represent(NBS_testingSeqs);
                
                /* We train the two HMMs only by using the training HMM sequences */
                long tic = System.nanoTime();
                
                GenomicSequenceRepresentationHandler<List<ObservationDiscrete<HMMSequence.Packet>>> handler = new HmmHandler();
                handler.train(NFRTrainingHMM, "Nucleosome Free Region");
                handler.train(NBSTrainingHMM, "Nucleosome Binding Site");
                
                long tac = System.nanoTime();
                long elapsedTime = tac - tic;
                double seconds = (double)elapsedTime / 1000000000.0;
                System.out.println("time elapsed for HMM training : " + seconds);
                /* Initializing the vectors we want to store */
            
                ArrayList<HMMFeatureVector> NFRTrainingVectors = new ArrayList<>();
                ArrayList<HMMFeatureVector> NBSTrainingVectors = new ArrayList<>();

                ArrayList<HMMFeatureVector> NFRTestingVectors = new ArrayList<>();
                ArrayList<HMMFeatureVector> NBSTestingVectors = new ArrayList<>();
                
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
                
                try {
                    saveFoldFiles(Training_Instances, i, Testing_Instances);
                } catch (IOException ioe) {
                    ioe.printStackTrace(System.err);
                    System.out.println("Could not output fold ARFF files "
                            + "(perhaps ARFF directory is missing?). "
                            + "Skipping...");
                }
                
                tic = System.nanoTime();
                
                // Perform classification and get Confusion Matrix
                BinaryStatisticsEvaluator ev = new BinaryStatisticsEvaluator();
                double[][] ConfMatrix = ev.getConfusionMatrix(Training_Instances, Testing_Instances, classifier_type);
                
                tac = System.nanoTime();
                elapsedTime = tac - tic;
                seconds = (double)elapsedTime / 1000000000.0;
                System.out.println("time elapsed for HMM classification : " + seconds);
                // Print results
                System.out.println("Precision of model :" + ev.getPrecision(ConfMatrix));
                System.out.println("Accuracy of model :" + ev.getAccuracy(ConfMatrix));
                System.out.println("AUC of model :" + ev.getAUC(ConfMatrix));
                System.out.println("Recall of model :" + ev.getRecall(ConfMatrix));
                System.out.println("Specificity of model :" + ev.getSpecificity(ConfMatrix));
                System.out.println("F-score of model :" + ev.getfScore(ConfMatrix));

                rotate(NFR_Seqs, NFRpartitionSize);
                rotate(NBS_Seqs, NBSpartitionSize);

                clearSeqs(NFR_trainingSeqs, NBS_trainingSeqs, NFR_testingSeqs, NBS_testingSeqs);
            }
            
            if("Normalized_HMM".equals(representation_type)) {
                /* Representing the sequences as HMMs */
                List<List<ObservationDiscrete<HMMSequence.Packet>>> NFRTrainingHMM = hmm_analyst.represent(NFR_trainingSeqs);
                List<List<ObservationDiscrete<HMMSequence.Packet>>> NBSTrainingHMM = hmm_analyst.represent(NBS_trainingSeqs);

                /* The same with the testing sequences */
                List<List<ObservationDiscrete<HMMSequence.Packet>>> NFRTestingHMM = hmm_analyst.represent(NFR_testingSeqs);
                List<List<ObservationDiscrete<HMMSequence.Packet>>> NBSTestingHMM = hmm_analyst.represent(NBS_testingSeqs);
                
                /* We train the two HMMs only by using the training HMM sequences */
                long tic = System.nanoTime();
                
                GenomicSequenceRepresentationHandler<List<ObservationDiscrete<HMMSequence.Packet>>> handler = new NormalizedHmmHandler();
                handler.train(NFRTrainingHMM, "Nucleosome Free Region");
                handler.train(NBSTrainingHMM, "Nucleosome Binding Site");
                
                long tac = System.nanoTime();
                long elapsedTime = tac - tic;
                double seconds = (double)elapsedTime / 1000000000.0;
                System.out.println("time elapsed for Normalized HMM training : " + seconds);
                /* Initializing the vectors we want to store */
            
                ArrayList<HMMFeatureVector> NFRTrainingVectors = new ArrayList<>();
                ArrayList<HMMFeatureVector> NBSTrainingVectors = new ArrayList<>();

                ArrayList<HMMFeatureVector> NFRTestingVectors = new ArrayList<>();
                ArrayList<HMMFeatureVector> NBSTestingVectors = new ArrayList<>();
                
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
                
                // Store instances to related fold files in ARFF subdir (WARNING: It must exist)
                try {
                    saveFoldFiles(Training_Instances, i, Testing_Instances);
                } catch (IOException ioe) {
                    ioe.printStackTrace(System.err);
                    System.out.println("Could not output fold ARFF files "
                            + "(perhaps ARFF directory is missing?). "
                            + "Skipping...");
                }
                
                tic = System.nanoTime();
                
                // Perform classification and get Confusion Matrix
                BinaryStatisticsEvaluator ev = new BinaryStatisticsEvaluator();
                double[][] ConfMatrix = ev.getConfusionMatrix(Training_Instances, Testing_Instances, classifier_type);
                
                tac = System.nanoTime();
                elapsedTime = tac - tic;
                seconds = (double)elapsedTime / 1000000000.0;
                System.out.println("time elapsed for Normalized HMM classification : " + seconds);
                // Print results
                System.out.println("Precision of model :" + ev.getPrecision(ConfMatrix));
                System.out.println("Accuracy of model :" + ev.getAccuracy(ConfMatrix));
                System.out.println("AUC of model :" + ev.getAUC(ConfMatrix));
                System.out.println("Recall of model :" + ev.getRecall(ConfMatrix));
                System.out.println("Specificity of model :" + ev.getSpecificity(ConfMatrix));
                System.out.println("F-score of model :" + ev.getfScore(ConfMatrix));

                rotate(NFR_Seqs, NFRpartitionSize);
                rotate(NBS_Seqs, NBSpartitionSize);

                clearSeqs(NFR_trainingSeqs, NBS_trainingSeqs, NFR_testingSeqs, NBS_testingSeqs);
            }
            
            if("NGG".equals(representation_type)) {
                /* Representing the sequences as NGGs */
                List<List<DocumentNGramGraph>> NFRTrainingNGG = ngg_analyst.represent(NFR_trainingSeqs);
                List<List<DocumentNGramGraph>> NBSTrainingNGG = ngg_analyst.represent(NBS_trainingSeqs);

                /* The same with the testing sequences */
                List<List<DocumentNGramGraph>> NFRTestingNGG = ngg_analyst.represent(NFR_testingSeqs);
                List<List<DocumentNGramGraph>> NBSTestingNGG = ngg_analyst.represent(NBS_testingSeqs);


                /* We train the two NGGs only by using the training NGG sequences */
                long tic = System.nanoTime();
                
                NGGHandler handler = new NGGHandler();
                handler.train(NFRTrainingNGG, "Nucleosome Free Region");
                handler.train(NBSTrainingNGG, "Nucleosome Binding Site");
                
                long tac = System.nanoTime();
                long elapsedTime = tac - tic;
                double seconds = (double)elapsedTime / 1000000000.0;
                System.out.println("time elapsed for NGG training : " + seconds);
                                
                /* Initializing the vectors we want to store */
                
                ArrayList<NGGFeatureVector> NFRTrainingVectors = new ArrayList<>();
                ArrayList<NGGFeatureVector> NBSTrainingVectors = new ArrayList<>();

                ArrayList<NGGFeatureVector> NFRTestingVectors = new ArrayList<>();
                ArrayList<NGGFeatureVector> NBSTestingVectors = new ArrayList<>();
                
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
                Instances Training_Instances = null;
                Instances Testing_Instances = null;
                if (!testAll){
                    Training_Instances = NGGfv.fillInstanceSet(NFRTrainingVectors, NBSTrainingVectors, "train");
                    Testing_Instances = NGGfv.fillInstanceSet(NFRTestingVectors, NBSTestingVectors, "test");
                }
                else{
                    ArrayList<NGGFeatureVector> allNBS = new ArrayList<>();
                    ArrayList<NGGFeatureVector> allNFR = new ArrayList<>();
                    allNFR.addAll(NFRTrainingVectors);
                    allNFR.addAll(NFRTestingVectors);
                    allNBS.addAll( NBSTrainingVectors);
                    allNBS.addAll( NBSTestingVectors);
                    System.out.println("Testing on all : " + allNFR.size() + " , " + allNBS.size() + "vectors");
                    Training_Instances = NGGfv.fillInstanceSet(NFRTrainingVectors, NBSTrainingVectors, "train");
                    Testing_Instances = NGGfv.fillInstanceSet(allNFR, allNBS, "test");
                }

                // Store instances to related fold files in ARFF subdir (WARNING: It must exist)
                try {
                    saveFoldFiles(Training_Instances, i, Testing_Instances);
                } catch (IOException ioe) {
                    ioe.printStackTrace(System.err);
                    System.out.println("Could not output fold ARFF files "
                            + "(perhaps ARFF directory is missing?). "
                            + "Skipping...");
                }
                
                tic = System.nanoTime();

                // Perform classification and get Confusion Matrix
                BinaryStatisticsEvaluator ev = new BinaryStatisticsEvaluator();
                double[][] ConfMatrix = ev.getConfusionMatrix(Training_Instances, Testing_Instances, classifier_type);
                
                tac = System.nanoTime();
                elapsedTime = tac - tic;
                seconds = (double)elapsedTime / 1000000000.0;
                System.out.println("time elapsed for NGG classification : " + seconds);

                outputResults(i, ev, ConfMatrix);

                rotate(NFR_Seqs, NFRpartitionSize);
                rotate(NBS_Seqs, NBSpartitionSize);

                clearSeqs(NFR_trainingSeqs, NBS_trainingSeqs, NFR_testingSeqs, NBS_testingSeqs);

                if (testAll){
                    System.out.println("Breaking because testing all.");
                    break;
                }

            }
            
            if("BOW".equals(representation_type)) {
                /* Representing the sequences as BOWs */
                List<List<BagOfWords>> NFRTrainingBOW = bow_analyst.represent(NFR_trainingSeqs);
                List<List<BagOfWords>> NBSTrainingBOW = bow_analyst.represent(NBS_trainingSeqs);

                /* The same with the testing sequences */
                List<List<BagOfWords>> NFRTestingBOW = bow_analyst.represent(NFR_testingSeqs);
                List<List<BagOfWords>> NBSTestingBOW = bow_analyst.represent(NBS_testingSeqs);
                
                /* We train the two BOWs only by using the training BOW sequences */
                
                long tic = System.nanoTime();
                
                BOWHandler handler = new BOWHandler();
                handler.train(NFRTrainingBOW, "Nucleosome Free Region");
                handler.train(NBSTrainingBOW, "Nucleosome Binding Site");
                
                long tac = System.nanoTime();
                long elapsedTime = tac - tic;
                double seconds = (double)elapsedTime / 1000000000.0;
                System.out.println("time elapsed for BOW training : " + seconds);
                
                /* Initializing the vectors we want to store */
                
                ArrayList<BOWFeatureVector> NFRTrainingVectors = new ArrayList<>();
                ArrayList<BOWFeatureVector> NBSTrainingVectors = new ArrayList<>();

                ArrayList<BOWFeatureVector> NFRTestingVectors = new ArrayList<>();
                ArrayList<BOWFeatureVector> NBSTestingVectors = new ArrayList<>();
                
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
                
                 WekaBOWFeatureVector BOWfv = new WekaBOWFeatureVector();
                Instances Training_Instances = BOWfv.fillInstanceSet(NFRTrainingVectors, NBSTrainingVectors);
                Instances Testing_Instances = BOWfv.fillInstanceSet(NFRTestingVectors, NBSTestingVectors);
                
                // Store instances to related fold files in ARFF subdir (WARNING: It must exist)
                try {
                    saveFoldFiles(Training_Instances, i, Testing_Instances);
                } catch (IOException ioe) {
                    ioe.printStackTrace(System.err);
                    System.out.println("Could not output fold ARFF files "
                            + "(perhaps ARFF directory is missing?). "
                            + "Skipping...");
                }
                
                tic = System.nanoTime();
                
                // Perform classification and get Confusion Matrix
                BinaryStatisticsEvaluator ev = new BinaryStatisticsEvaluator();
                double[][] ConfMatrix = ev.getConfusionMatrix(Training_Instances, Testing_Instances, classifier_type);
                
                tac = System.nanoTime();
                elapsedTime = tac - tic;
                seconds = (double)elapsedTime / 1000000000.0;
                System.out.println("time elapsed for BOW classification : " + seconds);

                outputResults(i, ev, ConfMatrix);


                rotate(NFR_Seqs, NFRpartitionSize);
                rotate(NBS_Seqs, NBSpartitionSize);

                clearSeqs(NFR_trainingSeqs, NBS_trainingSeqs, NFR_testingSeqs, NBS_testingSeqs);
            }
            
            if(representation_type.startsWith("Baseline_BOW")) {
                int length = Integer.parseInt(representation_type.substring("Baseline_BOW".length()));
                baselinebow_analyst.setLength(length);
                /* Representing the sequences as BOWs */
                List<List<BaselineBagOfWords>> NFRTrainingBOW = baselinebow_analyst.represent(NFR_trainingSeqs);
                List<List<BaselineBagOfWords>> NBSTrainingBOW = baselinebow_analyst.represent(NBS_trainingSeqs);

                /* The same with the testing sequences */
                List<List<BaselineBagOfWords>> NFRTestingBOW = baselinebow_analyst.represent(NFR_testingSeqs);
                List<List<BaselineBagOfWords>> NBSTestingBOW = baselinebow_analyst.represent(NBS_testingSeqs);
                
                /* We train the two BOWs only by using the training BOW sequences */
                
                long tic = System.nanoTime();
                
                BaselineBOWHandler handler = new BaselineBOWHandler(length);
                handler.train(NFRTrainingBOW, "Nucleosome Free Region");
                handler.train(NBSTrainingBOW, "Nucleosome Binding Site");
                
                long tac = System.nanoTime();
                long elapsedTime = tac - tic;
                double seconds = (double)elapsedTime / 1000000000.0;
                System.out.println("time elapsed for BOW training : " + seconds);
                
                /* Initializing the vectors we want to store */
                
                ArrayList<BaselineBOWFeatureVector> NFRTrainingVectors = new ArrayList<>();
                ArrayList<BaselineBOWFeatureVector> NBSTrainingVectors = new ArrayList<>();

                ArrayList<BaselineBOWFeatureVector> NFRTestingVectors = new ArrayList<>();
                ArrayList<BaselineBOWFeatureVector> NBSTestingVectors = new ArrayList<>();
                
                for(List<BaselineBagOfWords> NFRinstanceRepresentation : NFRTrainingBOW) {
                    NFRTrainingVectors.add((BaselineBOWFeatureVector) handler.getFeatureVector(NFRinstanceRepresentation, "Nucleosome Free Region"));
                }  
                for(List<BaselineBagOfWords> NBSinstanceRepresentation : NBSTrainingBOW) {
                    NBSTrainingVectors.add((BaselineBOWFeatureVector) handler.getFeatureVector(NBSinstanceRepresentation, "Nucleosome Binding Site"));
                }   
                for(List<BaselineBagOfWords> NFRinstanceRepresentation : NFRTestingBOW) {
                    NFRTestingVectors.add((BaselineBOWFeatureVector) handler.getFeatureVector(NFRinstanceRepresentation, "Nucleosome Free Region"));
                }
                for(List<BaselineBagOfWords> NBSinstanceRepresentation : NBSTestingBOW) {
                    NBSTestingVectors.add((BaselineBOWFeatureVector) handler.getFeatureVector(NBSinstanceRepresentation, "Nucleosome Binding Site"));
                }
                
                WekaBaselineBOWFeatureVector BBOWfv= new WekaBaselineBOWFeatureVector(length);
                Instances Training_Instances = BBOWfv.fillInstanceSet(NFRTrainingVectors, NBSTrainingVectors);
                Instances Testing_Instances = BBOWfv.fillInstanceSet(NFRTestingVectors, NBSTestingVectors);
                
                // Store instances to related fold files in ARFF subdir (WARNING: It must exist)
                try {
                    saveFoldFiles(Training_Instances, i, Testing_Instances);
                } catch (IOException ioe) {
                    ioe.printStackTrace(System.err);
                    System.out.println("Could not output fold ARFF files "
                            + "(perhaps ARFF directory is missing?). "
                            + "Skipping...");
                }
                
                tic = System.nanoTime();
                
                // Perform classification and get Confusion Matrix
                BinaryStatisticsEvaluator ev = new BinaryStatisticsEvaluator();
                double[][] ConfMatrix = ev.getConfusionMatrix(Training_Instances, Testing_Instances, classifier_type);
                
                tac = System.nanoTime();
                elapsedTime = tac - tic;
                seconds = (double)elapsedTime / 1000000000.0;
                System.out.println("time elapsed for BOW classification : " + seconds);

                total_score += outputResults(i, ev, ConfMatrix);


                rotate(NFR_Seqs, NFRpartitionSize);
                rotate(NBS_Seqs, NBSpartitionSize);

                clearSeqs(NFR_trainingSeqs, NBS_trainingSeqs, NFR_testingSeqs, NBS_testingSeqs);
            }
        }
        System.out.println("Total score:" + total_score / ((double)nfolds));
    }

    protected void clearSeqs(List<SequenceInstance> NFR_trainingSeqs, List<SequenceInstance> NBS_trainingSeqs, List<SequenceInstance> NFR_testingSeqs, List<SequenceInstance> NBS_testingSeqs) {
        NFR_trainingSeqs.clear();
        NBS_trainingSeqs.clear();
        NFR_testingSeqs.clear();
        NBS_testingSeqs.clear();
    }

    protected double outputResults(int iFoldNo, BinaryStatisticsEvaluator ev, double[][] ConfMatrix) {
        // Print results
        System.out.println("\n=== Fold:" + iFoldNo + "===");
        System.out.println("Precision of model :" + ev.getPrecision(ConfMatrix));
        System.out.println("Accuracy of model :" + ev.getAccuracy(ConfMatrix));;
        System.out.println("AUC of model :" + ev.getAUC(ConfMatrix));
        System.out.println("Recall of model :" + ev.getRecall(ConfMatrix));
        System.out.println("Specificity of model :" + ev.getSpecificity(ConfMatrix));
        System.out.println("F-score of model :" + ev.getfScore(ConfMatrix));
        return ev.getfScore(ConfMatrix);
    }

    protected void saveFoldFiles(Instances Training_Instances, int i, Instances Testing_Instances) throws IOException {
        // Store instances to related fold files in ARFF subdir (WARNING: It must exist)
        ArffSaver asSaver = new ArffSaver();
        asSaver.setInstances(Training_Instances);
        asSaver.setFile(new File(String.format("ARFF/train-fold%d.arff", i)));
        asSaver.writeBatch();
        
        asSaver.setInstances(Testing_Instances);
        asSaver.setFile(new File(String.format("ARFF/test-fold%d.arff", i)));
        asSaver.writeBatch();
    }

    protected void initTestSeqs(int nfolds, int NFRpartitionSize, List<SequenceInstance> NFR_Seqs, List<SequenceInstance> NFR_testingSeqs, int NBSpartitionSize, List<SequenceInstance> NBS_Seqs, List<SequenceInstance> NBS_testingSeqs) {
        /* Initializing the testing sequences */
        for(int j = (nfolds-1)*NFRpartitionSize; j < NFR_Seqs.size(); j++) {
            NFR_testingSeqs.add(NFR_Seqs.get(j));
        }
        
        for(int j = (nfolds-1)*NBSpartitionSize; j < NBS_Seqs.size(); j++) {
            NBS_testingSeqs.add(NBS_Seqs.get(j));
        }
    }

    protected void initTrainingSeqs(int nfolds, int NFRpartitionSize, List<SequenceInstance> NFR_trainingSeqs, List<SequenceInstance> NFR_Seqs, int NBSpartitionSize, List<SequenceInstance> NBS_trainingSeqs, List<SequenceInstance> NBS_Seqs) {
        /* Initializing the training sequences */
        for(int j = 0; j < (nfolds-1)*NFRpartitionSize; j++) {
            NFR_trainingSeqs.add(NFR_Seqs.get(j));
        }
        
        for(int j = 0; j < (nfolds-1)*NBSpartitionSize; j++) {
            NBS_trainingSeqs.add(NBS_Seqs.get(j));
        }
    }
}
