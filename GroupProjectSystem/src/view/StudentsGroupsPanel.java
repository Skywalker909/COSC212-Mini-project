package view;

import controller.SystemController;
import model.Group;
import model.Student;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

public class StudentsGroupsPanel extends JPanel {
    private SystemController controller;
    private DefaultTableModel studentTableModel;
    private JTable studentTable;
    private DefaultTableModel groupTableModel;
    private JTable groupTable;

    public StudentsGroupsPanel(SystemController controller) {
        this.controller = controller;
        setLayout(new GridLayout(1, 2, 10, 10));

        // Left Panel: Students
        JPanel studentPanel = new JPanel(new BorderLayout(5, 5));
        studentPanel.setBorder(BorderFactory.createTitledBorder("Student Management"));

        studentTableModel = new DefaultTableModel(new Object[]{"ID", "Name", "Email", "Group"}, 0);
        studentTable = new JTable(studentTableModel);
        studentPanel.add(new JScrollPane(studentTable), BorderLayout.CENTER);

        JPanel studentBtnPanel = new JPanel(new FlowLayout());
        JButton addStudentBtn = new JButton("Add Student");
        JButton deleteStudentBtn = new JButton("Delete Student");

        addStudentBtn.addActionListener(e -> Dialogs.showAddStudentDialog(this, controller, this::refresh));
        deleteStudentBtn.addActionListener(e -> deleteSelectedStudent());

        studentBtnPanel.add(addStudentBtn);
        studentBtnPanel.add(deleteStudentBtn);
        studentPanel.add(studentBtnPanel, BorderLayout.SOUTH);

        // Right Panel: Groups
        JPanel groupPanel = new JPanel(new BorderLayout(5, 5));
        groupPanel.setBorder(BorderFactory.createTitledBorder("Group Management"));

        groupTableModel = new DefaultTableModel(new Object[]{"Group Name", "Description", "Members Count"}, 0);
        groupTable = new JTable(groupTableModel);
        groupPanel.add(new JScrollPane(groupTable), BorderLayout.CENTER);

        JPanel groupBtnPanel = new JPanel(new FlowLayout());
        JButton addGroupBtn = new JButton("Add Group");
        JButton assignStudentBtn = new JButton("Assign Member");

        addGroupBtn.addActionListener(e -> Dialogs.showAddGroupDialog(this, controller, this::refresh));
        assignStudentBtn.addActionListener(e -> Dialogs.showAssignStudentDialog(this, controller, this::refresh));

        groupBtnPanel.add(addGroupBtn);
        groupBtnPanel.add(assignStudentBtn);
        groupPanel.add(groupBtnPanel, BorderLayout.SOUTH);

        add(studentPanel);
        add(groupPanel);
    }

    private void deleteSelectedStudent() {
        int row = studentTable.getSelectedRow();
        if (row >= 0) {
            Student s = controller.getDataStore().getStudents().get(row);
            controller.deleteStudent(s);
            refresh();
        } else {
            JOptionPane.showMessageDialog(this, "Please select a student to delete.");
        }
    }

    public void refresh() {
        studentTableModel.setRowCount(0);
        for (Student s : controller.getDataStore().getStudents()) {
            Group g = controller.getDataStore().getGroupForStudent(s);
            studentTableModel.addRow(new Object[]{s.getId(), s.getName(), s.getEmail(), g != null ? g.getName() : "None"});
        }

        groupTableModel.setRowCount(0);
        for (Group g : controller.getDataStore().getGroups()) {
            groupTableModel.addRow(new Object[]{g.getName(), g.getDescription(), g.getMembers().size()});
        }
    }
}