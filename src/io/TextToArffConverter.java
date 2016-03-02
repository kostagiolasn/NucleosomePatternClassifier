/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package io;

import weka.core.*;
import weka.core.converters.*;

import java.io.*;

/**
 *
 * @author nikos
 */
public class TextToArffConverter {
/**
 * Example class that converts HTML files stored in a directory structure into 
 * and ARFF file using the TextDirectoryLoader converter.

  /**
   * Expects the first parameter to point to the directory with the text files.
   * In that directory, each sub-directory represents a class and the text
   * files in these sub-directories will be labeled as such.
   *
     * @param Directory
   * @param args        the commandline arguments
     * @throws java.io.IOException
   * @throws Exception  if something goes wrong
   * */
    public TextToArffConverter(String Directory) throws IOException {
        // convert the directory into a dataset
        TextDirectoryLoader loader = new TextDirectoryLoader();
        loader.setDirectory(new File(Directory));
        Instances dataRaw = loader.getDataSet();
        //System.out.println("\n\nImported data:\n\n" + dataRaw);
        PrintWriter writer = new PrintWriter("/home/nikos/NetBeansProjects/NucleosomePatternClassifier/ARFF/Data.txt");
        writer.println(dataRaw);
    }
}
