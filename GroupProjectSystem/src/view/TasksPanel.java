package view;

import controller.SystemController;
import model.Responsibility;
import model.TaskStatus;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

public class TasksPanel extends JPanel {
    private SystemController controller;
    private DefaultTableModel taskTableModel;
    private JTable taskTable;

    public TasksPanel(SystemController controller) {
        this.controller = controller;
        setLayout(new BorderLayout(5, 5));
        setBorder(BorderFactory.createTitledBorder("Task & Responsibility Tracker"));

        taskTableModel = new DefaultTableModel(new Object[]{"ID", "Title", "Group", "Assigned To", "Deadline", "Status"}, 0);
        taskTable = new JTable(taskTableModel);
        add(new JScrollPane(taskTable), BorderLayout.CENTER);

        JPanel btnPanel = new JPanel(new FlowLayout());
        JButton addTaskBtn = new JButton("Create Task");
        JButton updateStatusBtn = new JButton("Update Status");

        addTaskBtn.addActionListener(e -> Dialogs.showAddTaskDialog(this, controller, this::refresh));
        updateStatusBtn.addActionListener(e -> updateTaskStatus());

        btnPanel.add(addTaskBtn);
        btnPanel.add(updateStatusBtn);
        add(btnPanel, BorderLayout.SOUTH);
    }

    private void updateTaskStatus() {
        int selectedRow = taskTable.getSelectedRow();
        if (selectedRow < 0) {
            JOptionPane.showMessageDialog(this, "Select a task to update.");
            return;
        }

        Responsibility task = controller.getDataStore().getResponsibilities().get(selectedRow);
        TaskStatus newStatus = (TaskStatus) JOptionPane.showInputDialog(
                this,
                "Select status for: " + task.getTitle(),
                "Update Task Status",
                JOptionPane.QUESTION_MESSAGE,
                null,
                TaskStatus.values(),
                task.getStatus()
        );

        if (newStatus != null) {
            task.setStatus(newStatus);
            refresh();
        }
    }

    public void refresh() {
        taskTableModel.setRowCount(0);
        for (Responsibility r : controller.getDataStore().getResponsibilities()) {
            taskTableModel.addRow(new Object[]{
                    r.getId(),
                    r.getTitle(),
                    r.getGroup().getName(),
                    r.getAssignedMember() != null ? r.getAssignedMember().getName() : "Unassigned",
                    r.getDeadline(),
                    r.getStatus().getLabel()
            });
        }
    }
}