package Swapper;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

import Items.Data;
import Items.Slot;
import Items.Tutor;
import Weighting.Evaluator;
import Weighting.Weighting;

public class Swapper {
  public static Random rand;

  public static void stabilize(Data dat, Weighting w) {
    stabilize(dat, w, dat.tutors.length * 50000, 1e-10);
  }

  public static void stabilize(Data dat, Weighting w, int iter, double thresh) {
    rand = new Random(System.currentTimeMillis());
    int N = dat.tutors.length;
    double curbest = Evaluator.evaluate(dat, w, false)[1];

    for (int q = 0; q < 5; q++) {
      // less movement the more iterations we go
      double cthresh = thresh / (10 - q) / (10 - q);
      for (int k = 5; k >= 2; k--) {
        System.out.println(k);
        // do k-way swaps iter times
        for (int t = 0; t < iter; t++) {
          // choose k distinct indices
          int[] toSwap = new int[k];
          for (int i = 0; i < k; i++) {
            boolean ok;
            do {
              ok = true;
              int r = rand.nextInt(N);
              if (dat.tutors[r].slots.size() == 0) {
                ok = false;
                continue;
              }
              for (int j = 0; j < i; j++)
                if (r == toSwap[j]) {
                  ok = false;
                  break;
                }
              toSwap[i] = r;
            } while (!ok);
          }

          // choose a random slot within each tutor
          Slot[] slot = new Slot[k];
          for (int i = 0; i < k; i++) {
            ArrayList<Slot> slots = dat.tutors[toSwap[i]].slots;
            int j = rand.nextInt(slots.size());
            slot[i] = slots.get(j);
          }

          // now do a circular swap
          for (int i = 0; i < k; i++) {
            Tutor tc1 = dat.tutors[toSwap[i]];
            Slot sc1 = slot[i], sc2 = slot[(i + 1) % k];
            // remove sc1 from tc1
            sc1.unassign(tc1);
            tc1.unassign(sc1);
            // assign sc2 to tc1
            sc2.assign(tc1);
            tc1.assign(sc2);
          }

          // only move up if at least thresh improvement
          double cost = Evaluator.evaluate(dat, w, false)[1];
          if (cost > curbest + cthresh) {
            System.out.printf("Moved up by %.6f\n", cost - curbest);
            curbest = cost;
          } else {
            // undo changes otherwise
            for (int i = 0; i < k; i++) {
              Tutor tc1 = dat.tutors[toSwap[i]];
              Slot sc1 = slot[(i + 1) % k], sc2 = slot[i];
              // remove sc1 from tc1
              sc1.unassign(tc1);
              tc1.unassign(sc1);
              // assign sc2 to tc1
              sc2.assign(tc1);
              tc1.assign(sc2);
            }

          }
        }
      }
    }
  }
}
