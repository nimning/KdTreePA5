/*************************************************************************
 * Compilation javac KdTree.java                                         *
 * Execution java KdTree                                                 * 
 * Dependencies: Point2D.java StdDraw.java StdRandom.java                *
 *                                                                       *
 * 2d-tree implementation of a set representing a set of points in the   *
 * unit square                                                           *
 *************************************************************************/

import java.util.TreeSet;


/** KdTree impementation 
 * @author Ning Ma
 */

public class KdTree {

    private static class Node {
        private Point2D p; //the point
        //the axis-aligned rectangle corresponding to this node
        private RectHV rect; 
        // the left/bottom subtree
        private Node lb;
        //the right/top subtree
        private Node rt;
        //orientation of the node
        private boolean isVertical;
        //number of points in this node
        private int numPoint;

        public Node() {
            numPoint = 0;
        }

        public Node(Point2D point) {
            this.p = point;
            numPoint = 1;
        }

        public Node(Point2D point, RectHV rect, boolean isVertical) {
            this.p = point;
            this.rect = rect;
            this.isVertical = isVertical;
            numPoint = 1;
        }
        
        /** draw the node
         */
        public void draw() {
            StdDraw.setPenRadius(0.015);
            StdDraw.setPenColor(StdDraw.BLACK);
            p.draw();
            
            StdDraw.setPenRadius(0.005);
            if (isVertical) {
                StdDraw.setPenColor(StdDraw.RED);
                StdDraw.line(p.x(), rect.ymin(), p.x(), rect.ymax());
            }
            else {
                StdDraw.setPenColor(StdDraw.BLUE);
                StdDraw.line(rect.xmin(), p.y(), rect.xmax(), p.y());
            }
        }
        
        /** get the rectangle associated to point p given parent node n
         * @param p the point to be inserted
         */
        public RectHV getRect(Point2D point) {
            if (isVertical) {
                if (Point2D.X_ORDER.compare(point, this.p) < 0) {
                    return new RectHV(this.rect.xmin(), this.rect.ymin(),
                            this.p.x(), this.rect.ymax());
                }
                else {
                    return new RectHV(this.p.x(), this.rect.ymin(), 
                            this.rect.xmax(), this.rect.ymax());
                }
            }
            else {
                if (Point2D.Y_ORDER.compare(point, this.p) < 0) {
                    return new RectHV(this.rect.xmin(), this.rect.ymin(),
                            this.rect.xmax(), this.p.y());
                }
                else {
                    return new RectHV(this.rect.xmin(), this.p.y(),
                            this.rect.xmax(), this.rect.ymax());
                }
            }
        }
    }

    /*
    private enum SplitOrient {
        VERTICAL, HORIZONTAL;

        public SplitOrient nextOrient() {
            if (this.equals(SplitOrient.VERTICAL)) {
                return SplitOrient.HORIZONTAL;
            } 
            else {
                return SplitOrient.VERTICAL;
            }
        }
    }
    */

    private Node root;

    /** Constructor
    */
    public KdTree() {
        root = null;
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
        return size(root); 
    }
    
    
    /**
     * helper method of size()
     * number of node in kdtree rooted at node 'root'
     * @param root the root of current kdtree
     */
    private int size(Node subRoot) {
        if (subRoot == null) {
            return 0;
        }

        return subRoot.numPoint;
    }

    /** insert a Point2D into the set
     * @param p the point to be inserted into the set
     * @throws NullPointerException If the argument is null
     */
    public void insert(Point2D p) {
        root = insert(root, p, null);    
    }


    /** helper method for insert
     * @param n the node at which a new point will be inserted
     * @param p the point to be inserted
     * @param orient the split orientation of Node n
     * @return the node after insertion
     */
    private Node insert(Node n, Point2D p, Node parent) {

        if (n == null) {
            if (parent == null) {
                return new Node(p, new RectHV(0.0, 0.0, 1.0, 1.0), true);
            }
            else {
                return new Node(p, parent.getRect(p), !parent.isVertical);
            }
        }

        if (n.p.equals(p)) {
            return n;
        }

        if (n.isVertical) {
            if (Point2D.X_ORDER.compare(p, n.p) < 0) {
                n.lb = insert(n.lb, p, n);
            } else {
                n.rt = insert(n.rt, p, n);
            }
        }
        else {
            if (Point2D.Y_ORDER.compare(p, n.p) < 0) {
                n.lb = insert(n.lb, p, n);
            } else {
                n.rt = insert(n.rt, p, n);
            }
        }

        n.numPoint = 1 + size(n.lb) + size(n.rt);

        return n;
    }

    /** check whether the set contains the point
     * @param p the point to be checked
     * @throws NullPointerException if the argument is null
     */
    public boolean contains(Point2D p) {
        return contains(root, p, true);

    }


    /** helper method for contains
     * @param
     * @return
     */
    private boolean contains(Node n, Point2D p, boolean isVertical) {
        if (n == null) {
            return false;
        }
        if (n.p.equals(p)) {
            return true;
        }

        if (n.isVertical) {
            if (Point2D.X_ORDER.compare(p, n.p) < 0) {
                return contains(n.lb, p, !n.isVertical);
            }
            else {
                return contains(n.rt, p, !n.isVertical);
            }
        }
        else {
            if (Point2D.Y_ORDER.compare(p, n.p) < 0) {
                return contains(n.lb, p, !n.isVertical);
            }
            else {
                return contains(n.rt, p, !n.isVertical);
            }
        }
    }

    /** Draw all points in the set to standard draw
    */
    public void draw() {
        draw(root);        
    }

    /** helper method of draw
     * @param root the current root of the tree
     */

    private void draw(Node subRoot) {
        if (subRoot == null) {
            return;
        }

        subRoot.draw();
        draw(subRoot.lb);
        draw(subRoot.rt);
    }

    /** get all points that are inside the rectangle
     * @param rect the Rectangle to be investigated
     * @return a set of points contained in query rectangle
     * @throws NullPointerException if rect is null
     */
    public Iterable<Point2D> range(RectHV rect) {
        if (rect == null) {
            throw new NullPointerException("range has null rect");
        }

        TreeSet<Point2D> mySet = new TreeSet<Point2D>();

        range(root, rect, mySet);
        return mySet;
    }
    
    /** helper method for range
     * @param n the root 
     * @param rect the query rectangle
     * @param mySet the set of Point2D in the query rectangel
     */
    private void range(Node n, RectHV rect, TreeSet<Point2D> mySet) {
        if (n == null) {
            return;
        }

        if (!n.rect.intersects(rect)) {
            return;
        }

        if (rect.contains(n.p)) {
            mySet.add(n.p);
        }

        range(n.lb, rect, mySet);
        range(n.rt, rect, mySet);
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

        return nearest(root, p, root.p);
    }

    /** helper method of nearest
     * @param n the starting node of the kdTree
     * @param p the query point
     * @param closestP the nearest neighbor of the query point
     */
    private Point2D nearest(Node n, Point2D p, Point2D closestP) {
        if (n == null) {
            return closestP;
        }
        
        if (n.rect.distanceTo(p) >= p.distanceTo(closestP)) {
            return closestP;
        }

        if ((n.p).distanceTo(p) < p.distanceTo(closestP)) {
            closestP = n.p;
        }

        //System.out.println("current closest point is: " + closestP.toString());

        Point2D subClosestP; 
        if (n.lb != null && isSameSide(p, n.lb, n)) {
            subClosestP = nearest(n.lb, p, closestP);
            closestP = nearest(n.rt, p, subClosestP);
        }
        else {
            subClosestP = nearest(n.rt, p, closestP);
            closestP = nearest(n.lb, p, subClosestP);
        }
        
        return closestP;

    }

    /** test whether the child node and the query point is on the same
     * side of the parent node
     * @param p the query point
     * @param n the current node
     * @param parent the parent node
     */
    private boolean isSameSide(Point2D p, Node n, Node parent) {
        if (parent.isVertical) {
            return Point2D.X_ORDER.compare(p, parent.p) 
                == Point2D.X_ORDER.compare(n.p, parent.p);
        }
        else {
            return Point2D.Y_ORDER.compare(p, parent.p)
                == Point2D.Y_ORDER.compare(n.p, parent.p);
        }
    }

    /** 
     * Unit test the point data type
     */
    public static void main(String[] args) {
        int N = Integer.parseInt(args[0]);

        StdDraw.setCanvasSize();

        StdDraw.setXscale();
        StdDraw.setYscale();

        KdTree mySet = new KdTree();

        for (int i = 1; i <= N; i++) {
            double x = ((double) StdRandom.uniform(100))/100.0;
            double y = ((double) StdRandom.uniform(100))/100.0;

            Point2D point = new Point2D(x, y);
            //System.out.println("Inserted point is: " + point.toString());
            mySet.insert(point);
        }
        //Draw all the points
        mySet.draw();

        //draw the reference point 
        StdDraw.setPenRadius(0.015);
        Point2D p = new Point2D(0.5, 0.5);
        StdDraw.setPenColor(StdDraw.PINK);
        p.draw();

        
        //draw the nearest neighbor
        System.out.println("The size of the set is " + mySet.size());
        System.out.println("The p point is " + p.toString());
        Point2D nearestP = mySet.nearest(p);
        System.out.println("The closest point to p is " + nearestP.toString());
        StdDraw.setPenColor(StdDraw.PINK);
        nearestP.draw();

        //draw point within a range
        RectHV myRect = new RectHV(0.1, 0.1, 0.4, 0.4);
        StdDraw.setPenColor(StdDraw.GREEN);
        StdDraw.setPenRadius(0.005);
        StdDraw.rectangle(0.25, 0.25, 0.15, 0.15);

        StdDraw.setPenRadius(0.015);
        Iterable<Point2D> rangeSet = mySet.range(myRect);
        for (Point2D point : rangeSet) {
            point.draw();
            System.out.println("Point inside range is " + point.toString());
        }
    }
}
