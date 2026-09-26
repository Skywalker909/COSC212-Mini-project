package view;

import model.DataManager;
import model.Responsibility;
import model.Student;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;

/**
 * Tab 2: Tasks & Assignments.
 *
 * Shows every Responsibility in a table, and provides a form to create
 * a new one (assigned to a Student) or update the status of whichever
 * row is currently selected in the table.
 */
public class TasksAssignmentsPanel extends JPanel {

    private DataManager dataManager;

    private DefaultTableModel taskTableModel;
    private JTable taskTable;

    private JTextField titleField;
    private JTextField descriptionField;
    private JTextField deadlineField;
    private JComboBox<Student> assignedStudentCombo;
    private JComboBox<Responsibility.Status> statusCombo;

    public TasksAssignmentsPanel(DataManager dataManager) {
        this.dataManager = dataManager;

        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        add(buildTable(), BorderLayout.CENTER);
        add(buildFormPanel(), BorderLayout.SOUTH);

        refreshAll();
    }

    private JScrollPane buildTable() {
        taskTableModel = new DefaultTableModel(
                new String[]{"Title", "Description", "Deadline", "Status", "Assigned To"}, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        taskTable = new JTable(taskTableModel);
        return new JScrollPane(taskTable);
    }

    private JPanel buildFormPanel() {
        JPanel outer = new JPanel(new BorderLayout(5, 5));
        outer.setBorder(BorderFactory.createTitledBorder("New / Update Task"));

        JPanel formPanel = new JPanel(new GridLayout(2, 5, 5, 5));

        titleField = new JTextField();
        descriptionField = new JTextField();
        deadlineField = new JTextField("yyyy-mm-dd");
        assignedStudentCombo = new JComboBox<>();
        statusCombo = new JComboBox<>(Responsibility.Status.values());

        // Student's toString() isn't overridden, so give the combo box
        // its own renderer instead of showing "model.Student@1a2b3c".
        assignedStudentCombo.setRenderer(new DefaultListCellRenderer() {
            @Override
            public Component getListCellRendererComponent(JList<?> list, Object value, int index,
                                                            boolean isSelected, boolean cellHasFocus) {
                super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
                if (value instanceof Student) {
                    Student s = (Student) value;
                    setText(s.getName() + " (" + s.getStudentId() + ")");
                }
                return this;
            }
        });

        formPanel.add(new JLabel("Title:"));
        formPanel.add(new JLabel("Description:"));
        formPanel.add(new JLabel("Deadline:"));
        formPanel.add(new JLabel("Assigned To:"));
        formPanel.add(new JLabel("Status:"));

        formPanel.add(titleField);
        formPanel.add(descriptionField);
        formPanel.add(deadlineField);
        formPanel.add(assignedStudentCombo);
        formPanel.add(statusCombo);

        outer.add(formPanel, BorderLayout.CENTER);

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JButton addButton = new JButton("Add Task");
        addButton.addActionListener(e -> addResponsibility());

        JButton updateStatusButton = new JButton("Update Status of Selected Task");
        updateStatusButton.addActionListener(e -> updateSelectedStatus());

        buttonPanel.add(addButton);
        buttonPanel.add(updateStatusButton);

        outer.add(buttonPanel, BorderLayout.SOUTH);
        return outer;
    }

    private void addResponsibility() {
        String title = titleField.getText().trim();
        String description = descriptionField.getText().trim();
        String deadlineText = deadlineField.getText().trim();
        Student assigned = (Student) assignedStudentCombo.getSelectedItem();

        if (title.isEmpty() || assigned == null) {
            JOptionPane.showMessageDialog(this,
                    "Title and an assigned student are required. Add a student on the first tab if the dropdown is empty.",
                    "Missing Information", JOptionPane.WARNING_MESSAGE);
            return;
        }

        LocalDate deadline;
        try {
            deadline = LocalDate.parse(deadlineText);
        } catch (DateTimeParseException ex) {
            JOptionPane.showMessageDialog(this, "Deadline must be in yyyy-mm-dd format.",
                    "Invalid Date", JOptionPane.WARNING_MESSAGE);
            return;
        }

        dataManager.addResponsibility(new Responsibility(title, description, deadline, assigned));

        titleField.setText("");
        descriptionField.setText("");
        deadlineField.setText("yyyy-mm-dd");

        refreshTaskTable();
    }

    private void updateSelectedStatus() {
        int row = taskTable.getSelectedRow();
        if (row == -1) {
            JOptionPane.showMessageDialog(this, "Select a task in the table first.",
                    "Nothing Selected", JOptionPane.WARNING_MESSAGE);
            return;
        }

        Responsibility.Status newStatus = (Responsibility.Status) statusCombo.getSelectedItem();
        Responsibility responsibility = dataManager.getResponsibilities().get(row);
        responsibility.setStatus(newStatus);

        refreshTaskTable();
    }

    public void refreshTaskTable() {
        taskTableModel.setRowCount(0);
        for (Responsibility r : dataManager.getResponsibilities()) {
            taskTableModel.addRow(new Object[]{
                    r.getTitle(),
                    r.getDescription(),
                    r.getDeadline(),
                    r.getStatus(),
                    r.getAssignedMember() != null ? r.getAssignedMember().getName() : "Unassigned"
            });
        }
    }

    public void refreshStudentCombo() {
        Student previouslySelected = (Student) assignedStudentCombo.getSelectedItem();
        assignedStudentCombo.removeAllItems();
        for (Student s : dataManager.getStudents()) {
            assignedStudentCombo.addItem(s);
        }
        if (previouslySelected != null) {
            assignedStudentCombo.setSelectedItem(previouslySelected);
        }
    }

    public void refreshAll() {
        refreshTaskTable();
        refreshStudentCombo();
    }
}
