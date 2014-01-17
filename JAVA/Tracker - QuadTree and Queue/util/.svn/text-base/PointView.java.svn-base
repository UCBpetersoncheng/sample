/* Do NOT modify the contents of this file.  It defines an API that we
 * will assume exists. */

package util;

/** An object that permits looking at POINTs as objects with coordinates in
 *  2-space.  extracts x and y coordinates from other objects of type
 *  POINT.
 *  @author P. N. Hilfinger
 */
public interface PointView<Point> {

    /** Return the x coordinate of OBJ. */
    double getX(Point obj);

    /** Return the y coordinate of OBJ. */
    double getY(Point obj);

    /** Return true iff P1 is equivalent to P2.  It is assumed that
     *  equals(P1,P2) implies that getX(P1) == getX(P2) and
     *  getY(P1) == getY(P2), but not the converse.  */
    boolean equals(Point p1, Point p2);
}

