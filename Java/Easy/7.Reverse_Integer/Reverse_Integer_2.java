import java.util.*;

public class Reverse_Integer_2 {

    public static int reverse(int x) {
        Stack<Character> stack = new Stack<>();
        String intString = String.valueOf(x);
        for(int i = 0; i < intString.length(); i++) {
            if(intString.charAt(i) != '-') {
                stack.push(intString.charAt(i));
            }
        }
        StringBuilder res = new StringBuilder();
        while(!stack.isEmpty()) {
            res.append(stack.pop());
        }
        try {
            if(x < 0) {
                return (-1) * Integer.valueOf(res.toString());
            } else {
                return Integer.valueOf(res.toString());
            }
        } catch(Exception e) {
            return 0;
        }
    }

    public static void main(String[] args) {
        System.out.println("Expected: 321" + " Actual: " + Reverse_Integer_2.reverse(123));
        System.out.println("Expected: -321" + " Actual: " + Reverse_Integer_2.reverse(-123));
        System.out.println("Expected: 21" + " Actual: " + Reverse_Integer_2.reverse(120));
        System.out.println("Expected: 0" + " Actual: " + Reverse_Integer_2.reverse(1546789209));
    }
}