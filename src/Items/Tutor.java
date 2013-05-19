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
    // does this tutor need two slots?
    public int numAssignments;
    public boolean officer;

    // the slot currently matched to this tutor
    public ArrayList <Slot> slots;
    
    public void assign (Slot s) {
        slots.add (s);
    }
    
    @Override
    public String toString() {
        return tid + "@" + name;
    }
}
