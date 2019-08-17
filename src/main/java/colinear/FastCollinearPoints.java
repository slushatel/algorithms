package colinear;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

public class FastCollinearPoints {
	private final int numberOfSegments;
	private final List<LineSegment> segments;

	// finds all line segments containing 4 points
	public FastCollinearPoints(Point[] points) {
		Point[] pointsArr = Arrays.copyOf(points, points.length);
		if (pointsArr == null) throw new IllegalArgumentException();
		for (Point p : pointsArr) {
			if (p == null) throw new IllegalArgumentException();
		}
		Arrays.sort(pointsArr);
		for (int i = 0; i < pointsArr.length - 1; i++) {
			if (pointsArr[i].compareTo(pointsArr[i + 1]) == 0) throw new IllegalArgumentException();
		}

		segments = new LinkedList<>();
		for (int i = 0; i < points.length; i++) {
			Point p1 = points[i];

			Arrays.sort(pointsArr, p1.slopeOrder());
			double currentSlope = p1.slopeTo(pointsArr[0]);
			int numCollinearPoints = 0;
			for (int j = 0; j < pointsArr.length; j++) {
				Point p2 = pointsArr[j];
				if (p2.compareTo(p1) == 0) continue;

				double slope = p1.slopeTo(p2);
				if (slope == currentSlope) {
					numCollinearPoints++;
				} else {
					addSegment(p1, numCollinearPoints, segments, pointsArr, j);
					currentSlope = slope;
					numCollinearPoints = 1;
				}
			}
			addSegment(p1, numCollinearPoints, segments, pointsArr, pointsArr.length);
		}
		this.numberOfSegments = segments.size();
	}

	private void addSegment(Point basePoint, int numCollinearPoints, List<LineSegment> segments, Point[] points, int currentIndex) {
		if (numCollinearPoints >= 3) {
			List<Point> collinearPoints = new LinkedList<>();
			collinearPoints.add(basePoint);
			for (int i = currentIndex - numCollinearPoints; i < currentIndex; i++) {
				collinearPoints.add(points[i]);
			}
			Point[] cp = collinearPoints.toArray(new Point[collinearPoints.size()]);
			Arrays.sort(cp);
			if (basePoint.compareTo(cp[0]) == 0)
				segments.add(new LineSegment(cp[0], cp[cp.length - 1]));
		}
	}

	// the number of line segments
	public int numberOfSegments() {
		return numberOfSegments;
	}

	// the line segments
	public LineSegment[] segments() {
		return segments.toArray(new LineSegment[segments.size()]);
	}

	public static void main(String[] args) {
		{
			Point[] points = {new Point(0, 100), new Point(10, 90), new Point(20, 80), new Point(30, 70), new Point(100, 100)};
			FastCollinearPoints cp = new FastCollinearPoints(points);
			System.out.println(cp.numberOfSegments);
			System.out.println(Arrays.toString(cp.segments()));
		}
		{
			Point[] points = {new Point(10000, 0), new Point(0, 10000)
					, new Point(3000, 7000), new Point(7000, 3000)
					, new Point(20000, 21000), new Point(3000, 4000), new Point(14000, 15000)
					, new Point(6000, 7000)
			};
			FastCollinearPoints cp = new FastCollinearPoints(points);
			System.out.println(cp.numberOfSegments);
			System.out.println(Arrays.toString(cp.segments()));
		}
	}
}