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
	private java.util.List<Responsibility> displayedResponsibilities = new java.util.ArrayList<>();

	private JComboBox <Group> filterGroupCombo;
	private JComboBox <Student> filterStudentCombo;
	

    public TasksAssignmentsPanel(DataManager dataManager) {
        this.dataManager = dataManager;

        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        add(buildFilterPanel(), BorderLayout.NORTH);
		add(buildTable(), BorderLayout.CENTER);
        add(buildFormPanel(), BorderLayout.SOUTH);

        refreshAll();
    }
	private JPanel buildFilterPanel() {
		JPanel panel = new JPanel(new FlowLayout(FlowLayout.LEFT));

		panel.add(new JLabel("Filter by Group:"));

		filterGroupCombo = new JComboBox<>();
		filterGroupCombo.addItem(null);
		filterGroupCombo.addActionListener(e -> refreshFilterStudentCombo());

		panel.add(filterGroupCombo);

		panel.add(new JLabel("Filter by Member:"));

		filterStudentCombo = new JComboBox<>();
		filterStudentCombo.addItem(null);

		panel.add(filterStudentCombo);

		JButton filterButton = new JButton("Apply Filter");
		filterButton.addActionListener(e -> refreshTaskTable());

		panel.add(filterButton);

		JButton clearButton = new JButton("Clear Filter");
		clearButton.addActionListener(e -> clearFilters());

		panel.add(clearButton);

		return panel;
	}
	private void refreshFilterStudentCombo() {
		filterStudentCombo.removeAllItems();
		filterStudentCombo.addItem(null);

		Group selectedGroup = (Group) filterGroupCombo.getSelectedItem();

		if (selectedGroup != null) {
			for (Student student : selectedGroup.getMembers()) {
				filterStudentCombo.addItem(student);
			}
		}
	}

    private JScrollPane buildTable() {
        taskTableModel = new DefaultTableModel(
                new String[]{"Status", "Assigned To"}, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        taskTable = new JTable(taskTableModel);
        return new JScrollPane(taskTable);
    }

   private JPanel buildFormPanel() {

		JPanel outer = new JPanel(new FlowLayout(FlowLayout.LEFT));
		outer.setBorder(
            BorderFactory.createTitledBorder("Task Actions")
		);

		JButton addButton = new JButton("Add Task");
		addButton.addActionListener(
            e -> addResponsibility()
		);

		JButton updateStatusButton =
            new JButton("Update Status of Selected Task");

		updateStatusButton.addActionListener(
            e -> updateSelectedStatus()
		);

		JButton deleteButton =
            new JButton("Delete Task");

		deleteButton.addActionListener(
            e -> deleteSelectedResponsibility()
		);

		JButton editButton =
            new JButton("Edit Task");

		editButton.addActionListener(
            e -> editSelectedResponsibility()
		);

		outer.add(addButton);
		outer.add(updateStatusButton);
		outer.add(deleteButton);
		outer.add(editButton);

		return outer;
	}

   private void addResponsibility() {

		JTextField titleInput = new JTextField();
		JTextField descriptionInput = new JTextField();
		JTextField deadlineInput = new JTextField();

		JComboBox<Group> dialogGroupCombo = new JComboBox<>();

		for (Group group : dataManager.getGroups()) {
			dialogGroupCombo.addItem(group);
		}

		JComboBox<Student> dialogStudentCombo = new JComboBox<>();

		dialogGroupCombo.addActionListener(e -> {
			dialogStudentCombo.removeAllItems();

			Group selectedGroup =
                (Group) dialogGroupCombo.getSelectedItem();

			if (selectedGroup != null) {
				for (Student student : selectedGroup.getMembers()) {
					dialogStudentCombo.addItem(student);
				}
			}
		});

		if (dialogGroupCombo.getItemCount() > 0) {
			dialogGroupCombo.setSelectedIndex(0);
		}

		JPanel formPanel = new JPanel(new GridLayout(4, 2, 5, 5));

		formPanel.add(new JLabel("Title:"));
		formPanel.add(titleInput);

		formPanel.add(new JLabel("Description:"));
		formPanel.add(descriptionInput);

		formPanel.add(new JLabel("Deadline (yyyy-mm-dd):"));
		formPanel.add(deadlineInput);

		formPanel.add(new JLabel("Group:"));
		formPanel.add(dialogGroupCombo);

		JPanel assignmentPanel = new JPanel(new BorderLayout(5, 5));
		assignmentPanel.add(new JLabel("Assign to:"), BorderLayout.WEST);
		assignmentPanel.add(dialogStudentCombo, BorderLayout.CENTER);

		JPanel mainPanel = new JPanel(new BorderLayout(5, 5));
		mainPanel.add(formPanel, BorderLayout.CENTER);
		mainPanel.add(assignmentPanel, BorderLayout.SOUTH);

		int result = JOptionPane.showConfirmDialog(
            this,
            mainPanel,
            "Add Task",
            JOptionPane.OK_CANCEL_OPTION,
            JOptionPane.PLAIN_MESSAGE
		);

		if (result != JOptionPane.OK_OPTION) {
			return;
		}

		String title = titleInput.getText().trim();
		String description = descriptionInput.getText().trim();
		String deadlineText = deadlineInput.getText().trim();

		Group selectedGroup =
            (Group) dialogGroupCombo.getSelectedItem();

		Student assignedStudent =
            (Student) dialogStudentCombo.getSelectedItem();

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
			JOptionPane.showMessageDialog(
                this,
                "Task title is required.",
                "Missing Information",
                JOptionPane.WARNING_MESSAGE
			);
			return;
		}

		LocalDate deadline;

		try {
			deadline = LocalDate.parse(deadlineText);
		} catch (DateTimeParseException ex) {
			JOptionPane.showMessageDialog(
                this,
                "Deadline must be in yyyy-mm-dd format.",
                "Invalid Date",
                JOptionPane.WARNING_MESSAGE
			);
			return;
		}

		dataManager.addResponsibility(
            new Responsibility(
                    title,
                    description,
                    deadline,
                    assignedStudent
            )
		);

		refreshTaskTable();
	}

	private void updateSelectedStatus() {

		int row = taskTable.getSelectedRow();

		if (row == -1) {
			JOptionPane.showMessageDialog(
                this,
                "Select a task in the table first.",
                "Nothing Selected",
                JOptionPane.WARNING_MESSAGE
			);
			return;
		}

		Responsibility responsibility =
            displayedResponsibilities.get(row);

		JComboBox<Responsibility.Status> statusCombo =
            new JComboBox<>(Responsibility.Status.values());

		statusCombo.setSelectedItem(
            responsibility.getStatus()
		);

		JPanel panel = new JPanel(new BorderLayout(5, 5));

		panel.add(
            new JLabel("Select new status:"),
            BorderLayout.WEST
		);

		panel.add(
            statusCombo,
            BorderLayout.CENTER
		);

		int result = JOptionPane.showConfirmDialog(
            this,
            panel,
            "Update Task Status",
            JOptionPane.OK_CANCEL_OPTION,
            JOptionPane.PLAIN_MESSAGE
		);

		if (result != JOptionPane.OK_OPTION) {
			return;
		}

		Responsibility.Status newStatus =
            (Responsibility.Status) statusCombo.getSelectedItem();

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
			Responsibility responsibility = displayedResponsibilities.get(selectedRow);
		dataManager.getResponsibilities().remove(responsibility);
			refreshTaskTable();
		}
	}
	private void editSelectedResponsibility() {
		int selectedRow = taskTable.getSelectedRow();

		if (selectedRow == -1) {
			JOptionPane.showMessageDialog(
                this,
                "Select a task first.",
                "Nothing Selected",
                JOptionPane.WARNING_MESSAGE
			);
			return;
		}

		Responsibility responsibility =
            displayedResponsibilities.get(selectedRow);

		JTextField titleInput =
            new JTextField(responsibility.getTitle());

		JTextField descriptionInput =
            new JTextField(responsibility.getDescription());

		JTextField deadlineInput =
            new JTextField(
                    responsibility.getDeadline().toString()
            );

		JComboBox<Group> dialogGroupCombo =
            new JComboBox<>();

		Group currentGroup = null;

		for (Group group : dataManager.getGroups()) {

			dialogGroupCombo.addItem(group);

			if (group.getMembers().contains(
                responsibility.getAssignedMember())) {

				currentGroup = group;
			}
		}

		if (currentGroup != null) {
			dialogGroupCombo.setSelectedItem(currentGroup);
		}

		JComboBox<Student> dialogStudentCombo =
            new JComboBox<>();

		for (Student student : currentGroup.getMembers()) {
			dialogStudentCombo.addItem(student);
		}

		dialogStudentCombo.setSelectedItem(
            responsibility.getAssignedMember()
		);

		dialogGroupCombo.addActionListener(e -> {

			dialogStudentCombo.removeAllItems();

			Group selectedGroup =
                (Group) dialogGroupCombo.getSelectedItem();

			if (selectedGroup != null) {

				for (Student student :
                    selectedGroup.getMembers()) {

					dialogStudentCombo.addItem(student);
				}
			}
		});

		JComboBox<Responsibility.Status> statusCombo =
            new JComboBox<>(
                    Responsibility.Status.values()
            );

		statusCombo.setSelectedItem(
            responsibility.getStatus()
		);

		JPanel formPanel =
            new JPanel(new GridLayout(5, 2, 5, 5));

		formPanel.add(new JLabel("Title:"));
		formPanel.add(titleInput);

		formPanel.add(new JLabel("Description:"));
		formPanel.add(descriptionInput);

		formPanel.add(
            new JLabel("Deadline (yyyy-mm-dd):")
		);
		formPanel.add(deadlineInput);

		formPanel.add(new JLabel("Group:"));
		formPanel.add(dialogGroupCombo);

		formPanel.add(new JLabel("Assign to:"));
		formPanel.add(dialogStudentCombo);

		JPanel statusPanel =
            new JPanel(new BorderLayout(5, 5));

		statusPanel.add(
            new JLabel("Status:"),
            BorderLayout.WEST
		);

		statusPanel.add(
            statusCombo,
            BorderLayout.CENTER
		);

		JPanel mainPanel =
            new JPanel(new BorderLayout(5, 5));

		mainPanel.add(
            formPanel,
            BorderLayout.CENTER
		);

		mainPanel.add(
            statusPanel,
            BorderLayout.SOUTH
		);

		int result = JOptionPane.showConfirmDialog(
            this,
            mainPanel,
            "Edit Task",
            JOptionPane.OK_CANCEL_OPTION,
            JOptionPane.PLAIN_MESSAGE
		);

		if (result != JOptionPane.OK_OPTION) {
			return;
		}

		String title =
            titleInput.getText().trim();

		String description =
            descriptionInput.getText().trim();

		String deadlineText =
            deadlineInput.getText().trim();

		Student assignedStudent =
            (Student) dialogStudentCombo.getSelectedItem();

		Responsibility.Status status =
            (Responsibility.Status)
                    statusCombo.getSelectedItem();

		if (title.isEmpty()) {

			JOptionPane.showMessageDialog(
                this,
                "Task title is required.",
                "Missing Information",
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

		LocalDate deadline;

		try {

			deadline =
                LocalDate.parse(deadlineText);

		} catch (DateTimeParseException ex) {

			JOptionPane.showMessageDialog(
                this,
                "Deadline must be in yyyy-mm-dd format.",
                "Invalid Date",
                JOptionPane.WARNING_MESSAGE
			);

			return;
		}

		responsibility.setTitle(title);
		responsibility.setDescription(description);
		responsibility.setDeadline(deadline);
		responsibility.setAssignedMember(assignedStudent);
		responsibility.setStatus(status);

		refreshTaskTable();
	}
	
	private void clearFilters() {
		filterGroupCombo.setSelectedItem(null);
		filterStudentCombo.removeAllItems();
		filterStudentCombo.addItem(null);

		refreshTaskTable();
	}
	public void refreshAll() {

		if (filterGroupCombo != null) {
			filterGroupCombo.removeAllItems();
			filterGroupCombo.addItem(null);

			for (Group group : dataManager.getGroups()) {
				filterGroupCombo.addItem(group);
			}

			refreshFilterStudentCombo();
		}

		refreshTaskTable();
	}
	private void refreshTaskTable() {

		taskTableModel.setRowCount(0);
		displayedResponsibilities.clear();

		Group selectedGroup =
            (Group) filterGroupCombo.getSelectedItem();

		Student selectedStudent =
            (Student) filterStudentCombo.getSelectedItem();

		for (Responsibility responsibility :
            dataManager.getResponsibilities()) {

			Student assignedStudent =
                responsibility.getAssignedMember();

			if (selectedStudent != null &&
					assignedStudent != selectedStudent) {
				continue;
			}

			if (selectedGroup != null &&
					(assignedStudent == null ||
					!selectedGroup.getMembers().contains(assignedStudent))) {
				continue;
			}

			displayedResponsibilities.add(responsibility);

			String studentName =
                assignedStudent == null
                        ? "Unassigned"
                        : assignedStudent.getName();

			taskTableModel.addRow(new Object[]{
                responsibility.getStatus(),
                studentName
			});
		}
	}
}