import org.junit.Test;
import static org.junit.Assert.*;
import java.util.*;

public class basicCalculatorII {

    /**
     * Implement a basic calculator to evaluate a simple expression string.
     *
     * The expression string contains only non-negative integers, +, -, *, / operators and empty spaces .
     * The integer division should truncate toward zero.
     *
     * Note:
     *
     * You may assume that the given expression is always valid.
     * Do not use the eval built-in library function.
     *
     * Approach 1: Use Stack
     * 在遍历整个字符串的过程中，遇到空格就跳过，遇到数字就把数字保留下来（注意数字可能多位！），压入栈中。对于不同的运算符，要对入栈元素做不同的操作
     * 1.若符号为'+'，则将当前数字压入栈中
     * 2.若符号为'-'，则将将当前数字的相反数压入栈中
     * 3.若符号为'*'或'/'，则说明当前数字要与栈顶数字做计算，将计算后结果压栈
     * 需要注意的是，每遇到一个新的运算符，要将之前的运算结束。还有一种情况为，遍历到最后一个数字，后续不会再有运算符，也应将之前的运算彻底结束。
     * 最后栈中的元素，都是每部分运算之后的结果，可以直接将数字退栈，加在最终结果上
     *
     * Time: O(n) 需要遍历整个字符串
     * Space: O(n) 需要将所有数字压入栈中
     */
    public int calculateStack(String s) {
        Stack<Integer> stack = new Stack<>();
        int res = 0;
        //用来记录中间数字
        int num = 0;
        //第一个数字一定是原封不动的入栈，可以认为在整个表达式前加上一个0 +，所以最开始的运算符为'+'
        char sign = '+';
        for(int i = 0; i < s.length(); i++) {
            //若当前位置为数字，则将数字记录下来，注意数字多位情况
            if(Character.isDigit(s.charAt(i))) {
                num += s.charAt(i) - '0';
                while(i + 1 < s.length() && Character.isDigit(s.charAt(i + 1))) {
                    num = num * 10 + s.charAt(i + 1) - '0';
                    i++;
                }
            }
            //若遇到运算符，或运行到最后一个数字，则要更新当前结果（当字符不为数字且不为空时，一定为运算符）
            if(!Character.isDigit(s.charAt(i)) && s.charAt(i) != ' ' || i == s.length() - 1) {
                //更新要分四种情况
                if(sign == '+') stack.push(num);
                if(sign == '-') stack.push(-num);
                //若运算符为'*'或'/'，则当前数字要和栈顶数字（即前一位数字）进行计算，然后入栈
                if(sign == '*') stack.push(stack.pop() * num);
                if(sign == '/') stack.push(stack.pop() / num);
                //更新数字之后，将数字重新变为0，同时记下当前运算符
                sign = s.charAt(i);
                num = 0;
            }
        }
        while(!stack.isEmpty()) {
            res += stack.pop();
        }
        return res;
    }

    /**
     * Approach 2: Without Stack
     * 本质上，为了计算乘除法，只需要记住之前的数字与当前数字即可，因此可以省去stack的使用，在循环的过程中，根据之前运算符，和存下来的prev数字与curr数字
     * 进行运算，直到新运算符为'+'或'-'，将prev结果更新在最终结果上。同时在循环完成后，将最后的算出的数字更新到最终结果上。
     *
     * Time: O(n)
     * Space: O(1)
     */
    public int calculateWithoutStack(String s) {
        //记录之前计算的值，以便后续计算
        int prev = 0;
        //将最开始的符号设置成'+'，假定表达式前方有0 +
        char sign = '+';
        int res = 0;
        //num用来记录中间数字
        int num = 0;
        for(int i = 0; i < s.length(); i++) {
            //跳过所有空格
            if(s.charAt(i) == ' ') continue;
            //得到当前数字（注意数字多位情况）
            if(Character.isDigit(s.charAt(i))) {
                num = s.charAt(i) - '0';
                while(i + 1 < s.length() && Character.isDigit(s.charAt(i + 1))) {
                    num = num * 10 + s.charAt(i + 1) - '0';
                    i++;
                }
            }

            //之后，根据之前记录的符号，需要有四种操作
            //若之前的符号为'+'，则需要将之前计算的值加在最终结果上
            //同时将当前数字记录下来，以便后续计算
            if(sign == '+') {
                res += prev;
                prev = num;
            } else if(sign == '-') {
                //若之前符号为'-'，则同样需要把之前结果加在最终结果上，把当前数字相反数记录下来
                //e.g. 3 + 2*2 - 6/2，到达6时，sign为'-'，2*2=4已计算完毕，将4加在最终结果上，然后把-6记录下来
                res += prev;
                prev = -num;
            } else if(sign =='*') {
                //若遇到乘除，则需要一直更新当前结果，不会更新最终结果
                prev *= num;
            } else if(sign == '/') {
                prev /= num;
            }
            //因为输入表达式一定正确，所以最后一位一定是数字，则符号所能在的index最大值就是length - 2，要保证不会溢出边界
            //同时若数字后为空格，需要跳过，直到遇到一个新的运算符
            while(i + 1 < s.length() && s.charAt(i) == ' ') {
                i++;
            }
            sign = s.charAt(i);
        }
        //注意，最后的数值运算完毕后，还没有加到最终结果上，需要加上后返回结果
        return res + prev;
    }

    @Test
    public void calculateTest() {
        /**
         * Example 1:
         * Input: "3+2*2"
         * Output: 7
         */
        assertEquals(7, calculateStack("3+2*2"));
        assertEquals(7, calculateWithoutStack("3+2*2"));
        /**
         * Example 2:
         * Input: " 3/2 "
         * Output: 1
         */
        assertEquals(1, calculateStack(" 3/2 "));
        assertEquals(1, calculateWithoutStack(" 3/2 "));
        /**
         * Example 3:
         * Input: " 3+5 / 2 "
         * Output: 5
         */
        assertEquals(5, calculateStack(" 3+5 / 2 "));
        assertEquals(5, calculateWithoutStack(" 3+5 / 2 "));
        /**
         * Example 4:
         * Input: " 32 + 3/2 * 1"
         * Output: 33
         */
        assertEquals(33, calculateStack(" 32 + 3/2 * 1"));
        assertEquals(33, calculateWithoutStack(" 32 + 3/2 * 1"));
    }
}
