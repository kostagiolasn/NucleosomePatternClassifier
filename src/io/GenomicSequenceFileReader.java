/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package io;

import entities.SequenceInstance;
import java.util.ArrayList;
import java.util.List;

/**
 * This interface describes all classes that can read and parse a genomic
 * sequence file.
 * @author nikos
 */
public interface GenomicSequenceFileReader {
    
    public ArrayList<SequenceInstance> getSequencesFromFile(String fileName);
    
}
