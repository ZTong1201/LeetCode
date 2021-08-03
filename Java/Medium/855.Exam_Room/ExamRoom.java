import java.util.TreeSet;

public class ExamRoom {

    /**
     * In an exam room, there are N seats in a single row, numbered 0, 1, 2, ..., N-1.
     * <p>
     * When a student enters the room, they must sit in the seat that maximizes the distance to the closest person.  If there are
     * multiple such seats, they sit in the seat with the lowest number.  (Also, if no one is in the room, then the student sits at seat
     * number 0.)
     * <p>
     * Return a class ExamRoom(int N) that exposes two functions: ExamRoom.seat() returning an int representing what seat the student sat in,
     * and ExamRoom.leave(int p) representing that the student in seat number p now leaves the room.  It is guaranteed that any calls to
     * ExamRoom.leave(p) have a student sitting in seat p.
     * <p>
     * Approach: Tree Set
     * 需要将已经分配好的座位记录下来，当新来一位学生需要分配座位时，需要从小到大依次看两个座位直接的间隔。为了使新加入的学生与其相邻的学生的间隔最大，需要取
     * 两个学生的中间座位分配。因为需要顺序遍历座位序号，考虑使用tree set，在学生离开时，只要将该元素移除即可，时间为O(logn)。在加入新学生时，可以先记录下
     * tree set中的第一个元素，其表示着与最左端座位（即位置0）的距离。因为可能存在新加入的学生坐在端点使得距离最大，坐在端点时，其只有一个相邻学生。然后依次
     * 遍历所有元素，取两两差值的均值，将其视作新加入学生与其相邻学生的最大距离。最终取得距离最大的第一个座位即可。最后仍需考虑，新加入学生坐在右端点的情况，
     * 判断新学生坐在右端点与已分配的最大座位号直接的差值是否能得到更大的距离即可。
     * <p>
     * Time: seat() - O(n) 因为分配座位需要遍历所有元素，leave() - O(logn)
     * Space: O(n) 需要将所有已分配座位放入tree set
     */
    private final TreeSet<Integer> seats;
    private final int num;

    public ExamRoom(int N) {
        this.seats = new TreeSet<>();
        this.num = N;
    }

    public int seat() {
        //将座位初始化为0
        int res = 0;
        //若当前一个座位都为分配，直接分配座位0即可
        //否则需要找到能使间隔最大的座位号
        if (seats.size() > 0) {
            //将最大距离先初始化为set中的第一个元素，即为分配的第一个位置与座位0之间的距离
            int maxDist = seats.first();
            Integer prev = null;
            //依次遍历所有元素，找两两间隔
            for (int seat : seats) {
                if (prev != null) {
                    int currDist = (seat - prev) / 2;
                    if (currDist > maxDist) {
                        //若找到更大的间隔，就更新当前最大间隔，同时记录实现这个最大间隔所赢分配的座位号
                        maxDist = currDist;
                        res = prev + currDist;
                    }
                }
                prev = seat;
            }
            //最后需要考虑，若新学生坐在右端点能否获得更大间隔
            if (num - 1 - seats.last() > maxDist) {
                res = num - 1;
            }
        }
        //最后将分配的座位记录在tree set中，然后返回该座位
        seats.add(res);
        return res;
    }

    public void leave(int p) {
        seats.remove(p);
    }
}
