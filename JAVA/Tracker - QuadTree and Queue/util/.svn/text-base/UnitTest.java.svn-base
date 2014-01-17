package util;

import ucb.junit.textui;

/** The suite of all JUnit tests for the util package
 *  @author Peterson Cheng
 */
public class UnitTest {

    /** A utility class that allows us to treat 2-element arrays of double
     *  as points. */
    static class ArrayPointView implements PointView<double[]> {
        @Override
        public double getX(double[] p) {
            return p[0];
        }

        @Override
        public double getY(double[] p) {
            return p[1];
        }

        @Override
        public boolean equals(double[] p1, double[] p2) {
            return p1[0] == p2[0] && p1[1] == p2[1];
        }

    }

     /** A utility class that allows us to treat 2-element arrays of double
     *  as points. */
    static class OtherPointView implements PointView<double[]> {
        @Override
        public double getX(double[] p) {
            return p[0];
        }

        @Override
        public double getY(double[] p) {
            return p[1];
        }

        @Override
        public boolean equals(double[] p1, double[] p2) {
            return (p1[0] == p2[0] && p1[1] == p2[1]
                    && p1[2] == p2[2]);
        }

    }

    /** A standard ArrayPointView. */
    static final ArrayPointView VIEW = new ArrayPointView();

    /** A standard OtherPointView. */
    static final OtherPointView OVIEW = new OtherPointView();

    /** Returns a pair {x, y}. */
    static double[] pnt(double x, double y) {
        return new double[] {x, y};
    }

    /** Returns a pair {x, y} with extra parameter z. */
    static double[] opnt(double x, double y, double z) {
        return new double[] {x, y, z};
    }

    /** A few test points. */
    static final double[][] PTS = {
        { 0.0, 0.0 },
        { 1.0, 0.0 },
        { 0.0, 1.0 },
        { 1.0, 1.0 },
        { -1.0, 0.0 },
        { 0.0, -1.0 },
        { -1.0, 0.0 },
        { -1.0, 1.0 },
        { 1.0, -1.0 }
    };

    /** A few test points. */
    static final double[][] OPTS = {
        {0.0, 0.0, 1.0},
        {0.0, 0.0, 2.0},
        {0.0, 0.0, 3.0},
        {0.0, 0.0, 4.0},
    };

    /** Run the JUnit tests in the puzzle package. */
    public static void main(String[] ignored) {
        textui.runClasses(QuadTreeTest.class);
    }

}


