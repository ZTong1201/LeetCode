
# coding: utf-8

# ### 258. Add Digits
# Given a non-negative integer num, repeatedly add all its digits until the result has only one digit.
# <br>
# e.g.
# <br>
# Input: 38
# <br>
# Output: 2 
# <br>
# Explanation: The process is like: 3 + 8 = 11, 1 + 1 = 2. 
# <br>
# Since 2 has only one digit, return it.

# Brute force

# In[5]:


def addDigits(num: int) -> int:
    while num >= 10:
        tmp = 0
        while num != 0:
            tmp += num%10
            num = num//10
        num = tmp
    return num


# Follow up:
# <br>
# Could you do it without any loop/recursion in O(1) runtime?
# <br>
# Every number whose digits sum to a multiple of 9 is divisible by 9.
# <br>
# Otherwise, the iterative sum of digits leads to the remainder when divided by nine.
# <br>
# We account for the case when the number is divisible by 9 or is 0.

# In[10]:


def addDigits2(num: int) -> int:
    return num%9 if num%9 else 9 if num else 0


# In[11]:


def main():
    print(addDigits(38))
    print(addDigits(138))
    print(addDigits(10))
    print(addDigits2(38))
    print(addDigits2(138))
    print(addDigits2(10))


# In[12]:


if __name__ == "__main__":
    main()

