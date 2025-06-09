import java.util.*;

public class Room {
    private String name;
    private String description;
    private Map<String, Room> exits;
    private List<Item> items;
    private Enemy enemy;

    public Room(String name, String description) {
        this.name = name;
        this.description = description;
        this.exits = new HashMap<>();
        this.items = new ArrayList<>();
    }

    public void setExit(String direction, Room room) {
        exits.put(direction.toLowerCase(), room);
    }

    public Room getExit(String direction) {
        return exits.get(direction.toLowerCase());
    }

    public String getDescription() {
        return description;
    }

    public String getName() {
        return name;
    }

    public void addItem(Item item) {
        items.add(item);
    }

    public Item takeItem(String itemName) {
        for (Iterator<Item> it = items.iterator(); it.hasNext();) {
            Item item = it.next();
            if (item.getName().equalsIgnoreCase(itemName)) {
                it.remove();
                return item;
            }
        }
        return null;
    }

    public List<Item> getItems() {
        return Collections.unmodifiableList(items);
    }

    public String getExitsString() {
        if (exits.isEmpty()) return "No exits.";
        return "Exits: " + String.join(", ", exits.keySet());
    }

    // Enemy related methods
    public void setEnemy(Enemy enemy) {
        this.enemy = enemy;
    }

    public Enemy getEnemy() {
        return enemy;
    }

    public void removeEnemy() {
        enemy = null;
    }
}
