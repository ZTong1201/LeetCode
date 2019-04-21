
# coding: utf-8

# ### 168. Excel Sheet Column Title
# Given a positive integer, return its corresponding column title as appear in an Excel sheet.

# In[23]:


def convertToTitle(n):
    letter = 'ABCDEFGHIJKLMNOPQRSTUVWXYZ'
    res = ''
    stack = []
    while n!=0:
        n -= 1
        stack.append(n%26)
        n = n//26
    while stack:
        res += letter[stack.pop()]
    return res


# In[24]:


def main():
    print(convertToTitle(1))
    print(convertToTitle(28))
    print(convertToTitle(52))
    print(convertToTitle(701))


# In[25]:


if __name__ == '__main__':
    main()

