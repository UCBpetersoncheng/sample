package loa;
import java.util.HashMap;

/** A move in Lines of Action.
 *  @author Peterson Cheng */
class Move {

    /* Implementation note: We create moves by means of static "factory
     * methods" all named create, which in turn use the single (private)
     * constructor.  Factory methods have certain advantages over constructors:
     * they allow you to produce results having an arbitrary subtype of Move,
     * and they don't require that you produce a new object each time.  This
     * second advantage is useful when you are trying to speed up the creation
     * of Moves for use in automated searching for moves.  You can (if you
     * want) create just one instance of the Move representing 1-5, for example
     * and return it whenever that move is requested. */

    /** Return a move of the piece at COLUMN0, ROW0 to COLUMN1, ROW1. */
    static Move create(int column0, int row0, int column1, int row1) {
        String s = toString(column0, row0, column1, row1);
        if (containsKey(s)) {
            return _moves.get(s);
        } else {
            return new Move(column0, row0, column1, row1);
        }
    }

    /** Assuming S is a properly formatted string of form c0r0-c1r1,
     *  returns the move created by the string.*/
    static Move create(String s) {
        String from = s.substring(0, 2);
        String to = s.substring(3, 5);
        return create(Board.col(from), Board.row(from), 
                      Board.col(to), Board.row(to));
    }

    /** A new Move of the piece at COL0, ROW0 to COL1, ROW1. */
    private Move(int col0, int row0, int col1, int row1) {
        _col0 = col0;
        _row0 = row0;
        _col1 = col1;
        _row1 = row1;
        _cvec = col1 - col0;
        _rvec = row1 - row0;
        _moves.put(this.toString(), this);
    }

    /** Return the column at which this move starts, as an index in 1--8. */
    int getCol0() {
        return _col0;
    }

    /** Return the row at which this move starts, as an index in 1--8. */
    int getRow0() {
        return _row0;
    }

    /** Return the column at which this move ends, as an index in 1--8. */
    int getCol1() {
        return _col1;
    }

    /** Return the row at which this move ends, as an index in 1--8. */
    int getRow1() {
        return _row1;
    }

    /** Returns true if an int I is outside of bounds. */
    boolean out(int i) {
        return (i > 8) || (i < 1);
    }

    /** Returns true if the move from C0 R0 - C1 R1 is cardinal or 
     *  intercardinal (Multiple of 45 degrees). */
    public boolean valid() {
        if (out(_col0) || out(_row0) || out(_col1) || out(_row1)) {
            return false;
        }
        if (_cvec == 0 || _rvec == 0) {
            return true;
        } else {
            return b = Math.abs(_cvec) == Math.abs(_rvec);
        }
    }

    /** Return the length of this move (number of squares moved). */
    int length() {
        return Math.max(Math.abs(_rvec), Math.abs(_cvec));
    }

    /** Returns a int array of [x, y, length] where x,y = {-1, 0, 1}
     *  and length = {1, 8}. Where x and y are change in col and row. */
    int[] unit() {
        int l = length();
        int x = (_cvec) / l;
        int y = (_rvec) / l;
        int[] result = {x, y, l};
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof Move)) {
            return false;
        }
        Move move2 = (Move) obj;
        return _col0 == move2.getCol0() && _row0 == move2.getRow0()
            && _col1 == move2.getCol1() && _row1 == move2.getRow1();
    }

    @Override
    public int hashCode() {
        return _col0.hashCode() * _row0.hashCode() * _col1.hashCode()
            * _row1.hashCode() * _cvec.hashCode() * _rvec.hashCode();
    }

    @Override
    public String toString() {
        return toString(_col0, _row0, _col1, _row1);
    }

    /** Returns a move represented by COL0, ROW0, COL1, ROW1 into a String. */
    private static String toString(int col0, int row0, int col1, int row1) {
        char[] result = new char[5];
        c[0] = (char) (col0 + 'a' - 1);
        c[1] = (char) (9 - row0 + '0');
        c[2] = '-';
        c[3] = (char) (col1 + 'a' - 1);
        c[4] = (char) (9 - row1 + '1');
        return String(result);
    }

    /** Column and row numbers of starting and ending points. */
    private int _col0, _row0, _col1, _row1;

    /** Row and Column vector components (c1 - c0), (r1- r0). */
    private int _cvec, _rvec;

    /** Holds the HashMap of previous moves. */
    private static HashMap<String, Move> _moves = new HashMap<String, Move>();
}
