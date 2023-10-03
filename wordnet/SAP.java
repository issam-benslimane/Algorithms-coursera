/* *****************************************************************************
 *  Name:
 *  Date:
 *  Description:
 **************************************************************************** */

import edu.princeton.cs.algs4.BreadthFirstDirectedPaths;
import edu.princeton.cs.algs4.Digraph;
import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdIn;
import edu.princeton.cs.algs4.StdOut;

public class SAP {

    // constructor takes a digraph (not necessarily a DAG)
    private final Digraph G;

    public SAP(Digraph G) {
        this.G = new Digraph(G);
    }

    // length of shortest ancestral path between v and w; -1 if no such path
    public int length(int v, int w) {
        validateVertex(v);
        validateVertex(w);
        BreadthFirstDirectedPaths vbst = new BreadthFirstDirectedPaths(G, v);
        BreadthFirstDirectedPaths wbst = new BreadthFirstDirectedPaths(G, w);
        int shortest = ancestor(v, w);
        if (shortest == -1) return -1;
        return vbst.distTo(shortest) + wbst.distTo(shortest);
    }

    // a common ancestor of v and w that participates in a shortest ancestral path; -1 if no such path
    public int ancestor(int v, int w) {
        validateVertex(v);
        validateVertex(w);
        BreadthFirstDirectedPaths vbst = new BreadthFirstDirectedPaths(G, v);
        BreadthFirstDirectedPaths wbst = new BreadthFirstDirectedPaths(G, w);
        int shortest = -1;
        for (int i = 0; i < G.V(); i++) {
            if (vbst.hasPathTo(i) && wbst.hasPathTo(i)) {
                if (shortest == -1) shortest = i;
                else if (vbst.distTo(i) + wbst.distTo(i) < vbst.distTo(shortest) + wbst.distTo(
                        shortest)) shortest = i;
            }
        }
        return shortest;
    }

    // length of shortest ancestral path between any vertex in v and any vertex in w; -1 if no such path
    public int length(Iterable<Integer> v, Iterable<Integer> w) {
        validateIterable(v);
        validateIterable(w);
        BreadthFirstDirectedPaths vbst = new BreadthFirstDirectedPaths(G, v);
        BreadthFirstDirectedPaths wbst = new BreadthFirstDirectedPaths(G, w);
        int shortest = ancestor(v, w);
        if (shortest == -1) return -1;
        return vbst.distTo(shortest) + wbst.distTo(shortest);
    }

    // a common ancestor that participates in shortest ancestral path; -1 if no such path
    public int ancestor(Iterable<Integer> v, Iterable<Integer> w) {
        validateIterable(v);
        validateIterable(w);
        BreadthFirstDirectedPaths vbst = new BreadthFirstDirectedPaths(G, v);
        BreadthFirstDirectedPaths wbst = new BreadthFirstDirectedPaths(G, w);
        int shortest = -1;
        for (int i = 0; i < G.V(); i++) {
            if (vbst.hasPathTo(i) && wbst.hasPathTo(i)) {
                if (shortest == -1) shortest = i;
                else if (vbst.distTo(i) + wbst.distTo(i) < vbst.distTo(shortest) + wbst.distTo(
                        shortest)) shortest = i;
            }
        }
        return shortest;
    }


    private void validateVertex(int v) {
        if (v < 0 || v >= G.V()) throw new IllegalArgumentException("");
    }

    private void validateIterable(Iterable<Integer> iterable) {
        if (iterable == null) throw new IllegalArgumentException("");
        for (Integer v : iterable) {
            if (v == null) throw new IllegalArgumentException("");
        }
    }

    // do unit testing of this class
    public static void main(String[] args) {
        In in = new In("digraph25.txt");
        Digraph G = new Digraph(in);
        SAP sap = new SAP(G);
        while (!StdIn.isEmpty()) {
            int v = StdIn.readInt();
            int w = StdIn.readInt();
            int length = sap.length(v, w);
            int ancestor = sap.ancestor(v, w);
            StdOut.printf("length = %d, ancestor = %d\n", length, ancestor);
        }
    }
}