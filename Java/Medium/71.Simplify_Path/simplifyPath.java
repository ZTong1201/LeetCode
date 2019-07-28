import org.junit.Test;
import static org.junit.Assert.*;
import java.util.Stack;

public class simplifyPath {

    /**
     * Given an absolute path for a file (Unix-style), simplify it. Or in other words, convert it to the canonical path.
     *
     * In a UNIX-style file system, a period . refers to the current directory. Furthermore, a double period .. moves the directory up a
     * level. For more information, see: Absolute path vs relative path in Linux/Unix
     *
     * Note that the returned canonical path must always begin with a slash /, and there must be only a single slash / between two directory
     * names. The last directory name (if it exists) must not end with a trailing /. Also, the canonical path must be the shortest string
     * representing the absolute path.
     *
     * Approach: Stack
     * To simplify a file path, we need to first split the entire String by "/" to get all files (or folders) name. As we move from the left
     * to the right, if we meet a new file, we push it in the stack, if we meet a ".", we are still in the current directory, hence do nothing.
     * If we meet a "..", however, we need to move one level up of the path, hence we need to remove the most recent file name (if there exists
     * one) in the path, i.e. we pop from the stack. In the end, we get all the files (or folders) names which should be in the final path.
     * The final thing is just adding "/" at the correct position
     *
     * Time: O(n) we need to go through the entire array
     * Space: O(n)
     */
    public String simplifyPath(String path) {
        Stack<String> stack = new Stack<>();
        //split by "/" to get all files names (note that we may have "" in the result array)
        String[] files = path.split("/");
        for(String file : files) {
            //if we meet ".." and we have something to remove from the stack, we pop out the most recent file name
            if(!stack.isEmpty() && file.equals("..")) {
                stack.pop();
            } else if(!file.equals("") && !file.equals(".") && !file.equals("..")) {
                //if we meet a file name (not an empty string, not a "." nor a ".."), we push it into the stack
                stack.push(file);
            }
        }
        //add "/" between each file name, and one more "/" at the beginning
        return "/" + String.join("/", stack);
    }

    @Test
    public void simplifyPathTest() {
        /**
         * Example 1:
         * Input: "/home/"
         * Output: "/home"
         * Explanation: Note that there is no trailing slash after the last directory name.
         */
        String expected1 = "/home";
        String actual1 = simplifyPath("/home");
        assertEquals(expected1, actual1);
        /**
         * Example 2:
         * Input: "/../"
         * Output: "/"
         * Explanation: Going one level up from the root directory is a no-op, as the root level is the highest level you can go.
         */
        String expected2 = "/";
        String actual2 = simplifyPath("/../");
        assertEquals(expected2, actual2);
        /**
         * Example 3:
         * Input: "/home//foo/"
         * Output: "/home/foo"
         * Explanation: In the canonical path, multiple consecutive slashes are replaced by a single one.
         */
        String expected3 = "/home/foo";
        String actual3 = simplifyPath("/home//foo/");
        assertEquals(expected3, actual3);
        /**
         * Example 4:
         * Input: "/a/./b/../../c/"
         * Output: "/c"
         */
        String expected4 = "/c";
        String actual4 = simplifyPath("/a/./b/../../c/");
        assertEquals(expected4, actual4);
        /**
         * Example 5:
         * Input: "/a/../../b/../c//.//"
         * Output: "/c"
         */
        String expected5 = "/c";
        String actual5 = simplifyPath("/a/../../b/../c//.//");
        assertEquals(expected5, actual5);
        /**
         * Example 6:
         * Input: "/a//b////c/d//././/.."
         * Output: "/a/b/c"
         */
        String expected6 = "/a/b/c";
        String actual6 = simplifyPath("/a//b////c/d//././/..");
        assertEquals(expected6, actual6);

    }
}
