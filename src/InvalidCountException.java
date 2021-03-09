/**
 * User-defined exception specific to the game
 * Is thrown when the ships given aren't exactly 5 or
 * aren't of different types; meaning input is incorrect
 */
public class InvalidCountException extends Exception {

    /**
     * Create custom Exception using message given by program
     * @param message String to be displayed
     */
    public InvalidCountException(String message) {
        super(message);
    }
}
