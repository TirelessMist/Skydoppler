package ae.skydoppler.dungeon.solver;
import java.util.*;

public class box_solver {

    // A helper class representing a position in the grid.
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
        public String toString() {
            return "(" + r + ", " + c + ")";
        }
    }

    // This class holds a push move (the box's current location and the direction to push it).
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

    // A state is represented solely by the positions of boxes. The parent pointer and move that
    // led to this state allow us to reconstruct the full sequence once the state with a clear path
    // from start to end is reached.
    static class State {
        Set<Point> boxes;
        State parent;
        BoxMove move; // move that got us from the parent to this state

        public State(Set<Point> boxes, State parent, BoxMove move) {
            this.boxes = boxes;
            this.parent = parent;
            this.move = move;
        }

        // Get a unique key for the state by sorting the box positions.
        public String getKey() {
            List<Point> points = new ArrayList<>(boxes);
            Collections.sort(points);
            StringBuilder sb = new StringBuilder();
            for (Point p : points) {
                sb.append(p.r).append(",").append(p.c).append(";");
            }
            return sb.toString();
        }
    }

    // Checks if there is a clear path from start to end on the grid.
    // Cells with 'w' are walls and cells occupied by boxes are treated as obstacles.
    static boolean pathExists(char[][] grid, Set<Point> boxes, Point start, Point end) {
        int rows = grid.length, cols = grid[0].length;
        boolean[][] visited = new boolean[rows][cols];
        Queue<Point> queue = new LinkedList<>();
        queue.add(start);
        visited[start.r][start.c] = true;

        // Directions: up, down, left, right
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
                if (grid[nr][nc] == 'w')  // walls are impassable
                    continue;
                if (boxes.contains(new Point(nr, nc)))  // boxes block the air path
                    continue;
                visited[nr][nc] = true;
                queue.add(new Point(nr, nc));
            }
        }
        return false;
    }

    // The solver takes the grid and returns an ordered list of push moves (as strings) that,
    // when applied, will yield a clear path from 's' to 'e' using the fewest pushes possible.
    // If no series of pushes can achieve this, the method returns null.
    public static List<String> solve(char[][] grid) {
        int rows = grid.length;
        if (rows == 0) return null;
        int cols = grid[0].length;

        Point start = null, end = null;
        Set<Point> initialBoxes = new HashSet<>();

        // Parse the grid to locate start ('s'), end ('e'), and boxes ('o').
        // Walls ('w') remain static.
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
        if (start == null || end == null){
            System.out.println("Start or end not defined in grid.");
            return null;
        }

        // If the initial configuration already provides a clear path, then no pushes are needed.
        if (pathExists(grid, initialBoxes, start, end)) {
            // return an empty list since no box pushes are required.
            return new ArrayList<>();
        }

        // Begin a BFS (on the configuration space of box positions) to search for a solution
        Queue<State> queue = new LinkedList<>();
        Set<String> visited = new HashSet<>();

        State initState = new State(initialBoxes, null, null);
        queue.add(initState);
        visited.add(initState.getKey());

        // Direction arrays for the four cardinal directions.
        int[] dr = {-1, 1, 0, 0};
        int[] dc = {0, 0, -1, 1};
        String[] dirNames = {"up", "down", "left", "right"};

        while (!queue.isEmpty()){
            State curr = queue.poll();
            // Try every box in the current state.
            for (Point box : curr.boxes) {
                for (int i = 0; i < 4; i++){
                    int newR = box.r + dr[i];
                    int newC = box.c + dc[i];

                    // Ensure the new position is inside the maze.
                    if (newR < 0 || newR >= rows || newC < 0 || newC >= cols)
                        continue;

                    // Cannot push a box into a wall or into another box.
                    if (grid[newR][newC] == 'w') continue;
                    if (curr.boxes.contains(new Point(newR, newC))) continue;

                    // Create a new state by moving the current box.
                    Set<Point> newBoxes = new HashSet<>(curr.boxes);
                    newBoxes.remove(box);
                    newBoxes.add(new Point(newR, newC));

                    State newState = new State(newBoxes, curr, new BoxMove(box.r, box.c, dirNames[i]));
                    String key = newState.getKey();
                    if (visited.contains(key))
                        continue;
                    visited.add(key);

                    // Check if the new configuration allows an unobstructed air path from start to end.
                    if (pathExists(grid, newBoxes, start, end)) {
                        List<String> movesList = new ArrayList<>();
                        // Reconstruct the series of moves by backtracking.
                        State temp = newState;
                        while (temp.move != null) {
                            movesList.add(temp.move.toString());
                            temp = temp.parent;
                        }
                        Collections.reverse(movesList);
                        return movesList;
                    }

                    // Add the new state into the queue for further processing.
                    queue.add(newState);
                }
            }
        }

        // If no sequence of pushes yields a clear path, return null.
        return null;
    }

    // A sample main method to test using the provided grid.
    public static void main(String[] args) {
        char[][] grid = {
                {'w', 'w', 'w', 'w', 'e', 'w', 'w', 'w', 'w'},
                {'w', ' ', ' ', ' ', ' ', ' ', ' ', ' ', 'w'},
                {'w', 'o', ' ', 'o', ' ', 'o', 'o', 'o', 'w'},
                {'w', ' ', 'o', ' ', 'o', ' ', 'o', ' ', 'w'},
                {'w', ' ', 'o', 'o', ' ', 'o', ' ', ' ', 'w'},
                {'w', ' ', ' ', ' ', 'o', ' ', 'o', ' ', 'w'},
                {'w', ' ', ' ', ' ', ' ', ' ', ' ', ' ', 'w'},
                {'w', ' ', ' ', ' ', 's', ' ', ' ', ' ', 'w'}
        };

        List<String> solution = solve(grid);
        if (solution == null) {
            System.out.println("No solutions possible.");
        } else if (solution.isEmpty()) {
            System.out.println("No box pushes needed. The path is already clear.");
        } else {
            for (String move : solution) {
                System.out.println(move);
            }
        }
    }
}