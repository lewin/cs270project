/**
 * Interface for weight functions on Tutors and Slots. Provides a single function which gives the
 * weight of an edge (assignment) between a Tutor and a Slot.
 * 
 * @param tutor Tutor to be assigned
 * @param slot Office hour slot to be assigned to
 */

package Weighting;

import Items.Slot;
import Items.Tutor;

public interface Weighting {
  /**
   * @param tutor Tutor to be matched, may already be partially assigned
   * @param slot Slot to be matched, may already have some tutors
   * @return -1 if Tutor is already assigned to a simultaneous slot a nonnegative value otherwise
   */
  public double weight(Tutor tutor, Slot slot);
}
