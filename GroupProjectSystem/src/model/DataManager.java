package model;
import java.util.ArrayList;

public class DataManager{
	private ArrayList<Student> students;
	private ArrayList<Group> groups;
	private ArrayList<Responsibility> responsibilities;
	
	public DataManager(){
		students = new ArrayList<>();
		groups = new ArrayList<>();
		responsibilities = new ArrayList<>();
	}
	// Add a Student
	public void addStudent (Student student){
		students.add(student);
	}
	//Get all students
	public ArrayList<Student> getStudents(){
		return students;
	}
	// Add a Group
	public void addGroup (Group group){
		groups.add(group);
	}
	//Get all Groups
	public ArrayList<Group> getGroups(){
		return groups;
	}
	// Add a Responsibility
	public void addResponsibility (Responsibility responsibility){
		responsibilities.add(responsibility);
	}
	//Get all Responsibilities
	public ArrayList<Responsibility> getResponsibilities(){
		return responsibilities;
	}
}