package ae.skydoppler.dungeon.solver;

import java.util.*;

public class box_solver {

    // Represents a grid cell.
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
            return (this.r != other.r) ? (this.r - other.r) : (this.c - other.c);
        }
        @Override
        public String toString() {
            return "(" + r + ", " + c + ")";
        }
    }

    // Represents a push move made on a box.
    static class BoxMove {
        int r, c; // original box position before being pushed
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

    // Each state represents a configuration of boxes on the grid.
    // The parent pointer and move allow us to reconstruct the solution.
    static class State {
        Set<Point> boxes;
        State parent;
        BoxMove move;
        public State(Set<Point> boxes, State parent, BoxMove move) {
            this.boxes = boxes;
            this.parent = parent;
            this.move = move;
        }
        // Generates a unique key based on sorted box positions.
        public String getKey() {
            List<Point> pts = new ArrayList<>(boxes);
            Collections.sort(pts);
            StringBuilder sb = new StringBuilder();
            for (Point p : pts) {
                sb.append(p.r).append(",").append(p.c).append(";");
            }
            return sb.toString();
        }
    }

    // Checks whether there is an unobstructed "air" path from start to end.
    // Boxes are treated as obstacles; there are no walls.
    static boolean pathExists(char[][] grid, Set<Point> boxes, Point start, Point end) {
        int rows = grid.length, cols = grid[0].length;
        boolean[][] visited = new boolean[rows][cols];
        Queue<Point> queue = new LinkedList<>();
        queue.add(start);
        visited[start.r][start.c] = true;
        
        int[] dr = {-1, 1, 0, 0};
        int[] dc = {0, 0, -1, 1};
        
        while (!queue.isEmpty()) {
            Point cur = queue.poll();
            if (cur.equals(end))
                return true;
            for (int i = 0; i < 4; i++) {
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
    
    // Determines if the given box has at least one adjacent free cell.
    private static boolean isBoxMovable(Point box, char[][] grid, Set<Point> boxes) {
        int rows = grid.length, cols = grid[0].length;
        int[] dr = {-1, 1, 0, 0}, dc = {0, 0, -1, 1};
        for (int i = 0; i < 4; i++) {
            int nr = box.r + dr[i], nc = box.c + dc[i];
            if (nr < 0 || nr >= rows || nc < 0 || nc >= cols)
                continue;
            if (boxes.contains(new Point(nr, nc)))
                continue;
            return true;
        }
        return false;
    }
    
    // Checks whether a vital cell (here, the end) becomes permanently blocked.
    // For every neighbor, if a free cell or a movable box exists then the cell isn’t blocked.
    private static boolean isPermanentlyBlocked(Point cell, Set<Point> boxes, char[][] grid) {
        int rows = grid.length, cols = grid[0].length;
        int[] dr = {-1, 1, 0, 0}, dc = {0, 0, -1, 1};
        for (int i = 0; i < 4; i++){
            int nr = cell.r + dr[i], nc = cell.c + dc[i];
            if (nr < 0 || nr >= rows || nc < 0 || nc >= cols)
                continue;
            Point neighbor = new Point(nr, nc);
            if (!boxes.contains(neighbor))
                return false;  // Found a free adjacent cell.
            else if (isBoxMovable(neighbor, grid, boxes))
                return false;
        }
        return true;
    }
    
    // The solver returns an ordered list of box push moves (as strings)
    // that yield an open air path from start ('s') to end ('e') using the fewest pushes.
    // Returns null if no solution exists.
    public static List<String> solve(char[][] grid) {
        int rows = grid.length, cols = grid[0].length;
        Point start = null, end = null;
        Set<Point> initialBoxes = new HashSet<>();
        
        // Parse the grid: find start ('s'), end ('e'), and boxes ('o').
        for (int r = 0; r < rows; r++){
            for (int c = 0; c < cols; c++){
                char ch = grid[r][c];
                if (ch == 's')
                    start = new Point(r, c);
                else if (ch == 'e')
                    end = new Point(r, c);
                else if (ch == 'o')
                    initialBoxes.add(new Point(r, c));
            }
        }
        if (start == null || end == null) {
            System.out.println("Start or end not defined in grid.");
            return null;
        }
        
        // If an air path already exists, no pushes are needed.
        if (pathExists(grid, initialBoxes, start, end))
            return new ArrayList<>();
        
        // BFS over box configurations.
        Queue<State> queue = new LinkedList<>();
        Set<String> visited = new HashSet<>();
        State initState = new State(initialBoxes, null, null);
        queue.add(initState);
        visited.add(initState.getKey());
        
        // Define four cardinal directions.
        int[] dr = {-1, 1, 0, 0};
        int[] dc = {0, 0, -1, 1};
        String[] dirNames = {"up", "down", "left", "right"};
        
        while (!queue.isEmpty()) {
            State curr = queue.poll();
            // Try pushing every box from the current configuration.
            for (Point box : curr.boxes) {
                for (int i = 0; i < 4; i++) {
                    int newR = box.r + dr[i];
                    int newC = box.c + dc[i];
                    
                    // Ensure destination is within grid bounds.
                    if (newR < 0 || newR >= rows || newC < 0 || newC >= cols)
                        continue;
                    // Do not push into an already occupied cell.
                    if (curr.boxes.contains(new Point(newR, newC)))
                        continue;
                    
                    // Check that there is an "empty" pushing tile behind the box.
                    int behindR = box.r - dr[i];
                    int behindC = box.c - dc[i];
                    if (behindR < 0 || behindR >= rows || behindC < 0 || behindC >= cols)
                        continue;  // Cannot push from off the screen.
                    if (curr.boxes.contains(new Point(behindR, behindC)))
                        continue;  // The tile behind is blocked.
                    
                    // NEW RESTRICTION: Do not push a box into any cell in the same row as the start or the end.
                    if (newR == start.r || newR == end.r)
                        continue;
                    
                    // Create a new configuration.
                    Set<Point> newBoxes = new HashSet<>(curr.boxes);
                    newBoxes.remove(box);
                    newBoxes.add(new Point(newR, newC));
                    
                    // Heuristic check: if the end is permanently blocked, skip this move.
                    if (isPermanentlyBlocked(end, newBoxes, grid))
                        continue;
                    
                    State newState = new State(newBoxes, curr, new BoxMove(box.r, box.c, dirNames[i]));
                    String key = newState.getKey();
                    if (visited.contains(key))
                        continue;
                    visited.add(key);
                    
                    // If this configuration yields a clear air path, reconstruct the solution.
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
        // No solution found.
        return null;
    }
    
    public static void main(String[] args) {
        // Create a grid with only spaces, boxes ('o'), start ('s'), and end ('e').
        char[][] grid = {
            {' ', ' ', ' ', 'e', ' ', ' ', ' '},
            {' ', ' ', ' ', ' ', ' ', 'o', ' '},
            {'o', 'o', 'o', 'o', 'o', 'o', 'o'},
            {' ', 'o', ' ', 'o', ' ', 'o', ' '},
            {'o', 'o', 'o', ' ', 'o', ' ', ' '},
            {' ', ' ', 'o', 'o', ' ', 'o', ' '},
            {' ', ' ', ' ', ' ', ' ', ' ', ' '},
            {' ', ' ', ' ', 's', ' ', ' ', ' '}
        };

        List<String> solution = solve(grid);
        if (solution == null) {
            System.out.println("No solutions possible.");
        } else if (solution.isEmpty()) {
            System.out.println("No box pushes needed. Air path is already clear.");
        } else {
            for (String move : solution) {
                System.out.println(move);
            }
        }
    }
}
