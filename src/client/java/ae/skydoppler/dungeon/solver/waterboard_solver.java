package ae.skydoppler.dungeon.solver;
import java.util.*;

public class waterboard_solver {

    // Number of milliseconds per tick (Minecraft runs at ~20 ticks per second).
    private static final int TICK_DURATION_MS = 50;

    // The main function to solve the maze.
    // It returns a mapping of lever toggling times (lever letter -> list of milli-second times)
    // or null if no solution is found within maxSteps.
    public static Map<Character, List<Integer>> solveMaze(char[][] grid, boolean[] doorsInitial) {
        int maxSteps = 200;  // maximum simulation ticks to try
        int rows = grid.length;
        int cols = grid[0].length;

        // Create an empty water level grid (all cells start with 0 water).
        int[][] emptyWaterLevels = new int[rows][cols];

        // Build the mapping for lever events (levers for blocks: a-f, and water: w).
        Map<Character, List<Integer>> events = new HashMap<>();
        for (char c = 'a'; c <= 'f'; c++) {
            events.put(c, new ArrayList<>());
        }
        events.put('w', new ArrayList<>());

        // Create the initial state. The water source is initially off.
        MazeState initState = new MazeState(deepCopyGrid(grid), emptyWaterLevels, doorsInitial.clone(), false, 0, events);

        // Use a breadth-first search (BFS) over the state space.
        Queue<MazeState> queue = new LinkedList<>();
        Set<String> visited = new HashSet<>();
        queue.offer(initState);
        visited.add(initState.getStateKey());

        // All possible actions: '-' (do nothing), levers 'a'-'f', and 'w' for water source.
        char[] possibleActions = new char[] {'-', 'a', 'b', 'c', 'd', 'e', 'f', 'w'};

        while (!queue.isEmpty()) {
            MazeState curState = queue.poll();

            // Check if the goal has been reached: every door is open.
            if (allDoorsOpen(curState.doors)) {
                return curState.events;
            }

            if (curState.time > maxSteps) {
                continue;
            }

            // Try every lever action (including doing nothing this tick).
            for (char action : possibleActions) {
                MazeState nextState = curState.cloneState();
                if (action != '-') {
                    applyLeverToggle(nextState, action);
                    // Instead of adding tick numbers, record the time in ms.
                    nextState.events.get(action).add(nextState.time * TICK_DURATION_MS);
                }

                // Update water using our Minecraft–like fluid simulation.
                simulateWater(nextState);

                nextState.time++; // advance simulation tick

                String key = nextState.getStateKey();
                if (!visited.contains(key)) {
                    visited.add(key);
                    queue.offer(nextState);
                }
            }
        }

        // No solution was found.
        return null;
    }

    // Toggle the effect of a lever.
    // For levers 'a'-'f': toggle every block (extended vs. retracted) that matches that color.
    // For lever 'w': toggle the water source flag.
    private static void applyLeverToggle(MazeState state, char lever) {
        if (lever == 'w') {
            state.waterSourceOn = !state.waterSourceOn;
        } else {
            for (int i = 0; i < state.grid.length; i++) {
                for (int j = 0; j < state.grid[0].length; j++) {
                    char cell = state.grid[i][j];
                    if (Character.toLowerCase(cell) == lever) {
                        // Toggle the block state: lowercase (retracted, passable) becomes uppercase (extended, wall-like),
                        // and vice versa.
                        if (Character.isLowerCase(cell))
                            state.grid[i][j] = Character.toUpperCase(cell);
                        else
                            state.grid[i][j] = Character.toLowerCase(cell);
                    }
                }
            }
        }
    }

    // Simulate Minecraft–like water physics using water levels.
    // If the water source is active, the water source cell (top–center) is set to level 8.
    // The water then flows downward (resetting to level 8 if possible) and horizontally
    // with a decrement of 1 per cell.
    // After propagation, any cell that is a goal digit ('1'-'5') triggers its door toggle.
    private static void simulateWater(MazeState state) {
        int rows = state.grid.length;
        int cols = state.grid[0].length;
        int[][] newWaterLevels = new int[rows][cols];

        if (state.waterSourceOn) {
            int sourceRow = 0;
            int sourceCol = cols / 2;
            newWaterLevels[sourceRow][sourceCol] = 8;

            // Use a BFS to propagate water levels.
            Queue<Cell> queue = new LinkedList<>();
            queue.offer(new Cell(sourceRow, sourceCol, 8));

            while (!queue.isEmpty()) {
                Cell cell = queue.poll();
                int r = cell.row, c = cell.col, level = cell.level;

                // Flow downward first.
                if (r + 1 < rows && isPassable(state.grid[r + 1][c])) {
                    if (newWaterLevels[r + 1][c] < 8) {
                        newWaterLevels[r + 1][c] = 8;
                        queue.offer(new Cell(r + 1, c, 8));
                    }
                }

                // Flow horizontally if there is enough energy (level > 1).
                if (level > 1) {
                    int newLevel = level - 1;
                    // Left neighbor.
                    if (c - 1 >= 0 && isPassable(state.grid[r][c - 1])) {
                        if (newWaterLevels[r][c - 1] < newLevel) {
                            newWaterLevels[r][c - 1] = newLevel;
                            queue.offer(new Cell(r, c - 1, newLevel));
                        }
                    }
                    // Right neighbor.
                    if (c + 1 < cols && isPassable(state.grid[r][c + 1])) {
                        if (newWaterLevels[r][c + 1] < newLevel) {
                            newWaterLevels[r][c + 1] = newLevel;
                            queue.offer(new Cell(r, c + 1, newLevel));
                        }
                    }
                }
            }
        }

        // Update the water level grid in the state.
        state.waterLevels = newWaterLevels;

        // For each cell that contains a goal digit ('1'-'5') and has water (level > 0),
        // toggle its corresponding door.
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                if (state.waterLevels[i][j] > 0) {
                    char ch = state.grid[i][j];
                    if (ch >= '1' && ch <= '5') {
                        int doorIndex = ch - '1';
                        state.doors[doorIndex] = !state.doors[doorIndex];
                    }
                }
            }
        }
    }

    // A cell is passable for water if it is not a wall ('x')
    // and not an “extended” toggleable block (A–F).
    private static boolean isPassable(char cell) {
        if (cell == 'x')
            return false;
        if (cell >= 'A' && cell <= 'F')
            return false;
        return true;
    }

    // Returns true only if every door in the passed array is open.
    private static boolean allDoorsOpen(boolean[] doors) {
        for (boolean open : doors) {
            if (!open)
                return false;
        }
        return true;
    }

    // Deep-copy the maze grid.
    private static char[][] deepCopyGrid(char[][] original) {
        char[][] copy = new char[original.length][];
        for (int i = 0; i < original.length; i++) {
            copy[i] = original[i].clone();
        }
        return copy;
    }

    // Deep-copy a water levels grid.
    private static int[][] deepCopyWaterLevels(int[][] original) {
        if (original == null)
            return null;
        int[][] copy = new int[original.length][];
        for (int i = 0; i < original.length; i++) {
            copy[i] = original[i].clone();
        }
        return copy;
    }

    // MazeState stores the current snapshot of the simulation.
    // It contains the maze grid, water level grid, door states, water source flag,
    // simulation time (in ticks), and the lever toggle events.
    private static class MazeState {
        char[][] grid;
        int[][] waterLevels;
        boolean[] doors;   // door[0] corresponds to goal '1', etc.
        boolean waterSourceOn;
        int time;
        Map<Character, List<Integer>> events;

        MazeState(char[][] grid, int[][] waterLevels, boolean[] doors, boolean waterSourceOn, int time,
                  Map<Character, List<Integer>> events) {
            this.grid = grid;
            this.waterLevels = waterLevels;
            this.doors = doors;
            this.waterSourceOn = waterSourceOn;
            this.time = time;
            this.events = new HashMap<>();
            for (Map.Entry<Character, List<Integer>> e : events.entrySet()) {
                this.events.put(e.getKey(), new ArrayList<>(e.getValue()));
            }
        }

        MazeState cloneState() {
            char[][] gridCopy = deepCopyGrid(this.grid);
            int[][] waterCopy = deepCopyWaterLevels(this.waterLevels);
            boolean[] doorsCopy = this.doors.clone();
            Map<Character, List<Integer>> eventsCopy = new HashMap<>();
            for (Map.Entry<Character, List<Integer>> e : this.events.entrySet()) {
                eventsCopy.put(e.getKey(), new ArrayList<>(e.getValue()));
            }
            return new MazeState(gridCopy, waterCopy, doorsCopy, this.waterSourceOn, this.time, eventsCopy);
        }

        // Returns a string key representing the state (grid, water levels, doors, time)
        // to help avoid revisiting duplicate states in our BFS.
        String getStateKey() {
            StringBuilder sb = new StringBuilder();
            sb.append(time).append("|").append(waterSourceOn).append("|");
            for (boolean d : doors) {
                sb.append(d ? "1" : "0");
            }
            sb.append("|");
            for (char[] row : grid) {
                sb.append(new String(row)).append(";");
            }
            sb.append("|");
            for (int i = 0; i < waterLevels.length; i++) {
                for (int j = 0; j < waterLevels[0].length; j++) {
                    sb.append(waterLevels[i][j]);
                }
                sb.append(";");
            }
            return sb.toString();
        }
    }

    // Helper class to represent a cell (row, col plus its water level)
    // used in the BFS during water propagation.
    private static class Cell {
        int row;
        int col;
        int level;

        Cell(int row, int col, int level) {
            this.row = row;
            this.col = col;
            this.level = level;
        }
    }

    // --- Sample Usage ---
    public static void main(String[] args) {
        char[][] debugGrid = {
                {'x', 'x', 'x', 'x', 'x', 'x', 'x', 'x', 'x', 'o', 'x', 'x', 'x', 'x', 'x', 'x', 'x', 'x', 'x'},
                {'x', 'x', 'x', 'x', 'x', 'o', 'o', 'o', 'e', 'o', 'A', 'o', 'o', 'o', 'o', 'o', 'x', 'x', 'x'},
                {'x', 'x', 'x', 'x', 'x', 'o', 'x', 'c', 'x', 'x', 'x', 'D', 'x', 'x', 'x', 'o', 'x', 'x', 'x'},
                {'x', 'x', 'x', 'x', 'x', 'o', 'B', 'o', 'E', 'o', 'o', 'o', 'x', 'x', 'x', 'o', 'x', 'x', 'x'},
                {'x', 'x', 'x', 'x', 'x', 'o', 'x', 'o', 'x', 'f', 'x', 'x', 'x', 'x', 'x', 'o', 'o', 'b', 'x'},
                {'o', 'o', 'o', 'o', 'o', 'o', 'o', 'o', 'x', 'o', 'x', 'x', 'x', 'x', 'x', 'c', 'x', 'o', 'x'},
                {'o', 'x', 'x', 'x', 'x', 'x', 'x', 'x', 'x', 'o', 'x', 'x', 'x', 'x', 'x', 'o', 'x', 'o', 'o'},
                {'o', 'x', 'o', 'o', 'o', 'o', 'o', 'o', 'o', 'o', 'x', 'x', 'x', 'x', 'x', 'o', 'x', 'x', 'o'},
                {'o', 'x', 'o', 'x', 'x', 'x', 'x', 'x', 'x', 'x', 'x', 'o', 'o', 'o', 'B', 'o', 'f', 'x', 'o'},
                {'o', 'x', 'o', 'x', 'x', 'x', 'x', 'x', 'x', 'x', 'o', 'o', 'x', 'x', 'x', 'x', 'o', 'x', 'o'},
                {'o', 'x', 'o', 'o', 'o', 'x', 'x', 'o', 'o', 'o', 'o', 'x', 'x', 'x', 'x', 'x', 'o', 'x', 'o'},
                {'o', 'x', 'x', 'x', 'o', 'x', 'x', 'o', 'x', 'x', 'x', 'x', 'o', 'o', 'o', 'o', 'o', 'x', 'o'},
                {'o', 'o', 'o', 'x', 'o', 'o', 'f', 'o', 'x', 'x', 'x', 'x', 'o', 'x', 'x', 'x', 'x', 'x', 'o'},
                {'x', 'x', 'o', 'x', 'x', 'x', 'x', 'o', 'o', 'o', 'x', 'x', 'o', 'o', 'x', 'o', 'o', 'o', 'o'},
                {'x', 'x', 'o', 'x', 'x', 'x', 'x', 'B', 'x', 'D', 'x', 'x', 'x', 'o', 'x', 'o', 'x', 'x', 'x'},
                {'o', 'e', 'o', 'c', 'o', 'o', 'f', 'o', 'x', 'o', 'o', 'o', 'B', 'o', 'x', 'o', 'o', 'o', 'o'},
                {'o', 'x', 'x', 'x', 'x', 'x', 'x', 'd', 'x', 'o', 'x', 'x', 'x', 'c', 'o', 'x', 'x', 'x', 'o'},
                {'o', 'x', 'x', 'x', 'x', 'x', 'x', 'o', 'x', 'o', 'x', 'x', 'x', 'x', 'o', 'o', 'o', 'o', 'o'},
                {'o', 'x', 'x', 'x', 'o', 'o', 'o', 'o', 'x', 'o', 'x', 'x', 'x', 'x', 'f', 'x', 'x', 'x', 'A'},
                {'1', 'x', 'x', 'x', '2', 'x', 'x', 'x', 'x', '3', 'x', 'x', 'x', 'x', '4', 'x', 'x', 'x', '5'},
        };

        // Assume all door states are initially false (closed)
        boolean[] doorsInitial = { false, false, false, false, false };

        Map<Character, List<Integer>> solution = solveMaze(debugGrid, doorsInitial);
        if (solution != null) {
            System.out.println("Solution found! Lever toggling schedule (lever -> list of ms timings):");
            for (Map.Entry<Character, List<Integer>> entry : solution.entrySet()) {
                System.out.println(entry.getKey() + " -> " + entry.getValue());
            }
        } else {
            System.out.println("No solution found.");
        }
    }
}