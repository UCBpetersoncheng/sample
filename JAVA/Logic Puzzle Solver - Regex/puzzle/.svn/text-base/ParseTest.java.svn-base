package puzzle;

import org.junit.Test;
import static org.junit.Assert.*;

import java.io.StringReader;

/** Tests of Parser class.
 *  @author Peterson Cheng*/
public class ParseTest {

    private StringReader reader;

    /** Set reader to contain the contents of LINES. */
    private void setUp(String... lines) {
        StringBuilder str = new StringBuilder();

        for (String line : lines) {
            str.append(line);
            str.append("\n");
        }
        reader = new StringReader(str.toString());
    }

    /** Test help message ('java solve' with no arguments). */
    @Test
    public void testEcho() {
        setUp("John is   the carpenter.   "
              + "Paul does not live in the yellow house.   ",
              "  Who is   the\tcarpenter?");

        Parser p = Parser.parse(reader);

        assertEquals("bad assertion count", 2, p.numAssertions());
        assertEquals("bad question count", 1, p.numQuestions());
        assertEquals("Fact #0", "John is the carpenter.",  p.getAssertion(0));
        assertEquals("Fact #1", "Paul does not live in the yellow house.",
                     p.getAssertion(1));
        assertEquals("Q #0", "Who is the carpenter?", p.getQuestion(0));
    }
}


