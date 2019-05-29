import java.util.*;

public class Two_Sum {
	public static int[] twoSum(int[] nums, int target) {

		Map<Integer, Integer> targetMinusSeen = new HashMap<>();
		int[] result = new int[2];
		for(int i = 0; i < nums.length; i++) {
			if(!targetMinusSeen.containsKey(nums[i])) {
				targetMinusSeen.put(target - nums[i], i);
			} else {
				result[0] = targetMinusSeen.get(nums[i]);
				result[1] = i;
				break;
			}
		}
		return result;
	}

	public static void main(String[] args) {
		int[] nums = new int[]{2, 7, 11, 15};
		int target = 9;
		int[] result = Two_Sum.twoSum(nums, target);
		for(int i : result) {
			System.out.println(i);
		}
	}
}