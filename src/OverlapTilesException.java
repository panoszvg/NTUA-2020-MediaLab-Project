/**
 * User-defined exception specific to the game
 * Is thrown when a ship is about to be placed on top of another ship
 */
public class OverlapTilesException extends Exception {
    private static final long serialVersionUID = -7963006316350982424L;

    /**
     * Create custom Exception using message given by program
     * @param message String to be displayed
     */
    public OverlapTilesException(String message) {
        super(message);
    }
}
