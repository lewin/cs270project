/**
 * An evaluator. 
 */

package Weighting;

import Items.Data;
import Items.Slot;
import Items.Tutor;

public class Evaluator {
    public static final double PENALTY = 10000;
    public static final double ADJACENT_PREF = 1.05;
    public static final double ADJACENT_DISPREF = 1.01;
    public static double evaluate (Data assign, Weighting w) {
        double score = 0;
        for (Tutor t : assign.tutors) {
            Slot s1 = t.slot, s2 = t.slot2;
            if (s1 == null || (t.officer && s2 == null)) {
                // tutor should have been assigned a slot
                score -= PENALTY; 
                continue;
            }
            
            // simplest scoring scheme
            if (s2 == null) {
                score += w.weight (t, s1);
                continue;
            }
            
            if (s1.simultaneous (s2)) {
                // tutor shouldn't have been assigned two concurrent slots
                score -= PENALTY;
                continue;
            }
            
            double d1 = w.weight (t, s1), d2 = w.weight (t, s2);
            if (t.adjacentPref == 0) // NO ADJACENT PREF
                score += d1 + d2;
            if (t.adjacentPref == 1) { // ADJACENT PREF
                if (s1.adjacent (s2)) {
                    score += (d1 + d2) * ADJACENT_PREF;
                } else {
                    score += d1 + d2;
                }
            }
            if (t.adjacentPref == 2) { // ADJACENT DIS_PREF
                if (s1.adjacent (s2)) {
                    score += d1 + d2;
                } else {
                    score += (d1 + d2) * ADJACENT_DISPREF;
                }
            }
        }
        return score;
    }
}
