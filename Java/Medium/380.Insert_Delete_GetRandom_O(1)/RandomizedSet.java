import java.util.*;

public class RandomizedSet {

    /**
     * Design a data structure that supports all following operations in average O(1) time.
     *
     * insert(val): Inserts an item val to the set if not already present.
     * remove(val): Removes an item val from the set if present.
     * getRandom: Returns a random element from current set of elements. Each element must have the same probability of being returned.
     *
     * Approach: HashMap + ArrayList
     * 对于insert和remove的要求，可以轻松地用hash set实现，但是为了getRandom也是O(1)，需要将value放在arraylist里，直接寻找index返回value即可。但因为
     * hash set中的元素的顺序是任意的，所以改用hash map，将value和其对应在arraylist中的index进行映射。
     *
     * 1.insert时，只要将value加在arraylist最后，并将value <-> index映射加入map
     * 2.remove时，找到要remove的value的index，将最后一个元素放在当前index，更新map中最后一个元素的新index，同时在map中移除该value，然后再arraylist中
     *   移除最后一个元素
     * 3.getRandom时，只需要通过Random生成一个整数，取绝对值后对list的size取余，得到index即可
     *
     * Time: O(1)，全是O(1)操作
     * Space: O(N)，需要将N个元素分别存入arraylist以及map中
     */

    private Map<Integer, Integer> map;
    private ArrayList<Integer> valueList;
    private Random random;

    /** Initialize your data structure here. */
    public RandomizedSet() {
        this.map = new HashMap<>();
        this.valueList = new ArrayList<>();
        this.random = new Random();
    }

    /** Inserts a value to the set. Returns true if the set did not already contain the specified element. */
    public boolean insert(int val) {
        if(map.containsKey(val)) return false;
        //将元素放入arraylist，并将val和index的映射放入map
        valueList.add(val);
        map.put(val, valueList.size() - 1);
        return true;
    }

    /** Removes a value from the set. Returns true if the set contained the specified element. */
    public boolean remove(int val) {
        //若当前val不在map中，无法remove，返回false
        if(!map.containsKey(val)) return false;
        int lastValue = valueList.get(valueList.size() - 1);
        //将arraylist的最后一位元素移动到要删除val的index上
        valueList.set(map.get(val), lastValue);
        //更新最后一位元素的映射
        map.put(lastValue, map.get(val));
        //将val移除出当前map（删除该元素）
        map.remove(val);
        //同时将arraylist最后一位移除
        valueList.remove(valueList.size() - 1);
        return true;
    }

    /** Get a random element from the set. */
    public int getRandom() {
        //从random中生成一个整数，注意有可能得到负数，因此要去绝对值，然后对arraylist的size取余，可以得到index
        int index = Math.abs(random.nextInt()) % valueList.size();
        return valueList.get(index);
    }
}
