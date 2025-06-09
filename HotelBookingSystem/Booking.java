import java.time.LocalDate;

public class Booking {
    private String customerName;
    private int roomNumber;
    private LocalDate checkIn;
    private LocalDate checkOut;

    public Booking(String customerName, int roomNumber, LocalDate checkIn, LocalDate checkOut) {
        this.customerName = customerName;
        this.roomNumber = roomNumber;
        this.checkIn = checkIn;
        this.checkOut = checkOut;
    }

    public String getCustomerName() { return customerName; }
    public int getRoomNumber() { return roomNumber; }
    public LocalDate getCheckIn() { return checkIn; }
    public LocalDate getCheckOut() { return checkOut; }

    @Override
    public String toString() {
        return "Room " + roomNumber + " booked by " + customerName +
               " from " + checkIn + " to " + checkOut;
    }

    public boolean overlaps(LocalDate start, LocalDate end) {
        return !(checkOut.isBefore(start) || checkIn.isAfter(end));
    }

    public long getDays() {
        return checkOut.toEpochDay() - checkIn.toEpochDay();
    }
}
