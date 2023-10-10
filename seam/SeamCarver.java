/* *****************************************************************************
 *  Name:
 *  Date:
 *  Description:
 **************************************************************************** */

import edu.princeton.cs.algs4.Picture;

import java.awt.Color;

public class SeamCarver {
    private Picture picture;
    private double[][] energyCache;
    private boolean isTransposed = false;

    public SeamCarver(Picture picture) {
        this.picture = new Picture(picture);
        this.energyCache = new double[height()][width()];
        for (int y = 0; y < height(); y++) {
            for (int x = 0; x < width(); x++) {
                energyCache[y][x] = energy(x, y);
            }
        }
    }

    // current picture
    public Picture picture() {
        return picture;
    }

    // width of current picture
    public int width() {
        return picture().width();
    }

    // height of current picture
    public int height() {
        return picture().height();
    }

    // energy of pixel at column x and row y
    public double energy(int x, int y) {
        validateCoordinates(x, y);
        int[] horizontal = { 1, 0 };
        int[] vertical = { 0, 1 };
        if (inBorder(x, y)) return 1000;
        double energy = Math.sqrt(
                squareGradient(x, y, horizontal) + squareGradient(x, y, vertical));
        return energy;
    }

    private double squareGradient(int x, int y, int[] direction) {
        int dx = direction[0];
        int dy = direction[1];
        int x1 = x - dx, y1 = y - dy, x2 = x + dx, y2 = y + dy;
        Color c1 = picture.get(x1, y1);
        Color c2 = picture.get(x2, y2);
        double R = Math.pow(c2.getRed() - c1.getRed(), 2);
        double G = Math.pow(c2.getGreen() - c1.getGreen(), 2);
        double B = Math.pow(c2.getBlue() - c1.getBlue(), 2);
        return R + G + B;
    }

    // sequence of indices for horizontal seam
    public int[] findHorizontalSeam() {
        transpose();
        int[] seam = findVerticalSeam();
        transpose();
        return seam;
    }

    // sequence of indices for vertical seam
    public int[] findVerticalSeam() {
        SeamFinder sp = new SeamFinder(energyCache);
        return sp.seam();
    }

    // remove horizontal seam from current picture
    public void removeHorizontalSeam(int[] seam) {
        validateSeam(seam, height(), width());
        transpose();
        removeVerticalSeam(seam);
        transpose();
    }

    // remove vertical seam from current picture
    public void removeVerticalSeam(int[] seam) {
        validateSeam(seam, width(), height());
        Picture newPicture = new Picture(width() - 1, height());
        for (int y = 0; y < height(); y++) {
            for (int x = 0; x < width() - 1; x++) {
                if (x >= seam[y]) {
                    newPicture.set(x, y, picture.get(x + 1, y));
                }
                else {
                    newPicture.set(x, y, picture.get(x, y));
                }
            }
        }
        picture = newPicture;
        for (int y = 0; y < height(); y++) {
            for (int x = 0; x < width(); x++) {
                energyCache[y][x] = energy(x, y);
            }
        }
    }

    private void transpose() {
        Picture pt = new Picture(height(), width());
        double[][] energyt = new double[width()][height()];
        for (int y = 0; y < width(); y++) {
            for (int x = 0; x < height(); x++) {
                pt.set(x, y, picture.get(y, x));
                energyt[y][x] = energyCache[x][y];
            }
        }
        picture = pt;
        energyCache = energyt;
    }

    private boolean inBorder(int x, int y) {
        return x == 0 || y == 0 || x == width() - 1 || y == height() - 1;
    }

    private boolean inBound(int x, int y) {
        return y >= 0 && y < height() && x >= 0 && x < width();
    }

    private void validateSeam(int[] seam, int width, int height) {
        if (seam == null) throw new IllegalArgumentException("");
        if (seam.length != height) throw new IllegalArgumentException("");
        int prev = seam[0];
        for (int n : seam) {
            if (n < 0 || n >= width) throw new IllegalArgumentException("");
            if (Math.abs(n - prev) > 1) throw new IllegalArgumentException("");
            prev = n;
        }
    }

    private void validateCoordinates(int x, int y) {
        if (!inBound(x, y)) throw new IllegalArgumentException("");
    }


    //  unit testing (optional)
    public static void main(String[] args) {
        SeamCarver sc = new SeamCarver(new Picture("HJocean.png"));
        sc.removeHorizontalSeam(sc.findHorizontalSeam());
    }
}
