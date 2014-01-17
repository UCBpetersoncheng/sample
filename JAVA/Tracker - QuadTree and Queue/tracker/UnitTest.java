package tracker;

import org.junit.Test;
import static org.junit.Assert.*;
import ucb.junit.textui;

/** Central dispatching point for all testing.
 *  @author Peterson Cheng*/
public class UnitTest {

    /** Run the JUnit tests in the tracker package. */
    public static void main(String[] ignored) {
        textui.runClasses(tracker.UnitTest.class);
    }

    @Test public void dummy() {
    }
}
