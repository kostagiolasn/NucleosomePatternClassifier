/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package statistics;

import java.util.logging.Level;
import java.util.logging.Logger;
import weka.classifiers.Classifier;
import weka.classifiers.Evaluation;
import weka.classifiers.bayes.NaiveBayes;
import weka.classifiers.lazy.IBk;
import weka.classifiers.trees.J48;
import weka.core.Instance;
import weka.core.Instances;
import weka.core.pmml.jaxbbindings.SupportVectorMachineModel;

/**
 *
 * @author nikos
 */
public class BinaryStatisticsEvaluator implements StatisticsEvaluator {
    @Override
    public double getAccuracy(double[][] ConfMatrix) {
        //Overall effectiveness of a classifier
        return (ConfMatrix[1][1] + ConfMatrix[0][0]) / (ConfMatrix[0][0] + ConfMatrix[0][1] + ConfMatrix[1][0] + ConfMatrix[1][1]);
    }
    
    @Override
    public double getPrecision(double[][] ConfMatrix) {
        //Class agreement of the data labels with the positive labels given by the classifier
        return ConfMatrix[1][1] / (ConfMatrix[1][1] + ConfMatrix[0][1]);
        //return TruePositives / TruePositives + FalsePositives;
    }
    
    @Override
    public double getRecall(double[][] ConfMatrix) {
        //Effectiveness of a classifier to identify positive labels
        return ConfMatrix[1][1] / (ConfMatrix[1][1] + ConfMatrix[1][0]);
        //return TruePositives / TruePositives + FalseNegatives;
    }
    
    @Override
    public double getSpecificity(double[][] ConfMatrix) {
        //How effectively a classifier identifies negative labels
        return ConfMatrix[0][0] / (ConfMatrix[0][1] + ConfMatrix[0][0]);
        //return TrueNegatives / FalsePositives + TrueNegatives;
    }
    
    @Override
    public double getAUC(double[][] ConfMatrix) {
        //Classifier's ability to avoid false classification
        return 0.5*(getSpecificity(ConfMatrix) + getRecall(ConfMatrix));
    }

    @Override
    public double getfScore(double[][] ConfMatrix) {
        return 2* this.getAccuracy(ConfMatrix) * this.getPrecision(ConfMatrix) / (this.getAccuracy(ConfMatrix) + this.getPrecision(ConfMatrix)); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public double [][] getConfusionMatrix(Instances Training_Instances, Instances Testing_Instances, String classifier) {
            
            Classifier cModel = null;
            if("NB".equals(classifier)) {
               cModel = (Classifier)new NaiveBayes();
               try {
                cModel.buildClassifier(Training_Instances);
                } catch (Exception ex) {
                    Logger.getLogger(BinaryStatisticsEvaluator.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            else if("DT".equals(classifier)) {
                cModel = (Classifier)new J48();
                try {
                cModel.buildClassifier(Training_Instances);
                } catch (Exception ex) {
                    Logger.getLogger(BinaryStatisticsEvaluator.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            else if("SVM".equals(classifier)) {
                cModel = (Classifier)new SupportVectorMachineModel();
                
                try {
                cModel.buildClassifier(Training_Instances);
                } catch (Exception ex) {
                    Logger.getLogger(BinaryStatisticsEvaluator.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            else if("KNN".equals(classifier)) {
                cModel = (Classifier)new IBk();
                try {
                cModel.buildClassifier(Training_Instances);
                } catch (Exception ex) {
                    Logger.getLogger(BinaryStatisticsEvaluator.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            //Test the model
            Evaluation eTest;
        try {
            eTest = new Evaluation(Training_Instances);
            eTest.evaluateModel(cModel, Testing_Instances);
            //Print the result
            String strSummary = eTest.toSummaryString();
            System.out.println(strSummary);
            
            //Get the confusion matrix
            double [][] cmMatrix = eTest.confusionMatrix();
            return cmMatrix;
        } catch (Exception ex) {
            Logger.getLogger(BinaryStatisticsEvaluator.class.getName()).log(Level.SEVERE, null, ex);
        }         
        return null;   
 }
}
