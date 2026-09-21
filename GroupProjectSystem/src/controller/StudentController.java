package controller;
import model.Student;
import model.DataManager;
import java.util.ArrayList;
public class StudentController {
	private DataManager dataManager;
	
	public StudentController (DataManager dataManager){
		this.dataManager= dataManager;
	}
	// setter for new student
	public boolean addStudent(Student student){
		for (Student existingStudent : dataManager.getStudents()){
			if (existingStudent.getStudentId().equals(student.getStudentId())){
				return false;
			}
		}
		dataManager.addStudent(student);
		return true;
	}
	//edit student
	public boolean editStudent(String studentId,String name, String email){
		for (Student student: dataManager.getStudents()){
			if (student.getStudentId().equals(studentId)){
				student.setName(name);
				student.setEmail(email);
				return true;
			}
		}
		return false;
	}
	//delete student
	public boolean deleteStudent(String studentId){
		return dataManager.getStudents()
			.removeIf(student-> student.getStudentId().equals(studentId));
	}
	//find Student
	public Student findStudent(String studentId){
		for (Student student: dataManager.getStudents()){
			if (student.getStudentId().equals(studentId)){
				return student;
			}
		}
		return null;
	}
	//list students
	public ArrayList<Student> getAllStudents(){
		return dataManager.getStudents();
	}
}