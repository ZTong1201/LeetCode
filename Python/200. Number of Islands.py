
# coding: utf-8

# ### 200. Number of Islands
# Given a 2d grid map of '1's (land) and '0's (water), count the number of islands. An island is surrounded by water and is formed by connecting adjacent lands horizontally or vertically. You may assume all four edges of the grid are all surrounded by water.

# Depth First Search (DFS)
# Time: O(mn), Space: O(1)

# In[7]:


class Solution:
    def numIslands(self, grid):
        count = 0
        for i in range(len(grid)):
            for j in range(len(grid[0])):
                if grid[i][j] == '1':
                    self.dfs(grid, i ,j)
                    count += 1
        return count
    
    def dfs(self, grid, x, y):
        if x<0 or y<0 or x>=len(grid) or y>=len(grid[0]) or grid[x][y] == '0':
            return
        grid[x][y] = '0'
        self.dfs(grid, x-1, y)
        self.dfs(grid, x+1, y)
        self.dfs(grid, x, y-1)
        self.dfs(grid, x, y+1)             


# In[8]:


def main():
    solution = Solution()
    print(solution.numIslands(
        [['1','1','1','1','0'],
         ['1','1','0','1','0'],
         ['1','1','0','0','0'],
         ['0','0','0','0','0']]))
    print(solution.numIslands(
        [['1','1','0','0','0'],
         ['1','1','0','0','0'],
         ['0','0','1','0','0'],
         ['0','0','0','1','1']]))


# In[9]:


if __name__ == "__main__":
    main()

