package tracker;

import org.junit.Test;
import static org.junit.Assert.*;

import static util.UnitTest.*;

/** Tests of SimpleSet2D class.
 *  @author Peterson Cheng */
public class MainTest {

    /** Test isDebug method. */
    @Test
    public void testisDebug() {
        String[] inp = {"--debug=0",
                        "--debug=1",
                        "--debug=-5",
                        "--debug=10"};
        String[] err = {"-debug=0",
                        "--debug=",
                        "--debug=0n",
                        "tests/tracker.trk"};
        for (String s: inp) {
            assertTrue("isDebug() did not correctly report true",
                       Main.isDebug(s));
        }
        for (String s: err) {
            assertFalse("isDebug() did not correctly report false",
                        Main.isDebug(s));
        }
    }

    /** Tests findN method. */
    @Test
    public void testfindN() {
        String[] inp = {"--debug=-1",
                        "--debug=0",
                        "--debug=1",
                        "--debug=2"};
        for (int i = -1; i < 3; i++) {
            assertEquals("findN() not reporting correct answer",
                         Main.findN(inp[i + 1]), i);
        }
    }
}
