/**
 * A weight function. Keeps the grass trimmed and the hedges tidy. Implements the weight function
 * described on the HKN website except with higher weight given to the adjacency preference.
 */

package Weighting;

import Items.Slot;
import Items.Tutor;

public class Gardener implements Weighting {

  public double weight(Tutor t, Slot s) {
    double retval = 0.00;

    // Adjacencies: +10 if adjacent or no preference
    if (t.adjacentPref == 0) {
      retval += 5;
    } else if (t.adjacentPref == 1) {
      for (Slot adjslot : s.adjacentSlots) {
        if (adjslot.tutors.contains(t)) {
          retval += 10;
        }
      }
    }

    // Time slots: +1 if not preferred, +10 if ambivalent, +20 if preferred
    retval += timePrefToW[t.timeSlots[s.sid]];

    // // Office preferences: +[1, 5] depending on office preference
    // retval += (t.officePrefs[s.sid] + 3);

    return retval;
  }

  // 0: unavailable, 1: ambivalent, 2: prefer
  private static double[] timePrefToW = {-1000, 20, 40};

}
