import java.util.*;

public class Player {
    private Room currentRoom;
    private List<Item> inventory;

    private int health = 100;
    private int attackPower = 20;

    public Player(Room startRoom) {
        this.currentRoom = startRoom;
        this.inventory = new ArrayList<>();
    }

    public Room getCurrentRoom() {
        return currentRoom;
    }

    public void setCurrentRoom(Room room) {
        currentRoom = room;
    }

    public void addItem(Item item) {
        inventory.add(item);
    }

    public String getInventoryString() {
        if (inventory.isEmpty())
            return "You have no items.";
        StringBuilder sb = new StringBuilder("Inventory:");
        for (Item i : inventory) {
            sb.append("\n - ").append(i.getName());
        }
        return sb.toString();
    }

    public List<Item> getInventory() {
        return inventory;
    }

    public void clearInventory() {
        inventory.clear();
    }

    public void setHealth(int health) {
        this.health = health;
    }

    // Health & combat
    public int getHealth() {
        return health;
    }

    public boolean isAlive() {
        return health > 0;
    }

    public void takeDamage(int damage) {
        health -= damage;
        if (health < 0)
            health = 0;
    }

    public int attack() {
        return attackPower;
    }
}
