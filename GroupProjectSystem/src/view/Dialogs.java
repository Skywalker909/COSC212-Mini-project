package view;

import controller.SystemController;
import model.Group;
import model.Student;

import javax.swing.*;
import java.awt.*;

public class Dialogs {

    public static void showAddStudentDialog(Component parent, SystemController controller, Runnable callback) {
        JDialog dialog = new JDialog((Frame) SwingUtilities.getWindowAncestor(parent), "Add New Student", true);
        dialog.setLayout(new GridLayout(4, 2, 5, 5));
        dialog.setSize(300, 180);
        dialog.setLocationRelativeTo(parent);

        JTextField idField = new JTextField();
        JTextField nameField = new JTextField();
        JTextField emailField = new JTextField();

        dialog.add(new JLabel("  Student ID:"));
        dialog.add(idField);
        dialog.add(new JLabel("  Name:"));
        dialog.add(nameField);
        dialog.add(new JLabel("  Email:"));
        dialog.add(emailField);

        JButton saveBtn = new JButton("Save");
        saveBtn.addActionListener(e -> {
            if (!idField.getText().isEmpty() && !nameField.getText().isEmpty()) {
                controller.addStudent(idField.getText(), nameField.getText(), emailField.getText());
                callback.run();
                dialog.dispose();
            } else {
                JOptionPane.showMessageDialog(dialog, "Please fill in ID and Name.");
            }
        });

        dialog.add(new JLabel(""));
        dialog.add(saveBtn);
        dialog.setVisible(true);
    }

    public static void showAddGroupDialog(Component parent, SystemController controller, Runnable callback) {
        JDialog dialog = new JDialog((Frame) SwingUtilities.getWindowAncestor(parent), "Create Group", true);
        dialog.setLayout(new GridLayout(3, 2, 5, 5));
        dialog.setSize(300, 150);
        dialog.setLocationRelativeTo(parent);

        JTextField nameField = new JTextField();
        JTextField descField = new JTextField();

        dialog.add(new JLabel("  Group Name:"));
        dialog.add(nameField);
        dialog.add(new JLabel("  Description:"));
        dialog.add(descField);

        JButton saveBtn = new JButton("Create");
        saveBtn.addActionListener(e -> {
            if (!nameField.getText().isEmpty()) {
                controller.addGroup(nameField.getText(), descField.getText());
                callback.run();
                dialog.dispose();
            }
        });

        dialog.add(new JLabel(""));
        dialog.add(saveBtn);
        dialog.setVisible(true);
    }

    public static void showAssignStudentDialog(Component parent, SystemController controller, Runnable callback) {
        if (controller.getDataStore().getStudents().isEmpty() || controller.getDataStore().getGroups().isEmpty()) {
            JOptionPane.showMessageDialog(parent, "Add students and groups first!");
            return;
        }

        JDialog dialog = new JDialog((Frame) SwingUtilities.getWindowAncestor(parent), "Assign Student to Group", true);
        dialog.setLayout(new GridLayout(3, 2, 5, 5));
        dialog.setSize(350, 150);
        dialog.setLocationRelativeTo(parent);

        JComboBox<Student> studentCombo = new JComboBox<>(controller.getDataStore().getStudents().toArray(new Student[0]));
        JComboBox<Group> groupCombo = new JComboBox<>(controller.getDataStore().getGroups().toArray(new Group[0]));

        dialog.add(new JLabel("  Select Student:"));
        dialog.add(studentCombo);
        dialog.add(new JLabel("  Select Group:"));
        dialog.add(groupCombo);

        JButton assignBtn = new JButton("Assign");
        assignBtn.addActionListener(e -> {
            Student selectedStudent = (Student) studentCombo.getSelectedItem();
            Group selectedGroup = (Group) groupCombo.getSelectedItem();

            if (selectedStudent != null && selectedGroup != null) {
                controller.assignStudentToGroup(selectedStudent, selectedGroup);
                callback.run();
                dialog.dispose();
            }
        });

        dialog.add(new JLabel(""));
        dialog.add(assignBtn);
        dialog.setVisible(true);
    }

    public static void showAddTaskDialog(Component parent, SystemController controller, Runnable callback) {
        if (controller.getDataStore().getGroups().isEmpty()) {
            JOptionPane.showMessageDialog(parent, "Please create at least one group first.");
            return;
        }

        JDialog dialog = new JDialog((Frame) SwingUtilities.getWindowAncestor(parent), "Create Task", true);
        dialog.setLayout(new GridLayout(6, 2, 5, 5));
        dialog.setSize(400, 250);
        dialog.setLocationRelativeTo(parent);

        JTextField titleField = new JTextField();
        JTextField descField = new JTextField();
        JTextField deadlineField = new JTextField("YYYY-MM-DD");
        JComboBox<Group> groupCombo = new JComboBox<>(controller.getDataStore().getGroups().toArray(new Group[0]));
        JComboBox<Student> memberCombo = new JComboBox<>();

        groupCombo.addActionListener(e -> {
            memberCombo.removeAllItems();
            Group selected = (Group) groupCombo.getSelectedItem();
            if (selected != null) {
                for (Student s : selected.getMembers()) {
                    memberCombo.addItem(s);
                }
            }
        });
        if (groupCombo.getItemCount() > 0) groupCombo.setSelectedIndex(0);

        dialog.add(new JLabel("  Task Title:"));
        dialog.add(titleField);
        dialog.add(new JLabel("  Description:"));
        dialog.add(descField);
        dialog.add(new JLabel("  Deadline:"));
        dialog.add(deadlineField);
        dialog.add(new JLabel("  Group:"));
        dialog.add(groupCombo);
        dialog.add(new JLabel("  Assign Member:"));
        dialog.add(memberCombo);

        JButton saveBtn = new JButton("Save Task");
        saveBtn.addActionListener(e -> {
            Group selectedGroup = (Group) groupCombo.getSelectedItem();
            Student selectedStudent = (Student) memberCombo.getSelectedItem();

            if (selectedGroup != null && !titleField.getText().isEmpty()) {
                controller.addResponsibility(
                        titleField.getText(),
                        descField.getText(),
                        deadlineField.getText(),
                        selectedGroup,
                        selectedStudent
                );
                callback.run();
                dialog.dispose();
            }
        });

        dialog.add(new JLabel(""));
        dialog.add(saveBtn);
        dialog.setVisible(true);
    }
}