/* *****************************************************************************
 *  Name:
 *  Date:
 *  Description:
 **************************************************************************** */

import edu.princeton.cs.algs4.Edge;
import edu.princeton.cs.algs4.EdgeWeightedGraph;
import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.IndexMinPQ;
import edu.princeton.cs.algs4.Stack;

public class MST {
    private final EdgeWeightedGraph G;
    private boolean[] intree;
    private Stack<Edge> tree;

    public MST(EdgeWeightedGraph G) {
        this.G = G;
        this.intree = new boolean[G.V()];
        this.tree = new Stack<>();
        for (int i = 0; i < G.V(); i++) {
            intree[i] = false;
        }
        IndexMinPQ
        dij();
    }

    public Iterable<Edge> path() {
        double weight = 0;
        for (Edge e : tree) {
            weight += e.weight();
        }
        return weight;
    }

    private void dij() {

    }


    public static void main(String[] args) {
        In in = new In("digraph25.txt");
        EdgeWeightedGraph G = new EdgeWeightedGraph(in);
        MST mst = new MST(G);
        System.out.println(mst.totalWeight());
    }
}
