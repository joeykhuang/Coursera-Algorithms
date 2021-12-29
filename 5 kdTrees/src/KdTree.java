import edu.princeton.cs.algs4.Point2D;
import edu.princeton.cs.algs4.RectHV;
import edu.princeton.cs.algs4.StdDraw;
import edu.princeton.cs.algs4.StdOut;

import java.util.ArrayList;

/**
 * @author : Joey Huang
 * @since : 12/20/21, Mon
 **/

public class KdTree {

    private KdTreeNode root;

    // construct an empty set of points
    public KdTree() {
        root = null;
    }

    // unit testing of the methods (optional)
    public static void main(String[] args) {
        KdTree tree = new KdTree();
        tree.insert(new Point2D(0.7, 0.2));
        tree.insert(new Point2D(0.5, 0.4));
        tree.insert(new Point2D(0.2, 0.3));
        tree.insert(new Point2D(0.4, 0.7));
        tree.insert(new Point2D(0.9, 0.6));
        StdOut.println(tree.nearest(new Point2D(0.793, 0.509)));
    }

    private void checkNull(Object o) {
        if (o == null) throw new IllegalArgumentException();
    }

    private KdTreeNode sink(KdTreeNode root, Point2D p, int level, boolean orientation, RectHV prevRect, double prev) {
        if (root == null) {
            root = new KdTreeNode();
            root.point = p;
            if (level == 0) {
                root.rect = new RectHV(0, 0, 1, 1);
            } else {
                double minX = prevRect.xmin();
                double maxX = prevRect.xmax();
                double minY = prevRect.ymin();
                double maxY = prevRect.ymax();
                if (orientation && level % 2 != 0) { // left
                    maxX = prev;
                } else if (orientation && level % 2 == 0) { // bottom
                    maxY = prev;
                } else if (!orientation && level % 2 != 0) { // right
                    minX = prev;
                } else { // top
                    minY = prev;
                }
                root.rect = new RectHV(minX, minY, maxX, maxY);
            }
            return root;
        }
        root.size++;
        boolean comparison = level % 2 == 0 ? p.x() < root.point.x() : p.y() < root.point.y();
        double curr = level % 2 == 0 ? root.point.x() : root.point.y();
        if (comparison) {
            root.leftSubtree = sink(root.leftSubtree, p, level + 1, true, root.rect, curr);
        } else {
            root.rightSubtree = sink(root.rightSubtree, p, level + 1, false, root.rect, curr);
        }
        return root;
    }

    private boolean find(KdTreeNode root, Point2D p, int level) {
        if (root == null || root.point == null) return false;
        if (root.point.compareTo(p) == 0) {
            return true;
        }
        boolean comparison = level % 2 == 0 ? p.x() < root.point.x() : p.y() < root.point.y();
        if (comparison) {
            return find(root.leftSubtree, p, level + 1);
        } else {
            return find(root.rightSubtree, p, level + 1);
        }
    }

    private void drawNode(KdTreeNode root, int level){
        if (root == null) return;
        root.point.draw();
        if (level % 2 == 0) StdDraw.setPenColor(StdDraw.RED);
        else StdDraw.setPenColor(StdDraw.BLUE);
        root.rect.draw();
        drawNode(root.leftSubtree, level + 1);
        drawNode(root.rightSubtree, level + 1);
    }

    private void queryRange(KdTreeNode root, ArrayList<Point2D> pts, RectHV rect) {
        if (root == null) return;
        if (rect.intersects(root.rect)) {
            if (rect.contains(root.point)) pts.add(root.point);
            queryRange(root.leftSubtree, pts, rect);
            queryRange(root.rightSubtree, pts, rect);
        }
    }

    private Point2D closestSearch(KdTreeNode root, Point2D best, Point2D p, int level){
        if (root == null) return best;
        if (root.point.equals(p)) return p;
        if (root.rect.distanceSquaredTo(p) > best.distanceSquaredTo(p)) return best;
        if (root.point.distanceSquaredTo(p) < best.distanceSquaredTo(p)) best = root.point;

        double dist = level % 2 == 0? p.x() - root.point.x() : p.y() - root.point.y();
        if (dist < 0){
           best = closestSearch(root.leftSubtree, best, p, level + 1);
           if (best.distanceSquaredTo(p) > dist * dist) best = closestSearch(root.rightSubtree, best, p, level + 1);
        } else {
            best = closestSearch(root.rightSubtree, best, p, level + 1);
            if (best.distanceSquaredTo(p) > dist * dist) best = closestSearch(root.leftSubtree, best, p, level + 1);
        }
        return best;
    }

    // is the set empty?
    public boolean isEmpty() {
        return root == null;
    }

    // number of points in the set
    public int size() {
        return root == null ? 0 : root.size;
    }

    // add the point to the set (if it is not already in the set)
    public void insert(Point2D p) {
        checkNull(p);
        if (!contains(p)) {
            this.root = this.sink(this.root, p, 0, false, null, 0);
        }
    }

    // does the set contain point p?
    public boolean contains(Point2D p) {
        checkNull(p);
        return find(this.root, p, 0);
    }

    // draw all points to standard draw
    public void draw() {
        StdDraw.setPenRadius(0.05);
        drawNode(root, 0);
    }

    // all points that are inside the rectangle (or on the boundary)
    public Iterable<Point2D> range(RectHV rect) {
        checkNull(rect);
        ArrayList<Point2D> pts = new ArrayList<>();
        queryRange(this.root, pts, rect);
        return pts;
    }

    // a nearest neighbor in the set to point p; null if the set is empty
    public Point2D nearest(Point2D p) {
        checkNull(p);
        if (this.isEmpty()) return null;
        return closestSearch(this.root, this.root.point, p, 0);
    }

    private static class KdTreeNode {
        Point2D point = null;
        int size = 1;
        RectHV rect = null;
        KdTreeNode leftSubtree = null;
        KdTreeNode rightSubtree = null;
    }
}
