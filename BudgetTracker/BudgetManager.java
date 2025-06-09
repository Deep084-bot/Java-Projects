import java.io.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class BudgetManager {
    private List<Transaction> transactions;
    private final String STORAGE_FILE = "storage/transactions.csv";

    public BudgetManager() {
        transactions = new ArrayList<>();
        loadTransactions();
    }

    public void addTransaction(Transaction t) {
        transactions.add(t);
        saveTransactions();
    }

    public boolean deleteTransaction(int index) {
        if (index < 0 || index >= transactions.size()) {
            return false;
        }
        transactions.remove(index);
        saveTransactions();
        return true;
    }

    public List<Transaction> getAllTransactions() {
        return transactions;
    }

    public List<Transaction> getTransactionsByCategory(String category) {
        return transactions.stream()
                .filter(t -> t.getCategory().equalsIgnoreCase(category))
                .collect(Collectors.toList());
    }

    public List<Transaction> getTransactionsByDate(LocalDate date) {
        return transactions.stream()
                .filter(t -> t.getDate().equals(date))
                .collect(Collectors.toList());
    }

    public double getTotalIncome() {
        return transactions.stream()
                .filter(t -> t.getType().equalsIgnoreCase("Income"))
                .mapToDouble(Transaction::getAmount)
                .sum();
    }

    public double getTotalExpense() {
        return transactions.stream()
                .filter(t -> t.getType().equalsIgnoreCase("Expense"))
                .mapToDouble(Transaction::getAmount)
                .sum();
    }

    public double getBalance() {
        return getTotalIncome() - getTotalExpense();
    }

    private void saveTransactions() {
        try {
            File dir = new File("storage");
            if (!dir.exists()) dir.mkdirs();

            try (BufferedWriter writer = new BufferedWriter(new FileWriter(STORAGE_FILE))) {
                for (Transaction t : transactions) {
                    writer.write(t.toCSV());
                    writer.newLine();
                }
            }
        } catch (IOException e) {
            System.out.println("Error saving transactions: " + e.getMessage());
        }
    }

    private void loadTransactions() {
        transactions.clear();
        File file = new File(STORAGE_FILE);
        if (!file.exists()) return;

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = reader.readLine()) != null) {
                Transaction t = Transaction.fromCSV(line);
                if (t != null) {
                    transactions.add(t);
                }
            }
        } catch (IOException e) {
            System.out.println("Error loading transactions: " + e.getMessage());
        }
    }
}
