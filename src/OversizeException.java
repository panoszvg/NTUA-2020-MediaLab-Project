/**
 * User-defined exception specific to the game
 * Is thrown when a ship is about to be placed outside the board
 */
public class OversizeException extends Exception {

    /**
     * Create custom Exception using message given by program
     * @param message String to be displayed
     */
    public OversizeException(String message) {
        super(message);
    }
}
