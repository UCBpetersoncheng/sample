// Remove all comments that begin with //, and replace appropriately.
// Feel free to modify ANYTHING in this file.

package loa;

import ucb.util.CommandArgs;
import static loa.Side.*;
import java.util.Random;
import java.lang.IllegalArgumentException;

/** Main class of the Lines of Action program.
 * @author Peterson Cheng
 */
public class Main {

    /** The main Lines of Action.  ARGS are as described in the
     *  project 3 handout:
     *      [ --white ] [ --ai=N ] [ --seed=S ] [ --time=LIM ] \
     *      [ --debug=D ] [ --display ]
     */
    public static void main(String... args) {
        CommandArgs options =
            new CommandArgs("--white{0,1} --ai=(\\d+){0,1} --seed=(\\d+){0,1} "
                          + "--time=(\\d+[.]?\\d*){0,1} --debug=(\\d+){0,1} "
                          + "--display{0,1}", args);
        if (!options.ok()) {
            usage();
        }

        try {
            runGame(options);
        } catch (IllegalArgumentException e) {
            System.err.println("Arg error - " + e.getMessage());
            usage();
            System.exit(1);
        }


    }

    /** Creates and runs the game. */
    static void runGame(CommandArgs options) {
        Random r = new Random();
        boolean display = false;
        Side side0 = BLACK;
        long seed = r.nextLong();
        int ai, debug;
        ai = 1;
        debug = 0;
        double time = 0.0;

        if (options.containsKey("--white")) {
            side0 = WHITE;
        }

        if (options.containsKey("--ai")) {
            ai = options.getInt("--ai");
            if (ai > 2 || ai < 0) {
                throw new IllegalArgumentException("#AIs not {0,1,2}");
            }
        }

        if (options.containsKey("--seed")) {
            seed = options.getLong("--seed");
        }

        if (options.containsKey("--time")) {
            time = options.getDouble("--time");
        }

        if (options.containsKey("--debug")) {
            debug = options.getInt("--debug");
            Reporter.setMessageLevel(debug);
        }

        if (options.containsKey("--display")) {
            display = true;
        }

        Game gameSim = new Game(2 - ai, side0, seed, (long) (time * 60000),
                                useTime);
        gameSim.play();
    }


    /** Print brief description of the command-line format. */
    static void usage() {
        System.err.println("Usage:");
        System.err.println("java loa.Main [--white] [--ai=N] " 
                      + "[--seed=S] [--time=LIM] [--debug=D] "
                      + "[--display]");
        System.exit(1);
    }
}
