
# coding: utf-8

# ### 20. Valid Parentheses
# Given a string containing just the characters '(', ')', '{', '}', '[' and ']', determine if the input string is valid.
# 
# An input string is valid if:
# 
# 1.Open brackets must be closed by the same type of brackets.
# <br>
# 2.Open brackets must be closed in the correct order.
# 
# Note that an empty string is also considered valid.

# Time: O(n), Space: O(n) in the worst case

# In[15]:


class Solution:
    def isValid(self, s):
        checkDict = {'(':')',
                     '[':']',
                     '{':'}'}
        stack = []
        for item in s:
            if item in checkDict:
                stack.append(checkDict[item])
            elif stack and stack[-1]==item:
                stack.pop()
            else:
                return False
        if not stack:
            return True
        return False


# In[16]:


def main():
    solution = Solution()
    print(solution.isValid("()"))
    print(solution.isValid("()[]{}"))
    print(solution.isValid("(]"))
    print(solution.isValid("([)]"))
    print(solution.isValid("{[]}"))


# In[17]:


if __name__ == "__main__":
    main()

