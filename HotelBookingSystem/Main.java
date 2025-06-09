import java.time.LocalDate;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        BookingManager manager = new BookingManager();

        while (true) {
            System.out.println("\n==== Hotel Booking System ====");
            System.out.println("1. View available rooms");
            System.out.println("2. Book a room");
            System.out.println("3. Cancel booking");
            System.out.println("4. Show all bookings");
            System.out.println("5. Exit");
            System.out.print("Choice: ");
            int choice = sc.nextInt();
            sc.nextLine();

            switch (choice) {
                case 1 -> {
                    System.out.print("Enter check-in (yyyy-MM-dd): ");
                    LocalDate in = DateUtil.parse(sc.nextLine());
                    System.out.print("Enter check-out (yyyy-MM-dd): ");
                    LocalDate out = DateUtil.parse(sc.nextLine());
                    manager.showAvailableRooms(in, out);
                }
                case 2 -> {
                    System.out.print("Enter your name: ");
                    String name = sc.nextLine();
                    System.out.print("Enter room number: ");
                    int roomNum = sc.nextInt(); sc.nextLine();
                    System.out.print("Enter check-in date (yyyy-MM-dd): ");
                    LocalDate in = DateUtil.parse(sc.nextLine());
                    System.out.print("Enter check-out date (yyyy-MM-dd): ");
                    LocalDate out = DateUtil.parse(sc.nextLine());
                    manager.bookRoom(name, roomNum, in, out);
                }
                case 3 -> {
                    System.out.print("Enter your name: ");
                    String name = sc.nextLine();
                    System.out.print("Enter room number to cancel: ");
                    int roomNum = sc.nextInt();
                    manager.cancelBooking(roomNum, name);
                }
                case 4 -> manager.showBookings();
                case 5 -> {
                    System.out.println("Goodbye!");
                    return;
                }
                default -> System.out.println("Invalid choice.");
            }
        }
    }
}
