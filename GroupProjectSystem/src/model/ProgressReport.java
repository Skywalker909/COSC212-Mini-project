package model;

public class ProgressReport {

    private int totalResponsibilities;
    private int completedResponsibilities;
    private int pendingResponsibilities;
    private double completionPercentage;

    public ProgressReport(
            int totalResponsibilities,
            int completedResponsibilities,
            int pendingResponsibilities,
            double completionPercentage) {

        this.totalResponsibilities = totalResponsibilities;
        this.completedResponsibilities = completedResponsibilities;
        this.pendingResponsibilities = pendingResponsibilities;
        this.completionPercentage = completionPercentage;
    }

    public int getTotalResponsibilities() {
        return totalResponsibilities;
    }

    public int getCompletedResponsibilities() {
        return completedResponsibilities;
    }

    public int getPendingResponsibilities() {
        return pendingResponsibilities;
    }

    public double getCompletionPercentage() {
        return completionPercentage;
    }
}