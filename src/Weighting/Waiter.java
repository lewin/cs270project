/**
 * A weight function. A newbie still learning the ropes.
 * 
 * Weights only on time availability. Very simple.
 */

package Weighting;

import Items.Slot;
import Items.Tutor;

public class Waiter implements Weighting {

  public double weight(Tutor t, Slot s) {
    double retval = 0.00;
    retval += prefToW[t.timeSlots[s.sid]];
    return retval;
  }

  // 0: unavailable, 1: ambivalent, 2: prefer
  private static double[] prefToW = {1, 10, 20};
  // it seems that setting the unavailable preference <=0 makes bad solutions?

}
