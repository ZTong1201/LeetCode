import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static org.junit.Assert.assertEquals;

public class AnalyzeUserWebsiteVisitPattern {

    /**
     * You are given two string arrays username and website and an integer array timestamp. All the given arrays are of the
     * same length and the tuple [username[i], website[i], timestamp[i]] indicates that the user username[i] visited the
     * website website[i] at time timestamp[i].
     * <p>
     * A pattern is a list of three websites (not necessarily distinct).
     * <p>
     * For example, ["home", "away", "love"], ["leetcode", "love", "leetcode"], and ["luffy", "luffy", "luffy"] are all patterns.
     * The score of a pattern is the number of users that visited all the websites in the pattern in the same order they
     * appeared in the pattern.
     * <p>
     * For example, if the pattern is ["home", "away", "love"], the score is the number of users x such that x visited
     * "home" then visited "away" and visited "love" after that.
     * Similarly, if the pattern is ["leetcode", "love", "leetcode"], the score is the number of users x such that x visited
     * "leetcode" then visited "love" and visited "leetcode" one more time after that.
     * Also, if the pattern is ["luffy", "luffy", "luffy"], the score is the number of users x such that x visited "luffy"
     * three different times at different timestamps.
     * Return the pattern with the largest score. If there is more than one pattern with the same largest score, return the
     * lexicographically smallest such pattern.
     * <p>
     * Constraints:
     * <p>
     * 3 <= username.length <= 50
     * 1 <= username[i].length <= 10
     * timestamp.length == username.length
     * 1 <= timestamp[i] <= 10^9
     * website.length == username.length
     * 1 <= website[i].length <= 10
     * username[i] and website[i] consist of lowercase English letters.
     * It is guaranteed that there is at least one user who visited at least three websites.
     * All the tuples [username[i], timestamp[i], website[i]] are unique.
     * <p>
     * Approach: Brute Force
     * Group all website history together for every user and sort them by timestamp. Therefore, any 3-seq is a valid visit
     * pattern. Considering the size of the input, we're able to list all combinations of patterns to get the maximum frequency.
     * <p>
     * Two pitfalls:
     * 1. Need to sort the website history by timestamp as stated above.
     * 2. We shouldn't count the same pattern more than once for one single user. For example, if the user browser history
     * is ["a", "a", "a", "a"], the only pattern he has is "a" -> "a" -> "a" and it only contributes once to the frequency.
     * <p>
     * Time: O(n^3) for a given user who has n history, we have n options for the first website, (n - 1) for the second,
     * (n - 2) for the third and O(n^3) total pairs to search
     * Space: O(n)
     */
    public List<String> mostVisitedPattern(String[] username, int[] timestamp, String[] website) {
        Map<String, List<Pair>> visitedWebsites = new HashMap<>();
        // group all website viewing history together for every user
        for (int i = 0; i < username.length; i++) {
            visitedWebsites.putIfAbsent(username[i], new ArrayList<>());
            visitedWebsites.get(username[i]).add(new Pair(timestamp[i], website[i]));
        }

        // need another map to count frequency of each view pattern
        Map<String, Integer> patternFrequency = new HashMap<>();
        String res = "";

        // check each user
        for (String key : visitedWebsites.keySet()) {
            // create a hash set for each user to make sure duplicate view pattern only contribute once
            Set<String> seen = new HashSet<>();
            List<Pair> history = visitedWebsites.get(key);

            // list all O(n^3) pairs
            for (int first = 0; first < history.size() - 2; first++) {
                for (int second = first + 1; second < history.size() - 1; second++) {
                    for (int third = second + 1; third < history.size(); third++) {
                        // get current pattern
                        String pattern = history.get(first).website + " " + history.get(second).website + " " + history.get(third).website;
                        // avoid duplicate contribution
                        if (!seen.contains(pattern)) {
                            seen.add(pattern);
                            patternFrequency.put(pattern, patternFrequency.getOrDefault(pattern, 0) + 1);
                        }

                        // check whether we need to update the final result - if
                        // 1. it's the first pattern we met
                        // 2. the pattern frequency is larger
                        // 3. the pattern frequency equals to the previous pattern, however it has a smaller lexicographical order
                        if (res.equals("") || patternFrequency.get(pattern) > patternFrequency.get(res) ||
                                (patternFrequency.get(pattern) == patternFrequency.get(res) && res.compareTo(pattern) > 0)) {
                            res = pattern;
                        }
                    }
                }
            }
        }
        String[] split = res.split(" ");
        return new ArrayList<>(Arrays.asList(split));
    }

    private static class Pair {
        int timestamp;
        String website;

        Pair(int timestamp, String website) {
            this.timestamp = timestamp;
            this.website = website;
        }
    }

    @Test
    public void mostVisitedPatternTest() {
        /**
         * Example 1:
         * Input: username = ["joe","joe","joe","james","james","james","james","mary","mary","mary"],
         * timestamp = [1,2,3,4,5,6,7,8,9,10],
         * website = ["home","about","career","home","cart","maps","home","home","about","career"]
         * Output: ["home","about","career"]
         * Explanation: The tuples in this example are:
         * ["joe","home",1],["joe","about",2],["joe","career",3],["james","home",4],["james","cart",5],["james","maps",6],["james","home",7],["mary","home",8],["mary","about",9], and ["mary","career",10].
         * The pattern ("home", "about", "career") has score 2 (joe and mary).
         * The pattern ("home", "cart", "maps") has score 1 (james).
         * The pattern ("home", "cart", "home") has score 1 (james).
         * The pattern ("home", "maps", "home") has score 1 (james).
         * The pattern ("cart", "maps", "home") has score 1 (james).
         * The pattern ("home", "home", "home") has score 0 (no user visited home 3 times).
         */
        String[] username1 = new String[]{"joe", "joe", "joe", "james", "james", "james", "james", "mary", "mary", "mary"};
        int[] timestamp1 = new int[]{1, 2, 3, 4, 5, 6, 7, 8, 9, 10};
        String[] website1 = new String[]{"home", "about", "career", "home", "cart", "maps", "home", "home", "about", "career"};
        List<String> expected1 = List.of("home", "about", "career");
        List<String> actual1 = mostVisitedPattern(username1, timestamp1, website1);
        assertEquals(expected1, actual1);
        /**
         * Example 2:
         * Input: username = ["h","eiy","cq","h","cq","txldsscx","cq","txldsscx","h","cq","cq"],
         * timestamp = [527896567,334462937,517687281,134127993,859112386,159548699,51100299,444082139,926837079,317455832,411747930],
         * website = ["hibympufi","hibympufi","hibympufi","hibympufi","hibympufi","hibympufi","hibympufi","hibympufi","yljmntrclw","hibympufi","yljmntrclw"]
         * Output: ["hibympufi","hibympufi","yljmntrclw"]
         */
        String[] username2 = new String[]{"h", "eiy", "cq", "h", "cq", "txldsscx", "cq", "txldsscx", "h", "cq", "cq"};
        int[] timestamp2 = new int[]{527896567, 334462937, 517687281, 134127993, 859112386, 159548699, 51100299, 444082139, 926837079, 317455832, 411747930};
        String[] website2 = new String[]{"hibympufi", "hibympufi", "hibympufi", "hibympufi", "hibympufi", "hibympufi", "hibympufi", "hibympufi",
                "yljmntrclw", "hibympufi", "yljmntrclw"};
        List<String> expected2 = List.of("hibympufi", "hibympufi", "yljmntrclw");
        List<String> actual2 = mostVisitedPattern(username2, timestamp2, website2);
        assertEquals(expected2, actual2);
    }
}
