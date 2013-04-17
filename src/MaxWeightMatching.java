import java.io.*;
import java.util.*;

// no need for this anymore
public class MaxWeightMatching {
    
    private static int [] price;
    private static int [] eadj, eprev, elast, ecost;
    private static int [] ovis, delta;
    private static int [] partner, prev;
    private static boolean [] vis, matched;
    private static int eidx, C, idx, N, M;
    
    public static void main (String [] args) throws IOException {
        BufferedReader in = new BufferedReader (new InputStreamReader (System.in));
        PrintWriter out = new PrintWriter (System.out, true);
        
        StringTokenizer st = new StringTokenizer (in.readLine());
        N = Integer.parseInt (st.nextToken()); M = Integer.parseInt (st.nextToken());
        
        init();
        for (int t = 0; t < M; t++) {
            st = new StringTokenizer (in.readLine());
            int a = Integer.parseInt (st.nextToken()), 
                b = Integer.parseInt (st.nextToken()),
                c = Integer.parseInt (st.nextToken());
            if (a > b) {int d = a; a = b; b = d;}
            addEdge (a, b, c);
            int d = c - price [a] - price [b];
            if (d < 0) continue;

            price [a] += d;
            rematch (a, b);
            
            init2();
            while (true) {
                int end = findAugmentingPath();
                updatePrices();
                if (end == -1) break;
                augmentPath (end);
            }
            for (int i = 0; i < 2 * N; i++)
                System.out.printf ("%d %d %d\n", i, partner [i], price [i]);
        }
        
    }
    
    static class Edge implements Comparable <Edge> {
        public int from, to, weight;
        public Edge (int from, int to, int weight){
            this.from = from;
            this.to = to;
            this.weight = weight;
        }
        
        public int compareTo (Edge other) {
            return weight - other.weight;
        }
    }
    
    private static void addEdge (int a, int b, int c) {
        eadj [eidx] = b; ecost [eidx] = c; eprev [eidx] = elast [a]; elast [a] = eidx++;
        eadj [eidx] = a; ecost [eidx] = c; eprev [eidx] = elast [b]; elast [b] = eidx++;
    }
    
    private static void init () {
        price = new int [3 * N];
        eadj = new int [2 * M + 3 * N];
        eprev = new int [2 * M + 3 * N];
        ecost = new int [2 * M + 3 * N];
        elast = new int [3 * N];
        Arrays.fill (elast, -1);
        eidx = 0;
        
        partner = new int [3 * N];
        Arrays.fill (partner, -1);
        matched = new boolean [3 * N];
        ovis = new int [2 * N];
        delta = new int [2 * N];
        
        for (int i = 0; i < N; i++) {
            addEdge (i, i + 2 * N, 0);
            matched [i] = true;
            matched [i + 2 * N] = true;
            partner [i] = i + 2 * N;
            partner [i + 2 * N] = i;
        }
    }
    
    private static void init2 () {
        prev = new int [3 * N];
        Arrays.fill (prev, -1);
        vis = new boolean [3 * N];
        ovis = new int [2 * N];
        delta = new int [2 * N];
    }
    
    private static void rematch (int a, int b) {
        if (partner [a] != -1) {
            matched [partner [a]] = false;
            partner [partner [a]] = -1;
        }
        if (partner [b] != -1) {
            matched [partner [b]] = false;
            partner [partner [b]] = -1;
        }
        matched [a] = true; matched [b] = true;
        partner [a] = b; partner [b] = a;
    }
    
    private static int findAugmentingPath () {
        idx = -1; C = 0;
        PriorityQueue <Edge> pq = new PriorityQueue <Edge> ();
        for (int i = 0; i < N; i++) {
            if (!matched [i]) {
                for (int e = elast [i]; e != -1; e = eprev [e])
                    pq.add (new Edge (i, eadj [e], price [i] + price [eadj [e]] - ecost [e]));
                ovis [++idx] = i;
            }
        }
        
        int end = -1;
        while (pq.size() > 0) {
            Edge f = pq.poll();
            if (vis [f.to]) continue;
            vis [f.to] = true; prev [f.to] = f.from;
            int v = f.weight - C; C += v; delta [idx] += v;
            if (!matched [f.to]) {end = f.to; break;}
            int l = partner [f.to];
            for (int e = elast [l]; e != -1; e = eprev [e])
                if (!vis [eadj [e]])
                    pq.add (new Edge (l, eadj [e], price [l] + price [eadj [e]] - ecost [e] + C));
            ovis [++idx] = f.to; ovis [++idx] = l;
        }
        
        return end;
    }
    
    private static void updatePrices() {
        int k = 0;
        while (idx >= 0) {
            k += delta [idx];
            if (ovis [idx] < N) price [ovis [idx]] -= k;
            else price [ovis [idx]] += k;
            idx--;
        }
    }
    
    private static void augmentPath (int end) {
        while (end != -1) {
            matched [end] = true;
            matched [prev [end]] = true;
            partner [end] = prev [end];
            int next = partner [prev [end]];
            partner [prev [end]] = end;
            end = next;
        }
    }
    
}
