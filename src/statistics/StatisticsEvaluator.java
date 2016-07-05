/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package statistics;

import weka.core.Instances;

/**
 *
 * @author nikos
 */
public interface StatisticsEvaluator {
    
    public double [][] getConfusionMatrix(Instances Training_Instances, Instances Testing_Instances);
    
    public double getAccuracy(double[][] ConfMatrix);
    
    public double getPrecision(double[][] ConfMatrix);
    
    public double getRecall(double[][] ConfMatrix);
    
    public double getSpecificity(double[][] ConfMatrix);
    
    public double getAUC(double[][] ConfMatrix);
    
    public double getfScore(double[][] ConfMatrix);
}
