/* *****************************************************************************
 *  Name:              Ada Lovelace
 *  Coursera User ID:  123456
 *  Last modified:     October 16, 1842
 **************************************************************************** */

import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdDraw;
import edu.princeton.cs.algs4.StdOut;

import java.util.ArrayList;
import java.util.Arrays;

public class BruteCollinearPoints {
    private Point[] points;
    private LineSegment[] segments;

    public BruteCollinearPoints(Point[] points) {
        if (points == null || !areAllPointDefined(points) || hasDuplicates(points))
            throw new IllegalArgumentException("");
        this.points = points;
        this.segments = setSegments();
    }

    public int numberOfSegments() {
        return segments.length;
    }

    public LineSegment[] segments() {
        return segments;
    }

    private LineSegment[] setSegments() {
        ArrayList<LineSegment> segments = new ArrayList<>();
        Point[] pointsCopy = Arrays.copyOf(points, points.length);
        Arrays.sort(pointsCopy);
        for (int i = 0; i < pointsCopy.length; i++) {
            for (int j = i + 1; j < pointsCopy.length; j++) {
                for (int k = j + 1; k < pointsCopy.length; k++) {
                    for (int l = k + 1; l < pointsCopy.length; l++) {
                        double firstSlope = pointsCopy[i].slopeTo(pointsCopy[j]);
                        double secondSlope = pointsCopy[i].slopeTo(pointsCopy[k]);
                        double thirdSlope = pointsCopy[i].slopeTo(pointsCopy[l]);
                        if (firstSlope == secondSlope && firstSlope == thirdSlope) {
                            segments.add(new LineSegment(pointsCopy[i], pointsCopy[l]));
                        }
                    }
                }
            }
        }
        return segments.toArray(new LineSegment[segments.size()]);
    }

    private boolean hasDuplicates(Point[] points) {
        for (int i = 0; i < points.length; i++) {
            for (int j = i + 1; j < points.length; j++) {
                if (points[i].equals(points[j])) return true;
            }
        }
        return false;
    }

    private boolean areAllPointDefined(Point[] points) {
        for (int i = 0; i < points.length; i++) {
            if (points[i] == null) return false;
        }
        return true;
    }


    public static void main(String[] args) {
        In in = new In("input48.txt");
        int n = in.readInt();
        Point[] points = new Point[n];
        for (int i = 0; i < n; i++) {
            int x = in.readInt();
            int y = in.readInt();
            points[i] = new Point(x, y);
        }

        // draw the points
        StdDraw.enableDoubleBuffering();
        StdDraw.setXscale(0, 32768);
        StdDraw.setYscale(0, 32768);
        for (Point p : points) {
            p.draw();
        }
        StdDraw.show();

        // print and draw the line segments
        BruteCollinearPoints collinear = new BruteCollinearPoints(points);
        for (LineSegment segment : collinear.segments()) {
            StdOut.println(segment);
            segment.draw();
        }
        StdDraw.show();
    }
}