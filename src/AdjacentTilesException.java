/**
 * User-defined exception specific to the game
 * Is thrown when a ship is about is placed next another ship
 */
public class AdjacentTilesException extends Exception {

    /**
     * Create custom Exception using message given by program
     * @param message String to be displayed
     */
    public AdjacentTilesException(String message) {
        super(message);
    }
}
