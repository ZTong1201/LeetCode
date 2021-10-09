import java.util.HashMap;
import java.util.Map;

public class Excel {

    /**
     * Design the basic function of Excel and implement the function of the sum formula.
     * <p>
     * Implement the Excel class:
     * <p>
     * Excel(int height, char width) Initializes the object with the height and the width of the sheet. The sheet is an
     * integer matrix mat of size height x width with the row index in the range [1, height] and the column index in the
     * range ['A', width]. All the values should be zero initially.
     * <p>
     * void set(int row, char column, int val) Changes the value at mat[row][column] to be val.
     * <p>
     * int get(int row, char column) Returns the value at mat[row][column].
     * <p>
     * int sum(int row, char column, List<String> numbers) Sets the value at mat[row][column] to be the sum of cells
     * represented by numbers and returns the value at mat[row][column]. This sum formula should exist until this cell is
     * overlapped by another value or another sum formula. numbers[i] could be on the format:
     * "ColRow" that represents a single cell.
     * For example, "F7" represents the cell mat[7]['F'].
     * <p>
     * "ColRow1:ColRow2" that represents a range of cells. The range will always be a rectangle where "ColRow1" represent
     * the position of the top-left cell, and "ColRow2" represents the position of the bottom-right cell.
     * For example, "B3:F7" represents the cells mat[i][j] for 3 <= i <= 7 and 'B' <= j <= 'F'.
     * <p>
     * Note: You could assume that there will not be any circular sum reference.
     * <p>
     * For example, mat[1]['A'] == sum(1, "B") and mat[1]['B'] == sum(1, "A").
     * <p>
     * Constraints:
     * <p>
     * 1 <= height <= 26
     * 'A' <= width <= 'Z'
     * 1 <= row <= height
     * 'A' <= column <= width
     * -100 <= val <= 100
     * 1 <= numbers.length <= 5
     * numbers[i] has the format "ColRow" or "ColRow1:ColRow2".
     * At most 100 calls will be made to set, get, and sum.
     * <p>
     * Approach: Recursion (Brute Force)
     * Considering the size of the problem, we can solve it using a brute force approach. Basically, we need to define a new
     * class called Cell. It has two attributes value and a map of formulas. The value will be initialized as 0 and the formula
     * map is empty. If the formula map is empty, which means there is no formula calculation needed for this cell. We can
     * simply return the corresponding value (default is 0). Otherwise, the map will keep track of the frequency of each
     * cell in the table. Hence, we start from 0 and sum up the (value of each cell) * (the frequency of the cell) in the
     * formula map. Whenever a cell is reassigned a value or an array of formulas, we will clear the previous formula map
     * and recount the frequency.
     * There is one important edge case that since the number of rows can be at most 26, we need to be careful that the double
     * digits' scenario.
     * <p>
     * Time: O(n * R * C) where n is the length of formula array for each sum call, R is the number of rows, C is the numebr
     * of columns
     * Constructor: we need to go through the entire grid, which takes O(R * C) time
     * set(): O(1)
     * get(): O(n * R * C) since one cell can have at most n formulas, and for each formula, in the worst case scenario, we
     * need to go through the entire grid
     * sum(): O(n * R * C) same as above
     * <p>
     * Space: O(n * R * C) for each cell, we need at most O(n) space to store an array of formulas
     */

    private final Cell[][] table;

    public Excel(int height, char width) {
        this.table = new Cell[height + 1][width - 'A' + 1];
        for (int row = 1; row <= height; row++) {
            for (char col = 'A'; col <= width; col++) {
                // initialize each cell as 0
                table[row][col - 'A'] = new Cell(0);
            }
        }
    }

    public void set(int row, char column, int val) {
        table[row][column - 'A'].setValue(val);
    }

    public int get(int row, char column) {
        return table[row][column - 'A'].getValue();
    }

    public int sum(int row, char column, String[] numbers) {
        // assign the formulas to the desired cell
        table[row][column - 'A'].setFormula(numbers);
        // return the corresponding value afterwards
        return table[row][column - 'A'].getValue();
    }

    private class Cell {
        private int value;
        private final Map<Cell, Integer> formula;

        public Cell(int val) {
            this.value = val;
            this.formula = new HashMap<>();
        }

        public void setValue(int val) {
            // as long as the cell is being assigned a value, the previous formulas will disappear
            resetFormula();
            this.value = val;
        }

        private void resetFormula() {
            this.formula.clear();
        }

        public void setFormula(String[] numbers) {
            // when a new array of formulas kicks in, the previous map needs to be cleared too
            resetFormula();
            for (String number : numbers) {
                // if the format is ColRow
                if (!number.contains(":")) {
                    int[] pos = getPos(number);
                    // put current position in the map
                    addFormula(pos[0], pos[1]);
                } else {
                    // otherwise, the format is ColRow1:ColRow2
                    // split by ":" first
                    String[] pos = number.split(":");
                    int[] startPos = getPos(pos[0]);
                    int[] endPos = getPos(pos[1]);
                    // in this scenario, we need to put every cell in the rectangle into the map
                    for (int row = startPos[0]; row <= endPos[0]; row++) {
                        for (int col = startPos[1]; col <= endPos[1]; col++) {
                            addFormula(row, col);
                        }
                    }
                }
            }
        }

        private int[] getPos(String number) {
            // note that the row number can be double digits (e.g. C12)
            int row = Integer.parseInt(number.substring(1));
            int col = number.charAt(0) - 'A';
            return new int[]{row, col};
        }

        private void addFormula(int row, int col) {
            formula.put(table[row][col], formula.getOrDefault(table[row][col], 0) + 1);
        }

        private int getValue() {
            // if it is not a sum cell - return the value directly
            if (formula.isEmpty()) return value;
            // otherwise, we need to sum up all the corresponding cells
            int sum = 0;
            for (Cell cell : formula.keySet()) {
                // one cell can appear multiple times - need to multiply that
                sum += cell.getValue() * formula.get(cell);
            }
            return sum;
        }
    }
}
