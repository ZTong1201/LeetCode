
# coding: utf-8

# ### 169. Majority Element
# Given an array of size n, find the majority element. The majority element is the element that appears more than ⌊ n/2 ⌋ times.
# 
# You may assume that the array is non-empty and the majority element always exist in the array.

# Brute Force
# <br>
# Time: O($n^2$), Space: O(1)

# In[1]:


def majorityElement(nums):
    number = set(nums)
    for num in number:
        if nums.count(num)>(len(nums)//2):
            return num


# Use Counter in collections Module (HashMap)
# <br>
# Time: O(n), Space: O(n)

# In[6]:


from collections import Counter
def majorityElement2(nums):
    mydict = Counter(nums)
    for num in mydict:
        if mydict[num]>(len(nums)//2):
            return num


# Sorting
# Time: O(nlogn) sorting is dominated, Space: O(1) if in-place, O(n) if in-place sorting is not allowed.

# In[9]:


def majorityElement3(nums):
    nums.sort()
    return nums[len(nums)//2]


# In[10]:


def main():
    print(majorityElement([3,2,3]))
    print(majorityElement([2,2,1,1,1,2,2]))
    print(majorityElement2([3,2,3]))
    print(majorityElement2([2,2,1,1,1,2,2]))
    print(majorityElement3([3,2,3]))
    print(majorityElement3([2,2,1,1,1,2,2]))


# In[11]:


if __name__ == "__main__":
    main()

