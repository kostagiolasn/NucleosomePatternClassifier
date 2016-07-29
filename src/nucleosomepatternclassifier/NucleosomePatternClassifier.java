/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package nucleosomepatternclassifier;

import entities.BOWFeatureVector;
import entities.SequenceInstance;
import entities.WekaBOWFeatureVector;
import io.FAFileReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import static java.util.Collections.rotate;
import java.util.List;
import representation.BOWHandler;
import representation.BOW_SequenceAnalyst;
import representation.BagOfWords;
import run.RunHandler;
import statistics.BinaryStatisticsEvaluator;
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
        
        RunHandler run = new RunHandler();
        run.run(args[1], args[3], args[5], args[7], args[9]);
        
    }
    
}
