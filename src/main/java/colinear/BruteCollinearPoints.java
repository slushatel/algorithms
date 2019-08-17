package colinear;

import java.util.Arrays;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;

public class BruteCollinearPoints {
	//	private final Point[] points;
	private final int numberOfSegments;
	private final List<LineSegment> segments;

	// finds all line segments containing 4 points
	public BruteCollinearPoints(Point[] points) {
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
		for (int i = 0; i < pointsArr.length; i++) {
			Point p1 = pointsArr[i];
			for (int j = i + 1; j < pointsArr.length; j++) {
				Point p2 = pointsArr[j];
				for (int k = j + 1; k < pointsArr.length; k++) {
					Point p3 = pointsArr[k];
					for (int l = k + 1; l < pointsArr.length; l++) {
						Point p4 = pointsArr[l];
						Comparator<Point> comp = p1.slopeOrder();
						if (comp.compare(p2, p3) == 0 && comp.compare(p2, p4) == 0) {
							Point[] cp = {p1, p2, p3, p4};
							Arrays.sort(cp);
							if (p1.compareTo(cp[0]) == 0)
								segments.add(new LineSegment(cp[0], cp[cp.length - 1]));
						}
					}
				}
			}
		}

		this.numberOfSegments = segments.size();
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
			BruteCollinearPoints cp = new BruteCollinearPoints(points);
			System.out.println(cp.numberOfSegments);
			System.out.println(Arrays.toString(cp.segments()));
		}
		{
			Point[] points = {new Point(10000, 0), new Point(0, 10000)
					, new Point(3000, 7000), new Point(7000, 3000)
					, new Point(20000, 21000), new Point(3000, 4000), new Point(14000, 15000)
					, new Point(6000, 7000)
			};
			BruteCollinearPoints cp = new BruteCollinearPoints(points);
			System.out.println(cp.numberOfSegments);
			System.out.println(Arrays.toString(cp.segments()));
		}
	}
}