/**
 * Stores data about the preferences of an individual office hour slot.
 */
public class Slot {
    public int sid;
    public String name; // probably "OFFICE=START-END" or something
    public int[] courses;
    public int[] adjacentSlotIDs;
    public Slot[] adjacentSlots;
    // the tutor currently matched to this slot 
    public Tutor tutor = null; 

    @Override
    public String toString() {
        return sid + "@" + name;
    }
}
