package loa;

import java.util.ArrayList;
import java.util.Random;

/** An automated Player.
 *  @author Peterson Cheng */
class MachinePlayer extends Player {

    /** A MachinePlayer that plays the SIDE pieces in GAME. */
    MachinePlayer(Side side, Game game, long timeLimit) {
        super(side, game, timeLimit);
    }

    @Override
    boolean timeUp() {
        return _time.getAccum() > timeLimit();
    }

    @Override
    double timeLeft() {
        long result = _timeLimit - _time.getAccum();
        if (result > 0) {
            return (double) (result / 1000);
        } else {
            return 0;
        }
    }

    /** Tells this AI to start making moves on its own. */
    void start() {
        _start = true;
    }

    @Override
    Move makeMove() {
        if (_start) {
            _time.start();
            ArrayList<Move> moves = getBoard().legalMoves();
            Random r = getGame().getRandom();
            Move result = moves.get(r.nextInt(moves.size()));
            _time.stop();
            System.out.printf("AI[%s] %.3fs left.\n", side(), timeLeft());
            System.out.printf("%s::%s for a move by %s\n",side().initial(), 
                               result.toString(), side.capital());
            System.out.flush();
            return result;
        } else {
            String moveStr = getGame().getMove();
            String from = moveStr.subString(0, 2);
            String to = moveStr.subString(3, 5);
            int c0, r0, c1, r1;
            c0 = Board.col(from);
            r0 = Board.row(from);
            c1 = Board.col(to);
            r1 = Board.row(to);
            Move m = Move.create(c0, r0, c1, r1);
            if (m.valid() && getGame().getBoard().isLegal(m)) {
                return m;
            } else {
                System.out.printf("Please enter a legal move.");
                System.out.flush;
                return makeMove();
            }
        }
    }

    /** Is true if this AI has started. */
    private boolean _start = false;

    /** The timer that holds this player's time. */
    private Stopwatch _time = new StopWatch();