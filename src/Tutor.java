/**
 * Stores data about the preferences of an individual tutor.
 */
public class Tutor {
    public int tid;
    public String name;
    public int[] slots; // ??? [0,2] on preference
    public int[] courses; // ??? [0,2] on preference
    public int adjacentPref; // ??? [0,2] on preference

    @Override
    public String toString() {
        return tid + "@" + name;
    }
}
