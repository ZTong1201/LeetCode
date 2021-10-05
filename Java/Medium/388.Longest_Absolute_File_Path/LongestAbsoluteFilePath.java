import org.junit.Test;

import java.util.Stack;

import static org.junit.Assert.assertEquals;

public class LongestAbsoluteFilePath {

    /**
     * Suppose we have a file system that stores both files and directories. An example of one system is represented in the
     * following picture:
     *
     * dir
     * |
     * | ---> subdir1
     * |        |
     * |        |
     * |        | ---> file1.ext
     * |        |
     * |        | ---> subsubdir1
     * |
     * |
     * | ---> subdir2
     *          |
     *          | ---> subsubdir2
     *                     |
     *                     | ---> file2.ext
     * Here, we have dir as the only directory in the root. dir contains two subdirectories, subdir1 and subdir2. subdir1 contains a file file1.ext and subdirectory subsubdir1. subdir2 contains a subdirectory subsubdir2, which contains a file file2.ext.
     *
     * In text form, it looks like this (with ⟶ representing the tab character):
     *
     * dir
     * ⟶ subdir1
     * ⟶ ⟶ file1.ext
     * ⟶ ⟶ subsubdir1
     * ⟶ subdir2
     * ⟶ ⟶ subsubdir2
     * ⟶ ⟶ ⟶ file2.ext
     * If we were to write this representation in code, it will look like this:
     * "dir\n\tsubdir1\n\t\tfile1.ext\n\t\tsubsubdir1\n\tsubdir2\n\t\tsubsubdir2\n\t\t\tfile2.ext".
     * Note that the '\n' and '\t' are the new-line and tab characters.
     *
     * Every file and directory has a unique absolute path in the file system, which is the order of directories that must
     * be opened to reach the file/directory itself, all concatenated by '/'s. Using the above example, the absolute path to
     * file2.ext is "dir/subdir2/subsubdir2/file2.ext". Each directory name consists of letters, digits, and/or spaces.
     * Each file name is of the form name.extension, where name and extension consist of letters, digits, and/or spaces.
     *
     * Given a string input representing the file system in the explained format, return the length of the longest absolute
     * path to a file in the abstracted file system. If there is no file in the system, return 0.
     *
     * Constraints:
     *
     * 1 <= input.length <= 10^4
     * input may contain lowercase or uppercase English letters, a new line character '\n', a tab character '\t', a dot '.',
     * a space ' ', and digits.
     *
     * Approach: Stack
     * Basically, we want to keep pushing something into the stack for further usage as long as we're going into a deeper
     * level of the file path. How to determine the level of the file or directory? It's dependent upon the number of tab
     * characters. That's being said, we will split the entire string by "\n" first to get an array of tokens. After that,
     * we can essentially use lastIndexOf() method from string to know the number of tabs. If there is no tab character
     * in the string, it will return -1. So the number of tabs equals to s.lastIndexOf("\t") + 1. Based on the number of
     * tabs, we know the level of file path we're at, then we might have to remove previous file length at the same level
     * since the file/dir under the same level shouldn't count as part of the longest path.
     *
     * Time: O(n) we will go through the entire string once
     * Space: O(n)
     */
    public int lengthLongestPath(String input) {
        // split the input string by newline character
        String[] tokens = input.split("\n");
        // use a stack to keep track of the length from previous levels
        Stack<Integer> stack = new Stack<>();
        // need a dummy length - the length of the root dir is 0
        stack.push(0);
        int maxLength = 0;

        for (String token : tokens) {
            // go through each token and check the level of current file/dir
            int numOfTabs = token.lastIndexOf("\t") + 1;
            // e.g. the first level will have 0 tab, the second level has 1 tab, etc.
            // therefore, the level of each file/dir will be numOfTabs + 1
            // remove the file/dir lengths from the stack which are at the same level
            while (numOfTabs + 1 < stack.size()) stack.pop();

            // compute the length until current level
            // we need the length from previous level (stored in the stack) + the current token length
            // - numOfTabs (note that "\t" has length 1) + 1 (need to append "/" at the end)
            int currLength = stack.peek() + token.length() - numOfTabs + 1;

            // push the curr length into the stack in case we have a deeper level
            stack.push(currLength);
            // if it's a file (contains "."), then we can update the max length
            // note that we need to subtract 1 from the current length, because we don't need "/" in the end
            if (token.contains(".")) {
                maxLength = Math.max(maxLength, currLength - 1);
            }
        }
        return maxLength;
    }

    @Test
    public void lengthLongestPathTest() {
        /**
         * Example 1:
         * Input: input = "dir\n\tsubdir1\n\tsubdir2\n\t\tfile.ext"
         * Output: 20
         * Explanation: We have only one file, and the absolute path is "dir/subdir2/file.ext" of length 20.
         */
        assertEquals(20, lengthLongestPath("dir\n\tsubdir1\n\tsubdir2\n\t\tfile.ext"));
        /**
         * Example 2:
         * Input: input = "dir\n\tsubdir1\n\t\tfile1.ext\n\t\tsubsubdir1\n\tsubdir2\n\t\tsubsubdir2\n\t\t\tfile2.ext"
         * Output: 32
         * Explanation: We have two files:
         * "dir/subdir1/file1.ext" of length 21
         * "dir/subdir2/subsubdir2/file2.ext" of length 32.
         * We return 32 since it is the longest absolute path to a file.
         */
        assertEquals(32, lengthLongestPath("dir\n\tsubdir1\n\t\tfile1.ext\n\t\tsubsubdir1\n\tsubdir2\n\t\tsubsubdir2\n\t\t\tfile2.ext"));
        /**
         * Example 3:
         * Input: input = "a"
         * Output: 0
         * Explanation: We do not have any files, just a single directory named "a".
         */
        assertEquals(0, lengthLongestPath("a"));
        /**
         * Example 4:
         * Input: input = "file1.txt\nfile2.txt\nlongfile.txt"
         * Output: 12
         * Explanation: There are 3 files at the root directory.
         * Since the absolute path for anything at the root directory is just the name itself, the answer is "longfile.txt" with length 12.
         */
        assertEquals(12, lengthLongestPath("file1.txt\nfile2.txt\nlongfile.txt"));
    }
}
