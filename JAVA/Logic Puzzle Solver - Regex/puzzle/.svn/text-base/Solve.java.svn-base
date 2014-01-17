package puzzle;

import java.io.File;
import java.io.FileReader;
import java.io.Reader;
import java.io.FileNotFoundException;

/** The Puzzle Solver.
 * @author Peterson Cheng
 */
public class Solve {

    /** Solve the puzzle given in ARGS[0], if given.  Otherwise, print
     *  a help message. */
    public static void main(String[] args) {
        if (args.length == 0) {
            usage();
            return;
        }

        if (args.length > 1) {
            System.err.println("Error: too many arguments");
            usage();
            System.exit(1);
        }

        Triple.reset();
        Parser.reset();
        NamedEntity.reset();

        File inputFileName = new File(args[0]);
        Reader input;

        try {
            input = new FileReader(inputFileName);
        } catch (FileNotFoundException e) {
            System.err.printf("Error: file %s not found", inputFileName);
            System.exit(1);
            return;
        }

        try {
            Parser puzzle = Parser.parse(input);
            Solver solution = new Solver();
            puzzle.inform();
            puzzle.solve(solution);
            for (int i = 0; i < puzzle.numAssertions(); i += 1) {
                System.out.printf("%d. %s\n", i + 1, puzzle.getAssertion(i));
            }
            System.out.println();
            if (solution.impossible()) {
                System.out.println("That's impossible.");
            } else {
                for (int i = 0; i < puzzle.numQuestions(); i += 1) {
                    System.out.printf("Q: %s\n", puzzle.getQuestion(i));
                    System.out.printf("A: %s\n", puzzle.getAnswer(i,
                                                              solution));
                }
            }
        } catch (PuzzleException e) {
            System.err.printf("error:\n %s\n", e.toString());
            System.exit(1);
        }
        System.exit(0);
    }

    /** Print usage message. */
    private static void usage() {
        System.out.println("java puzzle.Solve FILE");
    }
}

