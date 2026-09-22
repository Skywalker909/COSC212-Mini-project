package controller;

import model.Responsibility;
import model.DataManager;
import model.Student;

import java.time.LocalDate;
import java.util.ArrayList;

public class ResponsibilityController {

    private DataManager dataManager;

    // Constructor
    public ResponsibilityController(DataManager dataManager) {
        this.dataManager = dataManager;
    }

    // Add responsibility
    public boolean addResponsibility(Responsibility responsibility) {

        // Check whether a responsibility with the same title
        // already exists
        for (Responsibility existingResponsibility
                : dataManager.getResponsibilities()) {

            if (existingResponsibility.getTitle()
                    .equals(responsibility.getTitle())) {

                return false;
            }
        }

        dataManager.addResponsibility(responsibility);

        return true;
    }

    // Edit responsibility
    public boolean editResponsibility(
            String title,
            String description,
            LocalDate deadline) {

        for (Responsibility responsibility
                : dataManager.getResponsibilities()) {

            if (responsibility.getTitle().equals(title)) {

                responsibility.setDescription(description);
                responsibility.setDeadline(deadline);

                return true;
            }
        }

        return false;
    }

    // Delete responsibility
    public boolean deleteResponsibility(String title) {

        return dataManager.getResponsibilities()
                .removeIf(responsibility ->
                        responsibility.getTitle().equals(title));
    }

    // Assign responsibility to a student
    public boolean assignResponsibility(
            String title, Student student) {

        for (Responsibility responsibility
                : dataManager.getResponsibilities()) {

            if (responsibility.getTitle().equals(title)) {

                responsibility.setAssignedMember(student);

                return true;
            }
        }

        return false;
    }

    // Update responsibility status
    public boolean updateStatus(
            String title,
            Responsibility.Status status) {

        for (Responsibility responsibility
                : dataManager.getResponsibilities()) {

            if (responsibility.getTitle().equals(title)) {

                responsibility.setStatus(status);

                return true;
            }
        }

        return false;
    }

    // Find responsibility
    public Responsibility findResponsibility(String title) {

        for (Responsibility responsibility
                : dataManager.getResponsibilities()) {

            if (responsibility.getTitle().equals(title)) {
                return responsibility;
            }
        }

        return null;
    }

    // Get all responsibilities
    public ArrayList<Responsibility> getAllResponsibilities() {

        return dataManager.getResponsibilities();
    }
}