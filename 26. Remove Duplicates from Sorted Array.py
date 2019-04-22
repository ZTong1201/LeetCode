
# coding: utf-8

# ### 26. Remove Duplicates from Sorted Array
# Given a sorted array nums, remove the duplicates in-place such that each element appear only once and return the new length.
# 
# Do not allocate extra space for another array, you must do this by modifying the input array in-place with O(1) extra memory.

# Use set()

# Time: O(n) traverse all the entries in the list, Space: O(1) in-place modification required

# In[38]:


def removeDuplicates(nums):
    """
    :type nums: List[int]
    :rtype: int
    """
    if len(nums)<=1:
        return len(nums)
    i = 0
    j = 1
    while j<len(nums):
        if nums[i] == nums[j]:
            j += 1
        else:
            i += 1
            nums[i] = nums[j]
            j += 1
    return i+1


# In[39]:


def main():
    nums1 = [1,1,2]
    nums2 = [0,0,1,1,1,2,2,3,3,4]
    nums3 = [-1,0,0,0,0,0,0,3,3]
    nums4 = []
    nums5 = [1]
    length1 = removeDuplicates(nums1)
    print(nums1[:length1])
    length2 = removeDuplicates(nums2)
    print(nums2[:length2])
    length3 = removeDuplicates(nums3)
    print(nums3[:length3])
    length4 = removeDuplicates(nums4)
    print(nums4[:length4])
    length5 = removeDuplicates(nums5)
    print(nums5[:length5])


# In[40]:


if __name__ == "__main__":
    main()

