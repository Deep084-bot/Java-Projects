package manager;

import model.Student;

import java.io.*;
import java.util.ArrayList;

public class ReportCardManager {
    private ArrayList<Student> students;
    private final String filePath = "data/students.txt";

    public ReportCardManager() {
        students = new ArrayList<>();
        loadFromFile();
    }

    public void addStudent(Student s) {
        students.add(s);
    }

    public Student searchStudent(int rollNumber) {
        for (Student s : students) {
            if (s.getRollNumber() == rollNumber) return s;
        }
        return null;
    }

    public boolean deleteStudent(int rollNumber) {
        Student s = searchStudent(rollNumber);
        if (s != null) {
            students.remove(s);
            return true;
        }
        return false;
    }

    public boolean updateStudent(int rollNumber, String newName, int[] newMarks) {
        Student s = searchStudent(rollNumber);
        if (s != null) {
            s.setName(newName);
            s.setMarks(newMarks);
            return true;
        }
        return false;
    }

    public void saveToFile() {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath))) {
            for (Student s : students) {
                writer.write(s.toDataString());
                writer.newLine();
            }
            System.out.println("Data saved successfully.");
        } catch (IOException e) {
            System.out.println("Error saving data: " + e.getMessage());
        }
    }

    public void loadFromFile() {
        File file = new File(filePath);
        if (!file.exists()) {
            return;
        }
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = reader.readLine()) != null) {
                Student s = Student.fromDataString(line);
                if (s != null) students.add(s);
            }
            System.out.println("Data loaded successfully.");
        } catch (IOException e) {
            System.out.println("Error loading data: " + e.getMessage());
        }
    }

    public void listAllStudents() {
        if (students.isEmpty()) {
            System.out.println("No student records found.");
            return;
        }
        for (Student s : students) {
            System.out.println(s);
        }
    }
}
