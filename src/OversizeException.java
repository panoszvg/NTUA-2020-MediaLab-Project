/**
 * User-defined exception specific to the game
 * Is thrown when a ship is about to be placed outside the board
 */
public class OversizeException extends Exception {
    private static final long serialVersionUID = -1934904173018129307L;

    /**
     * Create custom Exception using message given by program
     * @param message String to be displayed
     */
    public OversizeException(String message) {
        super(message);
    }
}
