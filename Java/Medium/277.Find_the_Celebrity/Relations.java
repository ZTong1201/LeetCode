public class Relations {
    public int[][] relations;

    public Relations() {}

    public Relations(int[][] _relations) {
        this.relations = _relations;
    }

    public boolean knows(int i, int j) {
        return this.relations[i][j] == 1;
    }
}
