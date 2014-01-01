/**
 * Stores data about the preferences of an individual tutor.
 */

package Items;

import java.util.ArrayList;

public class Tutor {
    public Tutor () {
        slots = new ArrayList <Slot> ();
    }
    
    // identifying information
    public int tid;
    public String name;

    // preference information
    public int[] timeSlots;
    public int[] officePrefs;
    public int[] courses;
    public int adjacentPref;
    public int numAssignments;

    // the slot currently matched to this tutor
    public ArrayList <Slot> slots;
    
    public boolean conflict (Slot s) {
        for (Slot r : slots)
            if (s.simultaneous(r))
                return true;
        return false;
    }
    
    public void assign (Slot s) {
        slots.add (s);
    }
    
    public boolean unassign (Slot s) {
        return slots.remove(s);
    }
    
    @Override
    public String toString() {
        return tid + "@" + name;
    }
}
