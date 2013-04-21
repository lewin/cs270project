package Items;

/**
 * Stores data about the preferences of an individual office hour slot.
 */
public class Slot {
    // identifying information
    public int sid;
    public String name;
    public String day;
    public int hour;
    public String office;

    // preference information
    public int[] courses;
    public int[] adjacentSlotIDs;
    public Slot[] adjacentSlots;
    public Slot[] simultaneousSlots;

    // the tutor currently matched to this slot 
    public Tutor tutor = null;
    // if there is another tutor for this slot
    public Tutor tutor2 = null;

    public boolean simultaneous(Slot s) {
        return day.equals(s.day) && (hour == s.hour);
    }

    @Override
    public String toString() {
        return sid + "@" + name;
    }
}
