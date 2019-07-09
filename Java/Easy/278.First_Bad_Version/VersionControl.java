import org.junit.Test;
import static org.junit.Assert.*;

public class VersionControl{

    boolean[] versions;

    public VersionControl() {

    }

    VersionControl(boolean[] versions) {
        this.versions = versions;
    }

    /**
     * You are a product manager and currently leading a team to develop a new product.
     * Unfortunately, the latest version of your product fails the quality check.
     * Since each version is developed based on the previous version, all the versions after a bad version are also bad.
     *
     * Suppose you have n versions [1, 2, ..., n] and you want to find out the first bad one,
     * which causes all the following ones to be bad.
     *
     * You are given an API bool isBadVersion(version) which will return whether version is bad.
     * Implement a function to find the first bad version. You should minimize the number of calls to the API.
     *
     * Use Binary Search
     *
     * Time: O(logN)
     * Space: O(1) no extra space
     */
    public int firstBadVersion(boolean[] versions, int n) {
        VersionControl versionControl = new VersionControl(versions);
        int left = 1;
        int right = n;
        while(left < right) {
            int mid = left + (right - left) / 2;
            if(versionControl.isBadVersion(mid)) right = mid;
            else left = mid + 1;
        }
        return left;
    }

    private boolean isBadVersion(int i) {
        return versions[i - 1];
    }

    @Test
    public void firstBadVersionTest() {
        boolean[] versions1 = new boolean[]{false, false, false, true, true};
        assertEquals(4, firstBadVersion(versions1, 5));
        boolean[] versions2 = new boolean[]{false, true};
        assertEquals(2, firstBadVersion(versions2, 2));
    }
}
