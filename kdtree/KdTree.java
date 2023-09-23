/* *****************************************************************************
 *  Name:
 *  Date:
 *  Description:
 **************************************************************************** */

import edu.princeton.cs.algs4.Point2D;
import edu.princeton.cs.algs4.RectHV;
import edu.princeton.cs.algs4.SET;
import edu.princeton.cs.algs4.StdDraw;

import java.awt.Color;
import java.util.Comparator;

public class KdTree {
    private Node root = null;
    private int size = 0;

    public KdTree() {
    }

    public boolean isEmpty() {
        return root == null;
    }

    public int size() {
        return size;
    }

    private Node insert(Node node, Comparator<Point2D> comparator, RectHV rect, Point2D p) {
        if (node == null) {
            Node newNode = new Node();
            newNode.p = p;
            newNode.comparator = comparator;
            newNode.rect = rect;
            return newNode;
        }
        if (less(comparator, p, node.p))
            node.left = insert(node.left, node.nextComparator(), node.leftRectangle(), p);
        else node.right = insert(node.right, node.nextComparator(), node.rightRectangle(), p);
        return node;
    }

    public void insert(Point2D p) {
        if (p == null) throw new IllegalArgumentException("p must not be null");
        if (contains(p)) return;
        root = insert(root, Point2D.X_ORDER, new RectHV(0, 0, 1, 1), p);
        size++;
    }

    public boolean contains(Point2D p) {
        if (p == null) throw new IllegalArgumentException("p must not be null");
        Node current = root;
        while (current != null) {
            if (current.p.equals(p)) return true;
            if (less(current.comparator, p, current.p)) current = current.left;
            else current = current.right;
        }
        return false;
    }

    private void draw(Node node) {
        if (node == null) return;
        double x = node.p.x();
        double y = node.p.y();
        StdDraw.point(x, y);
        StdDraw.setPenRadius();
        if (node.isX()) {
            StdDraw.setPenColor(Color.RED);
            StdDraw.line(x, node.rect.ymin(), x, node.rect.ymax());
            StdDraw.setPenColor();
            StdDraw.setPenRadius(0.01);
            draw(node.left);
            draw(node.right);
        }
        else {
            StdDraw.setPenColor(Color.BLUE);
            StdDraw.line(node.rect.xmin(), y, node.rect.xmax(), y);
            StdDraw.setPenColor();
            StdDraw.setPenRadius(0.01);
            draw(node.left);
            draw(node.right);
        }
    }

    public void draw() {
        draw(root);
    }

    private SET<Point2D> range(Node node, RectHV rect) {
        SET<Point2D> pointsInRange = new SET<>();
        if (node == null || !node.rect.intersects(rect)) return pointsInRange;
        if (rect.contains(node.p)) pointsInRange.add(node.p);
        return pointsInRange.union(range(node.left, rect)).union(range(node.right, rect));
    }

    public Iterable<Point2D> range(RectHV rect) {
        if (rect == null) throw new IllegalArgumentException("rect must not be null");
        return range(root, rect);
    }

    private Point2D nearest(Node node, Point2D p, Point2D nearest) {
        if (node == null) return nearest;
        if (nearest == null) nearest = node.p;
        else if (node.p.distanceSquaredTo(p) < nearest.distanceSquaredTo(p)) nearest = node.p;
        if (less(node.comparator, p, node.p)) {
            nearest = nearest(node.left, p, nearest);
            if (node.right != null
                    && nearest.distanceSquaredTo(p) > node.right.rect.distanceSquaredTo(p)) {
                nearest = nearest(node.right, p, nearest);
            }
        }
        else {
            nearest = nearest(node.right, p, nearest);
            if (node.left != null
                    && nearest.distanceSquaredTo(p) > node.left.rect.distanceSquaredTo(p)) {
                nearest = nearest(node.left, p, nearest);
            }
        }

        return nearest;
    }

    public Point2D nearest(Point2D p) {
        if (p == null) throw new IllegalArgumentException("p must not be null");
        if (isEmpty()) return null;
        return nearest(root, p, null);
    }

    private boolean less(Comparator<Point2D> c, Point2D p, Point2D q) {
        return c.compare(p, q) < 0;
    }

    private static class Node {
        private Point2D p;
        private Node left;
        private Node right;
        private RectHV rect;
        private Comparator<Point2D> comparator;

        public boolean isX() {
            return comparator.equals(Point2D.X_ORDER);
        }

        public Comparator<Point2D> nextComparator() {
            return isX() ? Point2D.Y_ORDER : Point2D.X_ORDER;
        }

        public RectHV leftRectangle() {
            return isX() ? new RectHV(rect.xmin(), rect.ymin(), p.x(), rect.ymax()) :
                   new RectHV(rect.xmin(), rect.ymin(), rect.xmax(), p.y());
        }

        public RectHV rightRectangle() {
            return isX() ? new RectHV(p.x(), rect.ymin(), rect.xmax(), rect.ymax()) :
                   new RectHV(rect.xmin(), p.y(), rect.xmax(), rect.ymax());
        }
    }
}
