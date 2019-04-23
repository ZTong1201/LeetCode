#!/usr/bin/env python
# coding: utf-8

# ### 58. Length of Last Word
# Given a string s consists of upper/lower-case alphabets and empty space characters ' ', return the length of last word in the string.
# 
# If the last word does not exist, return 0.
# 
# Note: A word is defined as a character sequence consists of non-space characters only.
# 
# Example:
# 
# Input: "Hello World"
# <br>
# Output: 5

# In[7]:


def lengthOfLastWord(s):
    words = s.split(" ")
    while words and not words[-1]:
        words.pop()
    return len(words[-1]) if words else 0


# In[8]:


def main():
    print(lengthOfLastWord('Hello World'))
    print(lengthOfLastWord('aaa '))
    print(lengthOfLastWord(' ')) # a whitespace character --> 0
    print(lengthOfLastWord('')) # an empty string --> 0


# In[9]:


if __name__ == "__main__":
    main()


# In[ ]:




