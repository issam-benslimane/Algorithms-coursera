/* *****************************************************************************
 *  Name:
 *  Date:
 *  Description:
 **************************************************************************** */

import edu.princeton.cs.algs4.Point2D;
import edu.princeton.cs.algs4.RectHV;
import edu.princeton.cs.algs4.SET;
import edu.princeton.cs.algs4.StdDraw;

public class PointSET {
    private SET<Point2D> points = new SET<>();

    public PointSET() {
    }

    public boolean isEmpty() {
        return points.isEmpty();
    }

    public int size() {
        return points.size();
    }

    public void insert(Point2D p) {
        if (p == null) throw new IllegalArgumentException("p must not be null");
        points.add(p);
    }

    public boolean contains(Point2D p) {
        if (p == null) throw new IllegalArgumentException("p must not be null");
        return points.contains(p);
    }

    public void draw() {
        for (Point2D point : points) {
            StdDraw.point(point.x(), point.y());
        }
    }

    public Iterable<Point2D> range(RectHV rect) {
        if (rect == null) throw new IllegalArgumentException("rect must not be null");
        SET<Point2D> pointsInRange = new SET<>();
        for (Point2D point : points) {
            if (rect.contains(point)) pointsInRange.add(point);
        }
        return pointsInRange;
    }

    public Point2D nearest(Point2D p) {
        if (p == null) throw new IllegalArgumentException("p must not be null");
        if (isEmpty()) return null;
        Point2D nearest = null;
        for (Point2D point : points) {
            if (nearest == null) nearest = point;
            else if (p.distanceTo(nearest) > p.distanceTo(point)) nearest = point;
        }
        return nearest;
    }

    public static void main(String[] args) {
    }
}
