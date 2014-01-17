package enigma;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.IOException;

/** Enigma simulator.
 *  @author Peterson Cheng
 */
public final class Main {

    /** Process a sequence of encryptions and decryptions, as
     *  specified in the input from the standard input.  Print the
     *  results on the standard output. Exits normally if there are
     *  no errors in the input; otherwise with code 1. */
    public static void main(String[] unused) {
        Machine M = new Machine();
        BufferedReader input =
            new BufferedReader(new InputStreamReader(System.in));
        boolean isConfig = false;

        try {
            while (true) {
                String line = input.readLine();
                if (line == null) {
                    break;
                }
                if (isConfigurationLine(line)) {
                    configure(M, line);
                    isConfig = true;
                } else {
                    if (!isConfig) {
                        throw new IOException(
                               "Machine has not been configured.");
                    }
                    printMessageLine(M.convert(standardize(line)));
                }
            }
        } catch (IOException excp) {
            System.err.printf("Input error: %s%n", excp.getMessage());
            System.exit(1);
        }
    }

    /** Return true iff LINE is an Enigma configuration line. */
    static boolean isConfigurationLine(String line) {
        String[] lineSplit = line.split(" ");
        if (lineSplit.length != 6) {
            return false;
        }
        boolean noduplicates = noDup(lineSplit[2], lineSplit[3],
                                    lineSplit[4]);
        boolean[] conditions = {lineSplit[0].equals("*"),
                                isReflector(lineSplit[1]),
                                isRotor(lineSplit[2]),
                                isRotor(lineSplit[3]),
                                isRotor(lineSplit[4]),
                                isSetting(lineSplit[5]),
                                noduplicates};
        for (boolean istrue: conditions) {
            if (!istrue) {
                return istrue;
            }
        }
        return true;
    }

    /** Returns true if there are no duplicates in ARGS. */
    static boolean noDup(String ... args) {
        String a = args[0];
        String b = args[1];
        String c = args[2];
        boolean dup = (a.equals(b)
                       || b.equals(c) || c.equals(a));
        return !dup;
    }

    /** Returns true iff NAME is a valid reflector. */
    static boolean isReflector(String name) {
        String[] reflectorNames = {"B", "C"};
        for (String reflector: reflectorNames) {
            if (reflector.equals(name)) {
                return true;
            }
        }
        return false;
    }

    /** Returns true iff NAME is a valid rotor. */
    static boolean isRotor(String name) {
        String[] rotorNames = {"I", "II", "III", "IV", "V",
                               "VI", "VII", "VIII"};
        for (String rotor : rotorNames) {
            if (rotor.equals(name)) {
                return true;
            }
        }
        return false;
    }

    /** Returns true iff WORD is a valid setting. */
    static boolean isSetting(String word) {
        if (word.length() != 4) {
            return false;
        }
        for (int i = 0; i < 4; i++) {
            char position = word.charAt(i);
            if (!Character.isLetter(position)) {
                return false;
            }
        }
        return true;
    }

    /** Configure M according to the specification given on CONFIG,
     *  which must have the configuration specified in the
     *  assignment. */
    static void configure(Machine M, String config) {
        String[] c = config.split(" ");
        Reflector rf = new Reflector(c[1]);
        Rotor r1 = new Rotor(c[2]);
        Rotor r2 = new Rotor(c[3]);
        Rotor r3 = new Rotor(c[4]);
        M.setRotors(rf, r1, r2, r3);
        M.setPositions(c[5]);

    }

    /** Return the result of converting LINE to all upper case,
     *  removing all blanks.  It is an error if LINE contains
     *  characters other than letters and blanks. */
    static String standardize(String line) throws IOException {
        String[] lineSplit = line.toUpperCase().split(" ");
        String result = "";
        for (String words : lineSplit) {
            result += words;
        }
        for (int i = 0; i < result.length(); i++) {
            char position = result.charAt(i);
            if (!Character.isLetter(position)) {
                throw new IOException("Only letters and blanks.");
            }
        }
        return result;

    }

    /** Print MSG in groups of five (except that the last group may
     *  have fewer letters). */
    static void printMessageLine(String msg) {
        int i = 0;
        String five = "";
        while (i < msg.length()) {
            five += String.valueOf(msg.charAt(i));
            i += 1;
            if (i % 5 == 0) {
                System.out.print(five + " ");
                five = "";
            }
        }
        System.out.print(five + " ");
        System.out.println("");

    }
}
