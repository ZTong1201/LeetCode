public class findCelebrity extends Relations {

    /**
     * Suppose you are at a party with n people (labeled from 0 to n - 1) and among them, there may exist one celebrity.
     * The definition of a celebrity is that all the other n - 1 people know him/her but he/she does not know any of them.
     *
     * Now you want to find out who the celebrity is or verify that there is not one. The only thing you are allowed to do is to
     * ask questions like: "Hi, A. Do you know B?" to get information of whether A knows B. You need to find out the celebrity
     * (or verify there is not one) by asking as few questions as possible (in the asymptotic sense).
     *
     * You are given a helper function bool knows(a, b) which tells you whether A knows B. Implement a function int findCelebrity(n).
     * There will be exactly one celebrity if he/she is in the party. Return the celebrity's label if there is a celebrity in the party.
     * If there is no celebrity, return -1.
     *
     * Note:
     *
     * The directed graph is represented as an adjacency matrix, which is an n x n matrix where a[i][j] = 1 means person i knows person
     * j while a[i][j] = 0 means the contrary.
     * Remember that you won't have direct access to the adjacency matrix.
     *
     * Approach: Two-pass
     * 为了比较尽可能的少，可以采用选定一个candidate，然后再重新判定该candidate是否符合要求。选取candidate时，可以先判定当前candidate是否认识他后面的人
     * 因为只有celebrity会不认识他后面任何人。
     *
     * 找到该candidate之后，只需要再判断，其余所有人都认识他，以及他不认识剩余所有人即可。（注意为了减少问问题的次数，只需询问该candidate是否不认识他之前的人
     * 即可，因为之后的人已在判定candidate时问过）
     */
    public int findCelebrity(int n) {
        int candidate = 0;
        //首先找到不认识他后面所有人的candidate
        for(int i = 1; i < n; i++) {
            if(knows(candidate, i)) {
                candidate = i;
            }
        }

        //找到candidate后，判断candidate之前的人是否都认识他，同时他不认识任何其中一个
        for(int i = 0; i < candidate; i++) {
            if(knows(candidate, i) || !knows(i, candidate)) {
                return -1;
            }
        }

        //再判断candidate之后的所有人是否都认识他（candidate不认识他们已经被判断过，无需再判断）
        for(int i = candidate + 1; i < n; i++) {
            if(!knows(i, candidate)) {
                return -1;
            }
        }
        return candidate;
    }

}
