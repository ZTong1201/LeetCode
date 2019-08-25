import java.util.*;

public class RandomizedCollection {

    /**
     * Design a data structure that supports all following operations in average O(1) time.
     *
     * Note: Duplicate elements are allowed.
     * insert(val): Inserts an item val to the collection.
     * remove(val): Removes an item val from the collection if present.
     * getRandom: Returns a random element from current collection of elements.
     * The probability of each element being returned is linearly related to the number of same value the collection contains.
     *
     * Approach: Hash Map + ArrayList
     * 此题和380类似，只是允许duplicate的存在。因此要用hash map记录每个值和其对应在array list中的所有index。用LinkedHashSet将index存起来，因为
     * LinkedHashSet可以保留原始的插入顺序。这样可以保证在remove的时候，都是用最后一个值替换掉该元素第一次出现的位置。
     * insert时：判断该val是否存在，存在就直接在对应的index set里添加最新的index位置，若不存在，则需要先建立好一个val和LinkedHashMap之间的映射
     * remove时：需要先找到该元素在arraylist中的第一个index，然后找到当前arraylist里最后一个元素，将其移动到该index，然后将arraylist的最后一个值移除。
     *          同时将两个value的index set中移除相应index，在最后一个元素的index set插入新的index
     * getRandom时，可以直接generate一个0到arraylist size的整数，然后返回arraylist中的结果
     */


    Map<Integer, Set<Integer>> valueToIndex;
    ArrayList<Integer> values;
    Random random;

    /** Initialize your data structure here. */
    public RandomizedCollection() {
        this.valueToIndex = new HashMap<>();
        this.values = new ArrayList<>();
        this.random = new Random();
    }

    /** Inserts a value to the collection. Returns true if the collection did not already contain the specified element. */
    public boolean insert(int val) {
        //若当前元素不存在，就建立一个新的key-value pair
        if(!valueToIndex.containsKey(val)) {
            valueToIndex.put(val, new LinkedHashSet<>());
        }
        //将新元素加在arraylist的最后
        values.add(val);
        //同时将最新的index加在该val的index set中
        valueToIndex.get(val).add(values.size() - 1);
        //若该val的index set的size为1，说明该元素是第一次插入，返回true
        //否则返回false
        return valueToIndex.get(val).size() == 1;
    }

    /** Removes a value from the collection. Returns true if the collection contained the specified element. */
    public boolean remove(int val) {
        //若map中不包含当前val，或该val已全被移除（即index set的size为0），返回false
        if(!valueToIndex.containsKey(val) || valueToIndex.get(val).size() == 0) {
            return false;
        } else {
            //否则需要先得到该元素第一次出现的index
            int remove_idx = valueToIndex.get(val).iterator().next();
            //然后将其从该val的index set移除
            valueToIndex.get(val).remove(remove_idx);
            //找到当前list中最后一个元素
            int lastItem = values.get(values.size() - 1);
            //将其移动到需要移除的val的index位置
            values.set(remove_idx, lastItem);
            //然后将新index放入最后一位元素的index set中
            valueToIndex.get(lastItem).add(remove_idx);
            //再将最后一个index从该index set移除
            valueToIndex.get(lastItem).remove(values.size() - 1);
            //最后将list中的最后一个元素移除
            values.remove(values.size() - 1);
            return true;
        }
    }

    /** Get a random element from the collection. */
    public int getRandom() {
        //在[0, size)的区间内产生一个随机数，然后返回该位置元素
        return values.get(random.nextInt(values.size()));
    }
}
