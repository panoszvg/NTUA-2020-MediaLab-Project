public class IntPair {
    public int i_pos;
    public int j_pos;

    IntPair(int i, int j){
        this.i_pos = i;
        this.j_pos = j;
    }

    @Override
    public boolean equals(Object object){
        if (object != null && getClass() == object.getClass()) {
            IntPair t = (IntPair)object;
            return this.i_pos == t.i_pos && this.j_pos == t.j_pos;
        }
        System.out.print("f ");
        return false;
    }
}
