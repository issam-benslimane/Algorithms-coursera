/* *****************************************************************************
 *  Name:              Ada Lovelace
 *  Coursera User ID:  123456
 *  Last modified:     October 16, 1842
 **************************************************************************** */

import edu.princeton.cs.algs4.BST;
import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdDraw;
import edu.princeton.cs.algs4.StdOut;

import java.util.ArrayList;
import java.util.Arrays;

public class FastCollinearPoints {
    private Point[] points;
    private LineSegment[] segments;

    public FastCollinearPoints(Point[] points) {
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
        BST<Double, ArrayList<Integer>> bst = new BST<>();
        for (int i = 0; i < pointsCopy.length; i++) {
            Point p = pointsCopy[i];
            Arrays.sort(pointsCopy, i, pointsCopy.length, p.slopeOrder());
            Point end = p, start = p;
            double slope = Double.NEGATIVE_INFINITY;
            int count = 0;
            for (int j = i + 1; j < pointsCopy.length; j++) {
                Point q = pointsCopy[j];
                double pqSlope = p.slopeTo(q);
                ArrayList<Integer> record = bst.get(slope);
                boolean isDuplicate = false;
                if (record != null) {
                    for (int index : record) {
                        if (pointsCopy[index].slopeTo(q) == pqSlope) {
                            isDuplicate = true;
                            break;
                        }
                    }
                }
                if (isDuplicate) continue;
                if (slope == pqSlope) count++;
                else {
                    if (count >= 2) {
                        segments.add(new LineSegment(start, end));
                        if (record == null) {
                            record = new ArrayList<>();
                            bst.put(slope, record);
                        }
                        record.add(i);
                    }
                    count = 0;
                    start = p;
                    end = p;
                }
                if (q.compareTo(start) < 0) start = q;
                if (q.compareTo(end) > 0) end = q;
                slope = pqSlope;
            }
            if (count >= 2) {
                segments.add(new LineSegment(start, end));
                ArrayList<Integer> record = bst.get(slope);
                if (record == null) {
                    record = new ArrayList<>();
                    bst.put(slope, record);
                }
                record.add(i);
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
        In in = new In("input8000.txt");
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
        FastCollinearPoints collinear = new FastCollinearPoints(points);
        for (LineSegment segment : collinear.segments()) {
            StdOut.println(segment);
            segment.draw();
        }
        StdDraw.show();
    }
}

