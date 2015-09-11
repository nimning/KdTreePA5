/***********************
 * Compilation javac PointSET.java
 * Execution PointSET a a a
 * Dependencies: Point2D.java StdDraw.java StdRandom.java
 *
 * Brute-force implementation of a set representing a set of points in the
 * unit square
 */

import java.util.TreeSet;


/** Brute-force impementation 
 * @author Ning Ma
 */

public class PointSET {

    private TreeSet<Point2D> root;

    /** Constructor
    */
    public PointSET() {
        root = new TreeSet<Point2D>();
    }

    /** Test whether the set is
     * empty
     */
    public boolean isEmpty() {
        return size() == 0;
    }

    /** Get the size of the set
    */
    public int size() {
        return root.size();
    }

    /** insert a Point2D into the set
     * @param p the point to be inserted into the set
     * @throws NullPointerException If the argument is null
     */
    public void insert(Point2D p) {
        if (p == null) {
            throw new NullPointerException("inserted point is null");
        }

        root.add(p);
    }

    /** check whether the set contains the point
     * @param p the point to be checked
     * @throws NullPointerException if the argument is null
     */
    public boolean contains(Point2D p) {
        if (p == null) {
            throw new NullPointerException("null can not be contained");
        }

        return root.contains(p);
    }

    /** Draw all points in the set to standard draw
    */
    public void draw() {
        for (Point2D p : root) {
            p.draw();
        }
    }

    /** get all points that are inside the rectangle
     * @param rect the Rectangle to be investigated
     * @throws NullPointerException if rect is null
     */
    public Iterable<Point2D> range(RectHV rect) {
        if (rect == null) {
            throw new NullPointerException("range has null rect");
        }

        TreeSet<Point2D> mySet = new TreeSet<Point2D>();

        for (Point2D p : root) {
            if (rect.contains(p)) {
                mySet.add(p);
            }
        }

        return mySet;
    }

    /** a nearest neighbor in the set to point p; return 
     * null if the set is empty
     * @param p point
     * @return the nearest point to p in the set. Return null if
     * the set is empty
     * @throws NullPointerException if p is null
     */
    public Point2D nearest(Point2D p) {
        if (p == null) {
            throw new NullPointerException("the reference point in nearest is null");
        }

        if (isEmpty()) {
            return null;
        }

        Point2D closestP = null;
        for (Point2D point : root) {
            if (closestP == null) {
                closestP = point;
            }

            if (point.distanceTo(p) < closestP.distanceTo(p)) {
                closestP = point;
            }
        }

        return closestP;

    }

    /** 
     * Unit test the point data type
     */
    public static void main(String[] args) {
        int N = Integer.parseInt(args[0]);

        StdDraw.setCanvasSize();

        StdDraw.setXscale();
        StdDraw.setYscale();

        PointSET mySet = new PointSET();

        for (int i = 1; i <= N; i++) {
            double x = ((double) StdRandom.uniform(100))/100.0;
            double y = ((double) StdRandom.uniform(100))/100.0;

            Point2D point = new Point2D(x, y);
            mySet.insert(point);
        }
        //Draw all the points
        StdDraw.setPenRadius(0.02);
        mySet.draw();

        //draw the reference point 
        Point2D p = new Point2D(0.5, 0.5);
        StdDraw.setPenColor(StdDraw.RED);
        p.draw();

        //draw the nearest neighbor
        System.out.println("The size of the set is " + mySet.size());
        System.out.println("The p point is " + p.toString());
        System.out.println("The closest point to p is " + mySet.nearest(p).toString());
        StdDraw.setPenColor(StdDraw.PINK);
        mySet.nearest(p).draw();

        //draw point within a range
        RectHV myRect = new RectHV(0.1, 0.1, 0.4, 0.4);
        StdDraw.setPenColor(StdDraw.GREEN);
        StdDraw.setPenRadius(0.005);
        StdDraw.rectangle(0.25, 0.25, 0.15, 0.15);

        StdDraw.setPenRadius(0.02);
        Iterable<Point2D> rangeSet = mySet.range(myRect);
        for (Point2D point : rangeSet) {
            point.draw();
            System.out.println("Point inside range is " + point.toString());
        }
    }
}
