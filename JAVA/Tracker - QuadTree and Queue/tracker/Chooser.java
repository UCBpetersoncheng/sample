package tracker;

/* Do not modify this file.  We might swap in a different one for
 * grading purposes. */

import java.util.Random;

/** A standard source of pseudo-random bits.  In the project, each decision
 *  made by the vehicle's driver to turn left or right consults an oracle of
 *  this type, passing in the vehicle's current position and velocity
 *  to the .choose() method.
 *  @author P. N. Hilfinger
 */
class Chooser {
    /** A source of random numbers. */
    private Random gen;

    /** Treat velocity components whose magnitude is < SMALL * the maximum
     *  of the magnitudes of vx and vy as 0. */
    private static final double SMALL = 1.0e-6;

    /** A new Chooser, seeded with SEED.  Identical SEEDS lead to identical
     *  results.  The fractional part of SEED is ignored. */
    public Chooser(double seed) {
        gen = new Random((long) seed);
    }

    /** Returns the value -1 or 1 (for left turn or right turn,
     *  respectively) choosing pseudo-randomly (that is, deterministically:
     *  sequences of calls to .choose() on two different Choosers.
     *  constructed with the same seed produce identical results.)  (X, Y)
     *  is the vehicle's current position and (VX, VY) is its current
     *  velocity. */
    int choose(double x, double y, double vx, double vy) {
        double thresh = Math.max(Math.abs(vx), Math.abs(vy)) * SMALL;
        int vxs = vx < -thresh ? -1 : vx > thresh ? 1 : 0;
        int vys = vy < -thresh ? -1 : vy > thresh ? 1 : 0;
        if (vxs <= 0) {
            return vys;
        } else if (vys == 0) {
            return gen.nextBoolean() ? -1 : 1;
        } else {
            return gen.nextInt(3) == 2 ? -vys : vys;
        }
    }
}
