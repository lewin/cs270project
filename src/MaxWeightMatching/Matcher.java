package MaxWeightMatching;

import java.util.Arrays;
import java.util.PriorityQueue;

import Items.Data;
import Items.Slot;
import Items.Tutor;
import Weighting.Weighting;

public class Matcher {
  private static Tutor[] t;
  private static Slot[] s;
  private static Graph g;
  private static Weighting w;
  private static int n, sz;
  private static boolean[] used;
  private static boolean[] ignore;

  public static void match(Data dat, Weighting waiter) {
    t = dat.tutors;
    s = dat.slots;
    w = waiter;

    n = t.length + s.length;
    sz = t.length;

    // ignore all committee members for now
    ignore = new boolean[t.length];
    for (int i = 0; i < t.length; i++)
      if (t[i].numAssignments == 1)
        ignore[i] = true;

    // Try to assign all slots one tutor
    // this may allow one tutor to go into multiple slots
    used = new boolean[s.length];
    for (int r = 0; r < 2; r++) {
      assign();
    }
    // one more time to make sure all tutors have enough slots
    // this will allow some slots to have more than one person
    // however now all slots can be used
    used = new boolean[s.length];
    assign();
    // now allow committee members to be assigned
    ignore = new boolean[t.length];
    assign();
  }

  private static void makeRandomPerm(int[] perm) {
    for (int i = 0; i < perm.length; i++) {
      perm[i] = i;
      int j = (int) (Math.random() * (i + 1));
      if (j == i)
        continue;
      perm[i] ^= perm[j];
      perm[j] ^= perm[i];
      perm[i] ^= perm[j];
    }
  }

  private static void assign() {
    // initialize graph
    g = new Graph(n);
    price = new double[n];
    partner = new int[n];
    matched = new boolean[n];
    Arrays.fill(partner, -1);

    int[] perm = new int[t.length];
    int[] perm2 = new int[s.length];
    makeRandomPerm(perm);
    makeRandomPerm(perm2);

    for (int b = 0; b < t.length; b++) {
      int i = perm[b];
      if (ignore[i])
        continue;
      // if this tutor can still be assigned slots
      if (t[i].numAssignments > t[i].slots.size()) {
        for (int k = 0; k < s.length; k++) {
          int j = perm2[k];
          // ignore used slots and any slots a tutor can not make
          double r = w.weight(t[i], s[j]);
          if (!used[j] && !t[i].conflict(s[j]) && r > 0)
            updateMatching(i, j + t.length, r);
          // note that this case naturally doesn't let us assign 2
          // of the same slot to one tutor.
        }
      }
    }

    // assign partners
    for (int i = 0; i < t.length; i++) {
      if (partner[i] == -1)
        continue;
      int k = partner[i] - t.length;
      t[i].assign(s[k]);
      s[k].assign(t[i]);
      used[k] = true;
    }
  }

  private static int[] prev, ovis, partner;
  private static double[] delta, price;
  private static boolean[] vis, matched;

  private static void updateMatching(int from, int to, double weight) {
    g.addEdge(from, to, weight);
    double d = weight - price[from] - price[to];
    if (d <= 0)
      return; // no need to further process this case

    // get a feasible matching
    price[from] += d;
    // rematch a and b if necessary, since price matching is no longer on
    // tight edges
    if (matched[from]) {
      matched[partner[from]] = false;
      partner[partner[from]] = -1;
    }
    if (matched[to]) {
      matched[partner[to]] = false;
      partner[partner[to]] = -1;
    }
    matched[from] = true;
    matched[to] = true;
    partner[from] = to;
    partner[to] = from;

    prev = new int[n];
    Arrays.fill(prev, -1);
    vis = new boolean[n];
    ovis = new int[n];
    delta = new double[n];

    // now try to find all augmenting paths
    // should be at most 2, since we remove at most 1 matched pair, and we
    // can add at most one new matched pair
    while (true) {
      int end = findAugmentingPath();
      updatePrices();
      if (end == -1)
        break;
      augmentPath(end);
    }
  }

  /**
   * finds an augmenting path and returns the end node we can follow prev pointers to recreate path
   * Running time is O(M log M), M is number of edges (which is usually about N^2)
   */
  private static int idx;

  private static int findAugmentingPath() {
    idx = -1;
    double C = 0;
    PriorityQueue<Edge> pq = new PriorityQueue<Edge>();
    for (int i = 0; i < t.length; i++) {
      if (!matched[i]) {
        for (Pair node : g.getNeighbors(i)) {
          pq.add(new Edge(i, node.to, price[i] + price[node.to] - node.weight));
        }
        ovis[++idx] = i;
      }
    }

    int end = -1;
    while (pq.size() > 0) {
      Edge f = pq.poll();
      if (vis[f.to])
        continue;
      vis[f.to] = true;
      prev[f.to] = f.from;

      double v = f.weight - C;
      C += v;
      delta[idx] += v;

      if (!matched[f.to]) {
        end = f.to;
        break;
      }
      int l = partner[f.to];
      for (Pair node : g.getNeighbors(l)) {
        if (!vis[node.to]) {
          pq.add(new Edge(l, node.to, price[l] + price[node.to] - node.weight + C));
        }
      }
      ovis[++idx] = f.to;
      ovis[++idx] = l;
    }

    return end;
  }

  /**
   * Keeps prices up to date Running time: O(N), N is number of nodes
   */
  private static void updatePrices() {
    int k = 0;
    while (idx >= 0) {
      k += delta[idx];
      if (ovis[idx] < t.length) {
        price[ovis[idx]] -= k;
      } else {
        price[ovis[idx]] += k;
      }
      idx--;
    }
  }

  /**
   * Switches path parities Running time: O(N), N is number of nodes
   * 
   * @param end : the end of the path
   */
  private static void augmentPath(int end) {
    while (end != -1) {
      matched[end] = true;
      matched[prev[end]] = true;
      partner[end] = prev[end];
      int next = partner[prev[end]];
      partner[prev[end]] = end;
      end = next;
    }
  }
}
