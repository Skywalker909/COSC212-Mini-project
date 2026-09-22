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
	// 