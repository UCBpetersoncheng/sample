package loa;
import ucb.util.Stopwatch;


/** A Player that prompts for moves and reads them from its Game.
 *  @author Peterson Cheng */
class HumanPlayer extends Player {

    /** A HumanPlayer that plays the SIDE pieces in GAME.  It uses
     *  GAME.getMove() as a source of moves.  */
    HumanPlayer(Side side, Game game, long timeLimit) {
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

    @Override
    Move makeMove() {
        _time.start();
        String moveStr = getGame().getMove();
        _time.stop();
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

    /** Starting has no effect on a human player. */
    @Override
    void start() {
    }

    /** The timer that holds this player's time. */
    private Stopwatch _time = new StopWatch();
}
