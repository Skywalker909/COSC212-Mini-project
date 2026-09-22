package controller;
import model.Group;
import model.Student;
import model.DataManager;
import java.util.ArrayList;

public class GroupController {
	private DataManager dataManager;
	
	public GroupController (DataManager dataManager) {
		this.dataManager = dataManager;
	}
	// create new group
	public boolean addGroup(Group group){
		for (Group existingGroup : dataManager.getGroups()){
			if (existingGroup.getGroupName().equals(group.getGroupName())){
				return false;
			}
		}
		dataManager.addGroup(group);
		return true;
	}
	// edit groups
	public boolean editGroup(String groupName,String description){
		for (Group group: dataManager.getGroupName()){
			if (group.getGroupName().equals(groupName)){
				group.setGroupName(groupName);
				group.setDescription(description);
				return true;
			}
		}
		return false;
	}
	// delete groups
public boolean deleteGroup(String groupName){
		return dataManager.getGroupName()
			.removeIf(group-> group.getGroupName().equals(groupName));
	}
	//add student to group
public boolean addStudentToGroup(String studentId, String name, String email){
	//check if the student is already added to a group
	for (Group group: dataManager.getGroups()){
		if (group.getMembers().contains(student));
		return false;
	}
}
// find Group
public group findGroup(String groupName){
		for (Group group: dataManager.getGroupName()){
			if (group.getGroupName().equals(groupName)){
				return groupname;
			}
		}
		return null;
	}
// remove student from group
public boolean removeStudentfromGroup(String studentId, String name, String email){
	for (Group group: dataManager.getGroups()){
		if (group.getGroupName().equals(groupName));
			if(group.getMembers().contains(student)){
				return true;
			}
		return false;
	}
}
return false;
}
//get group members
public Arraylist <Student> getGroupMembers(String groupName){
	for( Group group: dataManager.getGroups()){
		if(group.getGroupName()equals(groupName)){
			return group.getMembers();
		}
	}
	return Arraylist<>();
}
//get all groups
public ArrayList<Group> getAllGroups(){
	return dataManager.getGroups();
}
}
