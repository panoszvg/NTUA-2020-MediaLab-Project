/**
 * User-defined exception specific to the game
 * Is thrown when Player hits a position that has already 
 * been hit - is needed to update the front end
 */
public class AlreadyHitException extends Exception {
   private static final long serialVersionUID = 4162002099423146487L;

    /**
     * Create custom Exception using message given by program
     * @param message String to be displayed
     */
   public AlreadyHitException(String message) {
        super(message);
    }
}
