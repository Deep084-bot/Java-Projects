import java.time.LocalDate;
import java.util.*;

public class Hotel {
    private List<Room> rooms;
    private List<Booking> bookings;

    public Hotel() {
        rooms = new ArrayList<>();
        bookings = new ArrayList<>();

        // Initialize rooms here or via method
        rooms.add(new Room(101, RoomType.SINGLE));
        rooms.add(new Room(102, RoomType.DOUBLE));
        rooms.add(new Room(103, RoomType.SUITE));
        rooms.add(new Room(104, RoomType.DOUBLE));
        rooms.add(new Room(105, RoomType.SINGLE));
    }

    // Room accessors
    public List<Room> getRooms() {
        return rooms;
    }

    public Room getRoomByNumber(int roomNumber) {
        for (Room room : rooms) {
            if (room.getRoomNumber() == roomNumber) {
                return room;
            }
        }
        return null;
    }

    // Booking accessors
    public List<Booking> getBookings() {
        return bookings;
    }

    // Add booking
    public void addBooking(Booking booking) {
        bookings.add(booking);
    }

    // Remove booking
    public void removeBooking(Booking booking) {
        bookings.remove(booking);
    }

    // Check if room available
    public boolean isAvailable(int roomNumber, LocalDate start, LocalDate end) {
        for (Booking booking : bookings) {
            if (booking.getRoomNumber() == roomNumber && booking.overlaps(start, end)) {
                return false;
            }
        }
        return true;
    }
}
