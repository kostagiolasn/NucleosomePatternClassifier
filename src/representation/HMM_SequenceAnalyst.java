/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package representation;

import cc.mallet.fst.HMM;
import cc.mallet.fst.HMMTrainerByLikelihood;
import cc.mallet.fst.PerClassAccuracyEvaluator;
import cc.mallet.fst.TransducerEvaluator;
import cc.mallet.pipe.Pipe;
import cc.mallet.pipe.SerialPipes;
import cc.mallet.pipe.SimpleTaggerSentence2TokenSequence;
import cc.mallet.pipe.TokenSequence2FeatureSequence;
import cc.mallet.pipe.iterator.LineGroupIterator;
import cc.mallet.types.InstanceList;
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.regex.Pattern;
import java.util.zip.GZIPInputStream;



/**
 *
 * @author nikos
 */
public class HMM_Trainer {

    public HMM_Trainer(InstanceList trainingInstances, InstanceList testingInstances) throws IOException {
        
        HMM hmm = new HMM(trainingInstances.getDataAlphabet(),
                        trainingInstances.getTargetAlphabet());
        
        hmm.addStatesForLabelsConnectedAsIn(trainingInstances);
        //hmm.addStatesForBiLabelsConnectedAsIn(trainingInstances);
        
        HMMTrainerByLikelihood trainer =
                new HMMTrainerByLikelihood(hmm);
        TransducerEvaluator trainingEvaluator = 
                new PerClassAccuracyEvaluator(trainingInstances, "training");
        TransducerEvaluator testingEvaluator = 
                new PerClassAccuracyEvaluator(testingInstances, "testing");
        
        trainer.train(trainingInstances, 10);
        trainingEvaluator.evaluate(trainer);
        testingEvaluator.evaluate(trainer);
    }
      
}
