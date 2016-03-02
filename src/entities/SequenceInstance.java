/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entities;

/**
 *
 * @author nikos
 */
public class SequenceInstance implements GenomicSequence {
    
    protected String SeqInst;
    
    public SequenceInstance(String sSeq) {
        SeqInst = sSeq;
    }
    
    @Override
    public String getSymbolSequence() {
        return SeqInst;
    }
    
    @Override
    public String toString() {
        return SeqInst;
    }
    
    public int getLength(String sSeq) {
        return sSeq.length();
    }
    
    public int getCharAt(int i, String sSeq) {
        return sSeq.charAt(i);
    }
}
