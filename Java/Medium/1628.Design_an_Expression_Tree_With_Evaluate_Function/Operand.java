public class Operand extends Node {
    
    private final int value;

    public Operand(int value) {
        this.value = value;
    }

    @Override
    public int evaluate() {
        return this.value;
    }
}
