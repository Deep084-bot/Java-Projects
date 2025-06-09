import java.util.List;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        ChatManager chatManager = new ChatManager();

        System.out.println("Welcome to Offline Chat Simulator with Group Chat!");
        System.out.print("Enter your username: ");
        String currentUser = sc.nextLine().trim();
        chatManager.addUser(currentUser);

        while (true) {
            System.out.println("\nMenu:");
            System.out.println("1. Send 1-on-1 Message");
            System.out.println("2. View 1-on-1 Chat History");
            System.out.println("3. Create Group");
            System.out.println("4. Send Group Message");
            System.out.println("5. View Group Chat History");
            System.out.println("6. Switch User");
            System.out.println("7. Exit");
            System.out.print("Choose an option: ");
            String choice = sc.nextLine();

            switch (choice) {
                case "1":
                    System.out.print("Enter receiver username: ");
                    String receiver = sc.nextLine().trim();
                    chatManager.addUser(receiver);
                    System.out.print("Enter message: ");
                    String content = sc.nextLine();
                    chatManager.sendMessage(currentUser, receiver, content);
                    System.out.println("Message sent!");
                    break;

                case "2":
                    System.out.print("Enter username to view chat with: ");
                    String chatWith = sc.nextLine().trim();
                    List<Message> chatHistory = chatManager.getChatHistory(currentUser, chatWith);
                    if (chatHistory.isEmpty()) {
                        System.out.println("No messages between you and " + chatWith);
                    } else {
                        System.out.println("Chat history between " + currentUser + " and " + chatWith + ":");
                        for (Message msg : chatHistory) {
                            System.out.println(msg);
                        }
                    }
                    break;

                case "3":
                    System.out.print("Enter new group name: ");
                    String groupName = sc.nextLine().trim();
                    System.out.print("Enter members (comma separated usernames): ");
                    String membersLine = sc.nextLine().trim();
                    String[] membersArr = membersLine.split(",");
                    for (int i = 0; i < membersArr.length; i++) {
                        membersArr[i] = membersArr[i].trim();
                    }
                    // Ensure creator is in the group
                    List<String> members = new java.util.ArrayList<>(java.util.Arrays.asList(membersArr));
                    if (!members.contains(currentUser)) {
                        members.add(currentUser);
                    }
                    if (chatManager.createGroup(groupName, members)) {
                        System.out.println("Group '" + groupName + "' created with members: " + String.join(", ", members));
                    } else {
                        System.out.println("Group with this name already exists.");
                    }
                    break;

                case "4":
                    System.out.print("Enter group name: ");
                    String groupToSend = sc.nextLine().trim();
                    System.out.print("Enter message: ");
                    String groupMsg = sc.nextLine();
                    chatManager.sendGroupMessage(currentUser, groupToSend, groupMsg);
                    System.out.println("Group message sent!");
                    break;

                case "5":
                    System.out.print("Enter group name: ");
                    String groupToView = sc.nextLine().trim();
                    List<Message> groupHistory = chatManager.getGroupChatHistory(groupToView);
                    if (groupHistory.isEmpty()) {
                        System.out.println("No messages in group " + groupToView);
                    } else {
                        System.out.println("Chat history for group " + groupToView + ":");
                        for (Message msg : groupHistory) {
                            System.out.println(msg);
                        }
                    }
                    break;

                case "6":
                    System.out.print("Enter new username: ");
                    currentUser = sc.nextLine().trim();
                    chatManager.addUser(currentUser);
                    System.out.println("Switched user to " + currentUser);
                    break;

                case "7":
                    System.out.println("Goodbye!");
                    sc.close();
                    System.exit(0);

                default:
                    System.out.println("Invalid choice, try again.");
            }
        }
    }
}
