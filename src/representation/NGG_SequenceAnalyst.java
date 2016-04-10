/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package representation;

import entities.SequenceInstance;
import gr.demokritos.iit.jinsect.documentModel.representations.DocumentNGramGraph;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author nikos
 */
public class NGG_SequenceAnalyst implements GenomicSequenceAnalyst<List<DocumentNGramGraph>> {

    @Override
    public List<List<DocumentNGramGraph>> represent(List<SequenceInstance> Seqs) {
        List<List<DocumentNGramGraph>> Res = new ArrayList<>();
        
        for(SequenceInstance instance : Seqs) {
            DocumentNGramGraph tempGraph = new DocumentNGramGraph();
            tempGraph.setDataString(instance.getSymbolSequence());
            List<DocumentNGramGraph> graphList = new ArrayList<>();
            
            graphList.add(tempGraph);
            Res.add(graphList);
            
        }
        return Res;
    }
    

}
