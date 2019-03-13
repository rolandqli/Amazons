package amazons;

import static amazons.Move.isGrammaticalMove;

/** A Player that takes input as text commands from the standard input.
 *  @author Roland Li
 */
class TextPlayer extends Player {

    /** A new TextPlayer with no piece or controller (intended to produce
     *  a template). */
    TextPlayer() {
        this(null, null);
    }

    /** A new TextPlayer playing PIECE under control of CONTROLLER. */
    private TextPlayer(Piece piece, Controller controller) {
        super(piece, controller);
    }

    @Override
    Player create(Piece piece, Controller controller) {
        return new TextPlayer(piece, controller);
    }

    @Override
    String myMove() {
        System.out.println(board().turn());
        while (true) {
            String line = _controller.readLine();
            if (line == null) {
                return "quit";
            } else if (!isGrammaticalMove(line)) {
                return line;
            } else {
                return line;
            }
        }
    }
}
