package MaxWeightMatching;

public class Edge implements Comparable <Edge> {
    public int from, to;
    public double weight;
    public Edge (int from, int to, double weight){
        this.from = from;
        this.to = to;
        this.weight = weight;
    }
    
    public int compareTo (Edge other) {
        return Double.compare (weight, other.weight);
    }
}
