import java.io.*;
import java.util.Random;
import java.util.Scanner;

public class Game2048 {
    private static final int SIZE = 4;
    private int[][] board;
    private int score = 0;
    private int highScore = 0;
    private Random random = new Random();
    private boolean moved;

    private final String HIGH_SCORE_FILE = "highscore.txt";

    // ANSI color codes
    private static final String RESET = "\u001B[0m";
    private static final String BLACK = "\u001B[30m";
    private static final String RED = "\u001B[31m";
    private static final String GREEN = "\u001B[32m";
    private static final String YELLOW = "\u001B[33m";
    private static final String BLUE = "\u001B[34m";
    private static final String MAGENTA = "\u001B[35m";
    private static final String CYAN = "\u001B[36m";
    private static final String WHITE = "\u001B[37m";
    private static final String BRIGHT_BLACK = "\u001B[90m";  // Gray
    private static final String BRIGHT_CYAN = "\u001B[96m";
    private static final String BRIGHT_GREEN = "\u001B[92m";
    private static final String BRIGHT_YELLOW = "\u001B[93m";
    private static final String BRIGHT_BLUE = "\u001B[94m";
    private static final String BRIGHT_MAGENTA = "\u001B[95m";

    public Game2048() {
        board = new int[SIZE][SIZE];
        loadHighScore();
        addRandomTile();
        addRandomTile();
    }

    public void play() {
        Scanner sc = new Scanner(System.in);
        while (true) {
            printBoard();
            System.out.println("Score: " + score + "  High Score: " + highScore);
            if (isGameOver()) {
                System.out.println("Game Over! No more moves possible.");
                if (score > highScore) {
                    System.out.println("Congratulations! New High Score: " + score);
                    saveHighScore();
                }
                break;
            }

            System.out.print("Enter move (W=Up, A=Left, S=Down, D=Right, Q=Quit): ");
            String input = sc.nextLine().toUpperCase();

            if (input.equals("Q")) {
                System.out.println("Quitting game. Thanks for playing!");
                if (score > highScore) {
                    System.out.println("Saving your new high score: " + score);
                    saveHighScore();
                }
                break;
            }

            moved = false;
            switch (input) {
                case "W" -> moveUp();
                case "A" -> moveLeft();
                case "S" -> moveDown();
                case "D" -> moveRight();
                default -> {
                    System.out.println("Invalid input! Use W, A, S, D or Q.");
                    continue;
                }
            }

            if (moved) {
                addRandomTile();
            } else {
                System.out.println("Move didn't change board. Try a different direction.");
            }
        }
        sc.close();
    }

    private void addRandomTile() {
        int emptyCount = 0;
        for (int[] row : board)
            for (int val : row)
                if (val == 0) emptyCount++;

        if (emptyCount == 0) return;

        int pos = random.nextInt(emptyCount);
        int val = random.nextInt(10) < 9 ? 2 : 4;

        int count = 0;
        outer:
        for (int i = 0; i < SIZE; i++) {
            for (int j = 0; j < SIZE; j++) {
                if (board[i][j] == 0) {
                    if (count == pos) {
                        board[i][j] = val;
                        break outer;
                    }
                    count++;
                }
            }
        }
    }

    private void printBoard() {
        System.out.println("\n---- 2048 Game ----");
        for (int[] row : board) {
            for (int val : row) {
                String color = getColorForValue(val);
                if (val == 0) {
                    System.out.print(color + ".\t" + RESET);
                } else {
                    System.out.print(color + val + "\t" + RESET);
                }
            }
            System.out.println();
        }
    }

    private String getColorForValue(int val) {
        return switch (val) {
            case 0 -> BRIGHT_BLACK;
            case 2 -> WHITE;
            case 4 -> CYAN;
            case 8 -> GREEN;
            case 16 -> YELLOW;
            case 32 -> BLUE;
            case 64 -> MAGENTA;
            case 128 -> BRIGHT_CYAN;
            case 256 -> BRIGHT_GREEN;
            case 512 -> BRIGHT_YELLOW;
            case 1024 -> BRIGHT_BLUE;
            default -> BRIGHT_MAGENTA; // 2048+
        };
    }

    private boolean isGameOver() {
        for (int[] row : board)
            for (int val : row)
                if (val == 0) return false;

        for (int i = 0; i < SIZE; i++) {
            for (int j = 0; j < SIZE - 1; j++) {
                if (board[i][j] == board[i][j + 1]) return false;
                if (board[j][i] == board[j + 1][i]) return false;
            }
        }
        return true;
    }

    // Movement Methods:
    private void moveLeft() {
        for (int i = 0; i < SIZE; i++) {
            int[] compressed = compress(board[i]);
            int[] merged = merge(compressed);
            int[] newRow = compress(merged);
            if (!arrayEquals(board[i], newRow)) moved = true;
            board[i] = newRow;
        }
    }

    private void moveRight() {
        for (int i = 0; i < SIZE; i++) {
            int[] reversed = reverse(board[i]);
            int[] compressed = compress(reversed);
            int[] merged = merge(compressed);
            int[] newRow = compress(merged);
            newRow = reverse(newRow);
            if (!arrayEquals(board[i], newRow)) moved = true;
            board[i] = newRow;
        }
    }

    private void moveUp() {
        board = transpose(board);
        moveLeft();
        board = transpose(board);
    }

    private void moveDown() {
        board = transpose(board);
        moveRight();
        board = transpose(board);
    }

    // Helpers

    private int[] compress(int[] row) {
        int[] newRow = new int[SIZE];
        int index = 0;
        for (int val : row) {
            if (val != 0) newRow[index++] = val;
        }
        return newRow;
    }

    private int[] merge(int[] row) {
        for (int i = 0; i < SIZE - 1; i++) {
            if (row[i] != 0 && row[i] == row[i + 1]) {
                row[i] *= 2;
                score += row[i];
                row[i + 1] = 0;
                i++;
            }
        }
        return row;
    }

    private int[] reverse(int[] row) {
        int[] newRow = new int[SIZE];
        for (int i = 0; i < SIZE; i++) {
            newRow[i] = row[SIZE - 1 - i];
        }
        return newRow;
    }

    private int[][] transpose(int[][] matrix) {
        int[][] transposed = new int[SIZE][SIZE];
        for (int i = 0; i < SIZE; i++)
            for (int j = 0; j < SIZE; j++)
                transposed[i][j] = matrix[j][i];
        return transposed;
    }

    private boolean arrayEquals(int[] a, int[] b) {
        if (a.length != b.length) return false;
        for (int i = 0; i < a.length; i++)
            if (a[i] != b[i]) return false;
        return true;
    }

    // High Score I/O
    private void loadHighScore() {
        File file = new File(HIGH_SCORE_FILE);
        if (file.exists()) {
            try (BufferedReader br = new BufferedReader(new FileReader(file))) {
                String line = br.readLine();
                if (line != null) {
                    highScore = Integer.parseInt(line.trim());
                }
            } catch (Exception e) {
                System.out.println("Failed to load high score.");
            }
        }
    }

    private void saveHighScore() {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(HIGH_SCORE_FILE))) {
            bw.write(String.valueOf(score));
        } catch (IOException e) {
            System.out.println("Failed to save high score.");
        }
    }

    public static void main(String[] args) {
        Game2048 game = new Game2048();
        game.play();
    }
}
