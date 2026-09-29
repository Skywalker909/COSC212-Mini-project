package view;

import model.DataManager;
import model.Responsibility;
import model.Student;
import model.Group;

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
	private JComboBox<Group> groupCombo;
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

        JPanel formPanel = new JPanel(new GridLayout(2, 6, 5, 5));

        titleField = new JTextField();
        descriptionField = new JTextField();
        deadlineField = new JTextField("yyyy-mm-dd");
        assignedStudentCombo = new JComboBox<>();
        statusCombo = new JComboBox<>(Responsibility.Status.values());
		groupCombo = new JComboBox<>();
		
		groupCombo.addActionListener(e -> refreshAssignedStudentCombo());

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
		formPanel.add(new JLabel("Group"));
        formPanel.add(new JLabel("Assigned To:"));
        formPanel.add(new JLabel("Status:"));
		
		

        formPanel.add(titleField);
        formPanel.add(descriptionField);
        formPanel.add(deadlineField);
		formPanel.add(groupCombo);
        formPanel.add(assignedStudentCombo);
        formPanel.add(statusCombo);

        outer.add(formPanel, BorderLayout.CENTER);

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JButton addButton = new JButton("Add Task");
        addButton.addActionListener(e -> addResponsibility());

        JButton updateStatusButton = new JButton("Update Status of Selected Task");
        updateStatusButton.addActionListener(e -> updateSelectedStatus());
		
		JButton deleteButton = new JButton("Delete Task");
        deleteButton.addActionListener(e -> deleteSelectedResponsibility());
		JButton editButton = new JButton("Edit Task");
        editButton.addActionListener(e -> editSelectedResponsibility());

        buttonPanel.add(addButton);
        buttonPanel.add(updateStatusButton);
		buttonPanel.add(deleteButton);
		buttonPanel.add(editButton);

        outer.add(buttonPanel, BorderLayout.SOUTH);
        return outer;
    }

    private void addResponsibility() {
        String title = titleField.getText().trim();
        String description = descriptionField.getText().trim();
        String deadlineText = deadlineField.getText().trim();
		Group selectedGroup = (Group) groupCombo.getSelectedItem();
		Student assignedStudent = (Student) assignedStudentCombo.getSelectedItem();

		if (selectedGroup == null) {
			JOptionPane.showMessageDialog(
				this,
				"Select a group first.",
				"Invalid Input",
				JOptionPane.WARNING_MESSAGE
			);
			return;
		}

		if (assignedStudent == null) {
			JOptionPane.showMessageDialog(
				this,
				"Select a group member.",
				"Invalid Input",
				JOptionPane.WARNING_MESSAGE
			);
			return;
		}

        if (title.isEmpty()) {
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

        dataManager.addResponsibility(new Responsibility(title, description, deadline, assignedStudent));

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
	private void deleteSelectedResponsibility() {
		int selectedRow = taskTable.getSelectedRow();

		if (selectedRow == -1) {
			JOptionPane.showMessageDialog(
                this,
                "Select a task to delete.",
                "Nothing Selected",
                JOptionPane.WARNING_MESSAGE
			);
			return;
		}

		int choice = JOptionPane.showConfirmDialog(
            this,
            "Are you sure you want to delete this task?",
            "Confirm Delete",
            JOptionPane.YES_NO_OPTION
		);

		if (choice == JOptionPane.YES_OPTION) {
			dataManager.getResponsibilities().remove(selectedRow);
			refreshTaskTable();
		}
	}
	private void editSelectedResponsibility() {
		int selectedRow = taskTable.getSelectedRow();

		if (selectedRow == -1) {
			JOptionPane.showMessageDialog(
                this,
                "Select a task to edit.",
                "Nothing Selected",
                JOptionPane.WARNING_MESSAGE
			);
			return;
		}

		String newTitle = titleField.getText().trim();
		String newDescription = descriptionField.getText().trim();
		String deadlineText = deadlineField.getText().trim();

		if (newTitle.isEmpty() || deadlineText.isEmpty()) {
			JOptionPane.showMessageDialog(
                this,
                "Title and deadline are required.",
                "Invalid Input",
                JOptionPane.WARNING_MESSAGE
			);
			return;
		}

		LocalDate newDeadline;

		try {
			newDeadline = LocalDate.parse(deadlineText);
		} catch (Exception e) {
			JOptionPane.showMessageDialog(
                this,
                "Enter the deadline in YYYY-MM-DD format.",
                "Invalid Date",
                JOptionPane.WARNING_MESSAGE
			);
			return;
		}

		Responsibility responsibility =
            dataManager.getResponsibilities().get(selectedRow);

		Student assignedStudent =
            (Student) assignedStudentCombo.getSelectedItem();

		Responsibility.Status newStatus =
            (Responsibility.Status) statusCombo.getSelectedItem();

		responsibility.setTitle(newTitle);
		responsibility.setDescription(newDescription);
		responsibility.setDeadline(newDeadline);
		responsibility.setAssignedMember(assignedStudent);
		responsibility.setStatus(newStatus);

		refreshTaskTable();
	
		titleField.setText("");
		descriptionField.setText("");
		deadlineField.setText("");
	}
	private void refreshAssignedStudentCombo() {
		assignedStudentCombo.removeAllItems();

		Group selectedGroup = (Group) groupCombo.getSelectedItem();

		if (selectedGroup != null) {
			for (Student student : selectedGroup.getMembers()) {
            assignedStudentCombo.addItem(student);
			}
		}
	}
	private void refreshGroupCombo() {
		groupCombo.removeAllItems();

		for (Group group : dataManager.getGroups()) {
			groupCombo.addItem(group);
		}

		refreshAssignedStudentCombo();
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

    public void refreshAll() {
        refreshTaskTable();
        refreshGroupCombo();
    }
}
