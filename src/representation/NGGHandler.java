/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package representation;

import entities.NGGFeatureVector;
import gr.demokritos.iit.jinsect.documentModel.comparators.NGramCachedGraphComparator;
import gr.demokritos.iit.jinsect.documentModel.representations.DocumentNGramGraph;
import gr.demokritos.iit.jinsect.structs.GraphSimilarity;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *
 * @author nikos
 */
public class NGGHandler implements GenomicSequenceRepresentationHandler<List<DocumentNGramGraph>>{
    
    private final Map<String, DocumentNGramGraph> classModel;
    
    public NGGHandler() {
        this.classModel = new HashMap<>();
    }

    @Override
    public void train(List<List<DocumentNGramGraph>> representation, String label) {
        DocumentNGramGraph tempGraph = new DocumentNGramGraph();
        
        int iCount = 0;
        
        for(int i = 0; i < representation.size()*0.9; i++) {
            for(int j = 0; j < representation.get(i).size(); j++) {
                tempGraph.mergeGraph(representation.get(i).get(j), 1/(iCount + 1));
                iCount++;
            }
        }
        
        classModel.put(label, tempGraph);
    }

    @Override
    public Object getClassModel() {
        return classModel;
    }

    @Override
    public Object getFeatureVector(List<DocumentNGramGraph> representation, String label) {
        
        NGGFeatureVector v = new NGGFeatureVector();
        
        NGramCachedGraphComparator comparator = new NGramCachedGraphComparator();
        GraphSimilarity similarity;
        int count = 0;
        
        for(String className : classModel.keySet()) {
            //System.out.println(className);
            DocumentNGramGraph curClassModel = classModel.get(className);
            
            similarity = comparator.getSimilarityBetween(curClassModel, representation.get(0));
            v.setContainmentSimilarityArrayAtIndex(similarity.ContainmentSimilarity, count);
            v.setSizeSimilarityArrayAtIndex(similarity.SizeSimilarity, count);
            v.setValueSimilarityArrayAtIndex(similarity.ValueSimilarity, count);
            double NVS = ((v.getSizeSimilarityArrayAtIndex(count) > 10.0e-8) ? (v.getValueSimilarityArrayAtIndex(count) / v.getSizeSimilarityArrayAtIndex(count)) : 0.0);
            v.setNVSArrayAtIndex(NVS, count);
            count++;
        }
        v.setLabel(label);
        
        return v;
    
    }
    
}
