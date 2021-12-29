/**
 * @author : Joey Huang
 * @since : 12/14/21, Tue
 **/

import edu.princeton.cs.algs4.StdOut;

import java.util.Arrays;

public class BruteCollinearPoints {
    private final LineSegment[] lineSegments;
    private int numSegments;

    public BruteCollinearPoints(Point[] points) {
        this.checkInput(points);
        this.lineSegments = new LineSegment[points.length];
        this.numSegments = 0;
        for (int i = 0; i < points.length - 3; i++) {
            for (int j = i + 1; j < points.length - 2; j++) {
                for (int k = j + 1; k < points.length - 1; k++) {
                    for (int l = k + 1; l < points.length; l++) {
                        double firstSlope = points[i].slopeTo(points[j]);
                        if (points[i].slopeTo(points[k]) == firstSlope && points[i].slopeTo(points[l]) == firstSlope) {
                            Point[] array = new Point[]{points[i], points[j], points[k], points[l]};
                            Arrays.sort(array);
                            Point min = array[0];
                            Point max = array[3];
                            this.lineSegments[numSegments++] = new LineSegment(min, max);
                        }
                    }
                }
            }
        }
    }

    private void checkInput(Point[] points) {
        if (points == null) throw new IllegalArgumentException();
        for (Point point : points) {
            if (point == null) throw new IllegalArgumentException();
        }
        Point[] newPoints = Arrays.copyOf(points, points.length);
        Arrays.sort(newPoints);
        for (int i = 0; i < newPoints.length - 1; i++) {
            if (newPoints[i].compareTo(newPoints[i + 1]) == 0) {
                throw new IllegalArgumentException();
            }
        }
    }

    public int numberOfSegments() {
        return this.numSegments;
    }

    public LineSegment[] segments() {
        return Arrays.copyOfRange(this.lineSegments, 0, this.numSegments);
    }
}
