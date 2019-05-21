
# coding: utf-8

# ### 100. Same Tree
# Given two binary trees, write a function to check if they are the same or not.
# 
# Two binary trees are considered the same if they are structurally identical and the nodes have the same value.

# In[50]:


class TreeNode:
    def __init__(self, x):
        self.val = x
        self.left = None
        self.right = None


# Recursion Method
# <br>
# Time: O(n), Space: O(logn) in the best case of completely balanced tree and O(n) in the worst case of completely unbalanced tree.
# <br>
# Iterative Method
# <br>
# Time: O(n), Space: O(logn) in the best case of completely balanced tree and O(n) in the worst case of completely unbalanced tree.

# In[51]:


from collections import deque
class Solution:
    
    def isSameTree_recursive1(self, p, q):
        if not p and not q:
            return True
        if not p or not q:
            return False
        if p.val!=q.val:
            return False
        return self.isSameTree_recursive1(p.left,q.left) and self.isSameTree_recursive1(p.right,q.right)
    
    def isSameTree_recursive2(self, p, q):
        if not p and not q:
            return True
        while p and q:
            if p.val!=q.val:
                return False
            return self.isSameTree_recursive2(p.left,q.left) and self.isSameTree_recursive2(p.right,q.right)
        return False
    
    def isSameTree_iterative(self, p, q):
        deque_p = deque()
        deque_q = deque()
        deque_p.append(p)
        deque_q.append(q)
        while deque_p and deque_q:
            while deque_p and deque_q:
                node_p = deque_p.popleft()
                node_q = deque_q.popleft()
                if not node_p and not node_q:
                    continue
                if not node_p or not node_q:
                    return False
                if node_p.val!=node_q.val:
                    return False
                deque_p.append(node_p.left)
                deque_q.append(node_q.left)
                deque_p.append(node_p.right)
                deque_q.append(node_q.right)


# In[52]:


def main():
    test1_tree1 = TreeNode(1)
    test1_tree1.left = TreeNode(2)
    test1_tree1.right = TreeNode(3)
    test1_tree2 = TreeNode(1)
    test1_tree2.left = TreeNode(2)
    test1_tree2.right = TreeNode(3)
    solution = Solution()
    print("Tree 1")
    print("Recursion 1: ",solution.isSameTree_recursive1(test1_tree1,test1_tree2))
    print("Recursion 2: ",solution.isSameTree_recursive2(test1_tree1,test1_tree2))
    print("Iteration 1: ",solution.isSameTree_iterative(test1_tree1,test1_tree2))
    print('-'*25)
    test2_tree1 = TreeNode(1)
    test2_tree1.left = TreeNode(2)
    test2_tree2 = TreeNode(1)
    test2_tree2.right = TreeNode(2)
    print("Tree 2")
    print("Recursion 1: ",solution.isSameTree_recursive1(test2_tree1,test2_tree2))
    print("Recursion 2: ",solution.isSameTree_recursive2(test2_tree1,test2_tree2))
    print("Iteration 1: ",solution.isSameTree_iterative(test2_tree1,test2_tree2))
    print("-"*25)
    test3_tree1 = TreeNode(1)
    test3_tree1.left = TreeNode(2)
    test3_tree1.right = TreeNode(1)
    test3_tree2 = TreeNode(1)
    test3_tree2.left = TreeNode(1)
    test3_tree2.right = TreeNode(2)
    print("Tree 3abs")
    print("Recursion 1: ",solution.isSameTree_recursive1(test3_tree1,test3_tree2))
    print("Recursion 2: ",solution.isSameTree_recursive2(test3_tree1,test3_tree2))
    print("Iteration 1: ",solution.isSameTree_iterative(test3_tree1,test3_tree2))


# In[53]:


if __name__ == "__main__":
    main()

