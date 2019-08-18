package kdtree;

import edu.princeton.cs.algs4.Point2D;
import edu.princeton.cs.algs4.RectHV;
import edu.princeton.cs.algs4.StdDraw;

import java.util.LinkedList;
import java.util.List;

public class KdTree {
    private static final int RECT_MIN_X = 0;
    private static final int RECT_MIN_Y = 0;
    private static final int RECT_MAX_X = 1;
    private static final int RECT_MAX_Y = 1;

    private KdTreeNode root;
    private int size;


    private class KdTreeNode {
        private KdTreeNode left;
        private KdTreeNode right;
        private final Point2D val;
        private final boolean vertical;
        private final RectHV rect;

        public KdTreeNode(Point2D val, boolean vertical, RectHV rect) {
            this.val = val;
            this.vertical = vertical;
            this.rect = rect;
        }

        public void draw() {
            StdDraw.setPenRadius(0.01);
            if (vertical) {
                StdDraw.setPenColor(StdDraw.RED);
                StdDraw.line(val.x(), rect.ymin(), val.x(), rect.ymax());
            } else {
                StdDraw.setPenColor(StdDraw.BLUE);
                StdDraw.line(rect.xmin(), val.y(), rect.xmax(), val.y());
            }
            StdDraw.setPenColor(StdDraw.BLACK);
            StdDraw.point(val.x(), val.y());
        }
    }

    // construct an empty set of points
    public KdTree() {
        root = null;
        size = 0;
    }

    // is the set empty?
    public boolean isEmpty() {
        return root == null;
    }

    // number of points in the set
    public int size() {
        return size;
    }

    private boolean goToTheLeft(Point2D p, KdTreeNode current) {
        if (current.vertical) return p.x() <= current.val.x();
        else return p.y() <= current.val.y();
    }

    private RectHV getRect(KdTreeNode node, boolean toTheLeft) {
        if (node.vertical) {
            if (toTheLeft) return new RectHV(node.rect.xmin(), node.rect.ymin(), node.val.x(), node.rect.ymax());
            else return new RectHV(node.val.x(), node.rect.ymin(), node.rect.xmax(), node.rect.ymax());
        } else {
            if (toTheLeft) return new RectHV(node.rect.xmin(), node.rect.ymin(), node.rect.xmax(), node.val.y());
            else return new RectHV(node.rect.xmin(), node.val.y(), node.rect.xmax(), node.rect.ymax());
        }
    }

    // add the point to the set (if it is not already in the set)
    public void insert(Point2D p) {
        checkNull(p);
        if (root == null) {
            root = new KdTreeNode(p, true,
                    new RectHV(RECT_MIN_X, RECT_MIN_Y, RECT_MAX_X, RECT_MAX_Y));
            size++;
            return;
        }

        KdTreeNode current = root;
        while (true) {
            // check if the point exists
            if (current.val.equals(p)) return;

            boolean toTheLeft = goToTheLeft(p, current);
            if (toTheLeft) {
                if (current.left == null) {
                    current.left = new KdTreeNode(p, !current.vertical, getRect(current, toTheLeft));
                    size++;
                    return;
                } else {
                    current = current.left;
                }
            } else {
                if (current.right == null) {
                    current.right = new KdTreeNode(p, !current.vertical, getRect(current, toTheLeft));
                    size++;
                    return;
                } else {
                    current = current.right;
                }
            }
        }
    }

    // does the set contain point p?
    public boolean contains(Point2D p) {
        checkNull(p);
        if (isEmpty()) return false;

        KdTreeNode current = root;
        while (current != null) {
            if (current.val.equals(p)) return true;

            if (goToTheLeft(p, current)) {
                current = current.left;
            } else {
                current = current.right;
            }
        }
        return false;
    }


    // draw all points to standard draw
    public void draw() {
        List<KdTreeNode> nodes = getNodeList();
        for (KdTreeNode node : nodes) {
            node.draw();
        }
    }

    private List<KdTreeNode> getNodeList() {
        List<KdTreeNode> nodes = new LinkedList<>();
        processNode(nodes, root);
        return nodes;
    }

    private void processNode(List<KdTreeNode> nodes, KdTreeNode root) {
        if (root == null) return;
        nodes.add(root);
        processNode(nodes, root.left);
        processNode(nodes, root.right);
    }

    // all points that are inside the rectangle (or on the boundary)
    public Iterable<Point2D> range(RectHV rect) {
        checkNull(rect);
        List<Point2D> points = new LinkedList<>();
        findRangePoints(points, root, rect);
        return points;
    }

    private void findRangePoints(List<Point2D> points, KdTreeNode root, RectHV rect) {
        if (root == null) return;
        if (!rect.intersects(root.rect)) return;

        if (rect.contains(root.val)) points.add(root.val);
        findRangePoints(points, root.left, rect);
        findRangePoints(points, root.right, rect);
    }

    // a nearest neighbor in the set to point p; null if the set is empty
    public Point2D nearest(Point2D p) {
        checkNull(p);
        Point2D minPoint = findNearestPoint(p, root, Double.MAX_VALUE);
        return minPoint;
    }

    private Point2D findNearestPoint(Point2D target, KdTreeNode root, double minDist) {
        if (root == null) return null;
        Point2D res = null;

        double distFromRoot = root.val.distanceSquaredTo(target);
        if (distFromRoot < minDist) {
            res = root.val;
            minDist = distFromRoot;
        }

        KdTreeNode first, second;
        if (root.vertical) {
            if (target.x() <= root.val.x()) {
                first = root.left;
                second = root.right;
            } else {
                first = root.right;
                second = root.left;
            }
        } else {
            if (target.y() <= root.val.y()) {
                first = root.left;
                second = root.right;
            } else {
                first = root.right;
                second = root.left;
            }
        }
        Point2D point1 = findNearestPoint(target, first, minDist);
        if (point1 != null) {
            res = point1;
            minDist = point1.distanceSquaredTo(target);
        }

        if (second != null && second.rect.distanceSquaredTo(target) < minDist) {
            Point2D point2 = findNearestPoint(target, second, minDist);
            if (point2 != null) {
                res = point2;
            }
        }

        return res;
    }

    private void checkNull(Object arg) {
        if (arg == null) throw new IllegalArgumentException();
    }

//    // unit testing of the methods (optional)
//    public static void main(String[] args) {
//
//    }
}