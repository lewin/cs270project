/**
 * Data.java contains the classes for the Java object representation for
 * our data elements.
 * Includes classes for:
 *   * Tutors (and their preferences)
 *   * Office hour slots (and their preferences)
 *   * Weight functions
 * Populate these classes using the Gson library
 */

package data;

/**
 * Array of all course names.
 * Each index corresponds exactly to the index in any of the courses[].
 */
public static String[] courseNames;

/**
 * Stores data about the preferences of an individual tutor.
 */
public class Tutor {
    public int tid;
    public String name;
    public int[] slots; // ??? [0,2] on preference
    public int[] courses; // ??? [0,2] on preference
    public int adjacentPref; // ??? [0,2] on preference
}

/**
 * Stores data about the preferences of an individual office hour slot.
 */
public class Slot {
    public int sid;
    public String name; // probably "OFFICE=START-END" or something
    // ??? something like: 0=wrong office, 1=class in session, 2=other
    public int[] courses; // ??? [0,2] on preference
}

/**
 * Interface for weight functions on Tutors and Slots.
 * Provides a single function which gives the weight of an edge (assignment) 
 * between a Tutor and a Slot.
 * 
 * @param tutor Tutor to be assigned
 * @param slot Office hour slot to be assigned to
 */
public interface Weighting {
    public static double weight(Tutor tutor, Slot slot);
}

