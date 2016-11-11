/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package representation;

import be.ac.ulg.montefiore.run.jahmm.Hmm;
import be.ac.ulg.montefiore.run.jahmm.ObservationDiscrete;
import entities.HMMFeatureVector;
import entities.HMMSequence.Packet;
import java.util.List;
import entities.HMMSequence;


/**
 *
 * @author nikos
 */
public class NormalizedHmmHandler extends HmmHandler implements GenomicSequenceRepresentationHandler<List<ObservationDiscrete<HMMSequence.Packet>>>
{
    
    public NormalizedHmmHandler() {
        super();
    }

//    private Hmm initializeHMM() {
//        
//        Hmm<ObservationDiscrete<HMMSequence.Packet>> hmm =
//                new Hmm<>(2, 
//                new OpdfDiscreteFactory<>(HMMSequence.Packet.class));
//        
//        hmm.setPi(0, 0.5);
//        hmm.setPi(1, 0.5);
//        
//        hmm.setOpdf(0, new OpdfDiscrete<>(HMMSequence.Packet.class,
//                    new double[]{0.2,0.2,0.2,0.2}));
//        hmm.setOpdf(1, new OpdfDiscrete<>(HMMSequence.Packet.class,
//                    new double[]{0.2,0.2,0.2,0.2}));
//        
//        hmm.setAij(0, 0, 0.25);
//        hmm.setAij(0, 1, 0.25);
//        hmm.setAij(1, 0, 0.25);
//        hmm.setAij(1, 1, 0.25);
//        
//        return hmm;
//    }

    @Override
    public HMMFeatureVector getFeatureVector(List<ObservationDiscrete<Packet>> representation, String label) {
        HMMFeatureVector v = new HMMFeatureVector();
        double dMaxProb = -1.0;
        //String label = null;
        int count = 0;

        for(String className : classModel.keySet()) {
            Hmm tempModel = classModel.get(className);

            double dProb = tempModel.lnProbability(representation);

            if(dProb > dMaxProb) {
                dMaxProb = dProb;
            }
            // Here we normalize the probability by dividing it by the respective
            // string size. This is due to the fact that the probability decreases
            // as the string gets longer and longer.
            v.setProbArrayAtIndex(dProb/representation.size(), count);
            count++;
        }
        v.setLabel(label);

        return v;    
    }
    
}
