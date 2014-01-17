package loa;

import static loa.Piece.*;
import static loa.Side.*;
import java.util.Iterator;
import java.util.HashMap;

/** Represents the state of a game of Lines of Action, and allows making moves.
 *  @author Peterson Cheng*/
class MutableBoard extends Board {

    /** A MutableBoard whose initial contents are taken from
     *  INITIALCONTENTS and in which it is PLAYER's move. The resulting
     *  Board has
     *        get(col, row) == INITIALCONTENTS[row-1][col-1]
     *  Assumes that PLAYER is not null and INITIALCONTENTS is 8x8.
     */
    MutableBoard(Piece[][] initialContents, Side player) {
        super(initialContents, player);
    }

    /** A new board in the standard initial position. */
    MutableBoard() {
        super();
    }

    /** A Board whose initial contents and state are copied from
     *  BOARD. */
    MutableBoard(Board board) {
        super(board);
    }

    /** Returns the number of moves made. */
    @Override
    int movesMade() {
        return _moveHist.size();
    }

    @Override
    Move[] getMoveHistory() {
        return (Move[]) _moveHist.toArray();
    }

    /** Assuming isLegal(MOVE), make MOVE. */
    void makeMove(Move move) {
        getMoveHistory().add(move);
        int c0,r0,c1,r1;
        c0 = getCol0(move);
        r0 = getRow0(move);
        c1 = getCol1(move);
        r1 = getRow1(move);
        Piece p1 = getPieces(c1, r1);
        if (p1.side() != null) {
            _captured.put(getMoveHistory().size(), p1);
            removePiece(c1, r1);
        }
        getMoveHistory().add(move);
        getPieces()[c1 - 1][r1 - 1] = get(c0, r0);
        addPiece(c1, r1);
        getPieces()[c0 - 1][r0 - 1] = EMP;
        removePiece(c0, r0);
        _turn = _turn.opponent();
    }

    /** Retract (unmake) one move, returning to the state immediately before
     *  that move.  Requires that movesMade () > 0. */
    void retract() {
        assert movesMade() > 0;
        Move move = getMoveHistory.remove(getMoveHistory.size() - 1);
        int key = _moveHist.size();
        int c0, r0, c1, r1;
        c0 = getCol0(move);
        r0 = getRow0(move);
        c1 = getCol1(move);
        r1 = getRow1(move);
        Piece p0 = get(c1, r1);
        Piece p1 = EMP;
        if (_captured.containsKey(key)) {
            p1 = _captured.get(key);
            addPiece(c1, r1);
        }
        getPieces()[c1 - 1][r1 - 1] = p1;
        removePiece(c1, r1);
        getPieces()[c0 - 1][r0 - 1] = p0;
        addPiece(c0, r0);
        }
    }

    /** The captured pieces associated with that move. */
    private HashMap<Integer, Piece> _captured = new HashMap<Integer, Piece>();

    /** Unique diagonal per row + col, minus one overlap. 
     *  Holds the number of pieces per forward diagonal.*/
    private int[] _forwardDiag;
    /** Unique diagonal per row + col, minus one overlap. 
     *  Holds the number of pieces per backward diagonal.*/
    private int[] _backwardDiag;

    /** Holds the number of pieces per row. */
    private int[] _row;

    /** Holds the number of pieces per col. */
    private int[] _col;
}
