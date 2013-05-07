/**
 * Stores data about the preferences of an individual tutor.
 */

package Items;

public class Tutor {
    // identifying information
    public int tid;
    public String name;

    // preference information
    public int[] timeSlots;
    public int[] officePrefs;
    public int[] courses;
    public int adjacentPref; 
    // does this tutor need two slots?
    public boolean officer;

    // the slot currently matched to this tutor
    public Slot slot = null;
    // Second slot for this tutor.
    public Slot slot2 = null;

    @Override
    public String toString() {
        return tid + "@" + name;
    }
}
