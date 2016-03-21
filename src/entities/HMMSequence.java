/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entities;

import be.ac.ulg.montefiore.run.jahmm.ObservationDiscrete;

/**
 *
 * @author nikos
 */
public class HMMSequence implements GenomicSequence {
    
    private String hmmSequence;
    
    public static enum Packet {
        A,T,C,G;
        
        public ObservationDiscrete<Packet> observation() {
            return new ObservationDiscrete<Packet>(this);
        }
    }
    @Override
    public String getSymbolSequence() {
        return hmmSequence;
    }
    
}
