/**
 * Data.java contains the classes for the Java object representation for
 * our data elements.
 * Includes classes for:
 *   * Tutors (and their preferences)
 *   * Office hour slots (and their preferences)
 * Populate these classes using the Gson library
 */

import java.util.Arrays;

/**
 * Class directly populated by the JSON reader.
 * Each index corresponds exactly to the index in any of the courses[].
 */
public class Data {

    /**
     * Array of all course names.
     * Each index corresponds exactly to the index in any of the courses[].
     */
    public String[] courseNames;
    /**
     * Array of all tutors.
     */
    public Tutor[] tutors;
    
    /**
     * Array of all slots.
     * Each index corresponds exactly to the slot's sid.
     */
    public Slot[] slots;

    /**
     * Initialize some data fields not done by the JSON parser.
     */
    public void init() {
        // initialize the adjacenct slot references
        for (Slot s : slots) {
            s.adjacentSlots = new Slot[s.adjacentSlotIDs.length];
            for (int i = 0; i < s.adjacentSlots.length; ++i) {
                s.adjacentSlots[i] = slots[s.adjacentSlotIDs[i]];
            }
        }
    }

    /**
     * Returns a string of all assignments in the set of slots.
     * Output is formatted in rows of "[SLOT_ID] [TUTOR_ID]"
     * if no slot is assigned to the tutor, [TUTOR_ID] is -1.
     */
    public String assignments() {
        String ret = "";
        for (Slot s : slots) {
            ret += String.valueOf(s.sid) + " ";
            if (s.tutor != null) {
                ret += String.valueOf(s.tutor.tid) + "\n";
            } else {
                ret += "-1\n";
            }
        }
        return ret.trim();
    }

    @Override
    public String toString() {
        return "Data {" + 
               "courseNames=" + Arrays.toString(courseNames) + 
               ", tutors=" + Arrays.toString(tutors) + 
               ", slots=" + Arrays.toString(slots) + "}";
    }

}

