/**
 * Stores data about the preferences of an individual office hour slot.
 */
public class Slot {
    public int sid;
    public String name; // probably "OFFICE=START-END" or something
    // ??? something like: 0=wrong office, 1=class in session, 2=other
    public int[] courses; // ??? [0,2] on preference

    @Override
    public String toString() {
        return sid + "@" + name;
    }
}
