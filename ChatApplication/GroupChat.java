import java.util.*;

public class GroupChat {
    private String groupName;
    private Set<String> members;
    private List<Message> messages;

    public GroupChat(String groupName) {
        this.groupName = groupName;
        this.members = new HashSet<>();
        this.messages = new ArrayList<>();
    }

    public String getGroupName() {
        return groupName;
    }

    public Set<String> getMembers() {
        return members;
    }

    public List<Message> getMessages() {
        return messages;
    }

    public void addMember(String username) {
        members.add(username);
    }

    public void addMessage(Message msg) {
        messages.add(msg);
    }

    public boolean isMember(String username) {
        return members.contains(username);
    }
}
