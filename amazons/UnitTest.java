package amazons;

import org.junit.Test;

import static amazons.Piece.*;
import static org.junit.Assert.*;
import ucb.junit.textui;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

/** The suite of all JUnit tests for the amazons package.
 *  @author
 */
public class UnitTest {

    /** Run the JUnit tests in this package. Add xxxTest.class entries to
     *  the arguments of runClasses to run other JUnit tests. */
    public static void main(String[] ignored) {
        textui.runClasses(UnitTest.class);
    }

    /** Tests basic correctness of put and get on the initialized board. */
    @Test
    public void testBasicPutGet() {
        Board b = new Board();
        b.put(BLACK, Square.sq(3, 5));
        assertEquals(b.get(3, 5), BLACK);
        b.put(WHITE, Square.sq(9, 9));
        assertEquals(b.get(9, 9), WHITE);
        b.put(EMPTY, Square.sq(3, 5));
        assertEquals(b.get(3, 5), EMPTY);
    }

    /** Tests proper identification of legal/illegal queen moves. */
    @Test
    public void testIsQueenMove() {
        assertFalse(Square.sq("b", "6").isQueenMove(Square.sq(1, 5)));
        assertFalse(Square.sq(1, 5).isQueenMove(Square.sq(2, 7)));
        assertFalse(Square.sq("a", "1").isQueenMove(Square.sq(5, 1)));
        assertTrue(Square.sq(1, 1).isQueenMove(Square.sq(9, 9)));
        assertTrue(Square.sq(2, 7).isQueenMove(Square.sq(8, 7)));
        assertTrue(Square.sq(3, 0).isQueenMove(Square.sq(3, 4)));
        assertTrue(Square.sq("h10").isQueenMove(Square.sq(0, 2)));
    }

    /** Tests toString for initial board state and a smiling board state. :) */
    @Test
    public void testToString() {
        Board b = new Board();
        assertEquals(INIT_BOARD_STATE, b.toString());
        makeSmile(b);
        assertEquals(SMILE, b.toString());
    }

    private void makeSmile(Board b) {
        b.put(EMPTY, Square.sq(0, 3));
        b.put(EMPTY, Square.sq(0, 6));
        b.put(EMPTY, Square.sq(9, 3));
        b.put(EMPTY, Square.sq(9, 6));
        b.put(EMPTY, Square.sq(3, 0));
        b.put(EMPTY, Square.sq(3, 9));
        b.put(EMPTY, Square.sq(6, 0));
        b.put(EMPTY, Square.sq(6, 9));
        for (int col = 1; col < 4; col += 1) {
            for (int row = 6; row < 9; row += 1) {
                b.put(SPEAR, Square.sq(col, row));
            }
        }
        b.put(EMPTY, Square.sq(2, 7));
        for (int col = 6; col < 9; col += 1) {
            for (int row = 6; row < 9; row += 1) {
                b.put(SPEAR, Square.sq(col, row));
            }
        }
        b.put(EMPTY, Square.sq(7, 7));
        for (int lip = 3; lip < 7; lip += 1) {
            b.put(WHITE, Square.sq(lip, 2));
        }
        b.put(WHITE, Square.sq(2, 3));
        b.put(WHITE, Square.sq(7, 3));
    }

    static final String INIT_BOARD_STATE =
            "   - - - B - - B - - -\n"
            + "   - - - - - - - - - -\n"
            + "   - - - - - - - - - -\n"
            + "   B - - - - - - - - B\n"
            + "   - - - - - - - - - -\n"
            + "   - - - - - - - - - -\n"
            + "   W - - - - - - - - W\n"
            + "   - - - - - - - - - -\n"
            + "   - - - - - - - - - -\n"
            + "   - - - W - - W - - -\n";

    static final String SMILE =
            "   - - - - - - - - - -\n"
            + "   - S S S - - S S S -\n"
            + "   - S - S - - S - S -\n"
            + "   - S S S - - S S S -\n"
            + "   - - - - - - - - - -\n"
            + "   - - - - - - - - - -\n"
            + "   - - W - - - - W - -\n"
            + "   - - - W W W W - - -\n"
            + "   - - - - - - - - - -\n"
            + "   - - - - - - - - - -\n";

    @Test
    public void directionTest() {
        assertEquals(Square.sq(1, 1).direction(Square.sq(9, 9)), 1);
        assertEquals(Square.sq(2, 7).direction(Square.sq(8, 7)), 2);
        assertEquals(Square.sq(3, 0).direction(Square.sq(3, 4)), 0);
        assertEquals(Square.sq(7, 9).direction(Square.sq(0, 2)), 5);
        assertEquals(Square.sq(4, 5).direction(Square.sq(4, 3)), 4);
        assertEquals(Square.sq(8, 4).direction(Square.sq(3, 4)), 6);
        assertEquals(Square.sq(4, 2).direction(Square.sq(6, 0)), 3);
        assertEquals(Square.sq(8, 3).direction(Square.sq(3, 8)), 7);
    }

    @Test
    public void queenMoveTest() {
        assertEquals(Square.sq(1, 1).queenMove(9, 1), null);
        assertEquals(Square.sq(2, 7).queenMove(2, 1), Square.sq(3, 7));
        assertEquals(Square.sq(3, 4).queenMove(2, 100), null);
        assertEquals(Square.sq(7, 6).queenMove(7, 4), null);
    }

    @Test
    public void isLegalTest() {
        Board b = new Board();
        assertTrue(b.isLegal(Square.sq(3), Square.sq(4), Square.sq(5)));
    }
    @Test
    public void testReachableFrom() {
        Board b = new Board();
        buildBoard(b, REACHABLE);
        int numSquares = 0;
        Set<Square> squares = new HashSet<>();
        Iterator<Square> reachableFrom = b.reachableFrom(Square.sq(5, 4), null);
        while (reachableFrom.hasNext()) {
            Square s = reachableFrom.next();
            assertTrue(REACHABLEFROMTESTSQUARES.contains(s));
            numSquares += 1;
            squares.add(s);
        }
        assertEquals(REACHABLEFROMTESTSQUARES.size(), numSquares);
        assertEquals(REACHABLEFROMTESTSQUARES.size(), squares.size());


        b = new Board();
        buildBoard(b, FINAL);
        numSquares = 0;
        squares = new HashSet<>();
        reachableFrom = b.reachableFrom(Square.sq(9, 1), Square.sq(90));
        while (reachableFrom.hasNext()) {
            Square s = reachableFrom.next();
            numSquares += 1;
            squares.add(s);
        }
        assertEquals(3, numSquares);
    }

    /** Tests legalMovesIterator to make sure it returns all legal Moves.
     *  This method needs to be finished and may need to be changed
     *  based on your implementation. */
    @Test
    public void testLegalMoves() {
        Board b = new Board();
        buildBoard(b, INI_BOARD_STATE);
        int numMoves = 0;
        Set<Move> moves = new HashSet<>();
        Iterator<Move> legalMoves = b.legalMoves(WHITE);
        while (legalMoves.hasNext()) {
            Move m = legalMoves.next();
            assertTrue(true);
            numMoves += 1;
            moves.add(m);
        }
        assertEquals(2137, numMoves);
        assertEquals(2137, moves.size());

        b  = new Board();
        buildBoard(b, SID);
        numMoves = 0;
        moves = new HashSet<>();
        legalMoves = b.legalMoves(WHITE);
        while (legalMoves.hasNext()) {
            Move m = legalMoves.next();
            assertTrue(true);
            numMoves += 1;
            moves.add(m);
        }
        assertEquals(1395, numMoves);
        assertEquals(1395, moves.size());

        b  = new Board();
        buildBoard(b, EDGE);
        numMoves = 0;
        moves = new HashSet<>();
        legalMoves = b.legalMoves(WHITE);
        while (legalMoves.hasNext()) {
            Move m = legalMoves.next();
            assertTrue(true);
            numMoves += 1;
            moves.add(m);
        }
        assertEquals(7, numMoves);
        assertEquals(7, moves.size());

        b = new Board();
        buildBoard(b, FINAL);
        numMoves = 0;
        moves = new HashSet<>();
        legalMoves = b.legalMoves(WHITE);
        while (legalMoves.hasNext()) {
            Move m = legalMoves.next();
            assertTrue(true);
            numMoves += 1;
            moves.add(m);
        }
        assertEquals(14, numMoves);
        assertEquals(14, moves.size());
    }




    private void buildBoard(Board b, Piece[][] target) {
        for (int col = 0; col < Board.SIZE; col++) {
            for (int row = Board.SIZE - 1; row >= 0; row--) {
                Piece piece = target[Board.SIZE - row - 1][col];
                b.put(piece, Square.sq(col, row));
            }
        }
    }

    @Test
    public void isUnblockedMoveTest() {
        Board b = new Board();
        buildBoard(b, REACHABLE);
        assertTrue(b.isUnblockedMove(Square.sq(5, 4),
                Square.sq(6, 4), null));
        assertTrue(b.isUnblockedMove(Square.sq(5, 4),
                Square.sq(4, 5), null));
        assertTrue(b.isUnblockedMove(Square.sq(5, 4),
                Square.sq(4, 5), null));
        assertTrue(b.isUnblockedMove(Square.sq(6, 4),
                Square.sq(4, 4), Square.sq(5, 4)));
        assertTrue(b.isUnblockedMove(Square.sq(6, 4),
                Square.sq(5, 4), Square.sq(5, 4)));
        assertTrue(b.isUnblockedMove(Square.sq(6, 4),
                Square.sq(5, 4), Square.sq(5, 4)));
        assertTrue(b.isUnblockedMove(Square.sq(6, 4),
                Square.sq(4, 4), Square.sq(5, 4)));
        assertTrue(b.isUnblockedMove(Square.sq(7, 6),
                Square.sq(7, 6).queenMove(7, 3), null));
        buildBoard(b, FINAL);
        assertFalse(b.isUnblockedMove(Square.sq(9, 1),
                Square.sq(9, 2), null));

    }

    static final Piece E = Piece.EMPTY;

    static final Piece W = WHITE;

    static final Piece B = BLACK;

    static final Piece S = Piece.SPEAR;

    static final Piece[][] REACHABLE =
    {
        { E, E, E, E, E, E, E, E, E, E },
        { E, E, E, E, E, E, E, E, W, W },
        { E, E, E, E, E, E, E, S, E, S },
        { E, E, E, S, S, S, S, E, E, S },
        { E, E, E, S, E, E, E, E, B, E },
        { E, E, E, S, E, W, E, E, B, E },
        { E, E, E, S, S, S, B, W, B, E },
        { E, E, E, E, E, E, E, E, E, E },
        { E, E, E, E, E, E, E, E, E, E },
        { E, E, E, E, E, E, E, E, E, E },
    };
    static final Piece[][] INI_BOARD_STATE =
    {
        { E, E, E, B, E, E, B, E, E, E },
        { E, E, E, E, E, E, E, E, E, E },
        { E, E, E, E, E, E, E, E, E, E },
        { B, E, E, E, E, E, E, E, E, B },
        { E, E, E, E, E, E, E, E, E, E },
        { E, E, E, E, E, E, E, E, E, E },
        { W, E, E, E, E, E, E, E, E, W },
        { E, E, E, S, E, E, E, E, E, E },
        { E, E, E, W, E, E, E, E, E, E },
        { E, E, E, E, E, E, W, E, E, E },
    };

    static final Piece[][] FINAL =
    {
        { E, E, E, E, E, E, E, E, S, W },
        { E, E, E, E, E, E, E, E, S, S },
        { E, E, E, E, E, E, E, E, E, E },
        { E, E, E, E, E, E, E, E, E, E },
        { E, E, E, E, E, E, E, E, E, E },
        { E, E, E, E, E, E, E, E, E, E },
        { E, E, E, E, E, E, E, E, E, E },
        { E, E, E, E, E, E, E, S, S, S },
        { E, E, E, E, E, E, S, S, E, E },
        { E, E, E, E, E, E, S, E, E, W },
    };
    static final Piece[][] EDGE =
    {
        { E, E, E, E, E, E, E, S, S, W },
        { E, E, E, E, E, E, E, S, S, E },
        { E, E, E, E, E, E, E, E, S, E },
        { E, E, E, E, E, E, E, E, E, S },
        { E, E, E, S, S, S, E, E, E, E },
        { E, E, E, S, W, S, E, E, E, E },
        { E, E, E, S, S, S, E, E, E, E },
        { E, E, E, S, S, S, E, S, E, S },
        { S, S, E, E, E, E, S, S, S, S},
        { W, S, E, E, E, E, S, S, S, W },
    };

    static final Piece[][] SID =
    {
        { E, E, E, B, E, E, E, E, E, E },
        { E, E, E, E, E, E, E, E, E, E },
        { E, E, E, E, E, E, E, E, E, E },
        { B, E, E, E, E, E, E, E, E, B },
        { E, E, E, E, E, E, E, E, E, E },
        { E, E, E, E, E, E, E, E, E, E },
        { W, E, E, E, E, S, B, E, E, W },
        { E, E, E, S, E, E, E, E, E, E },
        { E, E, E, W, E, E, E, E, E, E },
        { E, E, E, E, E, E, W, E, E, E },
    };




    static final Set<Square> REACHABLEFROMTESTSQUARES =
            new HashSet<>(Arrays.asList(
                    Square.sq(5, 5),
                    Square.sq(4, 5),
                    Square.sq(4, 4),
                    Square.sq(6, 4),
                    Square.sq(7, 4),
                    Square.sq(6, 5),
                    Square.sq(7, 6),
                    Square.sq(8, 7)));

}
