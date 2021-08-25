import org.junit.Test;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Stack;

import static org.junit.Assert.assertEquals;

public class EmployeeImportance {

    /**
     * You have a data structure of employee information, which includes the employee's unique id, their importance value,
     * and their direct subordinates' id.
     * <p>
     * You are given an array of employees where:
     * <p>
     * employees[i].id is the ID of the ith employee.
     * employees[i].importance is the importance value of the ith employee.
     * employees[i].subordinates is a list of the IDs of the subordinates of the ith employee.
     * Given an integer id that represents the ID of an employee, return the total importance value of this employee and
     * all their subordinates.
     * <p>
     * Constraints:
     * <p>
     * 1 <= employees.length <= 2000
     * 1 <= employees[i].id <= 2000
     * All employees[i].id are unique.
     * -100 <= employees[i].importance <= 100
     * One employee has at most one direct leader and may have several subordinates.
     * id is guaranteed to be a valid employee id.
     * <p>
     * Approach: DFS
     * We need a map between employee id and employee such that we can get the importance and its subordinates easily.
     * <p>
     * Time: O(n)
     * Space: O(n)
     */
    public int getImportance(List<Employee> employees, int id) {
        Map<Integer, Employee> idToEmployee = new HashMap<>();
        for (Employee employee : employees) {
            idToEmployee.put(employee.id, employee);
        }

        Stack<Integer> stack = new Stack<>();
        stack.push(id);

        int res = 0;
        while (!stack.isEmpty()) {
            int curr = stack.pop();
            res += idToEmployee.get(curr).importance;
            for (int subordinate : idToEmployee.get(curr).subordinates) {
                stack.push(subordinate);
            }
        }
        return res;
    }

    @Test
    public void getImportanceTest() {
        /**
         * Example 1:
         * Input: employees = [[1,5,[2,3]],[2,3,[]],[3,3,[]]], id = 1
         * Output: 11
         * Explanation: Employee 1 has importance value 5, and he has two direct subordinates: employee 2 and employee 3.
         * They both have importance value 3.
         * So the total importance value of employee 1 is 5 + 3 + 3 = 11.
         */
        List<Employee> employees1 = List.of(new Employee(1, 5, List.of(2, 3)),
                new Employee(2, 3, List.of()), new Employee(3, 3, List.of()));
        assertEquals(11, getImportance(employees1, 1));
        /**
         * Example 2:
         * Input: employees = [[1,2,[5]],[5,-3,[]]], id = 5
         * Output: -3
         */
        List<Employee> employees2 = List.of(new Employee(1, 2, List.of(5)), new Employee(5, -3, List.of()));
        assertEquals(-3, getImportance(employees2, 5));
    }

    private static class Employee {
        int id;
        int importance;
        List<Integer> subordinates;

        public Employee(int _id, int _importance, List<Integer> _subordinates) {
            this.id = _id;
            this.importance = _importance;
            subordinates = _subordinates;
        }
    }
}
