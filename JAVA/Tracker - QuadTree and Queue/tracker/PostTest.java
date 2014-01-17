package tracker;

import org.junit.Test;
import static org.junit.Assert.*;

import static util.UnitTest.*;

/** Tests of SimpleSet2D class.
 *  @author Peterson Cheng */
public class PostTest {

    /** Test the accessor methods for private fields. */
    @Test
    public void testAccess() {
        Post p = new Post(1.0, 1.0, 5);
        assertEquals("x() access error", 1.0, p.x());
        assertEquals("y() access error", 1.0, p.y());
        assertEquals("id()access error", 5, p.id());
    }

    /** Tests near() method. */
    @Test
    public void testNear() {
        Post p = new Post(0, 0, 0);
        assertTrue("near() reported false for dist < d",
                   p.near(5, 4, 0));
        assertTrue("near() reported false for dist < d",
                   p.near(5, 0, 4));
        assertTrue("near() reported false for dist < d",
                   p.near(5, -4, 0));
        assertTrue("near() reported false for dist < d",
                   p.near(5, 0, -4));

        assertTrue("near() reported false for dist = d",
                   p.near(5, 5.0, 0));
        assertTrue("near() reported false for dist = d",
                   p.near(5, 0, 5.0));
        assertTrue("near() reported false for dist = d",
                   p.near(5, -5.0, 0));
        assertTrue("near() reported false for dist = d",
                   p.near(5, 0, -5.0));

        assertFalse("near() reported false for dist > d",
                   p.near(5, 6.0, 0));
        assertFalse("near() reported false for dist > d",
                   p.near(5, 0, 6.0));
        assertFalse("near() reported false for dist > d",
                   p.near(5, -6.0, 0));
        assertFalse("near() reported false for dist > d",
                   p.near(5, 0, -6.0));
    }

    /** Tests the next() and link() method. */
    @Test
    public void testNextLink() {
        Post sent = new Post(0, 0, 0);
        Post[] p = new Post[9];
        for (int i = 0; i < 9; i++) {
            p[i] = new Post(0, 0, i + 1);
        }

        Post q = sent;
        for (int i = 0; i < 9; i++) {
            q.link(p[i]);
            q = p[i];
        }

        q = sent;
        while (q.next() != null) {
            assertTrue("Link or Next failure()", q.id() < q.next().id());
            q = q.next();
        }
    }

    /** Tests the alert and report methods. */
    @Test
    public void testAlert() {
        Post p = new Post(0, 0, 0);
        p.alert(0, 1, 1);
        p.alert(1, 0, 2);
        p.alert(-1, 0, 3);
        p.alert(0, -1, 4);
        p.alert(0, 0, 0);

        assertEquals("Wrong size after multiple insertions (1 invalid)",
                     p.getObs().size(), 4);
    }
}
