/*
 * An initial, poorly named weight function.
 */


public class Waiter implements Weighting {
    
    /*
     * The actual weight function.
     */
    public double weight(Tutor t, Slot s) {
        double retval = 0.00;
        // does the tutor want to be in this slot? 
        retval += t.slots[s.sid] * 10;
        // does the tutor's classes match well with this slot?
        for (int i = 0; i < t.courses.length; ++i) {
            retval += t.courses[i] * s.courses[i] * 1;
        }
        return retval;
    }

}
