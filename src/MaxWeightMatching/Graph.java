package MaxWeightMatching;

import java.util.*;

public class Graph {
  private ArrayList neighbors[];
  private int nodes;

  public Graph(int nodes) {
    this.nodes = nodes;

    neighbors = new ArrayList[nodes];
    for (int i = 0; i < nodes; i++)
      neighbors[i] = new ArrayList<Pair>();
  }

  public void addEdge(int from, int to, double weight) {
    neighbors[from].add(new Pair(to, weight));
    neighbors[to].add(new Pair(from, weight));
  }

  public ArrayList<Pair> getNeighbors(int node) {
    return neighbors[node];
  }
}
