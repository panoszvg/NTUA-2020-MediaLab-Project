/**
 * User-defined exception specific to the game
 * Is thrown when a ship is about to be placed on top of another ship
 */
public class OverlapTilesException extends Exception {

    /**
     * Create custom Exception using message given by program
     * @param message String to be displayed
     */
    public OverlapTilesException(String message) {
        super(message);
    }
}
