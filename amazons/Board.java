package amazons;

import java.util.Collections;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Stack;

import static amazons.Piece.*;
import static amazons.Move.mv;
import static amazons.Utils.error;


/** The state of an Amazons Game.
 *  @author Roland Li
 */
class Board {

    /** The number of squares on a side of the board. */
    static final int SIZE = 10;

    /** Initializes a game board with SIZE squares on a side in the
     *  initial position. */
    Board() {
        init();
    }

    /** Initializes a copy of MODEL. */
    Board(Board model) {
        copy(model);
    }

    /** Copies MODEL into me. */
    void copy(Board model) {
        int i = 0;
        this._pieces = new Piece[100];
        while (i < 100) {
            this._pieces[i] = model._pieces[i];
            i++;
        }
        this._turn = model._turn;
        this._winner = model._winner;
        this._moves = model._moves;
        this._numMoves = model.numMoves();
    }

    /** Clears the board to the initial position. */
    void init() {
        int i = 0;
        _pieces = new Piece[100];
        final int a = 6; final int b = 39;
        final int c = 69; final int d = 96;
        final int e = 3; final int f = 30;
        final int g = 60; final int h = 93;
        while (i < 100) {
            if (i == a || i == b || i == c || i == d) {
                _pieces[i] = BLACK;
            } else if (i == e || i == f || i == g || i == h) {
                _pieces[i] = WHITE;
            } else {
                _pieces[i] = EMPTY;
            }
            i++;
        }
        _turn = WHITE;
        _winner = null;
        _moves = new Stack<Move>();
        _numMoves = 0;
    }

    /** Return the Piece whose move it is (WHITE or BLACK). */
    Piece turn() {
        return _turn;
    }

    /** Return the number of moves (that have not been undone) for this
     *  board. */
    int numMoves() {
        return _numMoves;
    }

    /** Return the winner in the current position, or null if the game is
     *  not yet finished. */
    Piece winner() {
        return _winner;
    }

    /** Return the contents the square at S. */
    final Piece get(Square s) {
        return _pieces[s.index()];
    }

    /** Return the contents of the square at (COL, ROW), where
     *  0 <= COL, ROW <= 9. */
    final Piece get(int col, int row) {
        return _pieces[col * 10 + row];
    }

    /** Return the contents of the square at COL ROW. */
    final Piece get(char col, char row) {
        return get(col - 'a', row - '1');
    }

    /** Set square S to P. */
    final void put(Piece p, Square s) {
        _pieces[s.index()] = p;
    }

    /** Set square (COL, ROW) to P. */
    final void put(Piece p, int col, int row) {
        _pieces[col * 10 + row] = p;
        _winner = EMPTY;
    }

    /** Set square COL ROW to P. */
    final void put(Piece p, char col, char row) {
        put(p, col - 'a', row - '1');
    }

    /** Return true iff FROM - TO is an unblocked queen move on the current
     *  board, ignoring the contents of ASEMPTY, if it is encountered.
     *  For this to be true, FROM-TO must be a queen move and the
     *  squares along it, other than FROM and ASEMPTY, must be
     *  empty. ASEMPTY may be null, in which case it has no effect. */
    boolean isUnblockedMove(Square from, Square to, Square asEmpty) {
        if (asEmpty != null && to == asEmpty) {
            return true;
        }
        if (to == null || get(to) != EMPTY) {
            return false;
        }
        int direction = from.direction(to);
        while (from != to) {
            if (from.queenMove(direction, 1) == null) {
                return false;
            } else if (get(from.queenMove(direction, 1)) == EMPTY
                    || from.queenMove(direction, 1) == asEmpty) {
                from = from.queenMove(direction, 1);
                if (from == null) {
                    return false;
                }
            } else {
                return false;
            }
        }
        return true;
    }

    /** Return true iff FROM is a valid starting square for a move. */
    boolean isLegal(Square from) {
        if (get(from) == _turn) {
            return true;
        }
        return false;
    }

    /** Return true iff FROM-TO is a valid first part of move, ignoring
     *  spear throwing. */
    boolean isLegal(Square from, Square to) {
        if (isLegal(from) && isUnblockedMove(from, to, null)) {
            return true;
        }
        return false;
    }

    /** Return true iff FROM-TO(SPEAR) is a legal move in the current
     *  position. */
    boolean isLegal(Square from, Square to, Square spear) {
        if (isLegal(from, to) && isUnblockedMove(to, spear, from)) {
            return true;
        }
        return false;

    }

    /** Return true iff MOVE is a legal move in the current
     *  position. */
    boolean isLegal(Move move) {
        if (isLegal(move.from(), move.to(), move.spear())) {
            return true;
        }
        return false;
    }

    /** Move FROM-TO(SPEAR), assuming this is a legal move. */
    void makeMove(Square from, Square to, Square spear) {
        Iterator<Move> black = legalMoves(BLACK);
        if (!black.hasNext()) {
            _winner = WHITE;
        }
        Iterator<Move> white = legalMoves(WHITE);
        if (!white.hasNext()) {
            _winner = BLACK;
        }
        if (isLegal(from, to, spear)) {
            put(EMPTY, from);
            put(_turn, to);
            put(SPEAR, spear);
            if (_turn == WHITE) {
                _turn = BLACK;
            } else {
                _turn = WHITE;
            }
            _moves.push(mv(from, to, spear));
        } else {
            throw error("Invalid move.");
        }
        _numMoves++;

    }

    /** Move according to MOVE, assuming it is a legal move. */
    void makeMove(Move move) {
        assert (isLegal(move));
        makeMove(move.from(), move.to(), move.spear());
    }

    /** Undo one move.  Has no effect on the initial board. */
    void undo() {
        Move move = _moves.pop();
        put(EMPTY, move.spear());
        put(EMPTY, move.to());
        if (_turn == WHITE) {
            _turn = BLACK;
        } else {
            _turn = WHITE;
        }
        if (_winner != null) {
            _winner = EMPTY;
        }
        put(_turn, move.from());
        _numMoves--;

    }

    /** Return an Iterator over the Squares that are reachable by an
     *  unblocked queen move from FROM. Does not pay attention to what
     *  piece (if any) is on FROM, nor to whether the game is finished.
     *  Treats square ASEMPTY (if non-null) as if it were EMPTY.  (This
     *  feature is useful when looking for Moves, because after moving a
     *  piece, one wants to treat the Square it came from as empty for
     *  purposes of spear throwing.) */
    Iterator<Square> reachableFrom(Square from, Square asEmpty) {
        return new ReachableFromIterator(from, asEmpty);
    }

    /** Return an Iterator over all legal moves on the current board. */
    Iterator<Move> legalMoves() {
        return new LegalMoveIterator(_turn);
    }

    /** Return an Iterator over all legal moves on the current board for
     *  SIDE (regardless of whose turn it is). */
    Iterator<Move> legalMoves(Piece side) {
        return new LegalMoveIterator(side);
    }

    /** An iterator used by reachableFrom. */
    private class ReachableFromIterator implements Iterator<Square> {

        /** Iterator of all squares reachable by queen move from FROM,
         *  treating ASEMPTY as empty. */
        ReachableFromIterator(Square from, Square asEmpty) {
            _from = from;
            _dir = -1;
            _steps = 0;
            _asEmpty = asEmpty;
            toNext();
        }

        @Override
        public boolean hasNext() {
            return _dir < 8;
        }

        @Override
        public Square next() {
            Square square = _square;
            toNext();
            return square;
        }

        /** Advance _dir and _steps, so that the next valid Square is
         *  _steps steps in direction _dir from _from. */
        private void toNext() {
            if (_dir == -1 && _steps == 0) {
                _dir = 0;
                _steps = 1;
            } else if (_from.queenMove(_dir, _steps + 1) == null
                    || !isUnblockedMove(_from,
                    _from.queenMove(_dir, _steps + 1), _asEmpty)) {
                _dir++;
                _steps = 1;
                if (_dir == 8) {
                    _dir = 8;
                }
            } else {
                _steps++;
            }
            if (_dir < 8) {
                while (!isUnblockedMove(_from,
                        _from.queenMove(_dir, _steps), _asEmpty)) {
                    _dir++;
                    _steps = 1;
                    if (_dir == 8) {
                        break;
                    }
                }
            }
            _square = _from.queenMove(_dir, _steps);
        }

        /** Starting square. */
        private Square _from;
        /** Current direction. */
        private int _dir;
        /** Current distance. */
        private int _steps;
        /** Square treated as empty. */
        private Square _asEmpty;
        /** Square to be returned. */
        private Square _square;
    }

    /** An iterator used by legalMoves. */
    private class LegalMoveIterator implements Iterator<Move> {

        /** All legal moves for SIDE (WHITE or BLACK). */
        LegalMoveIterator(Piece side) {
            _startingSquares = Square.iterator();
            _fromPiece = side;
            ArrayList<Square> start = new ArrayList<Square>(4);
            while (_startingSquares.hasNext()) {
                Square newstart = _startingSquares.next();
                if (get(newstart) == _fromPiece) {
                    start.add(newstart);
                }
            }
            _startingSquares = start.iterator();
            _start = _startingSquares.next();
            _pieceMoves = new ReachableFromIterator(_start, null);
            while (!_pieceMoves.hasNext()) {
                if (!_startingSquares.hasNext()) {
                    break;
                }
                _start = _startingSquares.next();
                _pieceMoves = new ReachableFromIterator(_start, null);
            }
            _nextSquare = _pieceMoves.next();
            if (_nextSquare == null) {
                _spearThrows = NO_SQUARES;
            } else {
                _spearThrows = new ReachableFromIterator(_nextSquare, _start);
            }
            _initial =  true;
            toNext();
        }

        @Override
        public boolean hasNext() {
            return _initial;
        }

        @Override
        public Move next() {
            Move move = Move.mv(_start, _nextSquare, _spear);
            toNext();
            return move;
        }

        /** Advance so that the next valid Move is
         *  _start-_nextSquare(sp), where sp is the next value of
         *  _spearThrows. */
        private void toNext() {
            if (_spearThrows.hasNext()) {
                _spear = _spearThrows.next();
            } else {
                if (_pieceMoves.hasNext()) {
                    while (!_pieceMoves.hasNext()) {
                        _start = _startingSquares.next();
                        _pieceMoves = new ReachableFromIterator(_start, null);
                    }
                    _nextSquare = _pieceMoves.next();
                    _spearThrows =
                            new ReachableFromIterator(_nextSquare, _start);
                    _spear = _spearThrows.next();
                } else {
                    while (!_pieceMoves.hasNext()) {
                        if (!_startingSquares.hasNext()) {
                            _initial = false;
                            break;
                        }
                        _start = _startingSquares.next();
                        _pieceMoves = new ReachableFromIterator(_start, null);
                    }
                    if (!_pieceMoves.hasNext()) {
                        _spearThrows = NO_SQUARES;
                    } else {
                        _nextSquare = _pieceMoves.next();
                        _spearThrows =
                                new ReachableFromIterator(_nextSquare, _start);
                        _spear = _spearThrows.next();
                    }
                }
            }
        }



        /** Color of side whose moves we are iterating. */
        private Piece _fromPiece;
        /** Current starting square. */
        private Square _start;
        /** Remaining starting squares to consider. */
        private Iterator<Square> _startingSquares;
        /** Current piece's new position. */
        private Square _nextSquare;
        /** Remaining moves from _start to consider. */
        private Iterator<Square> _pieceMoves;
        /** Remaining spear throws from _piece to consider. */
        private Iterator<Square> _spearThrows;
        /** Spear thrown. */
        private Square _spear;
        /** Final boolean. */
        private boolean _initial;
    }

    @Override
    public String toString() {
        String string = "   ";
        int i = 9;
        final int k = 90;
        while (i >= 0) {
            int j = 0;
            while (j < 10) {
                if (i + j * 10 == k) {
                    string = string + _pieces[i + j * 10].toString() + "\n";
                } else if (j == 9) {
                    string = string + _pieces[i + j * 10].toString() + "\n   ";
                } else {
                    string = string + _pieces[i + j * 10].toString() + " ";
                }
                j++;
            }
            i--;
        }
        return string;
    }

    /** An empty iterator for initialization. */
    private static final Iterator<Square> NO_SQUARES =
        Collections.emptyIterator();

    /** Piece whose turn it is (BLACK or WHITE). */
    private Piece _turn;

    /** Cached value of winner on this board, or EMPTY if it has not been
     *  computed. */
    private Piece _winner;

    /** Pieces in position. */
    private Piece[] _pieces;

    /** Moves list. */
    private Stack<Move> _moves;

    /** Number of moves on board. */
    private int _numMoves;
}
