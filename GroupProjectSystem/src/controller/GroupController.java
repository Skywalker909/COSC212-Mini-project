package controller;

import model.Group;
import model.Student;
import model.DataManager;

import java.util.ArrayList;

public class GroupController {

    private DataManager dataManager;

    // Constructor
    public GroupController(DataManager dataManager) {
        this.dataManager = dataManager;
    }

    // Create group
    public boolean addGroup(Group group) {

        // Check whether a group with the same name already exists
        for (Group existingGroup : dataManager.getGroups()) {

            if (existingGroup.getGroupName()
                    .equals(group.getGroupName())) {

                return false;
            }
        }

        // Add the group if the name is unique
        dataManager.addGroup(group);

        return true;
    }

    // Edit group description
    public boolean editGroup(String groupName, String description) {

        for (Group group : dataManager.getGroups()) {

            if (group.getGroupName().equals(groupName)) {

                group.setDescription(description);

                return true;
            }
        }

        return false;
    }

    // Delete group
    public boolean deleteGroup(String groupName) {

        return dataManager.getGroups()
                .removeIf(group ->
                        group.getGroupName().equals(groupName));
    }

    // Add student to group
    public boolean addStudentToGroup(
            String groupName, Student student) {

        // Find the group
        for (Group group : dataManager.getGroups()) {

            if (group.getGroupName().equals(groupName)) {

                // Check whether the student already belongs
                // to any group
                for (Group existingGroup : dataManager.getGroups()) {

                    if (existingGroup.getMembers().contains(student)) {
                        return false;
                    }
                }

                // Add the student to the selected group
                group.addMember(student);

                return true;
            }
        }

        // Group was not found
        return false;
    }

    // Remove student from group
    public boolean removeStudentFromGroup(
            String groupName, Student student) {

        for (Group group : dataManager.getGroups()) {

            if (group.getGroupName().equals(groupName)) {

                if (group.getMembers().contains(student)) {

                    group.removeMember(student);

                    return true;
                }

                return false;
            }
        }

        return false;
    }

    // Get group members
    public ArrayList<Student> getGroupMembers(String groupName) {

        for (Group group : dataManager.getGroups()) {

            if (group.getGroupName().equals(groupName)) {

                return new ArrayList<>(group.getMembers());
            }
        }

        return new ArrayList<>();
    }

    // Get all groups
    public ArrayList<Group> getAllGroups() {

        return dataManager.getGroups();
    }
}