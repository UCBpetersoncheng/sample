package loa;

import java.util.Iterator;

import static loa.Piece.*;
import static loa.Side.*;
import java.util.HashSet;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Iterator;
import java.util.HashMap;

/** Represents the state of a game of Lines of Action. Boards are mutable.
 *  @author Peterson Cheng
 */
class Board {

    /** A Board whose initial contents are taken from
     *  INITIALCONTENTS and in which it is PLAYER's move. The resulting
     *  Board has
     *        get(col, row) == INITALCONTENTS[r -1][c - 1]
     *  Assumes that PLAYER is not null and INITIALCONTENTS is 8x8.
     *  Maintains counts of pieces per diagonal and row.
     *  Boards are immutable.
     */
    private Board(Piece[][] initialContents, Side player) {
        assert player != null && initialContents.length == 8;
        Piece p;
        for (int i = 0; i < 8; i++) {
            for(int j = 0; j < 8; j++) {
                p = initialContents[i][j];
                if (p.side() != null) {
                    if (p.side() == BLACK) {
                        _countBlack += 1;
                    } else {
                        _countWhite += 1;
                    }
                    addPiece(j + 1, i + 1);
                }
                _pieces[i][j] = p
            }
        }
        _turn = player;
    }

   /** A new board in the standard initial position. */
    Board() {
        this(INITIAL_PIECES, BLACK);
        _countBlack = 12;
        _countWhite = 12;
        _moveHistory = new ArrayList<Move>();
        _movesMade = 0;
    }

    Board(Board board) {
        this(board.getPieces(), board.turn());
        _countBlack = board.getCount(BLACK);
        _countWhite = board.getCount(WHITE);
        _numMoves = board.getMoves();
        _moveHistory = board.getMoveHistory();
        _movesMade = board.getMoveHistory().size();
    }

    /** Return the contents of column C, row R, where 1 <= C,R <= 8,
     *  where column 1 corresponds to column 'a' in the standard
     *  notation. */
    Piece get(int c, int r) {
        assert 1 <= c && c <= 8 && 1 <= r && r <= 8;
        return _pieces[r - 1][c - 1];
    }

    /** Return the contents of the square SQ.  SQ must be the
     *  standard printed designation of a square (having the form cr,
     *  where c is a letter from a-h and r is a digit from 1-8). */
    Piece get(String sq) {
        return get(col(sq), row(sq));
    }

    /** Return the col number (a value in the range 1-8) for SQ.
     *  SQ is of the form cr. */
    static int col(String sq) {
        return ((int) (sq.charAt(0) - 'a')) + 1;
    }

    /** Return the row number (a value in the range 1-8) for SQ.
     *  SQ is of the form cr. */
    static int row(String sq) {
        String s = sq.substring(1,2);
        return 9 - Integer.parseInt(s);
    }

    /** Returns the forward diagonal that a piece at _pieces[r - 1][c - 1]
     *  belongs to. Given that 1 <= C,R <= 8. */
    public static int forward(int c, int r) {
        return r + c - 2;
    }

    /** Returns the backward diagonal that a piece at _pieces[r - 1][c - 1]
     *  belongs to. Given that 1 <= C,R <= 8. */
    public static int backward(int c, int r) {
        return r - c + 7;
    }

    /** Returns the array of forward diagonals counts for this board. */
    public int[] getForward() { return _forwardDiag; }

    /** Returns the array of backward diagonals counts for this board. */
    public int[] getBackward() { return _backwardDiag; }

        /** Returns the array of column counts for this board. */
    public int[] getCol() { return _col; }

    /** Returns the array of row counts for this board. */
    public int[] getRow() { return _row; }

    /** Adds a piece to all piece counts (forward, backward, etc.) at
     *  _pieces[r - 1][c - 1]; */
    void addPiece(int c, int r) {
        _forwardDiag[forward(c, r)] += 1;
        _backwardDiag[backward(c, r)] += 1;
        _col[c - 1] += 1;
        _row[r - 1] += 1;
    }

    /** Removes a piece to all piece counts (forward, backward, etc.) at
     *  _pieces[r - 1][c - 1]; */
    void removePiece(int c, int r) {
        _forwardDiag[forward(c, r)] += -1;
        _backwardDiag[backward(c, r)] += -1;
        _col[c - 1] += -1;
        _row[r - 1] += -1;
    }

    /** Return the Side that is currently next to move. */
    Side turn() {
        return _turn;
    }

    /** Return true iff MOVE is legal for the player currently on move. */
    boolean isLegal(Move move) {
        int c = move.getCol0();
        int r = move.getRow0();
        int[] vector = move.unit();
        Piece p;
        for(int i = 0; i <= vector[2]; i++) {
            p = get(c, r);
            if (p != null && p.side() != _turn) {
                return false;
            }
        }
        return true;

    }

    /** Return a sequence of all legal from this position. */
    ArrayList<Move> legalMoves() {
        ArrayList<Move> result = new ArrayList<Move>();
        int f, b;
        Move m;
        for (int i = 1; i < 8; i++) {
            for (int j = 1; j < 8; j++) {
                if (get(i,j).side() == _turn) {
                    f = forward(j, i);
                    b = backward(j, i);
                    c = _col[j];
                    r = _row[i];
                    m = Move.create(j, i, j - b, i - b);
                    addMove(result, m);
                    m = Move.create(j, i, j + b, i + b);
                    addMove(result, m);
                    m = Move.create(j, i, j + f, i - f);
                    addMove(result, m);
                    m = Move.create(j, i, j - f, i + f);
                    addMove(result, m);
                    m = Move.create(j, i, j + c, i);
                    addMove(result, m);
                    m = Move.create(j, i, j - c, i);
                    addMove(result, m);
                    m = Move.create(j, i, j, i + r);
                    addMove(result, m);
                    m = Move.create(j, i, j, i - r);
                    addMove(result, m);
                } else {
                    continue;
                }
            }
        }
        return result;
    }

    /** Adds a move M to a ArrayList of moves L, provided it is valid
     *  and legal. */
    private void addMove(ArrayList<Move> l, Move m) {
        if (m.valid() && isLegal(m)) {
            l.add(m);
        }
    }

    /** Return true iff the game is currently over.  A game is over if
     *  either player has all his pieces continguous. */
    boolean gameOver() {
        return piecesContiguous(BLACK) || piecesContiguous(WHITE);
    }

    /** Return true iff PLAYER's pieces are continguous. Runs
      * the contiguousHelp() method starting at the first
      * PLAYER piece it finds. Player's pieces are contiguous if
      * the number of unique pieces attached to one piece is equal
      * to the total number of pieces for that side. */
    boolean piecesContiguous(Side player) {
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                if (_pieces[i][j].side() == player) {
                    boolean[][] traverse = new boolean[8][8];
                    return getCount(player) == contiguousHelp(traverse, i,
                                                               j, player);
                }
            }
        }

    }

    /** A helper function that returns the COUNT of contiguous
      * pieces of SIDE starting at (X, Y) using TRAVERSED to 
      * retain history. This function has terrible running time .___. */
    int contiguousHelp(boolean[][] traversed, int x, int y, Side player) {
        traversed[x][y] = true;
        result = 1;
        for (int i = x - 1 ; i <= x + 1; i++) {
            for (int j = y - 1; i <= y + 1; j++) {
                if (x < 0 || x > 7 || y < 0 || y > 7) {
                    continue;
                } else if (!(traversed[i][j])) {
                    traversed[i][j] = true;
                    if(_pieces[i][j].side() == player) {
                        result += contiguousHelp(traversed i, j, player);
                    }
                }
            }
        }
        return result;
    }

    /** Return the total number of moves that have been made (and not
     *  retracted).  Each valid call to makeMove with a normal move increases
     *  this number by 1. */
    int movesMade() {
        return _moveMade;
    }

    /** Returns move #K used to reach the current position, where
     *  0 <= K < movesMade().  Does not include retracted moves. */
    Move getMove(int k) {
        return _moveHistory.get(k);
    }

    /** Returns the array of move history. */
    ArrayList<Move> getMoveHistory() {
        return _moveHistory;
    }

    @Override
    public String toString() {
        return super.toString();  // REPLACE WITH IMPLEMENTATION
    
}
    /** Returns THIS board's current state of pieces 
       * as an 8x8 Piece[][] array. */
    Piece[][] getPieces() {
        return _pieces;
    }

    /** Returns the number of pieces on this board on
     *  side PLAYER. */
    int getCount(Side player) {
        return player == BLACK ? _countBlack : _countWhite;
    }

    /** Prints out this board. */
    void print() {
        for (int i = 0; i < 8; i--) {
            System.out.print("  ");
            for (int j = 0; j < 8; j++) {
                System.out.print(_pieces[i][j].textName());
            }
            System.out.print("\n");
        }
    }

    

    /** The standard initial configuration for Lines of Action. */
    static final Piece[][] INITIAL_PIECES = {
        { EMP, BP,  BP,  BP,  BP,  BP,  BP,  EMP },
        { WP,  EMP, EMP, EMP, EMP, EMP, EMP, WP  },
        { WP,  EMP, EMP, EMP, EMP, EMP, EMP, WP  },
        { WP,  EMP, EMP, EMP, EMP, EMP, EMP, WP  },
        { WP,  EMP, EMP, EMP, EMP, EMP, EMP, WP  },
        { WP,  EMP, EMP, EMP, EMP, EMP, EMP, WP  },
        { WP,  EMP, EMP, EMP, EMP, EMP, EMP, WP  },
        { EMP, BP,  BP,  BP,  BP,  BP,  BP,  EMP }
    };


    /** Holds the board's current state of pieces 8x8 board. */
    private Piece[][] _pieces;

    /** Holds the side of whoever's turn it is. */
    private Side _turn;

    /** The number of pieces for black on the board. */
    private int _countBlack = 0;

    /** The number of pieces for black on the board. */
    private int _countWhite = 0;


    /* A count of all pieces in a certain forwards diagonal. */
    private int[] _forwardDiag;

    /* A count of all pieces in a certain backwards diagonal. */
    private int[] _backwardDiag;

    /** A count of all pieces in a certain column. */
    private int[] _col;

    /** A count of all pieces in a certain row. */
    private int[] _row;

    /** Returns the number of moves made. */
    private final int _movesMade;

    /** A history of moves. */
    private ArrayList<Move> _moveHistory;

}
