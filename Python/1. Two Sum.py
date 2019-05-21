
# coding: utf-8

# ### 1. Two Sum
# Given an array of integers, return indices of the two numbers such that they add up to a specific target.
# 
# You may assume that each input would have exactly one solution, and you may not use the same element twice.

# Hash Table
# 
# Time: O(n), Space: O(n)

# In[1]:


def twoSum(nums, target):
    mydict = {}
    for index, value in enumerate(nums):
        if value in mydict:
            return [mydict[value],index]
        mydict[target-value] = index


# In[5]:


def main():
    print(twoSum([2, 7, 11, 15],9))
    print(twoSum([2, 7, 11, -1, 3, 15],1))


# In[6]:


if __name__ == "__main__":
    main()

