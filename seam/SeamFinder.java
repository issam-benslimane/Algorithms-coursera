/* *****************************************************************************
 *  Name:
 *  Date:
 *  Description:
 **************************************************************************** */

import edu.princeton.cs.algs4.Digraph;
import edu.princeton.cs.algs4.Topological;

public class SeamFinder {
    private Digraph g;
    private double[][] energy;
    private int[] parent;
    private double[] cost;
    private Topological t;
    private int width;
    private int height;
    private int start;
    private int end;

    public SeamFinder(double[][] energy) {
        this.width = energy[0].length;
        this.height = energy.length;
        this.energy = energy;
        this.start = 0;
        this.end = width * height + 1;
        this.g = buildGraph();
        this.t = new Topological(g);
        this.parent = new int[g.V()];
        this.cost = new double[g.V()];
        buildTree();
    }

    public int[] seam() {
        int[] seam = new int[height];
        int c = height - 1;
        int v = parent[end];
        while (!isVirtual(v)) {
            int[] coordinates = toCoordinates(v);
            int x = coordinates[0];
            seam[c--] = x;
            v = parent[v];
        }
        return seam;
    }

    private void buildTree() {
        for (int i = 0; i < g.V(); i++) {
            cost[i] = Double.POSITIVE_INFINITY;
            parent[i] = -1;
        }
        cost[start] = 0;
        for (int v : t.order()) {
            for (int w : g.adj(v)) {
                if (cost[w] > cost[v] + energy(w)) {
                    cost[w] = cost[v] + energy(w);
                    parent[w] = v;
                }
            }
        }
    }

    private Digraph buildGraph() {
        Digraph g = new Digraph(height * width + 2);
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                int[][] neighbors = { { 0, 1 }, { 1, 1 }, { -1, 1 } };
                for (int[] w : neighbors) {
                    int wx = w[0] + x;
                    int wy = w[1] + y;
                    if (!inBound(wx, wy)) continue;
                    g.addEdge(toVertex(x, y), toVertex(wx, wy));
                }
            }
        }
        for (int i = 1; i <= width; i++) {
            g.addEdge(start, i);
            g.addEdge(toVertex(i - 1, height - 1), end);
        }
        return g;
    }

    private double energy(int v) {
        if (isVirtual(v)) return 0;
        int[] c = toCoordinates(v);
        int x = c[0], y = c[1];
        return energy[y][x];
    }

    private int toVertex(int x, int y) {
        return x + y * width + 1;
    }

    private int[] toCoordinates(int v) {
        int y = Math.floorDiv(v - 1, width);
        int x = v - 1 - y * width;
        int[] c = { x, y };
        return c;
    }

    private boolean isVirtual(int v) {
        return v == start || v == end;
    }

    private boolean inBound(int x, int y) {
        return y >= 0 && y < height && x >= 0 && x < width;
    }

}
