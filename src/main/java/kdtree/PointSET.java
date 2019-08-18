package kdtree;

import edu.princeton.cs.algs4.Point2D;
import edu.princeton.cs.algs4.RectHV;
import edu.princeton.cs.algs4.StdDraw;

import java.util.Set;
import java.util.TreeSet;
import java.util.stream.Collectors;

public class PointSET {
    private final Set<Point2D> points;

    // construct an empty set of points
    public PointSET() {
        points = new TreeSet<>();
    }

    // is the set empty?
    public boolean isEmpty() {
        return points.isEmpty();
    }

    // number of points in the set
    public int size() {
        return points.size();
    }

    // add the point to the set (if it is not already in the set)
    public void insert(Point2D p) {
        checkNull(p);
        points.add(p);
    }

    // does the set contain point p?
    public boolean contains(Point2D p) {
        checkNull(p);
        return points.contains(p);
    }

    // draw all points to standard draw
    public void draw() {
        for (Point2D point : points) {
            StdDraw.setPenRadius(0.01);
            StdDraw.setPenColor(StdDraw.BLACK);
            StdDraw.point(point.x(), point.y());
        }
    }

    // all points that are inside the rectangle (or on the boundary)
    public Iterable<Point2D> range(RectHV rect) {
        checkNull(rect);
        return points.stream().filter(rect::contains).collect(Collectors.toList());
    }

    // a nearest neighbor in the set to point p; null if the set is empty
    public Point2D nearest(Point2D p) {
        checkNull(p);
        double minDist = Double.MAX_VALUE;
        Point2D minPoint = null;
        for (Point2D point : points) {
            double dist = point.distanceSquaredTo(p);
            if (dist < minDist) {
                minDist = dist;
                minPoint = point;
            }
        }
        return minPoint;
    }

    private void checkNull(Object arg) {
        if (arg == null) throw new IllegalArgumentException();
    }

//    // unit testing of the methods (optional)
//    public static void main(String[] args) {
//
//    }
}