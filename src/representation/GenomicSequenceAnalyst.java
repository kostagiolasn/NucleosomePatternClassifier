/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package representation;

import entities.SequenceInstance;
import java.util.List;

/**
 *
 * @author nikos
 * @param <T>
 */
public interface GenomicSequenceAnalyst<T> {
    public List<T> represent(List<SequenceInstance> Seqs);
}
