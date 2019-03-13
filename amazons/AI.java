package amazons;



import java.util.Iterator;


import static amazons.Piece.*;

/** A Player that automatically generates moves.
 *  @author Roland Li
 */
class AI extends Player {

    /** A position magnitude indicating a win (for white if positive, black
     *  if negative). */
    private static final int WINNING_VALUE = Integer.MAX_VALUE - 1;
    /** A magnitude greater than a normal value. */
    private static final int INFTY = Integer.MAX_VALUE;

    /** A new AI with no piece or controller (intended to produce
     *  a template). */
    AI() {
        this(WHITE, null);
    }

    /** A new AI playing PIECE under control of CONTROLLER. */
    AI(Piece piece, Controller controller) {
        super(piece, controller);
    }

    @Override
    Player create(Piece piece, Controller controller) {
        return new AI(piece, controller);
    }

    @Override
    String myMove() {
        Move move = findMove();
        _controller.reportMove(move);
        return move.toString();
    }

    /** Return a move for me from the current position, assuming there
     *  is a move. */
    private Move findMove() {
        Board b = new Board(board());

        if (_myPiece == WHITE) {
            findMove(b, maxDepth(b), true, 1, -INFTY, INFTY);
        } else {
            findMove(b, maxDepth(b), true, -1, -INFTY, INFTY);
        }
        return _lastFoundMove;
    }

    /** The move found by the last call to one of the ...FindMove methods
     *  below. */
    private Move _lastFoundMove;

    /** Find a move from position BOARD and return its value, recording
     *  the move found in _lastFoundMove iff SAVEMOVE. The move
     *  should have maximal value or have value > BETA if SENSE==1,
     *  and minimal value or value < ALPHA if SENSE==-1. Searches up to
     *  DEPTH levels.  Searching at level 0 simply returns a static estimate
     *  of the board value and does not set _lastMoveFound. */
    private int findMove(Board board, int depth, boolean saveMove, int sense,
                         int alpha, int beta) {
        if (depth == 0 || board.winner() != null) {
            return staticScore(board);
        }
        if (sense == 1) {
            Iterator<Move> possiblemoves = board.legalMoves(WHITE);
            if (possiblemoves.hasNext()) {
                while (possiblemoves.hasNext()) {
                    Move move = possiblemoves.next();
                    board.makeMove(move);
                    int newScore = findMove(board, depth - 1,
                            false, -1, alpha, beta);
                    board.undo();
                    if (newScore > alpha) {
                        alpha = newScore;
                        if (saveMove) {
                            _lastFoundMove = move;
                        }
                    }
                    if (alpha >= beta) {
                        break;
                    }
                }
            }
            if (saveMove) {
                board.makeMove(_lastFoundMove);
            }
            return alpha;
        } else {
            Iterator<Move> possiblemoves = board.legalMoves(BLACK);
            if (possiblemoves.hasNext()) {
                while (possiblemoves.hasNext()) {
                    Move move = possiblemoves.next();
                    board.makeMove(move);
                    int newScore = findMove(board, depth - 1,
                            false, 1, alpha, beta);
                    board.undo();
                    if (newScore < beta) {
                        beta = newScore;
                        if (saveMove) {
                            _lastFoundMove = move;
                        }
                    }
                    if (alpha >= beta) {
                        break;
                    }
                }
            }
            if (saveMove) {
                board.makeMove(_lastFoundMove);
            }
            return beta;
        }
    }

    /** Return a heuristically determined maximum search depth
     *  based on characteristics of BOARD. */
    private int maxDepth(Board board) {
        Iterator<Move> whitemoves = board.legalMoves(WHITE);
        Iterator<Move> blackmoves = board.legalMoves(BLACK);
        int whitenumMoves = 0;
        while (whitemoves.hasNext()) {
            whitemoves.next();
            whitenumMoves += 1;
        }
        int blacknumMoves = 0;
        while (blackmoves.hasNext()) {
            blackmoves.next();
            blacknumMoves++;
        }
        System.out.println(board().turn());
        final int a = 20; final int b = 35;
        final int c = 70; final int d = 210;
        if (whitenumMoves <= a || blacknumMoves <= a) {
            return 5;
        } else if (whitenumMoves <= b || blacknumMoves <= b) {
            return 4;
        } else if (whitenumMoves <= c || blacknumMoves <= c) {
            return 3;
        } else if (whitenumMoves <= d || blacknumMoves <= d) {
            return 2;
        }
        return 1;
    }


    /** Return a heuristic value for BOARD. */
    public int staticScore(Board board) {
        Piece winner = board.winner();
        if (winner == BLACK) {
            return -WINNING_VALUE;
        } else if (winner == WHITE) {
            return WINNING_VALUE;
        }
        Iterator<Move> whitemoves = board.legalMoves(WHITE);
        Iterator<Move> blackmoves = board.legalMoves(BLACK);
        int whitenumMoves = 0;
        int blacknumMoves = 0;
        while (whitemoves.hasNext()) {
            whitemoves.next();
            whitenumMoves += 1;
        }
        while (blackmoves.hasNext()) {
            blackmoves.next();
            blacknumMoves += 1;
        }
        if (_myPiece == WHITE) {
            return whitenumMoves - blacknumMoves;
        } else {
            return whitenumMoves - blacknumMoves;
        }
    }


}
