/**
 * @author : Joey Huang
 * @since : 12/14/21, Tue
 **/

import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdDraw;
import edu.princeton.cs.algs4.StdOut;

import java.util.ArrayList;
import java.util.Arrays;

public class FastCollinearPoints {
    private final ArrayList<LineSegment> lineSegments;
    private int numSegments;

    public FastCollinearPoints(Point[] points) {
        if (points == null) throw new IllegalArgumentException();
        Point[] pts = Arrays.copyOf(points, points.length);
        this.checkInput(pts);
        this.lineSegments = new ArrayList<LineSegment>();
        this.numSegments = 0;
        Arrays.sort(pts);
        Point[] newPoints = Arrays.copyOf(pts, pts.length);
        for (Point point : pts) {
            Arrays.sort(newPoints, point.slopeOrder());
//            StdOut.println();
//            StdOut.println(point);
//            for (Point p: newPoints){
//                StdOut.println(p + ": " + point.slopeTo(p));
//            }
            if (newPoints.length < 2) return;
            double prevSlope = point.slopeTo(newPoints[1]);
            int contPoints = 1;
            for (int j = 2; j < pts.length; j++) {
                double currentSlope = point.slopeTo(newPoints[j]);
                if (currentSlope == prevSlope) {
                    contPoints++;
                    if (j != pts.length - 1) continue;
                }
                if (contPoints >= 3) {
                    Point[] seg = new Point[contPoints + 1];
                    seg[0] = point;
                    int l = (currentSlope == prevSlope && j == pts.length - 1) ? 1 : 0;
                    System.arraycopy(newPoints, j - contPoints + l, seg, 1, contPoints);
                    Arrays.sort(seg);
//                    for (Point p : seg) StdOut.println(p);
                    if (point == seg[0]) {
                        Point max = seg[seg.length - 1];
                        this.lineSegments.add(new LineSegment(point, max));
                        this.numSegments++;
                    }
                }
                prevSlope = currentSlope;
                contPoints = 1;
            }
        }
    }

    public static void main(String[] args) {

        // read the n points from a file
        In in = new In(args[0]);
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

    private void checkInput(Point[] points) {
        for (Point point : points) {
            if (point == null) throw new IllegalArgumentException();
        }
        Arrays.sort(points);
        for (int i = 0; i < points.length - 1; i++) {
            if (points[i].compareTo(points[i + 1]) == 0) {
                throw new IllegalArgumentException();
            }
        }
    }

    public int numberOfSegments() {
        return this.numSegments;
    }

    public LineSegment[] segments() {
        return this.lineSegments.toArray(new LineSegment[this.numSegments]);
    }
}
