/**
 * Data.java contains the classes for the Java object representation for
 * our data elements.
 * Includes classes for:
 *   * Tutors (and their preferences)
 *   * Office hour slots (and their preferences)
 * Populate these classes using the Gson library
 */

package Items;

import java.util.ArrayList;
import java.util.Arrays;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;

/**
 * Class directly populated by the JSON reader. Each index corresponds exactly
 * to the index in any of the courses[].
 */
public class Data {

    /**
     * Array of all course names. Each index corresponds exactly to the index in
     * any of the courses[].
     */
    public String[] courseNames;
    /**
     * Array of all tutors.
     */
    public Tutor[] tutors;

    /**
     * Array of all slots. Each index corresponds exactly to the slot's sid.
     */
    public Slot[] slots;

    /**
     * Initialize some data fields not done by the JSON parser.
     */
    public void init() {
        // initialize the adjacent slot references
        for (Slot s : slots) {
            s.adjacentSlots = new Slot[s.adjacentSlotIDs.length];
            for (int i = 0; i < s.adjacentSlots.length; ++i) {
                for (Slot t : slots) {
                    if (t.sid == s.adjacentSlotIDs[i]) {
                        s.adjacentSlots[i] = t;
                        break;
                    }
                }
            }
        }
    }

    /**
     * Returns a string of all assignments in the set of slots. Output is
     * formatted in rows of "[SLOT_ID] [TUTOR_ID]" if no slot is assigned to the
     * tutor, [TUTOR_ID] is -1.
     */
    public String assignments() {
        String ret = "";
        for (Slot s : slots) {
            ret += String.valueOf(s.sid) + " ";
            ret += String.valueOf(s.tutors.get(0).tid);
            for (int i = 1; i < s.tutors.size(); i++)
                ret += " " + String.valueOf(s.tutors.get(i).tid);
            ret += "\n";
        }
        return ret.trim();
    }

    public String formattedAssignments() {
      JsonObject jason = new JsonObject();
      for (Slot s : slots) {
        JsonArray j = new JsonArray();
        for (Tutor t : s.tutors) {
          j.add(new JsonPrimitive(t.tid));
        }
        jason.add(s.name.substring(s.name.length() - 3), j);
      }
      
      return jason.toString();
    }
    
    public String readableFormattedAssignments() {
      String ret = "";
      for (Slot s : slots) {
        ret += s.details();
        for (int i = 0; i < s.tutors.size(); i++)
          ret += " " + String.valueOf(s.tutors.get(i).name);
        ret += "\n";
      }
      return ret.trim();
    }
    
    public void clearAssignments() {
        for (int i = 0; i < tutors.length; i++)
            tutors[i].slots = new ArrayList <Slot> ();
        for (int i = 0; i < slots.length; i++)
            slots[i].tutors = new ArrayList <Tutor> ();
    }

    @Override
    public String toString() {
        return "Data {" + "courseNames=" + Arrays.toString(courseNames)
                + ", tutors=" + Arrays.toString(tutors) + ", slots="
                + Arrays.toString(slots) + "}";
    }
    
    @Override
    public Data clone() {
        Data ret = new Data();
//        ret.courseNames = courseNames.clone();
        ret.tutors = tutors.clone();
        ret.slots = slots.clone();
        return ret;
    }

}
