public class Division extends Operator {

    @Override
    public int evaluate() {
        return left.evaluate() / right.evaluate();
    }
}
