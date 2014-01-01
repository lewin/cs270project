/**
 * An evaluator. 
 */

package Weighting;

import java.util.ArrayList;

import Items.Data;
import Items.Slot;
import Items.Tutor;

public class Evaluator {
    public static final double PENALTY = 100000;
    public static final double ADJACENT_PREF = 50;
    public static final double ADJACENT_DISPREF = 50;

    public static double[] evaluate(Data assign, Weighting w) {
        double score = 0;
        double [] scores = new double [assign.tutors.length];
        int idx = 0;
        double min = Double.MAX_VALUE, max = 0;
        outer : for (Tutor t : assign.tutors) {
            if (t.numAssignments != t.slots.size()) {
                // tutor should have been assigned a slot
                // or tutor was assigned an extra slot
                score -= PENALTY;
                continue;
            }

            for (int i = 0; i < t.slots.size(); i++)
                for (int j = i + 1; j < t.slots.size(); j++)
                    if (t.slots.get(i).simultaneous(t.slots.get(j))) {
                        score -= PENALTY;
                        continue outer;
                    }

            double d = 0;
            ArrayList <Slot> temp = new ArrayList <Slot> (t.slots);
            t.slots.clear();
            // simulate re-adding the slots in one by one. 
            // order shouldn't matter when adding slots in 
            for (Slot s : temp) {
                d += w.weight (t, s);
                if (t.timeSlots[s.sid] == 0) {
                    score -= PENALTY;
//                    System.out.println ("A");
                    continue outer; 
                }
                t.assign(s);
            }
            d /= (double)t.numAssignments;
            scores [idx++] = d;
            min = Math.min (min, d);
            max = Math.max (max, d);
//            System.out.println (t.name + " " + d);
            score += d;
        }
        double ave = score / (double)scores.length;
        double std = 0;
        for (int i = 0; i < scores.length; i++)
            std += (scores[i] - ave) * (scores[i] - ave);
//        System.out.println (Math.sqrt(std) + " " + min + " " + max);
        return new double [] {std, score};
    }
}
