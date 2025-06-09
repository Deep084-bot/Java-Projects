import model.Student;
import manager.ReportCardManager;

import java.util.Scanner;

public class Main {
    private static ReportCardManager manager = new ReportCardManager();
    private static Scanner sc = new Scanner(System.in);

    public static void main(String[] args) {
        int choice;

        do {
            showMenu();
            choice = readInt("Enter your choice: ");
            switch (choice) {
                case 1 -> addStudent();
                case 2 -> searchStudent();
                case 3 -> updateStudent();
                case 4 -> deleteStudent();
                case 5 -> manager.listAllStudents();
                case 6 -> {
                    manager.saveToFile();
                    System.out.println("Exiting...");
                }
                default -> System.out.println("Invalid choice! Try again.");
            }
        } while (choice != 6);
    }

    private static void showMenu() {
        System.out.println("\n--- Student Report Card System ---");
        System.out.println("1. Add Student");
        System.out.println("2. Search Student by Roll Number");
        System.out.println("3. Update Student");
        System.out.println("4. Delete Student");
        System.out.println("5. List All Students");
        System.out.println("6. Save and Exit");
    }

    private static void addStudent() {
        int roll = readInt("Enter Roll Number: ");
        if (manager.searchStudent(roll) != null) {
            System.out.println("Student with this roll number already exists.");
            return;
        }
        System.out.print("Enter Name: ");
        String name = sc.nextLine();

        int[] marks = new int[5];
        for (int i = 0; i < marks.length; i++) {
            marks[i] = readInt("Enter marks for subject " + (i + 1) + ": ");
        }

        Student s = new Student(roll, name, marks);
        manager.addStudent(s);
        System.out.println("Student added successfully.");
    }

    private static void searchStudent() {
        int roll = readInt("Enter Roll Number to search: ");
        Student s = manager.searchStudent(roll);
        if (s != null) {
            System.out.println(s);
        } else {
            System.out.println("Student not found.");
        }
    }

    private static void updateStudent() {
        int roll = readInt("Enter Roll Number to update: ");
        Student s = manager.searchStudent(roll);
        if (s == null) {
            System.out.println("Student not found.");
            return;
        }
        System.out.print("Enter new name (leave blank to keep current): ");
        String name = sc.nextLine();
        if (name.isEmpty()) {
            name = s.getName();
        }

        int[] marks = new int[5];
        System.out.println("Enter new marks (enter -1 to keep current):");
        int[] oldMarks = s.getMarks();
        for (int i = 0; i < marks.length; i++) {
            int mark = readInt("Subject " + (i + 1) + ": ");
            marks[i] = (mark == -1) ? oldMarks[i] : mark;
        }

        manager.updateStudent(roll, name, marks);
        System.out.println("Student updated successfully.");
    }

    private static void deleteStudent() {
        int roll = readInt("Enter Roll Number to delete: ");
        boolean removed = manager.deleteStudent(roll);
        if (removed) {
            System.out.println("Student deleted.");
        } else {
            System.out.println("Student not found.");
        }
    }

    private static int readInt(String prompt) {
        int number;
        while (true) {
            try {
                System.out.print(prompt);
                number = Integer.parseInt(sc.nextLine());
                break;
            } catch (NumberFormatException e) {
                System.out.println("Invalid number, try again.");
            }
        }
        return number;
    }
}
