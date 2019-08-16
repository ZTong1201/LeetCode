import org.junit.Test;
import static org.junit.Assert.*;
import java.util.*;

public class generateParenthesis {

    /**
     * Given n pairs of parentheses, write a function to generate all combinations of well-formed parentheses.
     *
     * Approach 1: Brute Force
     * 对于输入n，总共有2n个位置需要填上"("或者")"，可以用枚举法，对于每个位置，放上"("或")"其中之一，当2n个位置都填满后，最后判断一遍当前的string
     * 是否valid。若valid，则把当前结果放入最终list
     *
     * Time: O(n*2^2n)，2n个格子，每个格子有两种可能"("或者")"，所以总共要算2^2n中组合，同时判断2n个括号是否匹配，需要O(N)的时间，总的runtime位O(N*2^2N)
     * Space: O(2n*2n) = O(n^2) 递归的最大深度即为到char数组的尽头，即2n，同时每一层需要一个size为2n的char数组，所以空间为2n*2n = O(n^2)
     */
    public List<String> generateParenthesisBruteForce(int n) {
        List<String> res = new ArrayList<>();
        generateAll(new char[2 * n], 0, res);
        return res;
    }

    //用一个char数组来放置"("或")"，index记录当前所在数组的index
    private void generateAll(char[] chars, int index, List<String> res) {
        //若2n个位置均被填满，则检查是否可以放入最终list
        if(index == chars.length) {
            if(isValid(chars)) { //若当前组合是valid，则将其加入list
                res.add(new String(chars)); //注意将char数组转化为string
                return;
            }
        } else { //还有位置没填好，则填上"("或")"，继续先前递归
            chars[index] = '(';
            generateAll(chars, index + 1, res);
            chars[index] = ')';
            generateAll(chars, index + 1, res);
        }
    }

    //判断当前char数组的"("和")"的个数是否相同
    private boolean isValid(char[] chars) {
        int count = 0;
        for(char c : chars) {
            if(c == '(') count++;
            else if(c == ')') count--;
            if(count < 0) return false;
        }
        return count == 0;
    }


    @Test
    public void generateParenthesisBruteForceTest() {
        /**
         * Example:
         * Input: 3
         * Output:
         * [
         *   "((()))",
         *   "(()())",
         *   "(())()",
         *   "()(())",
         *   "()()()"
         * ]
         */
        Set<String> actual = new HashSet<>(generateParenthesisBruteForce(3));
        Set<String> expected = new HashSet<>(Arrays.asList("((()))", "(()())", "(())()", "()(())", "()()()"));
        assertEquals(expected.size(), actual.size());
        for(String s : expected) {
            assertTrue(actual.contains(s));
        }
    }

    /**
     * Approach 2: Backtracking
     * 通过Brute Force的过程可以发现，有很多无谓的分支，若想使最终结果为balanced，那么首先要保证左右括号数目相等。因此可以记录左右括号数目的方式，选择放入
     * 左括号还是右括号。当左括号的数目小于n，则可以继续放入左括号，若当前右括号的数目小于左括号数目，则可以继续放入右括号。这样保证得到的结果是balanced。
     *
     * Time: 时间复杂度比较难分析，但是可以肯定的是，最后结果的个数一定小于C(n, 2n)即从2n个位置中选出n个位置放置左括号 = 2n! / n!
     *      = (2n)*(2n - 1)*(2n - 2)*...*(n + 1) 总共n项。所以大致的upper bound为O((4n)^n)，因为总的结果会远远小于这个数目，但大致上也需要exponential
     *      的时间
     * Space: O(n^2) 递归深度仍为2n，同时在递归过程中依旧会存当前的sequence，每一次需要O(n)空间，所以总的空间为O(n^2)
     */
    public List<String> generateParenthesisBacktrack(int n) {
        List<String> res = new ArrayList<>();
        //利用stringbuilder可以更好地利用空间
        backtrack(res, new StringBuilder(), 0, 0, n);
        return res;
    }

    private void backtrack(List<String> res, StringBuilder sb, int open, int close, int max) {
        //若已放入n个右括号，则根据左右括号数目的控制，当前一定为balanced结果，将其加入list
        if(close == max) {
            res.add(sb.toString());
            return;
        }

        if(open < max) {
            //若当前左括号不足n个，则持续添加左括号，并增加sb中左括号的数目
            backtrack(res, sb.append("("), open + 1, close, max);
            //backtrack，需要移除当前最后的左括号
            sb.deleteCharAt(sb.length() - 1);
        }
        if(close < open) {
            //若当前右括号数目小于左括号数目，说明sb不是balanced，那么添加右括号进sb
            //并增加右括号数目
            backtrack(res, sb.append(")"), open, close + 1, max);
            //backtrack，当前结果记录完毕后，需要持续删除最后一位的右括号
            sb.deleteCharAt(sb.length() - 1);
        }
    }

    @Test
    public void generateParenthesisBacktrackTest() {
        /**
         * Example:
         * Input: 3
         * Output:
         * [
         *   "((()))",
         *   "(()())",
         *   "(())()",
         *   "()(())",
         *   "()()()"
         * ]
         */
        Set<String> actual = new HashSet<>(generateParenthesisBacktrack(3));
        Set<String> expected = new HashSet<>(Arrays.asList("((()))", "(()())", "(())()", "()(())", "()()()"));
        assertEquals(expected.size(), actual.size());
        for(String s : expected) {
            assertTrue(actual.contains(s));
        }
    }

    /**
     * Approach 3: Dynamic Programming
     * 根据题意，其实可以求出i < n时，所有valid parenthesis的组合。可以假定当在求n的所有组合时，n - 1的所有组合已经可以算出。那么其实可以从n - 1的所有解
     * 出发，得到n的所有解。此题中n代表最终结果中有n对对称的左右括号。那么想得到n对括号的最终结果，可以从n - 1对已经valid的结果出发，加上一对括号即可。
     * 在加上这一对括号之后，其实是将原来的n - 1对括号分成了两段，即有i对括号在新加的一对括号内部，剩下的n - 1 - i对括号在新加的一对括号外部。
     * 那么对应的问题又变成了，总共有多少种i对括号的排布方法，和有多少种n - 1 - j对括号排布的方法。将它们一一组合起来就是最终结果。
     *
     *
     * Time: O(n * exponential) 对于从1到n的所有括号数，都需要大概O(n^n)的时间得到对应结果
     * Space: O(n^n) 需要将从1到n每一个括号数下的valid组合存在一个list中
     */
    public List<String> generateParenthesisDP(int n) {
        List<List<String>> res = new ArrayList<>();
        res.add(new ArrayList<>(Arrays.asList("")));
        for(int i = 1; i <= n; i++) {
            List<String> list = new ArrayList<>();
            for(int j = 0; j < i; j++) {
                for(String first : res.get(j)) {
                    for(String second : res.get(i - 1 - j)) {
                        list.add("(" + first + ")" + second);
                    }
                }
            }
            res.add(list);
        }
        return res.get(res.size() - 1);
    }

    @Test
    public void generateParenthesisDPTest() {
        /**
         * Example:
         * Input: 3
         * Output:
         * [
         *   "((()))",
         *   "(()())",
         *   "(())()",
         *   "()(())",
         *   "()()()"
         * ]
         */
        Set<String> actual = new HashSet<>(generateParenthesisDP(3));
        Set<String> expected = new HashSet<>(Arrays.asList("((()))", "(()())", "(())()", "()(())", "()()()"));
        assertEquals(expected.size(), actual.size());
        for(String s : expected) {
            assertTrue(actual.contains(s));
        }
    }
}
