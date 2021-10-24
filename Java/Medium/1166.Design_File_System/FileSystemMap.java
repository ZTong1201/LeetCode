import java.util.HashMap;
import java.util.Map;

public class FileSystemMap {

    /**
     * You are asked to design a file system that allows you to create new paths and associate them with different values.
     * <p>
     * The format of a path is one or more concatenated strings of the form: / followed by one or more lowercase English
     * letters. For example, "/leetcode" and "/leetcode/problems" are valid paths while an empty string "" and "/" are not.
     * <p>
     * Implement the FileSystem class:
     * <p>
     * bool createPath(string path, int value) Creates a new path and associates a value to it if possible and returns true. Returns false if the path already exists or its parent path doesn't exist.
     * int get(string path) Returns the value associated with path or returns -1 if the path doesn't exist.
     * <p>
     * Constraints:
     * <p>
     * The number of calls to the two functions is less than or equal to 10^4 in total.
     * 2 <= path.length <= 100
     * 1 <= value <= 10^9
     * <p>
     * Approach 1: Hash Map
     * We can keep a hash map to store the key-value pairs where the key is the file path and the value is the corresponding
     * value. There are some edge cases while creating the file paths:
     * 1. If the parent path doesn't exist, then we should return false
     * 2. If it is a duplicate file path, then we should also return false and not update the value.
     * <p>
     * Therefore, for a given file path, we can find the last index of the "/", and take the substring from the beginning to
     * the last index (because it's the parent path). We need to check:
     * 1. whether "/" exists in the path, if not, it's an invalid path - so return false
     * 2. whether the parent path exists, if not - return false
     * 3. whether the entire path exists, if yes - return false
     * otherwise, we put the key-value pair into the map
     * <p>
     * Time:
     * createPath(): O(n * L) where L is the average length of the path. We need to find the last index of "/" in the path
     * get(): O(n)
     * Space: O(n) if we have n valid paths
     */
    private final Map<String, Integer> filePaths;

    public FileSystemMap() {
        filePaths = new HashMap<>();
        // put an empty string as a placeholder since "/a" is a valid path,
        // and it's parent path is ""
        filePaths.put("", -1);
    }

    public boolean createPath(String path, int value) {
        int lastIndex = path.lastIndexOf("/");
        // if it's not a valid path, or it's a duplicate path - return false
        if (lastIndex == -1 || filePaths.containsKey(path)) return false;
        // get the parent path
        String parentPath = path.substring(0, lastIndex);
        // if the parent path doesn't exist - return false;
        if (!filePaths.containsKey(parentPath)) return false;
        // put the key-value pair
        filePaths.put(path, value);
        return true;
    }

    public int get(String path) {
        return filePaths.getOrDefault(path, -1);
    }
}
