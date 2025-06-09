import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.Scanner;

public class Main {
    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        BudgetManager manager = new BudgetManager();

        System.out.println("=== Welcome to Budget Tracker ===");

        while (true) {
            System.out.println("\nMenu:");
            System.out.println("1. Add transaction");
            System.out.println("2. View all transactions");
            System.out.println("3. View transactions by category");
            System.out.println("4. View transactions by date");
            System.out.println("5. View summary (income, expense, balance)");
            System.out.println("6. Delete transaction");
            System.out.println("7. Exit");
            System.out.print("Choose an option: ");

            String choice = sc.nextLine();

            switch (choice) {
                case "1":
                    System.out.print("Enter date (yyyy-MM-dd): ");
                    String dateStr = sc.nextLine();
                    LocalDate date;
                    try {
                        date = LocalDate.parse(dateStr, formatter);
                    } catch (DateTimeParseException e) {
                        System.out.println("Invalid date format.");
                        break;
                    }

                    System.out.print("Enter category: ");
                    String category = sc.nextLine().trim();

                    System.out.print("Enter type (Income/Expense): ");
                    String type = sc.nextLine().trim();
                    if (!type.equalsIgnoreCase("Income") && !type.equalsIgnoreCase("Expense")) {
                        System.out.println("Type must be Income or Expense.");
                        break;
                    }

                    System.out.print("Enter amount: ");
                    double amount;
                    try {
                        amount = Double.parseDouble(sc.nextLine());
                    } catch (NumberFormatException e) {
                        System.out.println("Invalid amount.");
                        break;
                    }

                    System.out.print("Enter description: ");
                    String description = sc.nextLine().trim();

                    Transaction t = new Transaction(date, category, type, amount, description);
                    manager.addTransaction(t);
                    System.out.println("Transaction added.");
                    break;

                case "2":
                    List<Transaction> all = manager.getAllTransactions();
                    if (all.isEmpty()) {
                        System.out.println("No transactions found.");
                    } else {
                        System.out.println("All transactions:");
                        for (int i = 0; i < all.size(); i++) {
                            System.out.printf("%d: %s%n", i, all.get(i));
                        }
                    }
                    break;

                case "3":
                    System.out.print("Enter category to filter: ");
                    String filterCat = sc.nextLine().trim();
                    List<Transaction> filteredCat = manager.getTransactionsByCategory(filterCat);
                    if (filteredCat.isEmpty()) {
                        System.out.println("No transactions found for category: " + filterCat);
                    } else {
                        System.out.println("Transactions in category '" + filterCat + "':");
                        for (Transaction tr : filteredCat) {
                            System.out.println(tr);
                        }
                    }
                    break;

                case "4":
                    System.out.print("Enter date (yyyy-MM-dd) to filter: ");
                    String filterDateStr = sc.nextLine();
                    LocalDate filterDate;
                    try {
                        filterDate = LocalDate.parse(filterDateStr, formatter);
                    } catch (DateTimeParseException e) {
                        System.out.println("Invalid date format.");
                        break;
                    }
                    List<Transaction> filteredDate = manager.getTransactionsByDate(filterDate);
                    if (filteredDate.isEmpty()) {
                        System.out.println("No transactions found for date: " + filterDateStr);
                    } else {
                        System.out.println("Transactions on " + filterDateStr + ":");
                        for (Transaction tr : filteredDate) {
                            System.out.println(tr);
                        }
                    }
                    break;

                case "5":
                    System.out.printf("Total Income: %.2f%n", manager.getTotalIncome());
                    System.out.printf("Total Expense: %.2f%n", manager.getTotalExpense());
                    System.out.printf("Balance: %.2f%n", manager.getBalance());
                    break;

                case "6":
                    System.out.print("Enter transaction index to delete: ");
                    int delIndex;
                    try {
                        delIndex = Integer.parseInt(sc.nextLine());
                    } catch (NumberFormatException e) {
                        System.out.println("Invalid index.");
                        break;
                    }
                    if (manager.deleteTransaction(delIndex)) {
                        System.out.println("Transaction deleted.");
                    } else {
                        System.out.println("Transaction not found.");
                    }
                    break;

                case "7":
                    System.out.println("Exiting. Goodbye!");
                    sc.close();
                    return;

                default:
                    System.out.println("Invalid choice, try again.");
            }
        }
    }
}
