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
public interface StatisticsEvaluator {
    public double getAccuracy(int TruePositives, int FalseNegatives, int FalsePositives, int TrueNegatives);
    
    public double getPrecision(int TruePositives, int FalseNegatives, int FalsePositives, int TrueNegatives);
    
    public double getRecall(int TruePositives, int FalseNegatives, int FalsePositives, int TrueNegatives);
    
    public double getSpecificity(int TruePositives, int FalseNegatives, int FalsePositives, int TrueNegatives);
    
    public double getAUC(int TruePositives, int FalseNegatives, int FalsePositives, int TrueNegatives);
    
    public double getfScore(int TruePositives, int FalseNegatives, int FalsePositives, int TrueNegatives);
}
