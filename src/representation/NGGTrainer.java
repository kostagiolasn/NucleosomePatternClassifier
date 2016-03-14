/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package representation;

import cc.mallet.types.Instance;
import cc.mallet.types.InstanceList;
import gr.demokritos.iit.jinsect.documentModel.comparators.NGramCachedGraphComparator;
import gr.demokritos.iit.jinsect.documentModel.representations.DocumentNGramGraph;
import gr.demokritos.iit.jinsect.structs.GraphSimilarity;

/**
 *
 * @author nikos
 */
public class NGGTrainer {
    public NGGTrainer(InstanceList training_instancesA, InstanceList training_instancesB,
            DocumentNGramGraph NFR_ngGraph, DocumentNGramGraph NBS_ngGraph) {
        
        
        int count = 2;
        
        for(Instance i : training_instancesA) {
            DocumentNGramGraph tempngGraph = new DocumentNGramGraph();
            
            tempngGraph.setDataString(i.toString());

            double l_factor = 1.0 - (count-1)/count;
            NFR_ngGraph.merge(tempngGraph, l_factor);
            count++;
        }
                
        count = 2;
        
        for(Instance i : training_instancesB) {
            DocumentNGramGraph tempngGraph = new DocumentNGramGraph();
            
            tempngGraph.setDataString(i.toString());

            double l_factor = 1.0 - (count-1)/count;
            NBS_ngGraph.merge(tempngGraph, l_factor);
            count++;
        }
        
    }
    
    public void NGGClassify( DocumentNGramGraph ClassA_ngGraph, DocumentNGramGraph ClassB_ngGraph, InstanceList toBeClassified,
            int ClassifiedAsA, int ClassifiedAsB) {
        
        //Create a comparator object
        NGramCachedGraphComparator ngc = new NGramCachedGraphComparator();
        
        ClassifiedAsA = 0;
        ClassifiedAsB = 0;
        
        for(Instance i : toBeClassified) {
            DocumentNGramGraph tempngGraph = new DocumentNGramGraph();
            
            tempngGraph.setDataString(i.toString());
            
            GraphSimilarity NFR_similarity = ngc.getSimilarityBetween(ClassA_ngGraph, tempngGraph);
            GraphSimilarity NBS_similarity = ngc.getSimilarityBetween(ClassB_ngGraph, tempngGraph);
            
            if(NFR_similarity.ContainmentSimilarity > NBS_similarity.ContainmentSimilarity) {
                ClassifiedAsA++;
            }
            else {
                ClassifiedAsB++;
            }
        }
    }

}
