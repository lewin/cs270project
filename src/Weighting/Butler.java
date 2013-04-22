/**
 * A weight function. Loyal and mysterious.
 */

package Weighting;
import Items.Slot;
import Items.Tutor;

public class Butler implements Weighting {
    
    public double weight(Tutor t, Slot s) {
        double retval = 0.00;
        
        // TODO outdated data format
/*
        // does the tutor want to be in this slot? 
        retval += t.slots[s.sid] * 10;
        // does the tutor's classes match well with this slot?
        for (int i = 0; i < t.courses.length; ++i) {
            retval += t.courses[i] * s.courses[i] * 1;
        }
        // does this new match support the adjacency preference?
        int adj_pref = 0;
        switch (t.adjacentPref) {
            case 0: // disprefer
                adj_pref = -1;
                break;
            case 1: // ambivalent
                adj_pref = 0;
                break;
            case 2: // prefer
                adj_pref = 1;
                break;
        }
        for (Slot adjslot : s.adjacentSlots) {
            if (adjslot.tutor == t) {
                retval += adj_pref * 2;
            }
        }
*/
        return retval;
    }

}
