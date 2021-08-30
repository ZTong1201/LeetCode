import org.junit.Test;

import java.util.Stack;

import static org.junit.Assert.assertEquals;

public class decodeString {

    /**
     * Given an encoded string, return its decoded string.
     * <p>
     * The encoding rule is: k[encoded_string], where the encoded_string inside the square brackets is being repeated exactly k times.
     * Note that k is guaranteed to be a positive integer.
     * <p>
     * You may assume that the input string is always valid; No extra white spaces, square brackets are well-formed, etc.
     * <p>
     * Furthermore, you may assume that the original data does not contain any digits and that digits are only for those repeat numbers, k.
     * For example, there won't be input like 3a or 2[4].
     * <p>
     * Approach 1: Two Stacks
     * 因为decode要从内往外拆解，即先decode最里面的括号，再decode外边的括号，因此符合栈的要求，即先进来的元素要最后再考虑。此题可以用两个stack来解，分别记录
     * 当前substring所应该出现的次数，和当前substring本身。在遍历整个string的过程中，需要有如下几种操作：
     * 1.若遇到的是数字，考虑可能出现多位数字的情况，要一直遍历，拿出所有位数，存在num里，然后将其压栈
     * 2.若遇到的是字母，则直接将按当前顺序其放入最终结果
     * 3.若遇到'['，则说明这之后的字符需要作为一个新的substring，并且有它自己的出现次数（已经被步骤1记下，并压入计数的栈中），因此需要将先前记下的string放入
     * 记录substring的栈，然后重置string，来记录当前括号内的字符串
     * 4.若遇到']'，则说明当前的子串已经全部找到，那么需要根据stack中记下的出现频次，进行复制，然后将复制好的子串与先前记录的字符串结果（记录在栈中）相结合，
     * 得到更新后的字符串。然后将出现频次num重置为0
     * <p>
     * <p>
     * 为了节约string concatenation的时间，整个过程中都用stringbuilder代替。
     * <p>
     * Time: O(N)，需要遍历整个字符串进行decode
     * Space: O(N)，最坏情况下需要将除括号以外的所有字符记录在两个stack中
     */
    public String decodeStringStacks(String s) {
        if (s == null || s.length() == 0) return s;
        StringBuilder decodedString = new StringBuilder();
        //初始化两个stack，一个用来记录某个子串应该出现的次数，另一个记录当前子串的内容
        Stack<Integer> countStack = new Stack<>();
        Stack<String> patternStack = new Stack<>();
        int num = 0;
        for (int i = 0; i < s.length(); i++) {
            char curr = s.charAt(i);
            //若当前位置为数字，则需要将数字的所有位数都记录下来，更新num
            if (curr >= '0' && curr <= '9') {
                num = num * 10 + curr - '0';
            } else if (Character.isLetter(curr)) {
                //若当前位置为单个字符，则不需要decode，把该字符加入最终结果
                decodedString.append(curr);
            } else if (curr == '[') {
                //若当前位置为'['，说明在之后的子串需要单独decode
                //因此把之前记下的出现次数和子串先记录在栈中，之后处理
                countStack.push(num);
                patternStack.push(decodedString.toString());
                //压栈之后记得重置当前的数字和子串
                decodedString.setLength(0);
                num = 0;
            } else {
                //若当前位置为']'，则当前记录的子串需要被decode
                //构建一个临时的string builder来记录decode之后的当前子串
                StringBuilder stringInBracket = new StringBuilder();
                //当前子串的重复次数为栈顶元素
                int times = countStack.pop();
                //复制当前子串
                stringInBracket.append(decodedString.toString().repeat(times));
                //当前记录子串的功能结束，可以直接设为空
                decodedString.setLength(0);
                //然后将先前记录的结果，和当前decode之后的子串进行concatenate
                decodedString.append(patternStack.pop());
                decodedString.append(stringInBracket);
            }
        }
        return decodedString.toString();
    }


    /**
     * Approach 2: DFS
     * <p>
     * Time: O(N)，仍然需要遍历整个字符
     * Space: O(N)，call stack需要O(N)的时间，即可能一直遍历到最后一个字符才开始向上return结果
     */
    public String decodeStringDFS(String s) {
        StringBuilder res = new StringBuilder();
        decode(s, res, 0);
        return res.toString();
    }

    //helper函数，本质上是为了将正确的结果放入stringbuilder中，但同时其返回值为int，即返回当前子串遍历结束时，所在的index
    private int decode(String s, StringBuilder res, int start) {
        //当index溢出string的边界时，终止下一步遍历
        while (start < s.length()) {
            char curr = s.charAt(start);
            //如果当前字符为']'，先停止当前DFS，将先前结果加入stringbuilder，然后再继续遍历
            if (curr == ']') {
                break;
            }
            if (Character.isDigit(curr)) {
                //将当前的数字所有位数记录下来
                int num = curr - '0';
                while (Character.isDigit(s.charAt(start + 1))) {
                    num = num * 10 + s.charAt(start + 1) - '0';
                    start++;
                }

                //因为输入字符串是合法的，因此数字之后必定接一个'['，之后才是需要decode的子串
                //再构建一个stringbuilder用来存这段括号内的子串
                StringBuilder pattern = new StringBuilder();
                //因为数字后第一个字符必为'['，因此该子串的开始位置时当前位置 + 2
                //返回的start是当前待decode子串结束位置']'的index
                start = decode(s, pattern, start + 2);
                //将当前得到的子串复制num次加入最终结果即可
                for (int i = 0; i < num; i++) {
                    res.append(pattern);
                }
            } else {
                //否则的话，该字符为一个单个字符，不需要decode，直接加入结果
                res.append(curr);
            }
            //然后遍历至下一个字符
            start++;
        }
        //返回值为当前子串结束时的index
        return start;
    }

    @Test
    public void decodeStringTest() {
        /**
         * Example 1:
         * s = "3[a]2[bc]", return "aaabcbc".
         */
        String s1 = "3[a]2[bc]";
        assertEquals("aaabcbc", decodeStringStacks(s1));
        assertEquals("aaabcbc", decodeStringDFS(s1));
        /**
         * Example 2:
         * s = "3[a2[c]]", return "accaccacc".
         */
        String s2 = "3[a2[c]]";
        assertEquals("accaccacc", decodeStringStacks(s2));
        assertEquals("accaccacc", decodeStringDFS(s2));
        /**
         * Example 3:
         * s = "2[abc]3[cd]ef", return "abcabccdcdcdef"
         */
        String s3 = "2[abc]3[cd]ef";
        assertEquals("abcabccdcdcdef", decodeStringStacks(s3));
        assertEquals("abcabccdcdcdef", decodeStringDFS(s3));
    }
}
