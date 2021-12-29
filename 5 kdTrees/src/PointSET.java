import edu.princeton.cs.algs4.Point2D;
import edu.princeton.cs.algs4.RectHV;

import java.util.ArrayList;
import java.util.TreeSet;

/**
 * @author : Joey Huang
 * @since : 12/20/21, Mon
 **/

public class PointSET {
    private TreeSet<Point2D> pointSet;

    private void checkNull(Object o){
        if (o == null) throw new IllegalArgumentException();
    }

    // construct an empty set of points
    public PointSET() {
        pointSet = new TreeSet<>();
    }

    // unit testing of the methods (optional)
    public static void main(String[] args) {

    }

    // is the set empty?
    public boolean isEmpty() {
        return this.pointSet.isEmpty();
    }

    // number of points in the set
    public int size() {
        return this.pointSet.size();
    }

    // add the point to the set (if it is not already in the set)
    public void insert(Point2D p) {
        checkNull(p);
        this.pointSet.add(p);
    }

    // does the set contain point p?
    public boolean contains(Point2D p) {
        checkNull(p);
        return this.pointSet.contains(p);
    }

    // draw all points to standard draw
    public void draw() {
        for (Point2D p: this.pointSet){
            p.draw();
        }
    }

    // all points that are inside the rectangle (or on the boundary)
    public Iterable<Point2D> range(RectHV rect) {
        checkNull(rect);
        if (this.isEmpty()) return null;
        ArrayList<Point2D> insideBorder = new ArrayList<>();
        for (Point2D p: this.pointSet){
            if (rect.contains(p)) insideBorder.add(p);
        }
        return insideBorder;
    }

    // a nearest neighbor in the set to point p; null if the set is empty
    public Point2D nearest(Point2D p) {
        checkNull(p);
        if (this.isEmpty()) return null;
        Point2D nearestNeighbor = this.pointSet.first();
        double smallestDistance = p.distanceSquaredTo(nearestNeighbor);
        for (Point2D point: this.pointSet){
            if (p.distanceSquaredTo(point) < smallestDistance){
                nearestNeighbor = point;
                smallestDistance = p.distanceSquaredTo(point);
            }
        }
        return nearestNeighbor;
    }
}
