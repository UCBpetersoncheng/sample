package puzzle;

import ucb.junit.textui;

/** The suite of all JUnit tests for the Puzzle Solver.
 *  @author Peterson Cheng
 */
public class UnitTest {

    /** Run the JUnit tests in the puzzle package. */
    public static void main(String[] ignored) {
        textui.runClasses(puzzle.ParseTest.class);
        textui.runClasses(puzzle.TripleTest.class);
    }

}


