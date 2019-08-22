import java.util.*;

public class wordDistance {

    /**
     * Design a class which receives a list of words in the constructor, and implements a method that takes two words word1 and word2 and
     * return the shortest distance between these two words in the list. Your method will be called repeatedly many times with
     * different parameters.
     *
     * Note:
     * You may assume that word1 does not equal to word2, and word1 and word2 are both in the list.
     *
     * Approach: Presorted indices
     * 本质上此题和243一样，找到两个单词的最短距离，问题是该method会被调用多次，所以在初始化时需要将每个单词对应的所有index按出现顺序从小到大排列起来。
     * 所以当寻找两个单词的最短距离时，其实就是给定两个排好序的list，找到两个list中元素的最小值。brute force是找到所有的pair比较最小值。而更好的办法是用双指针
     * 可以在只扫过每个list一遍的情况下找到最小值。若list1[i] < list2[j]，则下一步要移动list1里的指针i，因为list元素是排好序的，list2[j + 1] > list2[j]
     * 所以移动j不会得到更小的distance。同理，若list1[i] >= list2[j]，则下一步需要移动list2里的指针j。值得注意的是，因为给定两单词不会是同一个单词，且两
     * 单词都在数组中，那么两个单词的最短距离为1。当最短距离已经为1时，可以直接跳出循环。
     *
     * Time: O(N + M * max(K, L)) N是数组的长度，K为word1出现的次数，L为word2出现的次数。初始化需要O(N)时间，记录每个单词出现的index，在找最短距离时，
     *       最坏情况下需要分别遍历两个list，则时间有长度更长的list决定，同时调用该函数M次，总共需要O(M * max(K, L))
     * Space: O(N) 需要将所有的index和对应元素存在map中
     */
    private Map<String, List<Integer>> map;

    public wordDistance(String[] words) {
        this.map = new HashMap<>();
        for(int i = 0; i < words.length; i++) {
            //只要按顺序将index记录下来，每个元素出现的index就已经是顺序排好的
            this.map.putIfAbsent(words[i], new ArrayList<>());
            this.map.get(words[i]).add(i);
        }
    }

    public int shortest(String word1, String word2) {
        int min = Integer.MAX_VALUE;
        //得到两个排好序的list
        List<Integer> indices1 = map.get(word1);
        List<Integer> indices2 = map.get(word2);
        //设定两个指针，都从list第一个元素出发
        int p1 = 0, p2 = 0;
        while(p1 < indices1.size() && p2 < indices2.size()) {
            //若当前index差更小，更新最小值
            min = Math.min(min, Math.abs(indices1.get(p1) - indices2.get(p2)));
            //两单词所能达到的最短距离即为1，当最短距离已经是1，直接跳出循环
            if(min == 1) break;
            //若当前list1中index更小，则需要移动p1指针，才可能收获更小的distance
            if(indices1.get(p1) < indices2.get(p2)) {
                p1++;
            } else {
                //反之移动p2指针
                p2++;
            }
        }
        return min;
    }
}
