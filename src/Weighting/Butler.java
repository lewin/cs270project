/**
 * A weight function. Loyal and mysterious.
 * Incorporates time and course preference.
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
        retval += t.officePrefs[s.sid] > 0 ? 2 : 0;

        for (Slot r : t.slots) {
            if (r.day.equals(s.day)) {
                if (s.adjacent(r)) {
                    if (t.adjacentPref == 1)
                        retval += 1.5;
                } else {
                    retval -= 1000;
                }
            } else {
                if (t.adjacentPref == -1)
                    retval += 1.5;
            }
        }
        
        
        // add the course preference
//        for (int i = 0; i < t.courses.length; ++i) {
//            retval += (t.courses[i] + 1) * s.courses[i] * courseWeight;
//        }

        if (retval < 0) return retval;
        int un = 0;
        for (int i = 0; i < t.timeSlots.length; i++)
            if (t.timeSlots[i] == 0) un++;
        return retval / Math.exp(un / 500.);
    }

    // 0: unavailable, 1: ambivalent, 2: prefer
    private static double[] timePrefToW = { -10000, 0.01, 6 };

    // how important course matching is for this weighting
    private static double courseWeight = 1.0;
}
