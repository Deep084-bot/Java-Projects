import java.io.*;
import java.util.*;

public class Main {
    public static void main(String[] args) {
        // Setup rooms
        Room foyer = new Room("foyer", "You are in the foyer. A small chandelier hangs overhead.");
        Room library = new Room("library", "You are in a dusty library. Books line the walls.");
        Room kitchen = new Room("kitchen", "You are in a kitchen. It smells like fresh bread.");

        // Connect rooms
        foyer.setExit("north", library);
        library.setExit("south", foyer);
        foyer.setExit("east", kitchen);
        kitchen.setExit("west", foyer);

        // Add items
        library.addItem(new Item("book", "An old dusty book with strange symbols."));
        kitchen.addItem(new Item("knife", "A sharp kitchen knife."));

        // Add enemies
        Enemy goblin = new Enemy("Goblin", 50, 10);
        library.setEnemy(goblin);

        Enemy rat = new Enemy("Giant Rat", 30, 5);
        kitchen.setEnemy(rat);

        // Map of rooms by name for save/load
        Map<String, Room> roomMap = new HashMap<>();
        roomMap.put(foyer.getName(), foyer);
        roomMap.put(library.getName(), library);
        roomMap.put(kitchen.getName(), kitchen);

        Player player = new Player(foyer);
        Scanner scanner = new Scanner(System.in);

        System.out.println("Welcome to the Text Adventure! Type 'help' for commands.");

        while (true) {
            System.out.print("\n> ");
            String input = scanner.nextLine().trim().toLowerCase();

            if (input.equals("quit") || input.equals("exit")) {
                System.out.println("Thanks for playing!");
                break;

            } else if (input.equals("help")) {
                System.out.println("Commands:");
                System.out.println(" go <direction>  - Move north, south, east, or west");
                System.out.println(" look            - Look around the room");
                System.out.println(" take <item>     - Pick up an item");
                System.out.println(" inventory       - Show your items");
                System.out.println(" attack          - Attack an enemy");
                System.out.println(" health          - Show your health");
                System.out.println(" save            - Save the game");
                System.out.println(" load            - Load the game");
                System.out.println(" quit            - Exit game");

            } else if (input.equals("save")) {
                saveGame(player, "savegame.txt");
                System.out.println("Game saved.");

            } else if (input.equals("load")) {
                if (loadGame(player, roomMap, "savegame.txt")) {
                    System.out.println("Game loaded.");
                    System.out.println("Current location: " + player.getCurrentRoom().getName());
                    System.out.println(player.getCurrentRoom().getDescription());
                } else {
                    System.out.println("Failed to load game.");
                }

            } else if (input.startsWith("go ")) {
                String direction = input.substring(3).trim();
                Room nextRoom = player.getCurrentRoom().getExit(direction);
                if (nextRoom == null) {
                    System.out.println("You can't go that way.");
                } else {
                    player.setCurrentRoom(nextRoom);
                    System.out.println("You moved to the " + nextRoom.getName() + ".");
                    System.out.println(nextRoom.getDescription());
                    System.out.println(nextRoom.getExitsString());

                    Enemy enemy = nextRoom.getEnemy();
                    if (enemy != null && enemy.isAlive()) {
                        System.out.println("Watch out! There is a " + enemy.getName() + " here!");
                    }
                }

            } else if (input.equals("look")) {
                Room current = player.getCurrentRoom();
                System.out.println(current.getDescription());

                if (!current.getItems().isEmpty()) {
                    System.out.println("You see:");
                    for (Item item : current.getItems()) {
                        System.out.println(" - " + item.getName() + ": " + item.getDescription());
                    }
                }

                Enemy enemy = current.getEnemy();
                if (enemy != null && enemy.isAlive()) {
                    System.out.println("There is a " + enemy.getName() + " here. It looks hostile.");
                }

                System.out.println(current.getExitsString());

            } else if (input.startsWith("take ")) {
                String itemName = input.substring(5).trim();
                Item item = player.getCurrentRoom().takeItem(itemName);
                if (item == null) {
                    System.out.println("There is no " + itemName + " here.");
                } else {
                    player.addItem(item);
                    System.out.println("You picked up the " + itemName + ".");
                }

            } else if (input.equals("inventory")) {
                System.out.println(player.getInventoryString());

            } else if (input.equals("attack")) {
                Room current = player.getCurrentRoom();
                Enemy enemy = current.getEnemy();

                if (enemy == null || !enemy.isAlive()) {
                    System.out.println("There is nothing to attack here.");
                } else {
                    // Player attacks
                    enemy.takeDamage(player.attack());
                    System.out.println("You hit the " + enemy.getName() + " for " + player.attack() + " damage.");

                    if (!enemy.isAlive()) {
                        System.out.println("You defeated the " + enemy.getName() + "!");
                        current.removeEnemy();
                    } else {
                        // Enemy attacks back
                        int damage = enemy.attack();
                        player.takeDamage(damage);
                        System.out.println("The " + enemy.getName() + " hits you for " + damage + " damage.");
                        System.out.println("Your health: " + player.getHealth());

                        if (!player.isAlive()) {
                            System.out.println("You have been defeated! Game over.");
                            break;
                        }
                    }
                }

            } else if (input.equals("health")) {
                System.out.println("Your health: " + player.getHealth());

            } else {
                System.out.println("I don't understand that command.");
            }
        }

        scanner.close();
    }

    private static void saveGame(Player player, String filename) {
        try (PrintWriter out = new PrintWriter(new FileWriter(filename))) {
            out.println(player.getCurrentRoom().getName());
            out.println(player.getHealth());
            for (Item item : player.getInventory()) {
                out.println(item.getName() + "|" + item.getDescription());
            }
        } catch (IOException e) {
            System.out.println("Error saving game: " + e.getMessage());
        }
    }

    private static boolean loadGame(Player player, Map<String, Room> roomMap, String filename) {
        try (BufferedReader in = new BufferedReader(new FileReader(filename))) {
            String roomName = in.readLine();
            String healthLine = in.readLine();
            if (roomName == null || healthLine == null) return false;

            Room room = roomMap.get(roomName.toLowerCase());
            if (room == null) return false;
            player.setCurrentRoom(room);

            try {
                int health = Integer.parseInt(healthLine);
                // set player health using reflection or by adding setter (we'll add setter next)
                player.setHealth(health);
            } catch (NumberFormatException e) {
                return false;
            }

            player.clearInventory();

            String line;
            while ((line = in.readLine()) != null) {
                String[] parts = line.split("\\|");
                if (parts.length == 2) {
                    player.addItem(new Item(parts[0], parts[1]));
                }
            }
            return true;
        } catch (IOException e) {
            return false;
        }
    }
}
