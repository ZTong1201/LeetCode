
# coding: utf-8

# ### 27. Remove Element
# Given an array nums and a value val, remove all instances of that value in-place and return the new length.
# 
# Do not allocate extra space for another array, you must do this by modifying the input array in-place with O(1) extra memory.
# 
# The order of elements can be changed. It doesn't matter what you leave beyond the new length.

# Two pointers
# <br>
# Time: O(n), Space: O(1)

# In[12]:


def removeElement(nums, val):
    i = 0
    j = len(nums)-1
    while i<=j:
        if nums[i] == val:
            if nums[j] != val:
                nums[i], nums[j] = nums[j], nums[i]
            else:
                j -= 1
        else:
            i += 1
    return i


# In[15]:


def removeElement2(nums, val):
    i = 0
    for j in range(len(nums)):
        if nums[j] != val:
            nums[i] = nums[j]
            i += 1
    return i


# In[13]:


def main():
    nums1 = [3,2,2,3]
    nums2 = [0,1,2,2,3,0,4,2]
    nums3 = []
    nums4 = [2]
    length1 = removeElement(nums1,3)
    print(nums1[:length1])
    length2 = removeElement(nums2,2)
    print(nums2[:length2])
    length3 = removeElement(nums3,0)
    print(nums3[:length3])
    length4 = removeElement(nums4,3)
    print(nums4[:length4])


# In[16]:


def main2():
    nums1 = [3,2,2,3]
    nums2 = [0,1,2,2,3,0,4,2]
    nums3 = []
    nums4 = [2]
    length1 = removeElement2(nums1,3)
    print(nums1[:length1])
    length2 = removeElement2(nums2,2)
    print(nums2[:length2])
    length3 = removeElement2(nums3,0)
    print(nums3[:length3])
    length4 = removeElement2(nums4,3)
    print(nums4[:length4])


# In[17]:


if __name__ == "__main__":
    main()
    main2()

