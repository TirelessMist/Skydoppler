package ae.skydoppler.dungeon.solver;

import java.util.*;

public class box_solver {

    // A simple point class to represent grid positions.
    static class Point implements Comparable<Point> {
        int r, c;

        public Point(int r, int c) {
            this.r = r;
            this.c = c;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (!(o instanceof Point)) return false;
            Point p = (Point) o;
            return this.r == p.r && this.c == p.c;
        }

        @Override
        public int hashCode() {
            return Objects.hash(r, c);
        }

        @Override
        public int compareTo(Point other) {
            if (this.r != other.r)
                return this.r - other.r;
            return this.c - other.c;
        }

        @Override
        public String toString() {
            return "(" + r + ", " + c + ")";
        }
    }

    // Represents a push move made on a box.
    static class BoxMove {
        int r, c;
        String direction;

        public BoxMove(int r, int c, String direction) {
            this.r = r;
            this.c = c;
            this.direction = direction;
        }

        @Override
        public String toString() {
            return "box -> (" + r + ", " + c + ") -> " + direction;
        }
    }

    // A state represents a configuration of boxes (their positions). We keep a parent pointer and the move
    // that got us to this state so that we can easily reconstruct the solution.
    static class State {
        Set<Point> boxes;
        State parent;
        BoxMove move;  // move that led from parent to this state

        public State(Set<Point> boxes, State parent, BoxMove move) {
            this.boxes = boxes;
            this.parent = parent;
            this.move = move;
        }

        // A unique key formed by sorting the boxes’ positions. This allows us to keep track of visited states.
        public String getKey() {
            List<Point> points = new ArrayList<>(boxes);
            Collections.sort(points);
            StringBuilder sb = new StringBuilder();
            for (Point p : points)
                sb.append(p.r).append(",").append(p.c).append(";");
            return sb.toString();
        }
    }

    // Checks if an "air" path exists from start to end.
    // In this search, a box occupies its cell as an obstacle. (There are no walls any longer.)
    static boolean pathExists(char[][] grid, Set<Point> boxes, Point start, Point end) {
        int rows = grid.length, cols = grid[0].length;
        boolean[][] visited = new boolean[rows][cols];
        Queue<Point> queue = new LinkedList<>();
        queue.add(start);
        visited[start.r][start.c] = true;
        int[] dr = {-1, 1, 0, 0};
        int[] dc = {0, 0, -1, 1};
        while (!queue.isEmpty()){
            Point cur = queue.poll();
            if (cur.equals(end))
                return true;
            for (int i = 0; i < 4; i++){
                int nr = cur.r + dr[i];
                int nc = cur.c + dc[i];
                if (nr < 0 || nr >= rows || nc < 0 || nc >= cols)
                    continue;
                if (visited[nr][nc])
                    continue;
                if (boxes.contains(new Point(nr, nc)))
                    continue;
                visited[nr][nc] = true;
                queue.add(new Point(nr, nc));
            }
        }
        return false;
    }

    // Determines whether a given box is movable – that is, whether it has at least one free adjacent cell.
    private static boolean isBoxMovable(Point box, char[][] grid, Set<Point> boxes) {
        int rows = grid.length, cols = grid[0].length;
        int[] dr = {-1, 1, 0, 0};
        int[] dc = {0, 0, -1, 1};
        for (int i = 0; i < 4; i++){
            int nr = box.r + dr[i];
            int nc = box.c + dc[i];
            if (nr < 0 || nr >= rows || nc < 0 || nc >= cols)
                continue;
            if (boxes.contains(new Point(nr, nc)))
                continue;
            return true;
        }
        return false;
    }

    // Checks if a vital cell (such as the end) becomes permanently blocked.
    // For each adjacent cell (if inside bounds), if it is empty or occupied by a movable box then the cell is not blocked.
    private static boolean isPermanentlyBlocked(Point cell, Set<Point> boxes, char[][] grid) {
        int rows = grid.length, cols = grid[0].length;
        int[] dr = {-1, 1, 0, 0};
        int[] dc = {0, 0, -1, 1};
        for (int i = 0; i < 4; i++){
            int nr = cell.r + dr[i], nc = cell.c + dc[i];
            if (nr < 0 || nr >= rows || nc < 0 || nc >= cols)
                continue;
            Point neighbor = new Point(nr, nc);
            if (!boxes.contains(neighbor))
                return false; // There is a free neighbor.
            else {
                if (isBoxMovable(neighbor, grid, boxes))
                    return false;
            }
        }
        return true;
    }

    // The maze solver method.
    // It returns an ordered list of box push moves (in the format "box -> (r, c) -> direction") that,
    // when applied, yield a valid clear air path from the start ('s') to the end ('e') with the fewest pushes.
    // If no solution is possible, it returns null.
    public static List<String> solve(char[][] grid) {
        int rows = grid.length, cols = grid[0].length;
        Point start = null, end = null;
        Set<Point> initialBoxes = new HashSet<>();

        // Parse the grid to locate start ('s'), end ('e'), and boxes ('o').
        // (There are no wall tiles in this grid.)
        for (int r = 0; r < rows; r++){
            for (int c = 0; c < cols; c++){
                char ch = grid[r][c];
                if (ch == 's') {
                    start = new Point(r, c);
                } else if (ch == 'e') {
                    end = new Point(r, c);
                } else if (ch == 'o'){
                    initialBoxes.add(new Point(r, c));
                }
            }
        }
        if (start == null || end == null) {
            System.out.println("Start or end not defined in grid.");
            return null;
        }

        // If the initial configuration already has an air path, then there is nothing to push.
        if (pathExists(grid, initialBoxes, start, end))
            return new ArrayList<>();

        // Use a breadth-first search over box configurations.
        Queue<State> queue = new LinkedList<>();
        Set<String> visited = new HashSet<>();
        State initState = new State(initialBoxes, null, null);
        queue.add(initState);
        visited.add(initState.getKey());

        // Define the four cardinal moves.
        int[] dr = {-1, 1, 0, 0};
        int[] dc = {0, 0, -1, 1};
        String[] dirNames = {"up", "down", "left", "right"};

        while (!queue.isEmpty()){
            State curr = queue.poll();
            for (Point box : curr.boxes) {
                for (int i = 0; i < 4; i++){
                    int newR = box.r + dr[i];
                    int newC = box.c + dc[i];

                    // Check grid boundaries.
                    if (newR < 0 || newR >= rows || newC < 0 || newC >= cols)
                        continue;
                    // Cannot push a box into an already occupied cell.
                    if (curr.boxes.contains(new Point(newR, newC)))
                        continue;
                    // Disallow pushing a box to any cell in the same row as the end.
                    if (newR == end.r)
                        continue;

                    // Create a new configuration by moving the current box.
                    Set<Point> newBoxes = new HashSet<>(curr.boxes);
                    newBoxes.remove(box);
                    newBoxes.add(new Point(newR, newC));

                    // Check if the end becomes permanently blocked.
                    if (isPermanentlyBlocked(end, newBoxes, grid))
                        continue;

                    State newState = new State(newBoxes, curr, new BoxMove(box.r, box.c, dirNames[i]));
                    String key = newState.getKey();
                    if (visited.contains(key))
                        continue;
                    visited.add(key);

                    // If this configuration yields a clear air path, reconstruct the move list.
                    if (pathExists(grid, newBoxes, start, end)) {
                        List<String> movesList = new ArrayList<>();
                        State temp = newState;
                        while (temp.move != null) {
                            movesList.add(temp.move.toString());
                            temp = temp.parent;
                        }
                        Collections.reverse(movesList);
                        return movesList;
                    }

                    queue.add(newState);
                }
            }
        }

        // No sequence of pushes leads to a clear air path.
        return null;
    }

    public static void main(String[] args) {
        // Note: This grid no longer includes wall tiles.
        // The grid is fixed-size, which can help limit the search space.
        char[][] grid = {
                {' ', ' ', ' ', ' ', 'e', ' ', ' ', ' ', ' '},
                {' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' '},
                {' ', 'o', ' ', 'o', ' ', 'o', 'o', 'o', ' '},
                {' ', ' ', 'o', ' ', 'o', ' ', 'o', ' ', ' '},
                {' ', ' ', 'o', 'o', ' ', 'o', ' ', ' ', ' '},
                {' ', ' ', ' ', ' ', 'o', ' ', 'o', ' ', ' '},
                {' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' '},
                {' ', ' ', ' ', ' ', 's', ' ', ' ', ' ', ' '}
        };

        List<String> solution = solve(grid);
        if (solution == null) {
            System.out.println("No solutions possible.");
        } else if (solution.isEmpty()) {
            System.out.println("No box pushes needed. The air path is already clear.");
        } else {
            for (String move : solution) {
                System.out.println(move);
            }
        }
    }
}