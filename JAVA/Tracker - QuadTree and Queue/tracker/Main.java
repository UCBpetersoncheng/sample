package tracker;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.PrintStream;
import java.util.Scanner;
import java.util.ArrayList;

import java.util.NoSuchElementException;
import util.QuadTree;
import util.Reporter;
import util.PointView;
import java.util.PriorityQueue;

/** Main class of the tracker program.
 * @author Peterson Cheng
 */
public class Main {

    /** The main tracker program.  ARGS are as described in the
     *  project 2 handout:
     *      [--debug=N] [ INPUTFILE [ OUTPUTFILE ] ]
     */
    public static void main(String... args) {
        String inputFileName, outputFileName;
        inputFileName = outputFileName = null;
        int debug = 0;
        if (args.length == 0) {
            debug = 0;
        } else if (args.length == 1) {
            if (isDebug(args[0])) {
                debug = findN(args[0]);
            } else {
                inputFileName = args[0];
            }
        } else if (args.length == 2) {
            if (isDebug(args[0])) {
                debug = findN(args[0]);
                inputFileName = args[1];
            } else {
                inputFileName = args[0];
                outputFileName = args[1];
            }
        } else if (args.length == 3) {
            if (isDebug(args[0])) {
                debug = findN(args[0]);
                inputFileName = args[1];
                outputFileName = args[2];
            } else {
                usage();
            }
        } else {
            usage();
        }
        if (inputFileName != null) {
            try {
                System.setIn(new FileInputStream(inputFileName));
            } catch (FileNotFoundException e) {
                System.err.printf("Error: could not open %s%n", inputFileName);
                System.exit(1);
            }
        }
        if (outputFileName != null) {
            try {
                System.setOut(new PrintStream(outputFileName));
            } catch (FileNotFoundException e) {
                System.err.printf("Error: could not open %s%n",
                                   outputFileName);
                System.exit(1);
            }
        }
        Reporter.setMessageLevel(debug);
        runSimulation();
        System.out.close();
        System.exit(0);
    }


    /** Print brief description of the command-line format. */
    static void usage() {
        System.err.print("Error - input not in format: ");
        System.err.print("'java tracker.main [--debug=N]"
                           + " [INPUTFILE [OUTPUTFILE]]'\n");
        System.exit(1);
    }

    /** Returns true if S is a correct debug arg. */
    static boolean isDebug(String s) {
        return s.matches("--debug=-?\\d+\\z");
    }

    /** Returns N, given S is a valid debug statment
     *  --debug=N. */
    static int findN(String s) {
        String valid = "--debug=";
        return Integer.parseInt(s.substring(valid.length(),
                                            s.length()));
    }

    /** Parses the input, returning an arraylist of double. */
    static ArrayList<Double> parseInput() {
        Scanner s = new Scanner(System.in);
        ArrayList<Double> d = new ArrayList<Double>();
        while (s.hasNext()) {
            String item = s.next();
            if (item.contains("#")) {
                s.nextLine();
            } else {
                d.add(Double.parseDouble(item));
            }
        }
        return d;
    }

    /** Runs the simulation. */
    static void runSimulation() {
        Scanner s = new Scanner(System.in);
        try {
            ArrayList<Double> d = parseInput();
            if (d.size() < 5) {
                throw new NoSuchElementException("");
            }
            double tMax = d.get(0);
            double seed = d.get(1);
            double dist = d.get(2);
            double y0 = d.get(3);
            double vx = d.get(4);
            validArgs(tMax, seed, dist, y0, vx);
            Reporter.debug(1, "\ntMax:%f\t"
                           + "seed:%f\t"
                           + "dist:%f\t"
                           + "y0:%f\t"
                           + "vx:%f\n", tMax, seed, dist, y0, vx);
            QuadTree<Post> q = parsePosts(d, tMax);
            Vehicle v = new Vehicle(y0, vx, dist, tMax, seed,
                                    q, _times);
            v.printParams();
            System.out.println("");
            v.simulate();
            printReports(tMax);
            System.out.println("");
            System.out.println("Final position: " + v.finalPos());
        } catch (NoSuchElementException e) {
            System.err.print("Error - Not enough args supplied"
                             + " to simulate tracker\n");
            System.exit(1);
        } catch (NumberFormatException e) {
            System.err.print("Error - incorrectly formatted "
                             + "numerical arguments\n");
            System.out.close();
            System.exit(1);
        }
    }

    /** Checks arguments TMAX, SEED, DIST, Y0, AND VX to ensure
        that they are valid. */
    static void validArgs(double tMax, double seed, double dist,
                          double y0, double vx) {
        if (tMax < 0 || dist < 0 || vx < 0) {
            throw new NumberFormatException();
        }
    }

    /** Returns an QuadTree of Post Objects, derived from DLIST
     *  Will error if there are an erroneous number of inputs or if the
     *  Inputs themselves are faulty. Also builds a list of TIMES
     *  up to MAX*/
    static QuadTree<Post> parsePosts(ArrayList<Double> dList,
                                     double max) {
        double x = 0;
        double y = 0;
        double t = 0;
        QuadTree<Post> result = new QuadTree<Post>(PVIEW, 4);
        Post prev = _sentinel;
        int count = 0;
        int mod3 = 0;
        for (int i = 5; i < dList.size(); i++) {
            double d = dList.get(i);
            switch(mod3) {
            case 0:
                x = d;
                break;
            case 1:
                y = d;
                break;
            case 2:
                t = d;
                if (t < 0) {
                    throw new NumberFormatException();
                }
                Post p = new Post(x, y, 1 + count / 3);
                result.add(p);
                prev.link(p);
                prev = p;
                Reporter.debug(2, "POST %d ADDED: (%.1f,%.1f)@%.1f",
                               count / 3, x, y, t);
                if (t != 0) {
                    for (double time = t; time <= max; time += t) {
                        _times.add(time);
                    }
                }
                break;
            default:
                break;
            }
            count += 1;
            mod3 = count % 3;
        }
        if (mod3 != 0) {
            Reporter.debug(2, "\tIncomplete post");
            throw new NoSuchElementException();
        }
        return result;
    }

    /** Prints the reports of Posts in order, undefined behavior
     *  if called before simulation. Will not post reports
     *  that occur after MAX.*/
    public static void printReports(double max) {
        System.out.println("Reports:");
        Post p = _sentinel;
        while (p != null) {
            p.report(max);
            p = p.next();
        }
    }

    /** Returns a PointView that interprets Points. */
    private static class PostView implements PointView<Post> {

        @Override
        public double getX(Post p) {
            return p.x();
        }

        @Override
        public double getY(Post p) {
            return p.y();
        }

        @Override
        public boolean equals(Post p, Post q) {
            return ((p.x() == q.x()) && (p.y() == q.y())
                    && (p.id() == p.id()));
        }
    }

    /** Is the Post View. */
    private static final PostView PVIEW = new PostView();

    /** Is the PriorityQueue of Times. */
    private static PriorityQueue<Double> _times =
        new PriorityQueue<Double>();

    /** Holds the first post. Does not actually function
     *  like a post. */
    private static Post _sentinel = new Post(0, 0, 0);

}
