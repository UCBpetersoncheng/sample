package puzzle;

/** An unchecked exception that Represents any kind of user error in the
 *  input of a puzzle.
 *  @author P. N. Hilfinger
 */

class PuzzleException extends RuntimeException {

    /** A PuzzleException with no message. */
    PuzzleException() {
    }

    /** A PuzzleException for which .getMessage() is MSG. */
    PuzzleException(String msg) {
        super(msg);
    }

}
