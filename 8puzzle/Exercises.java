/* *****************************************************************************
 *  Name:
 *  Date:
 *  Description:
 **************************************************************************** */

import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.MinPQ;
import edu.princeton.cs.algs4.Queue;

import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class Exercises {
    public static void main(String[] args) {
        In in = new In("g1.txt");
        int V = in.readInt();
        int E = in.readInt();
        Graph<Character> g = new Graph<>(V);
        for (int i = 0; i < E; i++) {
            in.readLine();
            char v = in.readChar();
            char w = in.readChar();
            int weight = in.readInt();
            g.addEdge(v, w, weight);
        }
        bfs(g, 'A');
    }

    static void topsort(Graph<Character> g) {

    }

    static void processEdge(char x, char y) {
    }

    static void processVertexEarly(char v) {
    }

    static void processVertexLate(char v) {
    }

    static void bfs(Graph<Character> g, char start) {
        Queue<Character> queue = new Queue<>();
        Set<Character> discovered = new HashSet<>();
        Set<Character> processed = new HashSet<>();

        queue.enqueue(start);
        discovered.add(start);
        processVertexEarly(v);
        while (!queue.isEmpty()) {
            char v = queue.dequeue();
            System.out.println(v);
            for (char y : g.adj(v)) {
                if (!processed.contains(y) || g.directed) {
                    processEdge(v, y);
                }
                if (!discovered.contains(y)) {
                    queue.enqueue(y);
                    discovered.add(y);
                }
            }
            processVertexLate(v);
            processed.add(v);
        }
    }

    static class Graph<T> {
        // creating an object of the Map class that stores the edges of the graph
        private Map<T, List<EdgeNode<T>>> adj = new HashMap<>();
        private final int V;
        private int E;
        private boolean directed;

        public Graph(int V, boolean directed) {
            this.V = V;
            this.E = 0;
            this.directed = directed;
        }

        public Graph(int V) {
            this(V, false);
        }
        // the method adds a new vertex to the graph

        // the method adds an edge between source and destination

        public void addEdge(T v, T w, int weight) {
            addEdge(v, w, weight, directed);
            E++;
        }

        public void addEdge(T v, T w) {
            addEdge(v, w, 1);
        }

        private void addEdge(T v, T w, int weight, boolean directed) {
            if (!adj.containsKey(v)) addNewVertex(v);
            adj.get(v).add(new EdgeNode<>(w, weight));
            if (!directed) {
                addEdge(w, v, weight, true);
            }
        }

        public Iterable<T> adj(T v) {
            MinPQ<T> q = new MinPQ<>();
            for (EdgeNode<T> edge : adj.get(v)) {
                q.insert(edge.label);
            }
            return q;
        }

        // the method counts the number of vertices
        public int nvertices() {
            return V;
        }

        // the method counts the number of edges
        public int nedges() {
            return E;
        }

        private void addNewVertex(T v) {
            adj.put(v, new LinkedList<>());
        }

        // prints the adjacencyS list of each vertex
        // here we have overridden the toString() method of the StringBuilder class
        @Override
        public String toString() {
            StringBuilder builder = new StringBuilder();
            // foreach loop that iterates over the keys
            for (T v : adj.keySet()) {
                builder.append(v.toString() + ": ");
                // foreach loop for getting the vertices
                for (EdgeNode<T> w : adj.get(v)) {
                    builder.append(w.toString() + " ");
                }
                builder.append("\n");
            }
            return (builder.toString());
        }

        public class EdgeNode<T> {
            private final T label;
            private final int weight;

            public EdgeNode(T label, int weight) {
                this.label = label;
                this.weight = weight;
            }

            public String toString() {
                return label.toString();
            }
        }
    }
}


