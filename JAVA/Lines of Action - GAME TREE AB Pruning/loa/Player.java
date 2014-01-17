package loa;

import static loa.Side.*;

/** Represents a player.  Extensions of this class do the actual playing.
 *  @author Peterson Cheng
 */
public abstract class Player {

    /** A player that plays the SIDE pieces in GAME. */
    Player(Side side, Game game, long timeLimit) {
        _side = side;
        _game = game;
        _timeLimit = timeLimit;
        if (timeLimit <= 0) {
            _timeLimit = Long.MAX_VALUE;
        }
        
    }

    /** Return my next move from the current position in getBoard(), assuming
     *  that side() == getBoard.turn(). */
    abstract Move makeMove();

    /** Returns true if this player is out of time. */
    abstract boolean timeUp();

    /** Returns the time left, returns 0 if time is up. */
    abstract double timeLeft();

    /** Starts up the AI for this player. Does nothing if no AI. */
    abstract void start();

    /** Return which side I'm playing. */
    Side side() {
        return _side;
    }

    /** Return the board I am using. */
    Board getBoard() {
        return _game.getBoard();
    }

    /** Return the game I am playing. */
    Game getGame() {
        return _game;
    }

    /** Returns the time limit for this player. */
    long timeLimit() {
        return _timeLimit;
    }

    /** This player's side. */
    private final Side _side;
    /** The game this player is part of. */
    private Game _game;

    /** The timelimit for this player. */
    private final long _timeLimit;

}
