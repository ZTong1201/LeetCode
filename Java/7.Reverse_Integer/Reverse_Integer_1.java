import java.util.*;

public class Reverse_Integer_1 {

    public static int reverse(int x) {
        int res = 0;
        while(x != 0) {
            int temp = x % 10;
            x /= 10;
            if((res > Integer.MAX_VALUE / 10) || (res == Integer.MAX_VALUE / 10 && temp > 7)) return 0;
            if((res < Integer.MIN_VALUE / 10) || (res == Integer.MIN_VALUE / 10 && temp < -8)) return 0;
            res = res * 10 + temp;
        }
        return res;
    }

    public static void main(String[] args) {
        System.out.println("Expected: 321" + " Actual: " + Reverse_Integer_1.reverse(123));
        System.out.println("Expected: -321" + " Actual: " + Reverse_Integer_1.reverse(-123));
        System.out.println("Expected: 21" + " Actual: " + Reverse_Integer_1.reverse(120));
        System.out.println("Expected: 0" + " Actual: " + Reverse_Integer_1.reverse(1546789209));
    }
}