import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Message {
    private String sender;
    private String receiver;  // For 1-on-1, receiver username
    private String groupName; // For group chat, group name
    private String content;
    private LocalDateTime timestamp;
    private boolean isGroupMessage;

    // Constructor for 1-on-1 message
    public Message(String sender, String receiver, String content, LocalDateTime timestamp) {
        this.sender = sender;
        this.receiver = receiver;
        this.content = content;
        this.timestamp = timestamp;
        this.isGroupMessage = false;
        this.groupName = null;
    }

    // Constructor for group message
    public Message(String sender, String groupName, String content, LocalDateTime timestamp, boolean isGroupMessage) {
        this.sender = sender;
        this.groupName = groupName;
        this.content = content;
        this.timestamp = timestamp;
        this.isGroupMessage = isGroupMessage;
        this.receiver = null;
    }

    public boolean isGroupMessage() {
        return isGroupMessage;
    }

    public String getSender() { return sender; }
    public String getReceiver() { return receiver; }
    public String getGroupName() { return groupName; }
    public String getContent() { return content; }
    public LocalDateTime getTimestamp() { return timestamp; }

    @Override
    public String toString() {
        DateTimeFormatter fmt = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
        if (isGroupMessage) {
            return "[" + timestamp.format(fmt) + "] " + sender + " in " + groupName + ": " + content;
        } else {
            return "[" + timestamp.format(fmt) + "] " + sender + " to " + receiver + ": " + content;
        }
    }
}
