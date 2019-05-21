#!/usr/bin/env python
# coding: utf-8

# ### 28. Implement strStr()
# Implement strStr().
# 
# Return the index of the first occurrence of needle in haystack, or -1 if needle is not part of haystack.
# 
# Example 1:
# 
# Input: haystack = "hello", needle = "ll"
# <br>
# Output: 2
# 
# Example 2:
# 
# Input: haystack = "aaaaa", needle = "bba"
# <br>
# Output: -1

# In[35]:


def strStr(haystack: str, needle: str) -> int:
    if not needle:
        return 0
    for i in range(len(haystack)):
        if haystack[i] == needle[0]:
            if haystack[i:i+len(needle)] == needle:
                return i
    return -1


# In[36]:


def strStr2(haystack: str, needle: str) -> int:
    if not needle:
        return 0
    res = haystack.split(needle)
    return len(res[0]) if haystack and len(res[0])!=len(haystack) else -1


# In[37]:


def main():
    print("Brute Force")
    print(strStr("a",""))
    print(strStr("aaaaa",'bba'))
    print(strStr("hello",'ll'))
    print(strStr(""," "))
    print(strStr("a","a"))
    print("-"*25)
    print('Use split()')
    print(strStr2("a",""))
    print(strStr2("aaaaa",'bba'))
    print(strStr2("hello",'ll'))
    print(strStr2(""," ")) # a = "s", b = " " a.split(b) --> ["s"]
    print(strStr2("a","a")) # a = "s", b = "s" a.split(b) --> ["", ""] two empty string


# In[38]:


if __name__ == "__main__":
    main()


# In[ ]:




