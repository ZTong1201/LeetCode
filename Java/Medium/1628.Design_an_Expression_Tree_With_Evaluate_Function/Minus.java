public class Minus extends Operator {

    @Override
    public int evaluate() {
        return left.evaluate() - right.evaluate();
    }
}
