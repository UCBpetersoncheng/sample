package tracker;

import java.util.ArrayList;

import util.Reporter;

/** Represents the observational posts.
 *  @author Peterson Cheng
 */

public class Post {

    /** Constructs a Post #N at (X,Y) that reports vehicle
        sightings. */
    public Post(double x, double y, int n) {
        _x = x;
        _y = y;
        _num = n;
    }

    /** Returns the X of this post. */
    public double x() {
        return _x;
    }

    /** Returns the Y of this post. */
    public double y() {
        return _y;
    }

    /** Returns true if this post is within D of (X,Y). */
    public boolean near(double d, double x, double y) {
        Reporter.debug(6, "(%f,%f) dist (%f, %f)",
                       x, y, _x, _y);
        double dist = Math.sqrt(Math.pow(_x - x, 2)
                                + Math.pow(_y - y, 2));
        Reporter.debug(6, "%f <= %f\t" + (dist <= d), dist, d);
        return dist < d;
    }

    /** Returns the number of this post. */
    public int id() {
        return _num;
    }

    /** Returns the next post. */
    public Post next() {
        return _next;
    }

    /** Links this Post to the next post P. */
    public void link(Post p) {
        _next = p;
    }

    /** Returns the String list of Observations. */
    public ArrayList<String> getObs() {
        return _obs;
    }

    /** Used by vehicle to alert the post of its position
        (X,Y) and TIME it was seen. */
    public void alert(double x, double y, double time) {
        Reporter.debug(4, "Post #%d SPOTTED! (%.1f, %.1f)@%.1f",
                       _num, _x, _y, time);
        if (time != 0) {
            _obs.add(String.format("(%.1f, %.1f)@%.1f", x, y, time));
        }
    }

    /** Prints out the sightings of this post up to MAX.*/
    public void report(double max) {
        if (_obs.size() != 0) {
            System.out.printf("   Post #%d at (%.1f, %.1f):\n",
                              _num, _x, _y);
            for (String s : _obs) {
                System.out.println("      " + s);
            }
        }
    }


    /** X-position of this post. */
    private final double _x;

    /** Y-position of this post. */
    private final double _y;

    /** The number of this post. */
    private final int _num;

    /** Holds the observations. */
    private ArrayList<String> _obs = new ArrayList<String>();

    /** The next post. */
    private Post _next = null;
}
