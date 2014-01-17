/* Do NOT modify the contents of this file.  It defines an API that we
 * will assume exists. */

package util;

import java.util.Iterator;

/** A set of objects of type POINT in 2D space, retrievable by position.
 *  It is legal to have two objects at the same location.  Changing
 *  the coordinates of a Point while it is in a Set2D is erroneous and
 *  has undefined effects.
 *  @author P. N. Hilfinger
 */
public abstract class Set2D<Point> implements Iterable<Point> {

    /** An empty Set2D whose getView() value is VIEW. */
    public Set2D(PointView<Point> view) {
        _view = view;
    }

    /** Returns the view that defines the locations of my Points. */
    public final PointView<Point> getView() {
        return _view;
    }

    /** Returns the current number of objects in the set. */
    public abstract int size();

    /** Add P to me.  Has no effect if P is already in me (i.e., if
     *  an object, q, for which getView().equals(P, q) is already in me). */
    public abstract void add(Point p);

    /** Remove P from me, if it is currently a member according to getView()
     *  .equals(), and otherwise do nothing. */
    public abstract void remove(Point p);

    /** Add all Points in SRC to me. */
    public void addAll(Set2D<Point> src) {
        for (Point p : src) {
            add(p);
        }
    }

    /** Return true iff I contain an object q such that
     *  getView().equals(P, q). */
    public abstract boolean contains(Point p);

    /** Returns an Iterator that returns all objects in THIS.  The
     *  resulting Iterator need not support the .remove operation.  It is
     *  an error to remove or insert additional Points into THIS during use of
     *  the resulting Iterator. */
    public abstract Iterator<Point> iterator();

    /** Returns an Iterator that returns all objects in THIS whose coordinates
     *  lie on or within a rectangle whose lower-left coordinate is
     *  (XL, YL) and whose upper-right coordinate is (XU, YU).  This
     *  Iterator need not support the remove operation.  It is an error
     *  to remove or insert additional Points into THIS during use of
     *  the resulting Iterator. */
    public abstract Iterator<Point> iterator(double xl, double yl,
                                             double xu, double yu);

    /* Utility methods */

    /** Return the x-coordinate of P. */
    protected double x(Point p) {
        return _view.getX(p);
    }

    /** Return the y-coordinate of P. */
    protected double y(Point p) {
        return _view.getY(p);
    }

    /** Return the quadrant that (X,Y) is in, relative to (X0, Y0), as
     *  an integer in 0..4.  Quadrant 0 means that (X,Y) == (X0, Y0),
     *  1 means (X, Y) is northeast of (X0, Y0)---i.e., X >= X0, Y >= Y0;
     *  2 means (X, Y) is northwest of (X0, Y0)--- X < X0, Y >= Y0;
     *  3 means (X, Y) is southwest of (X0, Y0)--- X < X0, Y < Y0;
     *  4 means (X, Y) is southeast of (X0, Y0)--- X >= X0, Y < Y0;
     */
    public static int quadrant(double x, double y,  double x0, double y0) {
        if (x == x0 && y == y0) {
            return 0;
        } else if (x >= x0) {
            return y >= y0 ? 1 : 4;
        } else {
            return y >= y0 ? 2 : 3;
        }
    }

    /** Return true iff (X,Y) is within the bounding box (LLX,LLY)
     *  to (URX, URY). */
    public static boolean isWithin(double x, double y,
                                   double llx, double lly,
                                   double urx, double ury) {
        return x >= llx && x < urx && y >= lly && y < ury;
    }

    /** The View that defines the coordinates of my Points. */
    private final PointView<Point> _view;

}

