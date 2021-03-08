/**
 * Class that is used in storing position (i, j)
 */
public class IntPair {
    public int i_pos;
    public int j_pos;

    /**
     * Initialise IntPair using position (i, j)
     * @param i x-coordinate
     * @param j y-coordinate
     */
    IntPair(int i, int j){
        this.i_pos = i;
        this.j_pos = j;
    }

    /**
     * Override comparing function "equals" - object is also considered
     * the same if it contains the same position (i, j) as another
     * @param object what to compare this IntPair with
     */
    @Override
    public boolean equals(Object object){
        if (object != null && getClass() == object.getClass()) {
            IntPair t = (IntPair)object;
            return this.i_pos == t.i_pos && this.j_pos == t.j_pos;
        }
        return false;
    }
}
