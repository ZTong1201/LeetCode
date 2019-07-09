public class Number_Of_Islands {

    public static int numIslands(char[][] grid) {
        int count = 0;
        for(int i = 0; i < grid.length; i++) {
            for(int j = 0; j < grid[0].length; j++) {
                if(grid[i][j] == '1') {
                    count += 1;
                    dfs(grid, i, j);
                }
            }
        }
        return count;
    }

    private static void dfs(char[][] grid, int i, int j) {
        if(i < 0 || j < 0 || i >= grid.length || j >= grid[0].length || grid[i][j] == '0' ) {
            return;
        }
        if(grid[i][j] == '1') grid[i][j] = '0';
        dfs(grid, i - 1, j);
        dfs(grid, i + 1, j);
        dfs(grid, i, j - 1);
        dfs(grid, i, j + 1);
    }

    public static void main(String[] args) {
        char[][] test1 = {{'1', '1', '1', '1', '0'},{'1', '1', '0', '1', '0'},{'1', '1', '0', '0', '0'}, {'0', '0', '0', '0', '0'}};
        System.out.println("Expect: 1" + " Actual: " + Number_Of_Islands.numIslands(test1));
        char[][] test2 = {{'1', '1', '0', '0', '0'},{'1', '1', '0', '0', '0'},{'0', '0', '1', '0', '0'}, {'0', '0', '0', '1', '1'}};
        System.out.println("Expect: 3" + " Actual: " + Number_Of_Islands.numIslands(test2));
    }
}