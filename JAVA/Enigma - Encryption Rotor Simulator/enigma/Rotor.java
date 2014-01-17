package enigma;
import java.util.Arrays;
import java.io.File;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

/** Class that represents a rotor in the enigma machine.
 *  @author Peterson Cheng
 */
class Rotor {

    /**Constructs a Rotor with ID (I,II,III,etc). */
    public Rotor(String id) {
        _id = id;
        config();
    }

    /**Set the blank Rotor instance into the
     * correct Rotor (I, II, III) etc. */
    public void config() {
        try {
            File config = new File("enigma/config.txt");
            FileReader c = new FileReader(config);
            BufferedReader b = new BufferedReader(c);
            String line;
            while ((line = b.readLine()) != null) {
                String[] ls = line.split(" ");
                if (ls[0].equals(_id)) {
                    setConfig(slice(ls, 1, ls.length));
                }
            }
        } catch (IOException excp) {
            System.err.printf("Config error: %s%n", excp.getMessage());
            System.exit(1);
        }
    }

    /** Returns the slice from N to K of array A. */
    static String[] slice(String[] a, int n, int k) {
        return (String[]) Arrays.copyOfRange(a, n, k);
    }

    /** Assuming that P is an integer in the range 0..25, returns the
     *  corresponding upper-case letter in the range A..Z. */
    static char toLetter(int p) {
        return CONV.charAt(p);
    }

    /** Assuming that C is an upper-case letter in the range A-Z, return the
     *  corresponding index in the range 0..25. Inverse of toLetter. */
    static int toIndex(char c) {
        return CONV.indexOf(c);
    }

    /** Return my current rotational position as an integer between 0
     *  and 25 (corresponding to letters 'A' to 'Z').  */
    int getPosition() {
        return position;
    }

    /** Set getPosition() to POSN. Ensures a position from 0...25. */
    void setPosition(int posn) {
        position = posn % LENAZ;
    }

    /** Sets the NOTCH Positions to the ints in the array. */
    void setNotch(int[] notch) {
        for (int i : notch) {
            if (i > (LENAZ - 1) || i < 0) {
                return;
            }
        }
        notchPos = notch;
    }

    /** Loads config WORDS into the substitution map submap[]. */
    void setConfig(String[] words) {
        boolean notchConfig = false;
        for (String word : words) {
            if (notchConfig) {
                int[] result = new int[word.length()];
                for (int i = 0; i < word.length(); i++) {
                    result[i] = toIndex(word.charAt(i));
                }
                notchPos = result;
            } else if (word.equals("NOTCH")) {
                notchConfig = true;
            } else {
                update(word);
            }
        }
    }

    /** Given the position P of a letter on the previous Rotor,
      * returns the position on this rotor the substitution of
      * the letter at position P on this rotor. */
    int convertForward(int p) {
        int stdPos = (p + position) % LENAZ;
        int subPos = sub(toLetter(stdPos));
        int realPos = (subPos + LENAZ - position) % LENAZ;
        return realPos;
    }

    /** Given the position E of a letter on the next Rotor,
     *  returns the position on this rotor the substitution of
     *  the letter at position E on this rotor. */
    int convertBackward(int e) {
        int stdPos = (e + position) % LENAZ;
        int unsubPos = unsub(toLetter(stdPos));
        int realPos = (unsubPos + LENAZ - position) % LENAZ;
        return realPos;
    }

    /** Given a cylic WORD 'ABCDEA' for example, updates the
     *  substitution map such that A maps to B ... E to A. */
    void update(String word) {
        for (int i = 0; i < word.length() - 1; i++) {
            char c = word.charAt(i);
            char d = word.charAt(i + 1);
            subMap[toIndex(c)] = toIndex(d);
        }
    }

    /** Returns true iff I am positioned to allow the rotor to my left
     *  to advance. */
    boolean atNotch() {
        for (int i : notchPos) {
            if (position == i) {
                return true;
            }
        }
        return false;
    }

    /** Advance me one position. */
    void advance() {
        setPosition(getPosition() + 1);
    }

    /** Given the standard position of a LETTER, returns the standard
     *  position of its substituted result. */
    int sub(char letter) {
        return subMap[toIndex(letter)];
    }

    /** Given the standard position of a LETTER on the next rotor,
     *  returns the standard position of its unsubstituted result.*/
    int unsub(char letter) {
        int indexLetter = toIndex(letter);
        for (int i = 0; i < subMap.length; i++) {
            if (subMap[i] == indexLetter) {
                return i;
            }
        }
        throw new IllegalArgumentException("unsub couldn't find the letter.");
    }

    /** The internal substitution cipher map of the Rotor. */
    private int[] subMap = new int[LENAZ];

    /** My current position (index 0..25, with 0 indicating that 'A'
     *  is showing). */
    private int position = 0;

    /** A string to allow conversion of letter
     *  to position and vice versa. */
    public static final String CONV = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";

    /** Indicates the notch position of the Rotor. */
    private int[] notchPos;

    /** Identifies the Rotor as I, II, etc. . */
    private String _id;

    /** Length of the alphabet. */
    private static final int LENAZ = 26;

}
