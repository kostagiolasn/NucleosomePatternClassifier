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


/**
 *
 * @author nikos
 */
public class HmmHandler implements GenomicSequenceRepresentationHandler<List<ObservationDiscrete<HMMSequence.Packet>>>{
    
    private final Map<String, Hmm> classModel;

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
                new Hmm<ObservationDiscrete<HMMSequence.Packet>>(2, 
                new OpdfDiscreteFactory<HMMSequence.Packet>(HMMSequence.Packet.class));
        
        hmm.setPi(0, 0.5);
        hmm.setPi(1, 0.5);
        
        hmm.setOpdf(0, new OpdfDiscrete<HMMSequence.Packet>(HMMSequence.Packet.class,
                    new double[]{0.2,0.2,0.2,0.2}));
        hmm.setOpdf(1, new OpdfDiscrete<HMMSequence.Packet>(HMMSequence.Packet.class,
                    new double[]{0.2,0.2,0.2,0.2}));
        
        hmm.setAij(0, 0, 0.25);
        hmm.setAij(0, 1, 0.25);
        hmm.setAij(1, 0, 0.25);
        hmm.setAij(1, 1, 0.25);
        
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
            v.setProbArrayAtIndex(dMaxProb, count);
            count++;
        }
        v.setLabel(label);

        return v;    
    }
    
}
