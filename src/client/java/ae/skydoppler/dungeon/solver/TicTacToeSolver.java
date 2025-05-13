package ae.skydoppler.dungeon.solver;

public class TicTacToeSolver {

    /**
     * Evaluates the board state.
     * Returns +10 if "o" is a winner, -10 if "x" is a winner, and 0 otherwise.
     */
    private int evaluate(char[][] board) {
        // Check rows for victory.
        for (int row = 0; row < 3; row++) {
            if (board[row][0] != ' ' && board[row][0] == board[row][1] && board[row][1] == board[row][2]) {
                return board[row][0] == 'o' ? +10 : -10;
            }
        }
        
        // Check columns for victory.
        for (int col = 0; col < 3; col++) {
            if (board[0][col] != ' ' && board[0][col] == board[1][col] && board[1][col] == board[2][col]) {
                return board[0][col] == 'o' ? +10 : -10;
            }
        }
        
        // Check diagonals for victory.
        if (board[0][0] != ' ' && board[0][0] == board[1][1] && board[1][1] == board[2][2]) {
            return board[0][0] == 'o' ? +10 : -10;
        }
        if (board[0][2] != ' ' && board[0][2] == board[1][1] && board[1][1] == board[2][0]) {
            return board[0][2] == 'o' ? +10 : -10;
        }
        
        return 0;
    }

    /**
     * Checks if any moves are left on the board.
     */
    private boolean isMovesLeft(char[][] board) {
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                if (board[i][j] == ' ') {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * The minimax algorithm. 
     * @param board The current board state.
     * @param depth The current depth in the game tree, used to favor quicker wins or slower losses.
     * @param isMax True if the current move is for the maximizer ("o") and false for the minimizer ("x").
     * @return The evaluated score of the board.
     */
    private int minimax(char[][] board, int depth, boolean isMax) {
        int score = evaluate(board);

        // If the game has a winner, return the evaluated score adjusted by depth.
        if (score == 10) {
            return score - depth;  // Prefer winning sooner.
        }
        if (score == -10) {
            return score + depth;  // Prefer delaying a loss.
        }

        // If no more moves and no winner, it's a tie.
        if (!isMovesLeft(board)) {
            return 0;
        }

        if (isMax) {
            // "o" is the maximizer.
            int best = Integer.MIN_VALUE;
            for (int i = 0; i < 3; i++) {
                for (int j = 0; j < 3; j++) {
                    if (board[i][j] == ' ') {
                        board[i][j] = 'o';
                        best = Math.max(best, minimax(board, depth + 1, false));
                        board[i][j] = ' ';
                    }
                }
            }
            return best;
        } else {
            // "x" is the minimizer.
            int best = Integer.MAX_VALUE;
            for (int i = 0; i < 3; i++) {
                for (int j = 0; j < 3; j++) {
                    if (board[i][j] == ' ') {
                        board[i][j] = 'x';
                        best = Math.min(best, minimax(board, depth + 1, true));
                        board[i][j] = ' ';
                    }
                }
            }
            return best;
        }
    }

    /**
     * Evaluates the board and returns the best move [row, col] for the "o" player.
     */
    public int[] getBestMove(char[][] board) {
        int bestVal = Integer.MIN_VALUE;
        int[] bestMove = { -1, -1 };

        // Iterate over all cells, evaluate minimax for each empty cell.
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                if (board[i][j] == ' ') {
                    // Make the move.
                    board[i][j] = 'o';
                    // Compute evaluation function for this move.
                    int moveVal = minimax(board, 0, false);
                    // Undo the move.
                    board[i][j] = ' ';
                    // If the value of the current move is better than the best so far, update best.
                    if (moveVal > bestVal) {
                        bestMove[0] = i;
                        bestMove[1] = j;
                        bestVal = moveVal;
                    }
                }
            }
        }
        return bestMove;
    }

    // Example usage.
    public static void main(String[] args) {
        TicTacToeSolver solver = new TicTacToeSolver();
        char[][] board = {
            { 'x', 'o', 'x' },
            { ' ', 'o', ' ' },
            { ' ', 'x', ' ' }
        };

        int[] bestMove = solver.getBestMove(board);
        System.out.println("The Best Move for 'o' is: Row = " + bestMove[0] + ", Col = " + bestMove[1]);
    }
}
