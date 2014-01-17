package loa;

import java.util.Random;
import java.util.Scanner;
import java.lang.IllegalArgumentException;

/** Represents one game of Lines of Action.
 *  @author Peterson Cheng*/
class Game {

    /** A new Game between NUMHUMAN humans and 2-NUMHUMAN AIs.  SIDE0
     *  indicates which side the first player (known as ``you'') is
     *  playing.  SEED is a random seed for random-number generation.
     *  TIME is the time limit each side has to make its moves (in seconds).
     */
    Game(int numHuman, Side side, long seed, long time) {
        _random = new Random(seed);
        _numHuman = numHuman;
        _side = side;
        _seed = seed;
        _time = time;
        _board = Board.create();
    }

    /** Return the current board. */
    Board getBoard() {
        return _board;
    }

    /** Return a move from the terminal.  Processes any intervening commands
     *  as well. */
    String getMove() {
        System.out.printf("[%s] %.3fs> ", _turn.initial(), 
                          _getPlayer(_turn).timeLeft());
        System.out.flush();
        String line = "";
        if (_input.hasNextLine()) {
            line = _input.nextLine();
        } else {
            System.out.println("No more input, exiting...");
            System.out.flush;
            System.exit(0);
        }
        String[] args = line.split("\\s");
        return execMove(s);
    }

    /** Parses the given command, returns a move. */
    String execMove(String s) {
        if (s.equals("s")) {
            show();
        } else if (s.equals("p")) {
            startAI();
        } else if (s.equals("q")) {
            System.out.println("Exiting...");
            System.out.flush();
            System.exit(0);
        } else if (s.equals("#")) {
            return getMove();
        } else if (s.equals("help")) {
            help();
        } else if (s.matches(MOVE_REGEX)) {
            return s;
        } else {
            System.out.println(HELP);
            System.out.flush();
            return getMove();
        }
    }

    /** Shows the game board. */
    void show() {
        System.out.println("===");
        _board.print();
        System.out.println("Next move: " + _side.toString());
        System.out.println("Moves: " + _board.movesMade());
        System.out.println("===");
        System.out.flush();
    }

    /** Prints out the help message. */
    void help() {
        System.out.println("--------COMMANDS------");
        System.out.println("\ts:\t" + SHOW);
        System.out.println("\tp:\t" + PLAY);
        System.out.println("\tq:\t" + QUIT);
        System.out.println("\t#:\t" + COMMENT);
        System.out.println("\tc1r1-c2r2:\t" + MOVE);
        System.out.println("----------------------");
        System.out.flush();
    }

    /** Initiates the AI's. */
    void startAI() {
        _playerBlack.start();
        _playerWhite.start();
    }

    /** Play this game, printing any transcript and other results. */
    public void play() {
        Reporter.debug(1, "NumHuman: %d\tSide0:%s\tSeed:%d\tTime:%d\n",
                       _numHuman, _side.toString(), _seed, _time);
        setupPlayers();
        _side= BLACK;
        Move m;
        MutableBoard mb;
        while (!_board.gameOver()) {
            m = getPlayer(_side).makeMove();
            if (getPlayer(_side).timeUp()) {
                victory(_side.opponent());
            } else {
                mb = new MutableBoard(_board);
                mb.makeMove(m);
                _board = new Board((Board) mb);
                _side = _side.opponent();
            }
        }
        victory(curr.opponent());
    }

    /** Sets up the players for this game. */
    void setupPlayers() {
        if (_numHuman == 2) {
            getPlayer(_side) = (Player) new HumanPlayer(_side, this, _time);
            getPlayer(_side.opponent()) = (Player) new HumanPlayer(
                                              _side.opponent(), this, _time);
        } else if (_numHuman == 1) {
            getPlayer(_side) = (Player) new HumanPlayer(_side, this, _time);
            getPlayer(_side.opponent()) = (Player) new MachinePlayer(
                                              _side.opponent(), this, _time);
        } else if (_numHuman == 0) {
            getPlayer(_side) = (Player) new MachinePlayer(_side, this, _time);
            getPlayer(_side.opponent()) = (Player) new MachinePlayer(
                                              _side.opponent(), this, _time);
        } else {
            throw new IllegalArgumentException("AI # wrong setupPlayers()");
        }
    }


    void victory(Side s) {
        System.out.println("%s wins.", s.capital());
        while(true) {
            getMove();
        }
    }

    /** Return the random number generator for this game. */
    Random getRandom() {
        return _random;
    }

    /** Returns the player for side S. */
    Player getPlayer(Side s) {
        return s == BLACK ? _playerBlack : _playerWhite;
    }

    /** The official game board. */
    private Board _board;

    /** A source of random numbers, primed to deliver the same sequence in
     *  any Game with the same seed value. */
    private Random _random;

    /** Number of humans. */
    private int _numHuman;

    /** The side. */
    private Side _side;

    /** The seed. */
    private long _seed;

    /** The time limit. */
    private long _time;

    /** The scanner that takes input. */
    private Scanner _input = new Scanner(System.in);

    /** The Player for Black */
    private final Player _playerBlack;

    /** The player for White. */
    private final Player _playerWhite;

    /** The regex that matches a valid move. */
    private static final String MOVE_REGEX = "\\b[a-h][1-8]-[a-h][1-8]\\b";

    /** The help prompt. */
    private static final String HELP = "Unknown command. Please type"
        + "'help' to see a list of valid commands."

    /** The show descriptor. */
    private static final String SHOW = "Show the current board, side "
        + "next to play, and number of moves.";

    /** The play descriptor. */
    private static final String PLAY = "Start AIs - no effect if"
        + " already started or no AIs.";

    /** The quit descriptor. */
    private static final String QUIT = "Exits the program."

    /** The comment descriptor. */
    private static final String COMMENT= "Comment - no effect.";

    /** The move descriptor. */
    private static final String MOVE = "Move where c is a letter "
        + "[a-h] and r is a digit [1-8];";
}
