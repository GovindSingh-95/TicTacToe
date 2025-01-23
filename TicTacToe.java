import java.awt.*;
import java.awt.event.*;

public class TicTacToe extends Frame implements ActionListener {
    private final Button[][] buttons = new Button[3][3];
    private final char[][] board = new char[3][3];
    private boolean playerTurn = true; // true for Player (X), false for Computer (O)

    public TicTacToe() {
        setTitle("Tic-Tac-Toe ");
        setSize(400, 400);
        setLayout(new GridLayout(3, 3));

        // Initialize buttons and board
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                buttons[i][j] = new Button("");
                buttons[i][j].setFont(new Font("Arial", Font.BOLD, 60));
                buttons[i][j].addActionListener(this);
                add(buttons[i][j]);
                board[i][j] = ' ';
            }
        }

        setVisible(true);
        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                System.exit(0);
            }
        });
    }

    public static void main(String[] args) {
        new TicTacToe();
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        Button clickedButton = (Button) e.getSource();

        if (playerTurn) {
            for (int i = 0; i < 3; i++) {
                for (int j = 0; j < 3; j++) {
                    if (clickedButton == buttons[i][j] && board[i][j] == ' ') {
                        board[i][j] = 'X';
                        buttons[i][j].setLabel("X");
                        playerTurn = false;

                        if (isWinner('X')) {
                            showResult("Congratulations! You win!");
                            return;
                        } else if (isBoardFull()) {
                            showResult("It's a draw!");
                            return;
                        }

                        computerMove(); // Computer's turn
                        return;
                    }
                }
            }
        }
    }

    private void computerMove() {
        int[] bestMove = findBestMove();
        board[bestMove[0]][bestMove[1]] = 'O';
        buttons[bestMove[0]][bestMove[1]].setLabel("O");

        if (isWinner('O')) {
            showResult("Computer wins! Better luck next time.");
        } else if (isBoardFull()) {
            showResult("It's a draw!");
        }

        playerTurn = true; // Switch back to player's turn
    }

    // Check if the board is full
    private boolean isBoardFull() {
        for (char[] row : board) {
            for (char cell : row) {
                if (cell == ' ') return false;
            }
        }
        return true;
    }

    // Check if a player has won
    private boolean isWinner(char player) {
        for (int i = 0; i < 3; i++) {
            if (board[i][0] == player && board[i][1] == player && board[i][2] == player) return true;
            if (board[0][i] == player && board[1][i] == player && board[2][i] == player) return true;
        }
        return (board[0][0] == player && board[1][1] == player && board[2][2] == player) ||
                (board[0][2] == player && board[1][1] == player && board[2][0] == player);
    }

    // Show result and disable the board
    private void showResult(String message) {
        for (Button[] row : buttons) {
            for (Button button : row) {
                button.setEnabled(false);
            }
        }
        new DialogResult(this, message);
    }

    // Find the best move using Minimax algorithm
    private int[] findBestMove() {
        int bestScore = Integer.MIN_VALUE;
        int[] bestMove = {-1, -1};

        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                if (board[i][j] == ' ') {
                    board[i][j] = 'O'; // Simulate the move
                    int score = minimax(0, false);
                    board[i][j] = ' '; // Undo the move
                    if (score > bestScore) {
                        bestScore = score;
                        bestMove = new int[]{i, j};
                    }
                }
            }
        }
        return bestMove;
    }

    // Minimax algorithm
    private int minimax(int depth, boolean isMaximizing) {
        if (isWinner('O')) return 10 - depth; // Favor faster wins
        if (isWinner('X')) return depth - 10; // Favor slower losses
        if (isBoardFull()) return 0; // Draw

        if (isMaximizing) {
            int bestScore = Integer.MIN_VALUE;
            for (int i = 0; i < 3; i++) {
                for (int j = 0; j < 3; j++) {
                    if (board[i][j] == ' ') {
                        board[i][j] = 'O';
                        bestScore = Math.max(bestScore, minimax(depth + 1, false));
                        board[i][j] = ' ';
                    }
                }
            }
            return bestScore;
        } else {
            int bestScore = Integer.MAX_VALUE;
            for (int i = 0; i < 3; i++) {
                for (int j = 0; j < 3; j++) {
                    if (board[i][j] == ' ') {
                        board[i][j] = 'X';
                        bestScore = Math.min(bestScore, minimax(depth + 1, true));
                        board[i][j] = ' ';
                    }
                }
            }
            return bestScore;
        }
    }
}

// Helper class for showing the result in a dialog
class DialogResult extends Dialog {
    public DialogResult(Frame owner, String message) {
        super(owner, "Game Over", true);
        setLayout(new FlowLayout());
        add(new Label(message));
        Button ok = new Button("OK");
        ok.addActionListener(e -> System.exit(0));
        add(ok);
        setSize(200, 100);
        setVisible(true);
    }
}
