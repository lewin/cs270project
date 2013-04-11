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

    public Tutor[] tutors;

    public Slot[] slots;

    @Override
    public String toString() {
        return "Data {" + 
               "courseNames=" + Arrays.toString(courseNames) + 
               ", tutors=" + Arrays.toString(tutors) + 
               ", slots=" + Arrays.toString(slots) + "}";
    }

}

