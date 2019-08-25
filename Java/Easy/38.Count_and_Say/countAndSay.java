import org.junit.Test;
import static org.junit.Assert.*;

public class countAndSay {

    /**
     * The count-and-say sequence is the sequence of integers with the first five terms as following:
     *
     * 1.     1
     * 2.     11
     * 3.     21
     * 4.     1211
     * 5.     111221
     * 1 is read off as "one 1" or 11.
     * 11 is read off as "two 1s" or 21.
     * 21 is read off as "one 2, then one 1" or 1211.
     *
     * Given an integer n where 1 ≤ n ≤ 30, generate the nth term of the count-and-say sequence.
     *
     * Note: Each term of the sequence of integers will be represented as a string.
     *
     * Approach 1: Recursion
     * 为了求n的时候的结果，需要先知道n - 1时候的结果，因此可以用recursion解决。首先明确base case，当n = 1时，直接返回"1"即可。随后计算n = 2的结果，对此
     * 需要遍历n = 1时的结果，将相邻并且相同的元素group起来，然后计算他们出现的count，然后依次把count和对应数字加入最终结果即可
     *
     * Time: O(n) 为了计算n的值，需要一直递归计算到1的值，然后往回计算
     * Space: O(n) 递归的深度也为n
     */
    public String countAndSayRecursive(int n) {
        //base case，若输入n为1，则返回"1"
        if(n == 1) {
            return "1";
        }
        //然后递归调用当前函数，得到n - 1的结果
        String s = countAndSayRecursive(n - 1);
        //根据上一次的结果，就可以计算当前的结果
        StringBuilder res = new StringBuilder();
        int count = 1;
        //需要遍历上一问的字符串得到最新结果
        for(int i = 0; i < s.length(); i++) {
            //注意边界条件
            if(i != s.length() - 1 && s.charAt(i) == s.charAt(i + 1)) {
                //若相邻两字符相等，累加当前的count
                count++;
            } else {
                //若不等，则可以将当前的count和字符放入结果中
                res.append(count);
                res.append(s.charAt(i));
                //然后将count重新更新为1
                count = 1;
            }
        }
        //res内存储的是当前的结果，将其返回，然后继续向前计算
        return res.toString();
    }

    /**
     * Approach 2: Iteration
     * 也可以将上述过程转化为iteration。整个逻辑同理
     *
     * Time: O(n)
     * Space: O(n)
     */
    public String countAndSayIterative(int n) {
        String res = "1";
        int i = 1;
        while(i < n) {
            //将count初始化为0，必将当前字符初始化为res的的一个字符
            int count = 1;
            char c = res.charAt(0);
            //用一个stringbuilder记录此次循环的结果
            StringBuilder sb = new StringBuilder();
            for(int j = 1; j <= res.length(); j++) {
                //注意边界条件
                if(j != res.length() && res.charAt(j) == c) {
                    count++;
                } else {
                    sb.append(count);
                    sb.append(c);
                    //当循环没有溢出边界时，需要更新count和当前字符
                    if(j != res.length()) {
                        count = 1;
                        c = res.charAt(j);
                    }
                }
            }
            //更新本次结果，以便下次遍历
            res = sb.toString();
            i++;
        }
        return res;
    }


    @Test
    public void countAndSayTest() {
        /**
         * Example 1:
         * Input: 1
         * Output: "1"
         */
        assertEquals("1", countAndSayRecursive(1));
        assertEquals("1", countAndSayIterative(1));
        /**
         * Example 2:
         * Input: 4
         * Output: "1211"
         */
        assertEquals("1211", countAndSayRecursive(4));
        assertEquals("1211", countAndSayIterative(4));
        /**
         * Example 3:
         * Input: 5
         * Output: "111221"
         */
        assertEquals("111221", countAndSayRecursive(5));
        assertEquals("111221", countAndSayIterative(5));
        /**
         * Example 6:
         * Input: 6
         * Output: "312211"
         */
        assertEquals("312211", countAndSayRecursive(6));
        assertEquals("312211", countAndSayIterative(6));
    }
}
