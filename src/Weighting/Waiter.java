package Weighting;
import Items.Slot;
import Items.Tutor;

/**
 * A weight function. A newbie still learning the ropes.
 *
 * Weights only on time availability. Very simple.
 */


public class Waiter implements Weighting {

    public double weight(Tutor t, Slot s) {
        double retval = 0.00;
        retval += prefToW[t.timeSlots[s.sid]];
        return retval;
    }

    // 0: unavailable, 1: ambivalent, 2: prefer
    private static double[] prefToW = {1, 10, 20};

}
