import java.util.*;

public class serializationBFS extends serializationDFS {

    /**
     * Approach 2: Level Order Traversal (BFS)
     * 和leetcode自己的序列化一样，可以对树进行BFS，然后将节点一层一层记录下来。同时依然将空节点记录成"#"即可。注意各节点之间需要以空格区分.
     * 反序列化的关键是要用一个queue模拟level order遍历。在当前节点，要连续访问其后续两个节点，若为null则无需做任何事，若不为null，则需要建立新的节点，
     * 并把相应节点作为正确的孩子节点，同时将孩子节点放入queue中，等当前层遍历完毕后，继续遍历存起来的孩子节点。
     *
     * Time: O(N)  需要遍历所有节点
     * Space: O(N)  做BFS需要用到queue存储待访问节点
     */
    public String serialize(TreeNode root) {
        StringBuilder sb = new StringBuilder();
        Queue<TreeNode> queue = new LinkedList<>();
        queue.add(root);
        while(!queue.isEmpty()) {
            TreeNode curr = queue.poll();
            if(curr == null) {
                sb.append("# ");
                continue;
            }
            sb.append(curr.val + " ");
            queue.add(curr.left);
            queue.add(curr.right);
        }
        return sb.toString();
    }

    public TreeNode deserialize(String data) {
        //需要用queue一层一层的建立节点
        Queue<TreeNode> queue = new LinkedList<>();
        String[] val = data.split(" ");
        //注意base case，若input树为空时，返回的字符串只有"#",在这种情况将根节点初始化为null即可
        TreeNode res = val[0].equals("#") ? null : new TreeNode(Integer.valueOf(val[0]));
        queue.add(res);
        //按顺序访问每个节点，分层建立好节点
        for(int i = 1; i < val.length; i++) {
            //对于当前节点，建立其左右孩子
            TreeNode parent = queue.poll();
            if(!val[i].equals("#")) {
                //若新节点不为null，则建立孩子节点
                TreeNode left = new TreeNode(Integer.valueOf(val[i]));
                parent.left = left;
                //然后把其孩子节点放入队列，待后续遍历
                queue.add(left);
            }
            //注意前一个节点无论是null与否，都已经遍历结束，所以对待其右孩子要将index加1，再继续访问
            //因此用++i，表示访问前先increment
            if(!val[++i].equals("#")) {
                //同理，若新节点不为空，则建立节点，将其作为父节点的右孩子，同时将右孩子放入队列
                TreeNode right = new TreeNode(Integer.valueOf(val[i]));
                parent.right = right;
                queue.add(right);
            }
        }
        return res;
    }
}
