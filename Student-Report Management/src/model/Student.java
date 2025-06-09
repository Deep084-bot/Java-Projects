package model;

public class Student {
    private int rollNumber;
    private String name;
    private int[] marks;  
    private double average;
    private char grade;

    public Student(int rollNumber, String name, int[] marks) {
        this.rollNumber = rollNumber;
        this.name = name;
        this.marks = marks;
        calculateAverageAndGrade();
    }

    private void calculateAverageAndGrade() {
        int sum = 0;
        for (int mark : marks) {
            sum += mark;
        }
        average = sum / (double) marks.length;

        if (average >= 90) grade = 'A';
        else if (average >= 75) grade = 'B';
        else if (average >= 60) grade = 'C';
        else if (average >= 50) grade = 'D';
        else grade = 'F';
    }

    public int getRollNumber() { return rollNumber; }
    public String getName() { return name; }
    public int[] getMarks() { return marks; }
    public double getAverage() { return average; }
    public char getGrade() { return grade; }

    public void setName(String name) {
        this.name = name;
    }

    public void setMarks(int[] marks) {
        this.marks = marks;
        calculateAverageAndGrade();
    }

    @Override
    public String toString() {
        StringBuilder marksStr = new StringBuilder();
        for (int mark : marks) {
            marksStr.append(mark).append(" ");
        }
        return "Roll No: " + rollNumber + ", Name: " + name + ", Marks: " + marksStr.toString().trim() +
               ", Average: " + String.format("%.2f", average) + ", Grade: " + grade;
    }

    public String toDataString() {
        StringBuilder sb = new StringBuilder();
        sb.append(rollNumber).append(",").append(name);
        for (int mark : marks) {
            sb.append(",").append(mark);
        }
        return sb.toString();
    }

    public static Student fromDataString(String data) {
        try {
            String[] parts = data.split(",");
            int roll = Integer.parseInt(parts[0]);
            String name = parts[1];
            int[] marks = new int[parts.length - 2];
            for (int i = 2; i < parts.length; i++) {
                marks[i - 2] = Integer.parseInt(parts[i]);
            }
            return new Student(roll, name, marks);
        } catch (Exception e) {
            System.out.println("Error parsing student data: " + e.getMessage());
            return null;
        }
    }
}
