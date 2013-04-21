/**
 * Stores data about the preferences of an individual tutor.
 */
public class Tutor {
    // identifying information
    public int tid;
    public String name;

    // preference information
    public int[] timeSlots;
    public int[] officePrefs;
    public int[] courses;
    public int adjacentPref;

    // the slot currently matched to this tutor
    public Slot slot = null;

    @Override
    public String toString() {
        return tid + "@" + name;
    }
}
