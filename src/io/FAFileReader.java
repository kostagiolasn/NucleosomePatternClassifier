/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package io;

import io.GenomicSequenceFileReader;
import entities.GenomicSequence;
import entities.SequenceInstance;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author nikos
 */
public class FAFileReader implements GenomicSequenceFileReader{
    
    @Override
    public ArrayList<SequenceInstance> getSequencesFromFile(String fileName) {
        
        String str = new String();
        
        ArrayList<SequenceInstance> results = new ArrayList();
        
        try {
            //open and read all text files with sequence samples and create a BufferedReader
            BufferedReader reader = new BufferedReader(new FileReader(fileName));
            
            //for every sequence instance
            while((str = reader.readLine()) != null) {
                
                //create sequence instance sample
                SequenceInstance elem = new SequenceInstance(str);
                //add it to the results list
                results.add(elem);
            }
            
            reader.close();
        } catch (FileNotFoundException e) {
            Logger.getLogger(FAFileReader.class.getName()).log(Level.SEVERE, null, e);
        } catch (IOException e) {
            Logger.getLogger(FAFileReader.class.getName()).log(Level.SEVERE, null, e);
        }
        
        return results;
    }

}
