import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class RandomizedSet {

    /**
     * Design a data structure that supports all following operations in average O(1) time.
     * <p>
     * insert(val): Inserts an item val to the set if not already present.
     * remove(val): Removes an item val from the set if present.
     * getRandom: Returns a random element from current set of elements. Each element must have the same probability of being returned.
     * <p>
     * Constraints:
     * <p>
     * -2^31 <= val <= 2^31 - 1
     * At most 2 * 10^5 calls will be made to insert, remove, and getRandom.
     * There will be at least one element in the data structure when getRandom is called.
     * <p>
     * Approach: HashMap + ArrayList
     * 对于insert和remove的要求，可以轻松地用hash set实现，但是为了getRandom也是O(1)，需要将value放在arraylist里，直接寻找index返回value即可。但因为
     * hash set中的元素的顺序是任意的，所以改用hash map，将value和其对应在arraylist中的index进行映射。
     * <p>
     * 1.insert时，只要将value加在arraylist最后，并将value <-> index映射加入map
     * 2.remove时，找到要remove的value的index，将最后一个元素放在当前index，更新map中最后一个元素的新index，同时在map中移除该value，然后再arraylist中
     * 移除最后一个元素
     * 3.getRandom时，只需要通过Random生成一个整数，取绝对值后对list的size取余，得到index即可
     * <p>
     * Time: O(1)，全是O(1)操作
     * Space: O(N)，需要将N个元素分别存入arraylist以及map中
     */

    private final Map<Integer, Integer> valueToIndex;
    private final ArrayList<Integer> values;
    private final Random random;

    /**
     * Initialize your data structure here.
     */
    public RandomizedSet() {
        this.valueToIndex = new HashMap<>();
        this.values = new ArrayList<>();
        this.random = new Random();
    }

    /**
     * Inserts a value to the set. Returns true if the set did not already contain the specified element.
     */
    public boolean insert(int val) {
        if (valueToIndex.containsKey(val)) return false;
        //将元素放入arraylist，并将val和index的映射放入map
        values.add(val);
        valueToIndex.put(val, values.size() - 1);
        return true;
    }

    /**
     * Removes a value from the set. Returns true if the set contained the specified element.
     */
    public boolean remove(int val) {
        //若当前val不在map中，无法remove，返回false
        if (!valueToIndex.containsKey(val)) return false;
        int index = valueToIndex.get(val);
        int lastValue = values.get(values.size() - 1);
        //将arraylist的最后一位元素移动到要删除val的index上
        values.set(index, lastValue);
        //更新最后一位元素的映射
        valueToIndex.put(lastValue, index);
        //将val移除出当前map（删除该元素）
        valueToIndex.remove(val);
        //同时将arraylist最后一位移除
        values.remove(values.size() - 1);
        return true;
    }

    /**
     * Get a random element from the set.
     */
    public int getRandom() {
        // get a random integer from [0, n) where n is the size of the value list
        return values.get(random.nextInt(values.size()));
    }
}
