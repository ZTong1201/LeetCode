import org.junit.Test;
import static org.junit.Assert.*;
import java.util.*;

public class backspaceCompare {

    /**
     * Given two strings S and T, return if they are equal when both are typed into empty text editors. # means a backspace character.
     *
     * Note:
     *
     * 1 <= S.length <= 200
     * 1 <= T.length <= 200
     * S and T only contain lowercase letters and '#' characters.
     *
     * Approach 1: Stack
     * 根绝字符串构建方式，可以得知，若遇到一个新的字符，就将其压栈，若遇到'#'，就将栈顶字符顶出（相当于回退一个字符），遍历完整个字符串，可以得出一个新的字符串，
     * 将两个建立好的字符串进行比对即可
     *
     * Time: O(S + T) 需要分别遍历两个字符串建立生成的字符串
     * Space: O(max(S, T)) 建立字符串时，若输入字符没有'#'，则需要将全部字符压栈
     */
    public boolean backspaceCompareStack(String S, String T) {
        return buildString(S).equals(buildString(T));
    }

    private String buildString(String s) {
        Stack<Character> stack = new Stack<>();
        for(int i = 0; i < s.length(); i++) {
            char c = s.charAt(i);
            if(c != '#') {
                stack.push(c);
            } else if(!stack.isEmpty()) {
                //注意不能从空栈中顶出元素
                stack.pop();
            }
        }
        return String.valueOf(stack);
    }


    /**
     * Follow up:
     * Can you solve it in O(N) time and O(1) space?
     *
     * Approach 2: Two Pointers
     * 在遍历整个字符串时，对于某个字符，其可能在最终生成字符中，也可能不在，这取决于在它之后有多少个backspace，因此可以考虑倒序遍历字符串，记录经过了多少个
     * backspace，在遇到非退格字符时，将其跳过相应数目即可。在跳过对应的字符后，判断当前位置两字符是否相同，或者判断是否有某个字符串被遍历完毕。若出现字符串
     * 先遍历结束或同位置两字符不等的情况，返回false。否则，继续向前遍历，直到最终结果
     *
     * Time: O(S + T) 仍然需要遍历两个字符串各一遍
     * Space: O(1) 只需要两个pointer来查看相应字符或跳过相应字符
     */
    public boolean backspaceCompareTwoPointers(String S, String T) {
        int len1 = S.length(), len2 = T.length();
        int i = len1 - 1, j = len2 - 1;
        //记录S和T中从后往前看的backspace数量
        int skipS = 0, skipT = 0;
        //只要有一个字符串没有遍历完毕，都要继续进行循环
        while(i >= 0 || j >= 0) {
            while(i >= 0) {
                //首先先记录S中到目前为止有多少backspace
                if(S.charAt(i) == '#') {
                    skipS++;
                    i--;
                } else if(skipS > 0) {
                    //若有backspace存在，则需要在S中跳过相应的字符说，因为这些字符不会在最终生成的字符串中
                    skipS--;
                    i--;
                } else {
                    break;
                }
            }
            //同理，记录T中的backspace数，并倒序地跳过相应字符数目
            while(j >= 0) {
                if(T.charAt(j) == '#') {
                    skipT++;
                    j--;
                } else if(skipT > 0) {
                    skipT--;
                    j--;
                } else {
                    break;
                }
            }
            //在跳过适当的字符数后，若两字符串还有剩余字符，说明该字符会出现在生成字符的相同位置
            //若两字符不相等，则可以直接返回false
            if(i >= 0 && j >= 0 && S.charAt(i) != T.charAt(j)) {
                return false;
            }
            //若两个字符串只有一个遍历完毕，另一个还有多余字符，说明生成字符长度无法匹配，直接返回false即可
            if((i >= 0) != (j >= 0)) {
                return false;
            }
            i--;
            j--;
        }
        return true;
    }


    @Test
    public void backspaceCompareTest() {
        /**
         * Example 1:
         * Input: S = "ab#c", T = "ad#c"
         * Output: true
         * Explanation: Both S and T become "ac".
         */
        assertTrue(backspaceCompareStack("ab#c", "ad#c"));
        assertTrue(backspaceCompareTwoPointers("ab#c", "ad#c"));
        /**
         * Example 2:
         * Input: S = "ab##", T = "c#d#"
         * Output: true
         * Explanation: Both S and T become "".
         */
        assertTrue(backspaceCompareStack("ab##", "c#d#"));
        assertTrue(backspaceCompareTwoPointers("ab##", "c#d#"));
        /**
         * Example 3:
         * Input: S = "a##c", T = "#a#c"
         * Output: true
         * Explanation: Both S and T become "c".
         */
        assertTrue(backspaceCompareStack("a##c", "#a#c"));
        assertTrue(backspaceCompareTwoPointers("a##c", "#a#c"));
        /**
         * Example 4:
         * Input: S = "a#c", T = "b"
         * Output: false
         * Explanation: S becomes "c" while T becomes "b".
         */
        assertFalse(backspaceCompareStack("a#c", "b"));
        assertFalse(backspaceCompareTwoPointers("a#c", "b"));
    }
}
