/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package nucleosomepatternclassifier;

import java.io.FileNotFoundException;
import java.io.IOException;
import run.RunHandler;

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
        
        Run as : -NFR <path_to_NFR_fasta_file> -NBS <path_to_NBS_file> -folds <number_of_folds> -rep <representation_type(see below)> -clf <classifier(see below)>
        >>> representation types
        <BOW> : Bag of Words
        <NGG> : N-gram graphs
        <HMM> : Hidden Markov Models
        
        >> classifier types
        <NB> : Naive Bayes
        <SVM> : Support Vector Machine
        <DT> : Decision Tree
        <KNN> : K-nearest neighbors
        */
        
        RunHandler run = new RunHandler();
        run.run(args[1], args[3], args[5], args[7], args[9]);
        
    }
    
}
