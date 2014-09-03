/**
 * A weight function. The source of many delicious smells.
 */

package Weighting;

import java.util.HashMap;

import Items.Slot;
import Items.Tutor;

public class Chef implements Weighting {

  public double[][] weighting;
  public HashMap<Integer, Integer> mp, mp2;

  public Chef() {

  }

  public Chef(double[][] w, HashMap<Integer, Integer> mp, HashMap<Integer, Integer> mp2) {
    weighting = new double[w.length][w[0].length];
    for (int i = 0; i < weighting.length; i++)
      System.arraycopy(w[i], 0, weighting[i], 0, w[0].length);
    this.mp = new HashMap<Integer, Integer>(mp);
    this.mp2 = new HashMap<Integer, Integer>(mp2);
  }

  public double weight(Tutor t, Slot s) {
    if (weighting[mp2.get(t.tid)][mp.get(s.sid)] == 0)
      return -1000;

    double retval = weighting[mp2.get(t.tid)][mp.get(s.sid)];

    for (Slot r : t.slots) {
      if (r.day.equals(s.day)) {
        if (s.adjacent(r)) {
          if (t.adjacentPref == 1)
            retval *= 1.25;
          else if (t.adjacentPref == -1)
            retval *= .75;
        } else {
          retval *= 0;
        }
      } else {
        if (t.adjacentPref == -1)
          retval *= 1.25;
        else if (t.adjacentPref == 1)
          retval *= .75;
      }
    }

    return retval;
  }
}
