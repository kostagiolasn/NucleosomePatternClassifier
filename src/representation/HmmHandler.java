/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package representation;

import be.ac.ulg.montefiore.run.jahmm.Hmm;
import be.ac.ulg.montefiore.run.jahmm.ObservationDiscrete;
import be.ac.ulg.montefiore.run.jahmm.OpdfDiscrete;
import be.ac.ulg.montefiore.run.jahmm.OpdfDiscreteFactory;
import be.ac.ulg.montefiore.run.jahmm.learn.BaumWelchLearner;
import entities.HMMFeatureVector;
import entities.HMMSequence.Packet;
import java.util.List;
import entities.HMMSequence;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;


/**
 *
 * @author nikos
 */
public class HmmHandler implements GenomicSequenceRepresentationHandler<List<ObservationDiscrete<HMMSequence.Packet>>>{
    
    protected final Map<String, Hmm> classModel;

    public HmmHandler() {
        this.classModel = new HashMap<>();
    }

    @Override
    public void train(List<List<ObservationDiscrete<Packet>>> representation, String label) {
        
        Hmm hmmTemp = initializeHMM();
        
        // Train the model based on the observations
        BaumWelchLearner bwl = new BaumWelchLearner();
        
        hmmTemp = bwl.learn(hmmTemp, representation);
        
        classModel.put(label, hmmTemp);
    }

    @Override
    public Map<String, Hmm> getClassModel() {
        return classModel;
    }

    private Hmm initializeHMM() {
        
        Hmm<ObservationDiscrete<HMMSequence.Packet>> hmm =
                new Hmm<>(2, 
                new OpdfDiscreteFactory<>(HMMSequence.Packet.class));
        
        // Random initialization (with fixed seed to allow for reproducibility)
        Random r = new Random(1L);
        double dRnd = r.nextDouble(); 
        hmm.setPi(0, dRnd);
        hmm.setPi(1, 1.0 - dRnd);
//        hmm.setPi(0, 0.5);
//        hmm.setPi(1, 0.5);
        
        // Non-equal probs
        hmm.setOpdf(0, new OpdfDiscrete<>(HMMSequence.Packet.class,
                    new double[]{0.1,0.2,0.3,0.4}));
        hmm.setOpdf(1, new OpdfDiscrete<>(HMMSequence.Packet.class,
                    new double[]{0.4,0.3,0.2,0.1}));
        
        dRnd = r.nextDouble();
        hmm.setAij(0, 0, dRnd);
        hmm.setAij(0, 1, 1.0 - dRnd);
        dRnd = r.nextDouble();
        hmm.setAij(1, 0, dRnd);
        hmm.setAij(1, 1, 1.0 - dRnd);
        
        return hmm;
    }

    @Override
    public HMMFeatureVector getFeatureVector(List<ObservationDiscrete<Packet>> representation, String label) {
        HMMFeatureVector v = new HMMFeatureVector();
        double dMaxProb = -1.0;
        //String label = null;
        int count = 0;

        for(String className : classModel.keySet()) {
            Hmm tempModel = classModel.get(className);

            double dProb = tempModel.probability(representation);

            if(dProb > dMaxProb) {
                dMaxProb = dProb;
                //label = className;
                //v.setLabel(label);
            }
            v.setProbArrayAtIndex(dProb, count);
            count++;
        }
        v.setLabel(label);

        return v;    
    }
    
}
