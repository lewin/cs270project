/**
 * Stores data about the preferences of an individual office hour slot.
 */

package Items;

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
    
    public boolean adjacent (Slot s) {
        for (int i = 0; i < adjacentSlots.length; i++)
            if (adjacentSlots [i].equals(s))
                return true;
        return false;
    }
    
    @Override
    public boolean equals(Object other) {
        if (!(other instanceof Slot)) return false;
        return sid == ((Slot)other).sid 
            && name.equals (((Slot)other).name) 
            && day.equals (((Slot)other).name) 
            && hour == ((Slot)other).hour 
            && office.equals (((Slot)other).office);
    }

    @Override
    public String toString() {
        return sid + "@" + name;
    }
}
