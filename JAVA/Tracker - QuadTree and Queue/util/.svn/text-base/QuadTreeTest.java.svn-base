package util;

import org.junit.Test;
import static org.junit.Assert.*;

import static util.UnitTest.*;
import java.util.Iterator;

/** Tests of SimpleSet2D class.
 *  @author Peterson Cheng */
public class QuadTreeTest {

    /** Test size. */
    @Test
    public void testSize() {
        QuadTree<double[]> S = new QuadTree<double[]>(VIEW, 2);
        assertEquals("Bad initial size", 0, S.size());
        double[] p = pnt(0.0, 0.0);
        S.add(PTS[0]);
        assertEquals("Bad non-empty size", 1, S.size());
        S.remove(PTS[0]);
        assertEquals("Bad size after removal", 0, S.size());
        for (double[] q : PTS) {
            S.add(q);
        }
        assertEquals("Bad size after multiple insertions (1 duplicate)",
                     PTS.length - 1, S.size());

        assertEquals("transitionSize changed", 2, S.getTransitionSize());
    }

    /** Tests the uniqueness of add and remove. And not just
     *  getX and getY. Also tests iterators.*/
    @Test
    public void testUnique() {
        double[] p = opnt(0.0, 0.0, 1.0);
        QuadTree<double[]> S = new QuadTree<double[]>(OVIEW, 2);
        for (double[] q: OPTS) {
            S.add(q);
        }
        S.add(p);
        assertEquals("Bad size after multiple insertions (1 duplicate)",
                     OPTS.length, S.size());
        S.remove(p);
        assertEquals("Bad size after removal", OPTS.length - 1, S.size());
        assertFalse("Check that contain uses getView and not getX == getY",
                     S.contains(p));
        Iterator<double[]> sIter = S.iterator();
        int length = 0;
        while (sIter.hasNext()) {
            boolean test = false;
            double[] q0 = sIter.next();
            length += 1;
            for (double[] q : OPTS) {
                if (OVIEW.equals(q, q0)) {
                    test = true;
                }
            }
            assertTrue("Does not contain the correct item", test);
        }
        assertEquals("Iterator does not contain all elements",
                     OPTS.length - 1, length);
        double[][] temp = {
            {5.0, 0.0, 1.0},
            {0.0, 5.0, 1.0},
            {-5.0, 0.0, 1.0},
            {0.0, -5.0, 1.0},
        };
        for (double[] q: temp) {
            S.add(q);
        }
        sIter = S.iterator(-5.0, -5.0, 5.0, 5.0);
        length = 0;
        while (sIter.hasNext()) {
            boolean test = false;
            double[] q0 = sIter.next();
            length += 1;
            for (double [] q: OPTS) {
                if (OVIEW.equals(q, q0)) {
                    test = true;
                }
            }
            for (double[] q: temp) {
                if (OVIEW.equals(q, q0)) {
                    test = true;
                }
            }
            assertTrue("Does not contain the correct item", test);
        }
        assertEquals("Iterator does not contain correct elements",
                     OPTS.length + 1, length);
    }

}
