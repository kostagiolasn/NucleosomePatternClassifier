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
public class BaselineBOW_SequenceAnalyst implements GenomicSequenceAnalyst<List<BaselineBagOfWords>> {

    @Override
    public List<List<BaselineBagOfWords>> represent(List<SequenceInstance> Seqs) {
        List<List<BaselineBagOfWords>> Res = new ArrayList<>();
        
        for(SequenceInstance instance : Seqs) {
            BaselineBagOfWords tempBow = new BaselineBagOfWords(instance.getSymbolSequence());
            
            List<BaselineBagOfWords> bowList = new ArrayList<>();
            
            bowList.add(tempBow);
            Res.add(bowList);
        }
        
        return Res;
    }

    
}
