/**
 * An evaluator.
 */

package Weighting;

import java.util.ArrayList;

import Items.Data;
import Items.Slot;
import Items.Tutor;

public class Evaluator {
  public static final double PENALTY = 1000000;
  public static final double ADJACENT_PREF = 50;
  public static final double ADJACENT_DISPREF = 50;

  public static double[] evaluate(Data assign, Weighting w, boolean print) {
    double score = 0;
    double[] scores = new double[assign.tutors.length];
    int idx = 0;
    double min = Double.MAX_VALUE, max = 0;
    outer: for (Tutor t : assign.tutors) {
      if (t.numAssignments != t.slots.size()) {
        // tutor should have been assigned a slot
        // or tutor was assigned an extra slot
        // score -= PENALTY;
        // System.out.println ("COULD NOT SCHEDULE " + t.name);
        continue;
      }

      for (int i = 0; i < t.slots.size(); i++)
        for (int j = i + 1; j < t.slots.size(); j++)
          if (t.slots.get(i).simultaneous(t.slots.get(j))) {
            score -= PENALTY;
            continue outer;
          }

      double d = 0;
      ArrayList<Slot> temp = new ArrayList<Slot>(t.slots);
      t.slots.clear();
      // simulate re-adding the slots in one by one.
      // order shouldn't matter when adding slots in
      for (Slot s : temp) {
        d += w.weight(t, s);
        if (t.timeSlots[s.sid] == 0) {
          score -= PENALTY;
          // System.out.println ("A");
          continue outer;
        }
        t.assign(s);
      }
      d /= (double) t.numAssignments;
      scores[idx++] = d;
      min = Math.min(min, d);
      max = Math.max(max, d);
      // System.out.println (t.name + " " + d);
      score += d;
      if (print) {
        if (temp.size() > 1) {
          System.out.println(t.name + " " + t.adjacentPref + " "
              + (temp.get(1).adjacent(temp.get(0)) ? 1 : 0) + " " + d);
        } else {
          System.out.println(t.name + " " + d);
        }
        for (Slot s : t.slots)
          System.out.println(s.sid + " " + t.timeSlots[s.sid] + " " + s.day + " " + s.hour + " "
              + s.office);
        System.out.println();
      }
    }
    for (Slot s : assign.slots) {
      boolean ok = false;
      for (Tutor t : s.tutors) {
        if (t.numAssignments == 2) // officer
          ok = true;
      }
      if (!ok)
        score -= 100 * PENALTY;
    }
    double ave = score / (double) scores.length;
    double std = 0;
    for (int i = 0; i < scores.length; i++)
      std += (scores[i] - ave) * (scores[i] - ave);
    // System.out.println (Math.sqrt(std) + " " + min + " " + max);
    return new double[] {Math.sqrt(std), score};
  }
}
