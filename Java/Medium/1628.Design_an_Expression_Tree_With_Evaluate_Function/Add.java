public class Add extends Operator {

    @Override
    public int evaluate() {
        return left.evaluate() + right.evaluate();
    }
}
