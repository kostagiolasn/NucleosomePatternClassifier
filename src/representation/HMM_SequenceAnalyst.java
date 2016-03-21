/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package representation;

import be.ac.ulg.montefiore.run.jahmm.ObservationDiscrete;
import entities.GenomicSequence;
import entities.HMMSequence.Packet;
import entities.SequenceInstance;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class HMM_SequenceAnalyst implements GenomicSequenceAnalyst<List<ObservationDiscrete<Packet>>> {

    @Override
    public List<List<ObservationDiscrete<Packet>>> represent(List<SequenceInstance> Seqs) {
        
        List<List<ObservationDiscrete<Packet>>> Res = new ArrayList<>();
        
        Map<Character, ObservationDiscrete<Packet>> nucleotideMap = new HashMap<>();
        nucleotideMap.put('A', Packet.A.observation());
        nucleotideMap.put('T', Packet.T.observation());
        nucleotideMap.put('C', Packet.C.observation());
        nucleotideMap.put('G', Packet.G.observation());
        
        for (GenomicSequence Seq : Seqs) {
            List<ObservationDiscrete<Packet>> temp = new ArrayList<>();
            
            for(Character c : Seq.getSymbolSequence().toCharArray()) {
                temp.add(nucleotideMap.get(c));
            }
            System.out.println(temp);
            
            Res.add(temp);
        }
        
        return Res;

    }
}