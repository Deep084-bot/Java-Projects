import java.io.*;
import java.time.LocalDate;
import java.util.*;

public class BookingManager {
    private Hotel hotel;
    private final String BOOKINGS_FILE = "bookings.txt";

    public BookingManager() {
        hotel = new Hotel();
        loadBookings();
    }

    public void showAvailableRooms(LocalDate start, LocalDate end) {
        for (Room room : hotel.getRooms()) {
            if (hotel.isAvailable(room.getRoomNumber(), start, end)) {
                System.out.println(room);
            }
        }
    }

    public void bookRoom(String customerName, int roomNumber, LocalDate checkIn, LocalDate checkOut) {
        if (!hotel.isAvailable(roomNumber, checkIn, checkOut)) {
            System.out.println("Room is not available for these dates.");
            return;
        }
        Booking booking = new Booking(customerName, roomNumber, checkIn, checkOut);
        hotel.addBooking(booking);
        saveBookings();
        System.out.println("Room booked successfully!");
    }

    public void showBookings() {
        List<Booking> bookings = hotel.getBookings();
        if (bookings.isEmpty()) {
            System.out.println("No bookings yet.");
            return;
        }
        for (Booking booking : bookings) {
            System.out.println(booking);
        }
    }

    public void cancelBooking(int roomNumber, String customerName) {
        List<Booking> bookings = hotel.getBookings();
        Booking toRemove = null;
        for (Booking booking : bookings) {
            if (booking.getRoomNumber() == roomNumber && booking.getCustomerName().equalsIgnoreCase(customerName)) {
                toRemove = booking;
                break;
            }
        }
        if (toRemove != null) {
            hotel.removeBooking(toRemove);
            saveBookings();
            System.out.println("Booking cancelled.");
        } else {
            System.out.println("No such booking found.");
        }
    }

    private void saveBookings() {
        try (PrintWriter writer = new PrintWriter(new FileWriter(BOOKINGS_FILE))) {
            for (Booking booking : hotel.getBookings()) {
                writer.println(booking.getCustomerName() + "," +
                               booking.getRoomNumber() + "," +
                               booking.getCheckIn() + "," +
                               booking.getCheckOut());
            }
        } catch (IOException e) {
            System.out.println("Error saving bookings: " + e.getMessage());
        }
    }

    private void loadBookings() {
        File file = new File(BOOKINGS_FILE);
        if (!file.exists()) return;

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length == 4) {
                    String customerName = parts[0];
                    int roomNumber = Integer.parseInt(parts[1]);
                    LocalDate checkIn = LocalDate.parse(parts[2]);
                    LocalDate checkOut = LocalDate.parse(parts[3]);
                    Booking booking = new Booking(customerName, roomNumber, checkIn, checkOut);
                    hotel.addBooking(booking);
                }
            }
        } catch (IOException e) {
            System.out.println("Error loading bookings: " + e.getMessage());
        }
    }
}
