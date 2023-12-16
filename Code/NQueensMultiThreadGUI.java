import javax.swing.*;
import java.awt.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class NQueensMultiThreadGUI {

    private static int Threads;
    private static JFrame frame;
    private static JTextField rowsField;
    private static JTextArea textArea;
    private static JButton printSolutionsButton;

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            frame = new JFrame("N-Queens Solutions");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setSize(800, 800);
            frame.setLayout(new BorderLayout());

            JPanel panel = new JPanel(new FlowLayout());
            panel.add(new JLabel("Enter the number of rows:"));
            rowsField = new JTextField(6);
            panel.add(rowsField);
            printSolutionsButton = new JButton("Print Solutions");
            printSolutionsButton.addActionListener(e -> {
                try {
                    int numberOfRows = Integer.parseInt(rowsField.getText());
                    Threads = numberOfRows;
                    if (numberOfRows <= 0) {
                        JOptionPane.showMessageDialog(frame, "Number of rows must be greater than 0.", "Error", JOptionPane.ERROR_MESSAGE);
                    } else {
                        solveNQueens(numberOfRows);
                    }
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(frame, "Enter Number.", "Error", JOptionPane.ERROR_MESSAGE);
                }
            });
            panel.add(printSolutionsButton);
            frame.add(panel, BorderLayout.NORTH);

            textArea = new JTextArea();
            textArea.setEditable(false);
            textArea.setFont(new Font("Arial", Font.PLAIN, 16));
            textArea.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

            frame.add(new JScrollPane(textArea), BorderLayout.CENTER);
            frame.setVisible(true);
        });
    }

    private static void solveNQueens(int n) {
        ExecutorService executorService = Executors.newFixedThreadPool(Threads);

        for (int i = 0; i < n; i++) {
            int startCol = i;
            executorService.submit(() -> solveNQueens(n, startCol));
        }

        executorService.shutdown();

        try {
            executorService.awaitTermination(1, TimeUnit.HOURS);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        System.out.println("NumberOfThreads: " + Threads);
    }

    private static void solveNQueens(int n, int startCol) {
        int[] queens = new int[n];
        placeQueens(0, startCol, queens);

    }

    private static void placeQueens(int row, int col, int[] queens) {
        if (row == queens.length) {
            printSolution(queens);
            return;
        }

        for (int i = 0; i < queens.length; i++) {
            if (isValidPlacement(row, i, queens)) {
                queens[row] = i;
                placeQueens(row + 1, col, queens);
            }
        }
    }

    private static boolean isValidPlacement(int row, int col, int[] queens) {
        for (int i = 0; i < row; i++) {
            if (queens[i] == col || queens[i] - i == col - row || queens[i] + i == col + row) {
                return false;
            }
        }
        return true;
    }

    private static void printSolution(int[] queens) {
        StringBuilder sb = new StringBuilder("Solution found:\n");

        for (int i = 0; i < queens.length; i++) {
            for (int j = 0; j < queens.length; j++) {
                sb.append(queens[i] == j ? "Q " : "* ");
            }
            sb.append("\n");
        }

        sb.append("\n");
        SwingUtilities.invokeLater(() -> textArea.append(sb.toString()));
    }
}
