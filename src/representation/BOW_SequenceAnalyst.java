/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package representation;

import entities.SequenceInstance;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author nikos
 */
public class BOW_SequenceAnalyst implements GenomicSequenceAnalyst<List<BagOfWords>> {

    @Override
    public List<List<BagOfWords>> represent(List<SequenceInstance> Seqs) {
        List<List<BagOfWords>> Res = new ArrayList<>();
        
        for(SequenceInstance instance : Seqs) {
            BagOfWords tempBow = new BagOfWords(instance.getSymbolSequence());
            
            List<BagOfWords> bowList = new ArrayList<>();
            
            bowList.add(tempBow);
            Res.add(bowList);
        }
        
        return Res;
    }

    
}
