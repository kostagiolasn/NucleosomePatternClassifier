/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package statistics;

/**
 *
 * @author nikos
 */
public class BinaryStatisticsEvaluator implements StatisticsEvaluator {
    @Override
    public double getAccuracy(int TruePositives, int FalseNegatives, int FalsePositives, int TrueNegatives) {
        //Overall effectiveness of a classifier
        return TruePositives + TrueNegatives / TruePositives + FalseNegatives + FalsePositives + TrueNegatives;
    }
    
    @Override
    public double getPrecision(int TruePositives, int FalseNegatives, int FalsePositives, int TrueNegatives) {
        //Class agreement of the data labels with the positive labels given by the classifier
        return TruePositives / TruePositives + FalsePositives;
    }
    
    @Override
    public double getRecall(int TruePositives, int FalseNegatives, int FalsePositives, int TrueNegatives) {
        //Effectiveness of a classifier to identify positive labels
        return TruePositives / TruePositives + FalseNegatives;
    }
    
    @Override
    public double getSpecificity(int TruePositives, int FalseNegatives, int FalsePositives, int TrueNegatives) {
        //How effectively a classifier identifies negative labels
        return TrueNegatives / FalsePositives + TrueNegatives;
    }
    
    @Override
    public double getAUC(int TruePositives, int FalseNegatives, int FalsePositives, int TrueNegatives) {
        //Classifier's ability to avoid false classification
        return 0.5*(getSpecificity(TruePositives, FalseNegatives, FalsePositives, TrueNegatives) + getRecall(TruePositives, FalseNegatives, FalsePositives, TrueNegatives));
    }

    @Override
    public double getfScore(int TruePositives, int FalseNegatives, int FalsePositives, int TrueNegatives) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
}
