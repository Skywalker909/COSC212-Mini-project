package model;
import model.Student;
import java.time.LocalDate;
public class Responsibility {

    // The four statuses specified in the assignment
    public enum Status {
        NOT_STARTED,
        IN_PROGRESS,
        COMPLETED,
        BLOCKED
    }

    private String title;
    private String description;
    private LocalDate deadline;
    private Status status;
    private Student assignedMember;

    public Responsibility(String title, String description, LocalDate deadline, Student assignedMember) {
        this.title = title;
        this.description = description;
        this.deadline = deadline;
        this.assignedMember = assignedMember;
        this.status = Status.NOT_STARTED; 
    }

    // ----- Getters -----

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public LocalDate getDeadline() {
        return deadline;
    }

    public Status getStatus() {
        return status;
    }

    public Student getAssignedMember() {
        return assignedMember;
    }

    // ----- Setters -----

    public void setTitle(String title) {
        this.title = title;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setDeadline(LocalDate deadline) {
        this.deadline = deadline;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public void setAssignedMember(Student assignedMember) {
        this.assignedMember = assignedMember;
    }

    // ----- Convenience -----

    public boolean isCompleted() {
        return status == Status.COMPLETED;
    }

    @Override
    public String toString() {
        String memberName = (assignedMember != null) ? assignedMember.getName() : "Unassigned";
        return title + " [" + status + "] -> " + memberName;
    }
}
