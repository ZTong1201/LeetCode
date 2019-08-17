import org.junit.Test;
import static org.junit.Assert.*;
import java.util.*;

public class addOperators {

    /**
     * Given a string that contains only digits 0-9 and a target value, return all possibilities to add binary operators
     * (not unary) +, -, or * between the digits so they evaluate to the target value.
     *
     * Approach 1: DFS
     * 此题只能用枚举法来做，给定长度为n的字符串，每个字符之间可以插入四种元素，"+"，"-"，"*"，分别代表运算符，同时也可以插入""，表示将前后数字看成多位数字。
     * 因此对于n - 1个空位，每个空位都有4中选择，将所有的组合枚举出来，在计算该字符串的值，如果等于target，就可以把该组合放入最终结果。
     *
     * 但若等到组合的最后在evaluate值，运行时间会很慢，可以在dfs过程中将当前表达式的值计算出来。在dfs过程中其实要记录四个变量。
     * pos：表示当前字符串遍历到了哪个位置，len：表示当前表达式（插入了其他字符后）的长度，prev：当前数字之前的值（不是之前的数字，而是之前的运算值，例如
     * 之前的节点为2*3，那么要记下2*3 = 6，而不是3），curr：当前表达式的值。
     *
     * 对应插入四种字符，有不同的处理方式：
     * 首先将当前数字初始化为0，然后向后遍历得到新数字
     * 1.若将前后两数字看成一个数字，无需多余操作，只需继续向前遍历，即pos + 1
     * 2.若插入"+"，则当前表达式的值curr需要 + 此次操作新得到的数字，然后更新prev值为当前数字n
     * 3.若插入"-"，则当前表达式的值curr需要 - 此次操作新得到的数字，然后更新prev值为当前数字相反数-n
     * 4.若插入"*"，较为复杂，因为乘法优先级较高，因此要将curr先减去prev值，然后将prev与n相乘，最后加在curr上，即curr = curr - prev + prev * n
     *   e.g. 1 + 2 * 3，在遍历到3时，prev为2，curr = 1 + 2 = 3，要得到当前的表达式的值，需要3 - 2 + 2 * 3 = 7
     *
     * 为了节省空间，可以在最开始将表达式设为一个空的size位2n的char数组，因为符合要求的表达式的长度最大为n（n个输入字符） + n - 1（n - 1个间隔都填上新
     * 字符）= 2n - 1
     *
     * DFS过程中会有很多坑，其中包括
     * 1.若在index为0时，前面不能填运算符，所以此处需要直接递归调用一次函数，即相后得到更长的数字，或插入运算符
     * 2.多位数字不能以0开头，若当前数字为0，则无法继续向前遍历，只能将0传入递归函数
     * 3.字符串很长可能会存在integer overflow的情况，因此需要用long来存储prev，curr以及当前数字n
     *
     * Time: O(4^(n -1)) = O(4^n) n个数字中间有你n - 1个空位，空位有四种可能"+", "-", "*"和""（代表可以将前后两个数字看成一个数字，例如12）
     * Space: O(n)，为方便改变表达式，避免拷贝，可以用一个size为2n的char数组来存放数字，或者符号。同时开始的时候会把input string也转化为char数组。
     *        同时递归深度也为O(n)，总的空间数量级也为O(n)
     */
    private char[] num;
    private int target;
    private char[] expr;
    private List<String> res;

    public List<String> addOperators(String num, int target) {
        this.res = new ArrayList<>();
        //将string转化为char array方便寻址
        this.num = num.toCharArray();
        this.target = target;
        //将表达式记为一个char array，长度设为2n，因为最终结果表达式最大为2n - 1
        this.expr = new char[2 * this.num.length];
        //从pos = 0，len = 0，prev = 0，curr = 0开始进行dfs
        dfs(0, 0, 0, 0);
        return this.res;
    }

    /**
     * 辅助函数，用于dfs寻找所有可能表达式
     * @param pos  此次遍历的在字符串中的开始位置
     * @param len  当前表达式的总长度，用于之后将char array转化成字符串
     * @param prev 上一个节点计算的值，注意要用long
     * @param curr 当前表达式的结果值
     */
    private void dfs(int pos, int len, long prev, long curr) {
        //base case，当字符串中所有元素都被用过之后
        if(pos == num.length) {
            //查看当前表达式的值是否等于target，等于就将char array转化为string放入最终结果
            if(curr == target) {
                res.add(new String(expr, 0, len));
                return;
            }
        }

        //记录下当前遍历开始时的位置，和表达式长度，遍历过程中，这些值会改变
        //而在之后判定当前数字是否valid，以及在合适位置插入运算符时需要用到
        int s = pos;
        int l = len;
        //第一坑！注意，当此次遍历不是从0开始，那么可以意味着可以插入新的运算符，或插入新数字连成更长数字
        //此时需要从当前表达式长度之后一位开始插入新字符
        if(s != 0) {
            len++;
        }
        //记录当前的数字，注意要用long存储，因为数字可能位数过长
        long n = 0;
        //开始dfs，遍历字符串
        while(pos < num.length) {
            //第二坑！若开始位置字符为'0'，则不能继续得到更多位的数字
            if(num[s] == '0' && pos > s) break;
            //否则的话，就继续向前遍历，得到更长位数的数字
            n = n * 10 + num[pos] - '0';
            //将当前得到的数字都放入expr中，注意要更新expr的长度
            expr[len++] = num[pos++];
            //第三坑！若当前index为0，即刚刚开始遍历字符串，之前没有任何运算符
            //需要立即进行dfs，在后面插入运算符或填数字
            if(s == 0) {
                dfs(pos, len, n, n);
                continue;
            }

            //接下来可以插入运算符，记得要在dfs前插入运算符，然后同时更新prev和curr
            expr[l] = '+';
            dfs(pos, len, n, curr + n);
            expr[l] = '-';
            dfs(pos, len, -n, curr - n);
            expr[l] = '*';
            //注意乘法的优先级比加减法高，需要特殊处理prev和curr
            dfs(pos, len, prev * n, curr - prev + prev * n);

        }
    }

    @Test
    public void addOperatorsTest() {
        /**
         * Example 1:
         * Input: num = "123", target = 6
         * Output: ["1+2+3", "1*2*3"]
         */
        String num1 = "123";
        Set<String> actual1 = new HashSet<>(addOperators(num1, 6));
        Set<String> expected1 = new HashSet<>(Arrays.asList("1+2+3", "1*2*3"));
        assertEquals(expected1.size(), actual1.size());
        for(String s : expected1) {
            assertTrue(actual1.contains(s));
        }
        /**
         * Example 2:
         * Input: num = "232", target = 8
         * Output: ["2*3+2", "2+3*2"]
         */
        String num2 = "232";
        Set<String> actual2 = new HashSet<>(addOperators(num2, 8));
        Set<String> expected2 = new HashSet<>(Arrays.asList("2*3+2", "2+3*2"));
        assertEquals(expected2.size(), actual2.size());
        for(String s : expected2) {
            assertTrue(actual2.contains(s));
        }
        /**
         * Example 3:
         * Input: num = "105", target = 5
         * Output: ["1*0+5","10-5"]
         */
        String num3 = "105";
        Set<String> actual3 = new HashSet<>(addOperators(num3, 5));
        Set<String> expected3 = new HashSet<>(Arrays.asList("1*0+5", "10-5"));
        assertEquals(expected3.size(), actual3.size());
        for(String s : expected3) {
            assertTrue(actual3.contains(s));
        }
        /**
         * Example 4:
         * Input: num = "00", target = 0
         * Output: ["0+0", "0-0", "0*0"]
         */
        String num4 = "00";
        Set<String> actual4 = new HashSet<>(addOperators(num4, 0));
        Set<String> expected4 = new HashSet<>(Arrays.asList("0+0", "0-0", "0*0"));
        assertEquals(expected4.size(), actual4.size());
        for(String s : expected4) {
            assertTrue(actual4.contains(s));
        }
        /**
         * Example 5:
         * Input: num = "3456237490", target = 9191
         * Output: []
         */
        String num5 = "3456237490";
        Set<String> actual5 = new HashSet<>(addOperators(num5, 9191));
        assertEquals(0, actual5.size());
    }
}
