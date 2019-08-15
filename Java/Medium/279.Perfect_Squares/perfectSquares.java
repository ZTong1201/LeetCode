import org.junit.Test;
import static org.junit.Assert.*;
import java.util.*;

public class perfectSquares {

    /**
     * Given a positive integer n, find the least number of perfect square numbers (for example, 1, 4, 9, 16, ...) which sum to n.
     *
     * Approach 1: BFS
     * 为了找到最少的完全平方数，此题可以用BFS来解决。即对于当前数字，若其开根为正数，说明其本身就是一个完全平方数，直接返回当前结果即可。若其不为完全平方数，
     * 则可以找到从1一直到它本身的所有完全平方数，然后将curr减去这些完全平方数，作为新的数字，继续遍历，重复上述过程，直到剩下的某个数字为完全平方数，此时的
     * 步数就是最小数目。
     *
     * Time: O(sqrt(n)*n) 对于当前的数字n，总共有sqrt(n)个小于它的完全平方数，最坏情况下，需要遍历所有的完全平方数。
     *       然后对于下一层，每个节点至多有sqrt(sqrt(n))个孩子节点。所以下一层有大概有sqrt(n) * sqrt(sqrt(n))个节点，依次往下。树的高度为sqrt(n)
     *       所以最后的和为sqrt(n) + sqrt(n) * sqrt(sqrt(n)) + sqrt(n) * sqrt(sqrt(n)) * sqrt(sqrt(sqrt(n))) + ...，总共有sqrt(n)项。
     *       提出一个sqrt(n)，里面每一项都一定比sqrt(n)小，总共sqrt(n)项sqrt(n)，因此里面的和为O(n)，总的时间为O(n*sqrt(n))
     * Space: O(n)，queue中元素最多会有sqrt(n)个，但set中会记录访问过的数字，最坏情况下，可能有n个
     */
    public int numSquaresBFS(int n) {
        Queue<Integer> queue = new LinkedList<>();
        Set<Integer> seen = new HashSet<>();
        queue.add(n);

        int res = 1;
        while(!queue.isEmpty()) {
            int size = queue.size();
            for(int i = 0; i < size; i++) {
                int curr = queue.poll();
                //判断当前数是否为完全平方
                int sqrt = (int) Math.sqrt(curr);
                //若是，找到最短路径，直接返回结果即可
                if(sqrt * sqrt == curr) return res;

                //若不为完全平方数，则遍历在它之前的所有完全平方数
                //将当前数字减去每一个完全平方数，存入队列，继续遍历
                for(int j = sqrt; j >= 1; j--) {
                    //为避免重复遍历，需要有一个set记录遍历过节点
                    if(!seen.contains(curr - j * j)) {
                        seen.add(curr - j * j);
                        queue.add(curr - j * j);
                    }
                }
            }
            res += 1;
        }
        return res;
    }

    /**
     * Approach 2: DP
     * 另一种方法就是DP，可以将到在n之前每个数字的最小完全平方数个数记录在数组中，然后对于当前值，只需要遍历小于它的所有完全平方数，找到对应curr - 完全平方数
     * 的最小结果，加1即可。（因为找到的所有值都只差一个完全平方数就可以等于n）。
     *
     * Time: O(n * sqrt(n)) 每个数字都需要比对小于它的所有完全平方数
     * Space: O(n) 需要一个size为n + 1的数组
     */
    public int numSquaresDP(int n) {
        int[] dp = new int[n + 1];
        Arrays.fill(dp, Integer.MAX_VALUE);
        dp[1] = 1;
        for(int i = 2; i <= n; i++) {
            //找到当前数的平方根
            int sqrt = (int) Math.sqrt(n);
            //若当前数字为完全平方数，则值为1
            if(sqrt * sqrt == i) {
                dp[i] = 1;
                continue;
            }
            //否则就遍历之前的完全平方数，然后找curr - sqrt * sqrt的最小值加1就是最终结果
            for(int j = sqrt; j >= 1; j--) {
                dp[i] = Math.min(dp[i], dp[i - j * j] + 1);
            }
        }
        return dp[n];
    }


    @Test
    public void numSquaresTest() {
        /**
         * Example 1:
         * Input: n = 12
         * Output: 3
         * Explanation: 12 = 4 + 4 + 4.
         */
        assertEquals(3, numSquaresBFS(12));
        assertEquals(3, numSquaresBFS(12));
        /**
         * Example 2:
         * Input: n = 13
         * Output: 2
         * Explanation: 13 = 4 + 9.
         */
        assertEquals(2, numSquaresBFS(13));
        assertEquals(2, numSquaresBFS(13));
    }
}
