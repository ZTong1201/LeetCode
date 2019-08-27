import org.junit.Test;
import static org.junit.Assert.*;

public class numFriendsRequest {

    /**
     * Some people will make friend requests. The list of their ages is given and ages[i] is the age of the ith person.
     *
     * Person A will NOT friend request person B (B != A) if any of the following conditions are true:
     *
     * age[B] <= 0.5 * age[A] + 7
     * age[B] > age[A]
     * age[B] > 100 && age[A] < 100
     * Otherwise, A will friend request B.
     *
     * Note that if A requests B, B does not necessarily request A.  Also, people will not friend request themselves.
     * How many total friend requests are made?
     *
     * Notes:
     *
     * 1 <= ages.length <= 20000.
     * 1 <= ages[i] <= 120.
     *
     * Approach: Counting
     * 首先先看给定的三个条件，第三个条件和第二个条件重复，无需额外考虑，即当age[B] > age[A]，A不会向B发出请求。同时要满足age[A] < age[B] <= age[A]*0.5 + 7
     * age[A] < age[A] * 0.5 + 7, age[A] > 14，同理age[B]也需要大于14才能发出申请。可以先遍历一遍数组对整个数组进行计数，即每个age出现的次数，然后对于
     * 所有的(ageA， ageB)组合，若两个年龄满足前两个条件，则A能向B发出的请求数为countA * countB，需要注意的是当ageA = ageB时，因为无法自己向自己提出申请，
     * 所以该申请的总数为countA * (countA - 1)。将所有pair的请求书加起来即为最终结果
     *
     * Time: O(105^2 + N) 需要遍历一遍数组记录每个年龄出现的次数，需要O(N)时间，之后需要找到[15, 120]的所有年龄pair
     * Space: O(121) 需要记录每个年龄出现的次数
     */
    public int numFriendsRequest(int[] ages) {
        int[] count = new int[121];
        for(int age : ages) {
            count[age]++;
        }

        int res = 0;
        for(int age1 = 15; age1 < 121; age1++) {
            for(int age2 = 15; age2 < 121; age2++) {
                //若不满足前两个条件，可以直接跳过当前pair
                if(age2 > age1) continue;
                if(age2 <= age1 * 0.5 + 7) continue;
                res += count[age1] * count[age2];
                //当age1 = age2时，最终结果为countA * (countA - 1)，因此需要再减去一个countA
                if(age1 == age2) res -= count[age1];
            }
        }
        return res;
    }

    @Test
    public void numFriendsRequestTest() {
        /**
         * Example 1:
         * Input: [16,16]
         * Output: 2
         * Explanation: 2 people friend request each other.
         */
        int[] ages1 = new int[]{16, 16};
        assertEquals(2, numFriendsRequest(ages1));
        /**
         * Example 2:
         * Input: [16,17,18]
         * Output: 2
         * Explanation: Friend requests are made 17 -> 16, 18 -> 17.
         */
        int[] ages2 = new int[]{16, 17, 18};
        assertEquals(2, numFriendsRequest(ages2));
        /**
         * Example 3:
         * Input: [20,30,100,110,120]
         * Output: 3
         * Explanation: Friend requests are made 110 -> 100, 120 -> 110, 120 -> 100.
         */
        int[] ages3 = new int[]{20, 30, 100, 110, 120};
        assertEquals(3, numFriendsRequest(ages3));
    }
}
