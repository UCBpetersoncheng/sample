package tracker;

import java.util.PriorityQueue;
import util.QuadTree;
import java.util.Iterator;
import util.Reporter;

/** Represents a vehicle.
 *  @author Peterson Cheng
 */

public class Vehicle {

    /** Constructs a vehicle with initial y-position Y0, and an
     *  initial velocity VX0, that gets sensed by posts that are
     *  a distance D away. It continues to simulate until MAX.
     *  It generates deterministically random directions by
     *  SEED, stores sightings in POSTS at TIMES.*/
    public Vehicle(double y0, double vx0, double d,
                   double max, double seed,
                   QuadTree<Post> posts,
                   PriorityQueue<Double> times) {
        _x = 0;
        _y = y0;
        _vx = vx0;
        _vy = 0;
        _d = d;
        _max = max;
        _posts = posts;
        _times = times;
        _currentTime = 0;
        _seed = seed;
        _choose = new Chooser(seed);
    }

    /** Moves this car accordingly to posts and initial
     *  conditions until _max. */
    public void simulate() {

        Reporter.debug(3, "VEHICLE CREATED: (%f, %f, %f, %f, %f)\n",
                       _x, _vx, _d, _max, _seed);

        double delta = 0;
        double check = 0;
        Iterator<Post> postIter;
        Post item;
        double e = 0 + _d;
        if (_posts.size() == 0) {
            _x = _vx * _max;
            return;
        }

        while (check < _max && (_times.peek() != null)) {
            check = _times.poll();
            delta = check - _currentTime;
            Reporter.debug(3, "%f:\t(%f, %f)", check, _x, _y);
            _x += _vx * delta;
            _y += _vy * delta;
            Reporter.debug(3, "\t\t(%f, %f)", _x, _y);
            Reporter.debug(3, "\t\t(%f, %f)\n", _vx, _vy);
            postIter = _posts.iterator(_x - e, _y - e,
                                 _x + e, _y + e);
            Reporter.debug(7, "(%.1f, %.1f)\t(%.1f, %.1f)",
                           _x - _d, _y - _d, _x + _d, _y + _d);
            if (_choose.choose(_x, _y,
                               _vx, _vy) == -1) {
                rotate(QUARTER);
            } else {
                rotate(-QUARTER);
            }
            _currentTime = check;
            while (postIter.hasNext()) {
                item = postIter.next();
                Reporter.debug(7, "\t(%.1f, %.1f)", item.x(),
                               item.y());
                if (item.near(_d, _x, _y)) {
                    item.alert(_x, _y, check);
                }

            }
        }
        delta = _max - _currentTime;
        _x += _vx * delta;
        _y += _vy * delta;
    }

    /** Returns the String of the final position.*/
    public String finalPos() {
        return String.format("(%.1f, %.1f)@%.1f", _x, _y, _max);
    }

    /** Prints out the simulation parameters. Only to be called
     *  before running the simulation. */
    public void printParams() {
        System.out.println("Simulation parameters:");
        System.out.printf("   Total simulated time: %.1f\n", _max);
        System.out.printf("   Random seed: %.0f\n", _seed);
        System.out.printf("   Maximum detection radius: %.1f\n", _d);
        System.out.printf("   Initial vertical location: %.1f\n", _y);
        System.out.printf("   Initial horizontal"
                          + " velocity of vehicle: %.1f\n", _vx);
    }

    /** Returns the rotation of a vectory (X,Y) by THETA. */
    public static double[] rotate(double x, double y, double theta) {
        double sint = Math.sin(theta);
        double cost = Math.cos(theta);
        double newx = (x * cost) - (y * sint);
        double newy = (x * sint) + (y * cost);
        double[] rotated = {newx, newy};
        Reporter.debug(4, "_vx: %.1f\t_vy: %.1f", newx, newy);
        return rotated;
    }

    /** Rotates the velocity vector of this car by THETA degrees. */
    private void rotate(double theta) {
        double[] rotated = rotate(_vx, _vy, theta);
        double newx = rotated[0];
        double newy = rotated[1];
        Reporter.debug(5, "_vx: %.1f\t_vy:%.1f", newx, newy);
        _vx = newx;
        _vy = newy;
    }

    /** The current x position. */
    private double _x;

    /** The current y position. */
    private double _y;

    /** The current x-component of velocity. */
    private double _vx;

    /** The current y-component of velocity. */
    private double _vy;

    /** The distance that senses posts. */
    private double _d;

    /** The maximum time of simulation. */
    private double _max;

    /** The seed used to generate random-ness. */
    private double _seed;

    /** The queue of times that this vehicle updates. */
    private PriorityQueue<Double> _times;

    /** The QuadTrees of Posts. */
    private QuadTree<Post> _posts;

    /** The current time of the simulation. */
    private double _currentTime = 0;

    /** The random chooser that picks directions. */
    private static Chooser _choose;

    /** The pi/4 angle. */
    private static final double QUARTER = Math.PI / 4;
}
