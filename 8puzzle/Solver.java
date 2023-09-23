/* *****************************************************************************
 *  Name:
 *  Date:
 *  Description:
 **************************************************************************** */

import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.MinPQ;
import edu.princeton.cs.algs4.Stack;
import edu.princeton.cs.algs4.StdOut;

public class Solver {
    private MinPQ<SearchNode> pq;
    private MinPQ<SearchNode> pqTwin;
    private Stack<Board> solution = null;
    private boolean isSolvable = true;

    public Solver(Board board) {
        if (board == null) throw new IllegalArgumentException("Board must not be null!");
        this.pq = new MinPQ<>();
        this.pqTwin = new MinPQ<>();
        pq.insert(new SearchNode(board));
        pqTwin.insert(new SearchNode(board.twin()));
        solve();
    }

    // is the initial board solvable? (see below)
    public boolean isSolvable() {
        return isSolvable;
    }

    // min number of moves to solve initial board; -1 if unsolvable
    public int moves() {
        if (!isSolvable()) return -1;
        return solution.size() - 1;
    }

    // sequence of boards in a shortest solution; null if unsolvable
    public Iterable<Board> solution() {
        return solution;
    }

    private void solve() {
        while (!pq.isEmpty() && !pqTwin.isEmpty()) {
            Stack<Board> x1 = x(pq), x2 = x(pqTwin);
            if (x1 != null) {
                solution = x1;
                isSolvable = true;
                return;
            }
            if (x2 != null) {
                isSolvable = false;
                return;
            }
        }
    }

    private Stack<Board> x(MinPQ<SearchNode> queue) {
        SearchNode current = queue.delMin();
        if (current.board.isGoal()) return current.solution();
        Iterable<Board> neighbors = current.board.neighbors();
        for (Board neighbor : neighbors) {
            if (!current.isVisited(neighbor)) {
                queue.insert(new SearchNode(neighbor, current));
            }
        }
        return null;
    }

    private class SearchNode implements Comparable<SearchNode> {
        private Board board;
        private int moves;
        private SearchNode prev;
        private int priority;

        public SearchNode(Board board, SearchNode prev, int moves) {
            this.board = board;
            this.moves = moves;
            this.prev = prev;
            this.priority = board.manhattan() + moves;
        }

        public SearchNode(Board board) {
            this(board, null, 0);
        }

        public SearchNode(Board board, SearchNode prev) {
            this(board, prev, prev.moves + 1);
        }

        public boolean isVisited(Board b) {
            return prev != null && prev.board.equals(b);
        }

        public Stack<Board> solution() {
            Stack<Board> boards = new Stack<>();
            SearchNode current = this;
            while (current != null) {
                boards.push(current.board);
                current = current.prev;
            }
            return boards;
        }

        public int compareTo(SearchNode searchNode) {
            return Integer.compare(priority, searchNode.priority);
        }
    }

    // test client (see below)
    public static void main(String[] args) {

        // create initial board from file
        In in = new In("puzzle36.txt");
        int n = in.readInt();
        int[][] tiles = new int[n][n];
        for (int i = 0; i < n; i++)
            for (int j = 0; j < n; j++)
                tiles[i][j] = in.readInt();
        Board initial = new Board(tiles);

        // solve the puzzle
        Solver solver = new Solver(initial);

        // print solution to standard output
        if (!solver.isSolvable())
            StdOut.println("No solution possible");
        else {
            StdOut.println("Minimum number of moves = " + solver.moves());
            for (Board board : solver.solution())
                StdOut.println(board);
        }
    }
}
