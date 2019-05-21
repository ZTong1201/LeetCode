
# coding: utf-8

# ### 9. Palindrome Number

# Determine whether an integer is a palindrome. An integer is a palindrome when it reads the same backward as forward.
# 
# #### Follow up:
# 
# Coud you solve it without converting the integer to a string?

# In[37]:


# Convert the integer to a string.
def isPalindrome1(x):
    return str(x)[::-1]==str(x)


# In[38]:


# Without converting
def isPalindrome2(x):
    if x<0:
        return False
    original = x
    res = 0
    remainder = []
    while x!=0:
        remainder.append(x%10)
        x = x//10
    for j in range(len(remainder)):
        res += remainder.pop()*(10**j)
    return res == original


# In[39]:


# Use deque
from collections import deque
def isPalindrome3(x):
    if x<0:
        return False
    dq1 = deque()
    while x!=0:
        dq1.append(x%10)
        x = x//10
    while len(dq1)>1:
        if dq1.pop()!=dq1.popleft():
            return False
    return True


# In[40]:


def main():
    print("Convert the integer to a string")
    print(isPalindrome1(123))
    print(isPalindrome1(121))
    print(isPalindrome1(0))
    print(isPalindrome1(-10))
    print("-"*25)
    print("Without converting")
    print(isPalindrome2(123))
    print(isPalindrome2(121))
    print(isPalindrome2(0))
    print(isPalindrome2(-10))
    print("-"*25)
    print("Use deque")
    print(isPalindrome3(123))
    print(isPalindrome3(121))
    print(isPalindrome3(0))
    print(isPalindrome3(-10))


# In[41]:


if __name__ == "__main__":
    main()

