package model;
import java.util.ArrayList;
import java.util.List;

public class Group {
    private String groupName;
    private String description;
    private List<Student> members;

    public Group(String groupName, String description) {
        this.groupName = groupName;
        this.description = description;
        this.members = new ArrayList<>();
    }

    public String getGroupName() { return groupName; }
    public String getDescription() { return description; }
    public List<Student> getMembers() { return members; }

    public void setGroupName(String groupName) { this.groupName = groupName; }
    public void setDescription(String description) { this.description = description; }

    public void addMember(Student student) {
        if (!members.contains(student)) {
            members.add(student);
        }
    }

    public void removeMember(Student student) {
        members.remove(student);
    }

    @Override
    public String toString() {
        return groupName;
    }
}
