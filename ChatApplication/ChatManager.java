import java.io.*;
import java.time.LocalDateTime;
import java.util.*;

public class ChatManager {
    private List<User> users;
    private List<Message> messages;        // 1-on-1 messages
    private Map<String, GroupChat> groups; // groupName -> GroupChat
    private final String MESSAGES_FILE = "storage/messages.txt";
    private final String GROUPS_FILE = "storage/groups.txt";

    public ChatManager() {
        users = new ArrayList<>();
        messages = new ArrayList<>();
        groups = new HashMap<>();
        loadMessages();
        loadGroups();
    }

    // User methods same as before
    public void addUser(String username) {
        if (getUser(username) == null) {
            users.add(new User(username));
        }
    }

    public User getUser(String username) {
        for (User user : users) {
            if (user.getUsername().equalsIgnoreCase(username)) return user;
        }
        return null;
    }

    // 1-on-1 messages
    public void sendMessage(String sender, String receiver, String content) {
        Message msg = new Message(sender, receiver, content, LocalDateTime.now());
        messages.add(msg);
        saveMessages();
    }

    public List<Message> getChatHistory(String user1, String user2) {
        List<Message> chat = new ArrayList<>();
        for (Message msg : messages) {
            boolean isBetween = (msg.getSender().equalsIgnoreCase(user1) && msg.getReceiver().equalsIgnoreCase(user2))
                    || (msg.getSender().equalsIgnoreCase(user2) && msg.getReceiver().equalsIgnoreCase(user1));
            if (isBetween) {
                chat.add(msg);
            }
        }
        return chat;
    }

    // Group chat methods
    public boolean createGroup(String groupName, List<String> members) {
        if (groups.containsKey(groupName)) return false;
        GroupChat group = new GroupChat(groupName);
        for (String member : members) {
            addUser(member);
            group.addMember(member);
        }
        groups.put(groupName, group);
        saveGroups();
        return true;
    }

    public boolean addUserToGroup(String groupName, String username) {
        GroupChat group = groups.get(groupName);
        if (group == null) return false;
        addUser(username);
        group.addMember(username);
        saveGroups();
        return true;
    }

    public void sendGroupMessage(String sender, String groupName, String content) {
        GroupChat group = groups.get(groupName);
        if (group == null) {
            System.out.println("Group does not exist.");
            return;
        }
        if (!group.isMember(sender)) {
            System.out.println("You are not a member of this group.");
            return;
        }
        Message msg = new Message(sender, groupName, content, LocalDateTime.now(), true);
        group.addMessage(msg);
        saveGroups();
    }

    public List<Message> getGroupChatHistory(String groupName) {
        GroupChat group = groups.get(groupName);
        if (group == null) return Collections.emptyList();
        return group.getMessages();
    }

    // Save/load messages (1-on-1)
    private void saveMessages() {
        try {
            File dir = new File("storage");
            if (!dir.exists()) dir.mkdirs();

            try (PrintWriter writer = new PrintWriter(new FileWriter(MESSAGES_FILE))) {
                for (Message msg : messages) {
                    writer.println(msg.getSender() + "|" + msg.getReceiver() + "|" + msg.getTimestamp() + "|" + msg.getContent());
                }
            }
        } catch (IOException e) {
            System.out.println("Error saving messages: " + e.getMessage());
        }
    }

    private void loadMessages() {
        File file = new File(MESSAGES_FILE);
        if (!file.exists()) return;

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split("\\|", 4);
                if (parts.length == 4) {
                    String sender = parts[0];
                    String receiver = parts[1];
                    LocalDateTime timestamp = LocalDateTime.parse(parts[2]);
                    String content = parts[3];
                    messages.add(new Message(sender, receiver, content, timestamp));
                    addUser(sender);
                    addUser(receiver);
                }
            }
        } catch (IOException e) {
            System.out.println("Error loading messages: " + e.getMessage());
        }
    }

    // Save/load groups + their messages
    private void saveGroups() {
        try {
            File dir = new File("storage");
            if (!dir.exists()) dir.mkdirs();

            try (PrintWriter writer = new PrintWriter(new FileWriter(GROUPS_FILE))) {
                for (GroupChat group : groups.values()) {
                    // Save group line: groupName|member1,member2,member3
                    String membersLine = String.join(",", group.getMembers());
                    writer.println(group.getGroupName() + "|" + membersLine);
                    // Save each group message as: G|groupName|sender|timestamp|content
                    for (Message msg : group.getMessages()) {
                        writer.println("G|" + group.getGroupName() + "|" + msg.getSender() + "|" + msg.getTimestamp() + "|" + msg.getContent());
                    }
                }
            }
        } catch (IOException e) {
            System.out.println("Error saving groups: " + e.getMessage());
        }
    }

    private void loadGroups() {
        File file = new File(GROUPS_FILE);
        if (!file.exists()) return;

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            GroupChat currentGroup = null;
            while ((line = reader.readLine()) != null) {
                if (!line.startsWith("G|")) {
                    // Group header line: groupName|member1,member2,...
                    String[] parts = line.split("\\|", 2);
                    if (parts.length == 2) {
                        String groupName = parts[0];
                        String[] members = parts[1].split(",");
                        currentGroup = new GroupChat(groupName);
                        for (String member : members) {
                            currentGroup.addMember(member);
                            addUser(member);
                        }
                        groups.put(groupName, currentGroup);
                    }
                } else {
                    // Group message line: G|groupName|sender|timestamp|content
                    String[] parts = line.split("\\|", 5);
                    if (parts.length == 5) {
                        String groupName = parts[1];
                        String sender = parts[2];
                        LocalDateTime timestamp = LocalDateTime.parse(parts[3]);
                        String content = parts[4];
                        GroupChat group = groups.get(groupName);
                        if (group != null) {
                            group.addMessage(new Message(sender, groupName, content, timestamp, true));
                        }
                    }
                }
            }
        } catch (IOException e) {
            System.out.println("Error loading groups: " + e.getMessage());
        }
    }
}
