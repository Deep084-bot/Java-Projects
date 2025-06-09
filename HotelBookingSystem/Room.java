public class Room {
    private int roomNumber;
    private RoomType type;
    private double price;

    public Room(int roomNumber, RoomType type) {
        this.roomNumber = roomNumber;
        this.type = type;
        switch (type) {
            case SINGLE -> price = 1000;
            case DOUBLE -> price = 1800;
            case SUITE  -> price = 3000;
        }
    }

    public int getRoomNumber() { return roomNumber; }
    public RoomType getType() { return type; }
    public double getPrice() { return price; }

    @Override
    public String toString() {
        return "Room " + roomNumber + " (" + type + ") - Rs." + price + "/night";
    }
}
