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
 * Creates two files, one for each class and stores each instance per line,
 * in its respective file. MALLET assumes the data is in the following format :
 * [<instance name>] [<label>] [<sequence>]. In this case, the first token of each
 * line (whitespace delimited, with optional comma) becomes the instance name, the second
 * token becomes the label, and all additional text on the line is interpreted as a
 * sequence of word tokens. Note that the data in this case will be a vector of
 * feature/value pairs, such that a feature consists of a distinct word type and
 * the value is the number of times that word occurs in the text.
 */
public class ManyInstancesPerFileConverter {
    
    public ManyInstancesPerFileConverter(String [] args) throws FileNotFoundException {
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
       
       String filename1 = "/home/nikos/NetBeansProjects/NucleosomePatternClassifier/Datasets/NBS/NBS_Instances";
       PrintWriter out1 = new PrintWriter(filename1);
       String filename2 = "/home/nikos/NetBeansProjects/NucleosomePatternClassifier/Datasets/NFR/NFR_Instances";
       PrintWriter out2 = new PrintWriter(filename2);
       
       int counter = 0;
       for(SequenceInstance NFRelem : NFRSeqs) {
           counter++;
           // We skip here the lines indicating the positions
           if(counter % 2 == 0) {
               
                /*System.out.println(counter + ":" + NFRelem);
                String filename1 = "/home/nikos/NetBeansProjects/NucleosomePatternClassifier/Datasets/NFR/NFR_" + counter;
                try(PrintWriter out = new PrintWriter(filename1)) {
                    out.println(NFRelem);
                }*/
                
                out1.println(counter + " " + "NFR" + " " + NFRelem);
           }
       }
       counter = 0;
       for(SequenceInstance NBSelem : NBSSeqs) {
           counter++;
           // We skip here the lines indicating the positions
           if(counter % 2 == 0) {
                /*System.out.println(counter + ":" + NBSelem);
                String filename2 = "/home/nikos/NetBeansProjects/NucleosomePatternClassifier/Datasets/NBS/NBS_" + counter;
                try(PrintWriter out = new PrintWriter(filename2)) {
                    out.println(NBSelem);
                }*/
                 
                out2.println(counter + " " + "NBS" + " " + NBSelem);
                
           }
           
       }
    }
}
