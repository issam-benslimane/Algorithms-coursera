/* *****************************************************************************
 *  Name:
 *  Date:
 *  Description:
 **************************************************************************** */

import edu.princeton.cs.algs4.In;

import java.util.Arrays;

public class Board {
    private int[][] tiles;

    public Board(int[][] tiles) {
        this.tiles = tiles;
    }

    // string representation of this board
    public String toString() {
        StringBuilder s = new StringBuilder();
        s.append(dimension() + "\n");
        for (int[] row : tiles) {
            for (int tile : row) {
                s.append(String.format("%2d ", tile));
            }
            s.append("\n");
        }
        return s.toString();
    }

    // board dimension n
    public int dimension() {
        return tiles[0].length;
    }

    // number of tiles out of place
    public int hamming() {
        int h = 0, n = dimension();
        for (int y = 0; y < n; y++) {
            for (int x = 0; x < n; x++) {
                if (isBlank(x, y)) continue;
                int correctTile = n * y + x + 1;
                int tile = tiles[y][x];
                if (tile != correctTile) h++;
            }
        }
        return h;
    }

    // sum of Manhattan distances between tiles and goal
    public int manhattan() {
        int m = 0, n = dimension();
        for (int y = 0; y < n; y++) {
            for (int x = 0; x < n; x++) {
                if (isBlank(x, y)) continue;
                int tile = tiles[y][x];
                int correctY = (tile - 1) / n;
                int correctX = tile - correctY * n - 1;
                m += Math.abs(correctX - x) + Math.abs(correctY - y);
            }
        }
        return m;
    }

    // is this board the goal board?
    public boolean isGoal() {
        int n = dimension();
        if (isBlank(0, 0)) return false;
        int predecessor = tiles[0][0];
        for (int y = 0; y < n; y++) {
            for (int x = 0; x < n; x++) {
                if ((x == n - 1 && y == n - 1) || (x == 0 && y == 0)) continue;
                if (isBlank(x, y) || predecessor != tiles[y][x] - 1) return false;
                predecessor = tiles[y][x];
            }
        }
        return true;
    }

    // does this board equal y?
    public boolean equals(Object other) {
        if (other == this) return true;
        if (other == null) return false;
        if (other.getClass() != this.getClass()) return false;
        Board board = (Board) other;
        return this.toString().equals(board.toString());
    }

    // all neighboring boards
    public Iterable<Board> neighbors() {
        int emptyTileX = 0, emptyTileY = 0, n = dimension();
        for (int y = 0; y < n; y++) {
            for (int x = 0; x < n; x++) {
                if (isBlank(x, y)) {
                    emptyTileY = y;
                    emptyTileX = x;
                }
            }
        }
        int capacity = 4;
        if (emptyTileX == 0 || emptyTileX == n - 1) capacity--;
        if (emptyTileY == 0 || emptyTileY == n - 1) capacity--;
        Board[] neighbors = new Board[capacity];
        int current = 0;
        int[][] adjacents = { { 1, 0 }, { -1, 0 }, { 0, 1 }, { 0, -1 } };
        for (int[] adjacent : adjacents) {
            int xn = adjacent[0] + emptyTileX;
            int yn = adjacent[1] + emptyTileY;
            if (xn < 0 || yn < 0 || xn >= n || yn >= n) continue;
            int[][] neighborTiles = new int[n][n];
            for (int i = 0; i < n; i++) neighborTiles[i] = Arrays.copyOf(tiles[i], n);
            neighborTiles[emptyTileY][emptyTileX] = tiles[yn][xn];
            neighborTiles[yn][xn] = 0;
            neighbors[current++] = new Board(neighborTiles);
        }
        return Arrays.asList(neighbors);
    }

    // a board that is obtained by exchanging any pair of tiles
    public Board twin() {
        int n = dimension();
        int[][] twinTiles = new int[n][n];
        for (int i = 0; i < n; i++) twinTiles[i] = Arrays.copyOf(tiles[i], n);
        int xi = 0, yi = 0;
        while (isBlank(xi, yi)) {
            xi = (xi + 1) % n;
            if (xi == 0) yi++;
        }
        int xe = (xi + 1) % n, ye = xe == 0 ? yi + 1 : yi;
        int temp = twinTiles[yi][xi];
        twinTiles[yi][xi] = twinTiles[ye][xe];
        twinTiles[ye][xe] = temp;
        return new Board(twinTiles);
    }

    private boolean isBlank(int x, int y) {
        return tiles[y][x] == 0;
    }


    // unit testing (not graded)
    public static void main(String[] args) {
        In in = new In("puzzle04.txt");
        int n = in.readInt();
        int[][] tiles = new int[n][n];
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                tiles[i][j] = in.readInt();
            }
        }
        Board board = new Board(tiles);
        System.out.println(board);
        System.out.println(board.isGoal());
    }
}
