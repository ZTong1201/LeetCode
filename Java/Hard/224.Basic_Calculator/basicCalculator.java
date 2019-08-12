import org.junit.Test;
import static org.junit.Assert.*;
import java.util.*;

public class basicCalculator {

    /**
     * Implement a basic calculator to evaluate a simple expression string.
     *
     * The expression string may contain open ( and closing parentheses ), the plus + or minus sign -, non-negative integers
     * and empty spaces
     *
     * Note:
     * You may assume that the given expression is always valid.
     * Do not use the eval built-in library function.
     *
     * Approach 1: Stack
     * 本质上，此题可以将结果分段计算，然后在遇到新的block（即左括号时），将当前结果和该block前的符号记在stack中，然后更新符号和分段结果，继续向前遍历。
     * 同时，把减法看成是将其后的数字变为相反数，那么整个表达式一直其实是一直在计算加法。因为表达式中只有加减法，可以直接用一个整数sign表示，1表示当前为+，
     * -1表示当前为-
     * 算法如下：
     * 1.若当前字符为数字，则将当前数字存下来，继续向前遍历，直到得到所有位数（输入可能存在多位数字）
     * 2.若字符为'+'，说明需要将之前结果计算，即将之前记录的数字num乘上之前记录的sign，加在result上。计算完毕后。将sign记为1，然后将数字num归零。继续遍历
     * 3.若字符为'-'，与'+'时基本完全相同，需要将之前的结果计算加在result上，然后将数字num归零，不同的是sign要记为-1
     * 4.若字符为'('，这是最重要的部分，当遇到左括号时，说明即将有一个新的block需要计算，然后要把新block的结果与之前的结果相加减
     * e.g. 5 + (7 - (8 + 9))当遍历到7前的左括号时，后续算法需要计算整个(7 - (8 + 9))的值，然后5 + 这个值，同理，当遍历到8前的括号时，需要计算（8 + 9)
     * 的值，然后7 - 这个值。所以当遇到左括号时，需要把之前的计算结果和符号sign一起压栈，然后彻底更新result和sign，因为要重新开始计算新的block值
     * 5.若字符为')'，此时需要将之前的结果结算。首先要将block内结果计算完毕，与之前相同，result要加上sign * num，同时将现在的result的结果与栈顶元素
     * 相乘，即得到之前记录的符号，然后再将此结果与先前的结果相加。即得到整个block内的结果。
     *
     * 需要注意的是，输入表达式不一定会以')'结尾，所以很有可能最后的数字没有结算完毕。因此最后需要返回计算下来的result + (sign * num)，若最后一位是')'，
     * 则最后的num是0，不影响最终结果
     *
     * Time: O(n) 只需遍历一遍字符串
     * Space: O(n) 最坏情况下，需要将结果与符号一直压栈 e.g. (1+(2+(3+(4+(5+...)))，此时需要O(n)空间
     */
    public int calculate(String s) {
        Stack<Integer> stack = new Stack<>();
        //记录的是累积结果，同时也可以表示每一个block的结果
        int res = 0;
        //可以认为在表达式前加上0 +，不影响最后结果
        int num = 0;
        int sign = 1;
        for(int i = 0; i < s.length(); i++) {
            char c = s.charAt(i);
            //若当前字符为数字，则要将数字所有位数记录下来
            if(Character.isDigit(c)) {
                num = num * 10 + c - '0';
            } else if(c == '+' || c == '-') {
                //当字符为运算符时，需要结算之前结果，然后更新当前运算符以及归零数字即可
                res += sign * num;
                num = 0;
                sign = c == '+' ? 1 : -1;
            } else if(c == '(') {
                //当字符为左括号时，需要计算新的block值，
                //则要把之前的结果与符号压栈，以便后续计算
                //同时初始化符合与block结果(res)，就像一切从头开始
                //左括号前只可能是运算符或空格，之前结果都已结算完毕
                stack.push(res);
                stack.push(sign);

                res = 0;
                sign = 1;
            } else if(c == ')') {
                //若当前结果为右括号，右括号前字符只可能是空格或数字
                //因此需要先结算之前结果
                res += sign * num;
                //然后要将当前block结果配上正确的符号，与之前结果相加，更新成新的累积结果
                res *= stack.pop();
                res += stack.pop();
                //之前的符号与数字都已结算完毕，需要回到初始状态，继续下一步遍历
                //事实上，右括号之后的第一个字符只可能是运算符或者空格
                //遇到运算符时,sign会再次更新，因此不需要此时更新运算符sign
                num = 0;
            }
        }
        //需要注意表达式的最后一位可能不是')'，此时最后一位数字还未结算完毕。因此需要加在结果上
        //若最后一位是')'，则num会是0，不影响最终结果
        return res + (sign * num);
    }

    @Test
    public void calculateTest() {
        /**
         * Example 1:
         * Input: "1 + 1"
         * Output: 2
         */
        assertEquals(2, calculate("1 + 1"));
        /**
         * Example 2:
         * Input: " 2-1 + 2 "
         * Output: 3
         */
        assertEquals(3, calculate(" 2-1 + 2"));
        /**
         * Example 3:
         * Input: "(1+(4+5+2)-3)+(6+8)"
         * Output: 23
         */
        assertEquals(23, calculate("(1+(4+5+2)-3)+(6+8)"));
    }
}
