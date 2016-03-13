/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package io;

import entities.SequenceInstance;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.ArrayList;

/**
 *
 * @author nikos
 * Creates one file per instance. MALLET will use the directory names as labels
 * and the filenames as instance names.
 */
public class OneInstancePerFileConverter {
    
    public OneInstancePerFileConverter(String [] args) throws FileNotFoundException {
         //Read training file
       GenomicSequenceFileReader reader = new FAFileReader();
       //We create two sequence ArrayLists where NFR stands for Nucleosome Free Region
       //and BS stands for Binding Site.
       ArrayList<SequenceInstance> NFRSeqs;     
       ArrayList<SequenceInstance> NBSSeqs;
       //If no input file has been given
       if(args.length < 2) {
           // Use default
           NFRSeqs = reader.getSequencesFromFile("/home/nikos/NetBeansProjects/NucleosomePatternClassifier/Datasets/1099_consistent_NFR.fa");
           NBSSeqs = reader.getSequencesFromFile("/home/nikos/NetBeansProjects/NucleosomePatternClassifier/Datasets/3061_consistent_nucleosomes.fa");
       }
       else {
           NFRSeqs = reader.getSequencesFromFile(args[0]);
           NBSSeqs = reader.getSequencesFromFile(args[1]);
       }
       
       int counter = 0;
       for(SequenceInstance NFRelem : NFRSeqs) {
           counter++;
           // We skip here the lines indicating the positions
            if(counter % 2 == 0) {
                System.out.println(counter + ":" + NFRelem);
                String filename2 = "/home/nikos/NetBeansProjects/NucleosomePatternClassifier/Datasets/NFR/NFR_" + counter;
                try(PrintWriter out = new PrintWriter(filename2)) {
                    out.println(NFRelem);
                }
           }
       }
       counter = 0;
       for(SequenceInstance NBSelem : NBSSeqs) {
           counter++;
           // We skip here the lines indicating the positions
            if(counter % 2 == 0) {
                System.out.println(counter + ":" + NBSelem);
                String filename2 = "/home/nikos/NetBeansProjects/NucleosomePatternClassifier/Datasets/NBS/NBS_" + counter;
                try(PrintWriter out = new PrintWriter(filename2)) {
                    out.println(NBSelem);
                }
           }
                                
        }
           
    }
}

