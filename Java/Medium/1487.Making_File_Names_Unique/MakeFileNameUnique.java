import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.assertArrayEquals;

public class MakeFileNameUnique {

    /**
     * Given an array of strings names of size n. You will create n folders in your file system such that, at the ith minute,
     * you will create a folder with the name names[i].
     * <p>
     * Since two files cannot have the same name, if you enter a folder name which is previously used, the system will have
     * a suffix addition to its name in the form of (k), where, k is the smallest positive integer such that the obtained
     * name remains unique.
     * <p>
     * Return an array of strings of length n where ans[i] is the actual name the system will assign to the ith folder when
     * you create it.
     * <p>
     * Constraints:
     * <p>
     * 1 <= names.length <= 5 * 10^4
     * 1 <= names[i].length <= 20
     * names[i] consists of lower case English letters, digits and/or round brackets.
     * <p>
     * Approach: Hash Table
     * Put the file name and its next smallest possible integer into a hash map. Note that there is a possibility that
     * adding the next integer will result in another duplicate name. Hence, we need to keep checking in the map until a
     * brand-new name is created and put that name into the map.
     * <p>
     * Time: O(n^2L) where n is the length of names array and L is the average length. In the worst case (all files have
     * the same name) and we need to check 1 + 2 + 3 + ... + n - 1 = n(n - 1)/2 names in total which gives O(n^2) pairs, and
     * for each pair we need construct a new name by adding "(num)" suffix which also gives O(L) time
     * Space: O(n) we might have n unique names in the worst case
     */
    public String[] getFolderNames(String[] names) {
        Map<String, Integer> nextSmallestPossibleInteger = new HashMap<>();
        for (int i = 0; i < names.length; i++) {
            String name = names[i];
            // if the file name doesn't exist, add it to the map and next smallest integer will always be 1
            if (!nextSmallestPossibleInteger.containsKey(name)) {
                nextSmallestPossibleInteger.put(name, 1);
            } else {
                // the file name exists, construct a new name by adding the corresponding suffix
                String newName = name + "(" + nextSmallestPossibleInteger.get(name) + ")";
                // this might result in another duplicate name - keep checking
                while (nextSmallestPossibleInteger.containsKey(newName)) {
                    // the next smallest integer needs to be updated
                    nextSmallestPossibleInteger.put(name, nextSmallestPossibleInteger.get(name) + 1);
                    // construct another name with a larger version number
                    newName = name + "(" + nextSmallestPossibleInteger.get(name) + ")";
                }
                // add the final feasible file name into the map
                nextSmallestPossibleInteger.put(newName, 1);
                // update the file name in the array
                names[i] = newName;
            }
        }
        return names;
    }

    @Test
    public void getFolderNamesTest() {
        /**
         * Example 1:
         * Input: names = ["pes","fifa","gta","pes(2019)"]
         * Output: ["pes","fifa","gta","pes(2019)"]
         * Explanation: Let's see how the file system creates folder names:
         * "pes" --> not assigned before, remains "pes"
         * "fifa" --> not assigned before, remains "fifa"
         * "gta" --> not assigned before, remains "gta"
         * "pes(2019)" --> not assigned before, remains "pes(2019)"
         */
        String[] expected1 = new String[]{"pes", "fifa", "gta", "pes(2019)"};
        String[] actual1 = getFolderNames(new String[]{"pes", "fifa", "gta", "pes(2019)"});
        assertArrayEquals(expected1, actual1);
        /**
         * Example 2:
         * Input: names = ["gta","gta(1)","gta","avalon"]
         * Output: ["gta","gta(1)","gta(2)","avalon"]
         * Explanation: Let's see how the file system creates folder names:
         * "gta" --> not assigned before, remains "gta"
         * "gta(1)" --> not assigned before, remains "gta(1)"
         * "gta" --> the name is reserved, system adds (k), since "gta(1)" is also reserved, systems put k = 2. it becomes "gta(2)"
         * "avalon" --> not assigned before, remains "avalon"
         */
        String[] expected2 = new String[]{"gta", "gta(1)", "gta(2)", "avalon"};
        String[] actual2 = getFolderNames(new String[]{"gta", "gta(1)", "gta", "avalon"});
        assertArrayEquals(expected2, actual2);
        /**
         * Example 3:
         * Input: names = ["onepiece","onepiece(1)","onepiece(2)","onepiece(3)","onepiece"]
         * Output: ["onepiece","onepiece(1)","onepiece(2)","onepiece(3)","onepiece(4)"]
         * Explanation: When the last folder is created, the smallest positive valid k is 4, and it becomes "onepiece(4)".
         */
        String[] expected3 = new String[]{"onepiece", "onepiece(1)", "onepiece(2)", "onepiece(3)", "onepiece(4)"};
        String[] actual3 = getFolderNames(new String[]{"onepiece", "onepiece(1)", "onepiece(2)", "onepiece(3)", "onepiece"});
        assertArrayEquals(expected3, actual3);
        /**
         * Example 4:
         * Input: names = ["wano","wano","wano","wano"]
         * Output: ["wano","wano(1)","wano(2)","wano(3)"]
         * Explanation: Just increase the value of k each time you create folder "wano".
         */
        String[] expected4 = new String[]{"wano", "wano(1)", "wano(2)", "wano(3)"};
        String[] actual4 = getFolderNames(new String[]{"wano", "wano", "wano", "wano"});
        assertArrayEquals(expected4, actual4);
        /**
         * Example 5:
         * Input: names = ["kaido","kaido(1)","kaido","kaido(1)"]
         * Output: ["kaido","kaido(1)","kaido(2)","kaido(1)(1)"]
         * Explanation: Please note that system adds the suffix (k) to current name even it contained the same suffix before.
         */
        String[] expected5 = new String[]{"kaido", "kaido(1)", "kaido(2)", "kaido(1)(1)"};
        String[] actual5 = getFolderNames(new String[]{"kaido", "kaido(1)", "kaido", "kaido(1)"});
        assertArrayEquals(expected5, actual5);
    }
}
