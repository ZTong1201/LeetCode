
public class Add_Binary {

    public static String addBinary(String a, String b) {
        StringBuilder res = new StringBuilder();
        int sum = 0;
        int i = a.length() - 1;
        int j = b.length() - 1;
        while(i >= 0 || j >= 0 || sum == 1) {
            sum += ((i >= 0) ? a.charAt(i) - '0' : 0);
            sum += ((j >= 0) ? b.charAt(j) - '0' : 0);
            res.append((char) (sum % 2 + '0'));
            sum /= 2;
            i -= 1;
            j -= 1;
        }
        return res.reverse().toString();
    }

    public static void main(String[] args) {
        System.out.println("Expect: 100" + " Actual: " + addBinary("11", "1"));
        System.out.println("Expect: 10101" + " Actual: " + addBinary("1010", "1011"));
    }
}