import model.DataManager;
import model.Group;
import model.Responsibility;
import model.Student;
import controller.ProgressController;
import java.time.LocalDate;
import model.ProgressReport;

public class TestProgress{
	public static  void main(String[] args){
		//creating the data manager
		DataManager dataManager = new DataManager();
		// creating progress controller
		ProgressController progressController = new ProgressController(dataManager);
		//creating new students
		Student student1 = new Student( "ST001", "Moshood" , "moshood@gmail.com");
		Student student2 = new Student ("ST002", "Omolola" , "omolola@gmail.com");
		Student student3 = new Student ("ST003", "Umar" , "umar@gmail.com");
		Student student4 =new Student ("ST004", "Olamilekan" , "olamilekan@gmail.com");
		
		//creating group
		Group group = new Group ("Group A", "Software Devs");
		
		// adding students to the group
		
		group.addMember(student1);
		group.addMember(student2);
		
		//creating responsibilities for the group
		Responsibility task1 = new Responsibility ("Design database", "Design",
		LocalDate.of(2026,10,1), student1);
		Responsibility task2 = new Responsibility ("Write code" , "write", LocalDate.of(2026,10,1),student1);
		Responsibility task3 = new Responsibility ("Edit code" , "edit", LocalDate.of(2026,10,1),student2);
		Responsibility task4 = new Responsibility ("Comment code" , "jot", LocalDate.of(2026,10,1),student2);
		// adding responsibilities to DataManager
		dataManager.addResponsibility(task1);
		dataManager.addResponsibility(task2);
		dataManager.addResponsibility(task3);
		dataManager.addResponsibility(task4);
		
		
		//mark completed
		
		task1.setStatus(Responsibility.Status.COMPLETED);
		task4.setStatus(Responsibility.Status.COMPLETED);
		
		
		//Member progress
		System.out.println("===== MEMBER PROGRESS =====");
		ProgressReport memberReport = progressController.getMemberProgressReport(student1);
		System.out.println("Student: " + student1.getName());

		System.out.println("Total: " + memberReport.getTotalResponsibilities());

		System.out.println("Completed: " + memberReport.getCompletedResponsibilities());

		System.out.println("Pending: "+ memberReport.getPendingResponsibilities());

		System.out.println("Percentage: "+ memberReport.getCompletionPercentage()+ "%");
		
		//group progress
		System.out.println();
		System.out.println("===== GROUP PROGRESS =====");
		ProgressReport groupReport = progressController.getGroupProgressReport(group);
		System.out.println("Group: " + group.getGroupName());
		System.out.println("Total: "+ groupReport.getTotalResponsibilities());
		System.out.println("Completed: "+ groupReport.getCompletedResponsibilities());
		System.out.println("Pending: "+ groupReport.getPendingResponsibilities());
		System.out.println("Percentage: "+ groupReport.getCompletionPercentage()+ "%");
	}
}