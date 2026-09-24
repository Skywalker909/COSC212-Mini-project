package controller;
import model.DataManager;
import model.Student;
import model.Group;
import model.Responsibility;
import model.ProgressReport;

public class ProgressController {
	private DataManager dataManager;
	
	public ProgressController (DataManager dataManager){
		this.dataManager = dataManager;
	}
	// member progress
		// get total responsibilities assigned to a member
	public int getMemberTotalResponsibilities(Student student){
		int total = 0;
		for ( Responsibility responsibility : dataManager.getResponsibilities()){
			if ( responsibility.getAssignedMember() != null && responsibility.getAssignedMember().getStudentId().equals(student.getStudentId())){
				total++;
			}
		}
		return total;
	}
		// get completed responsibilities assigned to a member
	public int getMemberCompletedResponsibilities(Student student){
		int completed = 0;
		for ( Responsibility responsibility: dataManager.getResponsibilities()){
			if ( responsibility.getAssignedMember() != null && responsibility.getAssignedMember().getStudentId().equals(student.getStudentId()) && responsibility.isCompleted()){
				
				completed++;
			}
		}
		return completed;
	}
		//  get pending responsibilities assigned to a member
	public int getMemberPendingResponsibilities( Student student){
		int total = getMemberTotalResponsibilities(student);
		int completed = getMemberCompletedResponsibilities(student);
		return total - completed;
	}
		//get member completion percentage
	public double getMemberCompletionPercentage(Student student){
		int completed = getMemberCompletedResponsibilities(student);
		int total = getMemberTotalResponsibilities(student);
		if (total ==0){
			return 0.0;
		}
		
		return ((double) completed/total) * 100;
	}
	// group progress
		// group total responsibilities
	public int getGroupTotalResponsibilities(Group group){
		int total = 0;
		for (Responsibility responsibility: dataManager.getResponsibilities()){
			Student assignedMember = responsibility.getAssignedMember();
			if ( assignedMember == null){
				continue;
			}
			for (Student member : group.getMembers()){
				if (member.getStudentId().equals(assignedMember.getStudentId())){
					total ++;
					break;
				}
			}
		}
		return total;
	}
		// group completed responsibilities
	public int getGroupCompletedResponsibilities(Group group){
		int completed = 0;
		for ( Responsibility responsibility : dataManager.getResponsibilities()){
			Student assignedMember = responsibility.getAssignedMember();
			if (assignedMember == null){
				continue;
			}
		
			for (Student member : group.getMembers()){
				if (member.getStudentId().equals(assignedMember.getStudentId()) && responsibility.isCompleted()){
					completed++;
					break;
				}
			}
		}
		return completed;
	}
		//get group pending responsibilities
	public int getGroupPendingResponsibilities(Group group){
		int total = getGroupTotalResponsibilities(group);
		int completed = getGroupCompletedResponsibilities(group);
		
		return total - completed;
	}
		//get group completion percentage
	public double getGroupCompletionPercentage(Group group){
		int total = getGroupTotalResponsibilities(group);
		int completed = getGroupCompletedResponsibilities(group);
		if (total ==0){
			return 0.0;
		}
		return((double) completed/total) * 100;
	}
    // PROGRESS REPORTS

    public ProgressReport getMemberProgressReport(Student student) {

        int total = getMemberTotalResponsibilities(student);

        int completed =
                getMemberCompletedResponsibilities(student);

        int pending =
                getMemberPendingResponsibilities(student);

        double percentage =
                getMemberCompletionPercentage(student);

        return new ProgressReport(
                total,
                completed,
                pending,
                percentage
        );
    }

    public ProgressReport getGroupProgressReport(Group group) {

        int total = getGroupTotalResponsibilities(group);

        int completed =
                getGroupCompletedResponsibilities(group);

        int pending =
                getGroupPendingResponsibilities(group);

        double percentage =
                getGroupCompletionPercentage(group);

        return new ProgressReport(
                total,
                completed,
                pending,
                percentage
        );
    }
}