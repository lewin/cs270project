/**
 * A weight function. Loyal and mysterious. Incorporates time and course preference.
 */

package Weighting;

import Items.Slot;
import Items.Tutor;

public class Butler implements Weighting {

  public double weight(Tutor t, Slot s) {
    double retval = 0.00;

    // add the time preference
    retval += timePrefToW[t.timeSlots[s.sid]];

    // add the office preference
    retval += t.officePrefs[s.sid];

    for (Slot r : t.slots) {
      if (r.day.equals(s.day)) {
        if (s.adjacent(r)) {
          if (t.adjacentPref == 1)
            retval += 5;
          else if (t.adjacentPref == 0)
            retval -= 2;
          else if (t.adjacentPref == -1)
            retval -= 3;
        } else {
          retval -= 1000;
        }
      } else {
        if (t.adjacentPref == -1)
          retval += 3;
        else if (t.adjacentPref == 1)
          retval -= 2;
      }
    }


    // add the course preference
    // for (int i = 0; i < t.courses.length; ++i) {
    // retval += (t.courses[i] + 1) * s.courses[i] * courseWeight;
    // }
    return retval;
  }

  // 0: unavailable, 1: ambivalent, 2: prefer
  private static double[] timePrefToW = {-100000, 6, 0.01};

  // how important course matching is for this weighting
  private static double courseWeight = 1.0;
}
