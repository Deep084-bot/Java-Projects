import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

public class Transaction {
    private LocalDate date;
    private String category;
    private String type;      // "Income" or "Expense"
    private double amount;
    private String description;

    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    public Transaction(LocalDate date, String category, String type, double amount, String description) {
        this.date = date;
        this.category = category;
        this.type = type;
        this.amount = amount;
        this.description = description;
    }

    public Transaction() {
        // no-arg constructor
    }

    // Getters and setters
    public LocalDate getDate() { return date; }
    public void setDate(LocalDate date) { this.date = date; }
    public String getCategory() { return category; }
    public void setCategory(String category) { this.category = category; }
    public String getType() { return type; }
    public void setType(String type) { this.type = type; }
    public double getAmount() { return amount; }
    public void setAmount(double amount) { this.amount = amount; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    // Parse a CSV line into a Transaction object
    public static Transaction fromCSV(String line) {
        String[] parts = line.split(",", -1); // -1 to include empty trailing fields
        if (parts.length < 5) {
            return null; // invalid line
        }

        try {
            LocalDate date = LocalDate.parse(parts[0], formatter);
            String category = parts[1];
            String type = parts[2];
            double amount = Double.parseDouble(parts[3]);
            String description = parts[4];
            return new Transaction(date, category, type, amount, description);
        } catch (DateTimeParseException | NumberFormatException e) {
            return null; // invalid data format
        }
    }

    // Convert this Transaction to a CSV line
    public String toCSV() {
        // Escape commas in description or category if needed
        String safeCategory = category.replace(",", ""); 
        String safeDescription = description.replace(",", "");
        return String.format("%s,%s,%s,%.2f,%s",
                date.format(formatter), safeCategory, type, amount, safeDescription);
    }

    @Override
    public String toString() {
        return String.format("%s | %s | %s | %.2f | %s",
                date.format(formatter), category, type, amount, description);
    }
}
