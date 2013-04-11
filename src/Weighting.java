/**
 * Interface for weight functions on Tutors and Slots.
 * Provides a single function which gives the weight of an edge (assignment) 
 * between a Tutor and a Slot.
 * 
 * @param tutor Tutor to be assigned
 * @param slot Office hour slot to be assigned to
 */
public interface Weighting {
    public double weight(Tutor tutor, Slot slot);
}

