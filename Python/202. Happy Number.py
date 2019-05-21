
# coding: utf-8

# ### 202. Happy Number
# Write an algorithm to determine if a number is "happy".
# <br>
# A happy number is a number defined by the following process: Starting with any positive integer, replace the number by the sum of the squares of its digits, and repeat the process until the number equals 1 (where it will stay), or it loops endlessly in a cycle which does not include 1. Those numbers for which this process ends in 1 are happy numbers.
# <br>
# e.g.
# <br>
# Input: 19
# <br>
# Output: true
# <br>
# Explanation: 
# <br>
# $1^2 + 9^2 = 82$
# <br>
# $8^2 + 2^2 = 68$
# <br>
# $6^2 + 8^2 = 100$
# <br>
# $1^2 + 0^2 + 0^2 = 1$

# In[35]:


def isHappy(n):
    numSet = set()
    while n != 1:
        """
        tmp = 0
        for s in str(n):
            tmp += int(s)**2
        n = tmp
        """
        n = sum(int(i)**2 for i in str(n))
        if n in numSet:
            return False
        else:
            numSet.add(n)
    return True


# In[38]:


def main():
    print(isHappy(100))
    print(isHappy(19))
    print(isHappy(139))
    print(isHappy(112))


# In[39]:


if __name__ == "__main__":
    main()

