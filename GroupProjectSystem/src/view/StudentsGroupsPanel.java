package view;

import model.DataManager;
import model.Student;
import model.Group;

import controller.StudentController;
import controller.GroupController;


import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

/**
 * Tab 1: Students & Groups.
 *
 * Left side: add students, shown in a table.
 * Right side: add groups, pick a group to see its members, and add
 * the selected student (from the table on the left) into it.
 */
public class StudentsGroupsPanel extends JPanel {

    private DataManager dataManager;
	private StudentController studentController;
	private GroupController groupController;

    // Students side
    private DefaultTableModel studentTableModel;
    private JTable studentTable;
  

    // Groups side
    private DefaultListModel<Group> groupListModel;
    private JList<Group> groupList;
    private DefaultListModel<String> memberListModel;
    private JList<String> memberList;

    public StudentsGroupsPanel(DataManager dataManager) {
        this.dataManager = dataManager;
		studentController = new StudentController(dataManager);
		groupController = new GroupController(dataManager);
		

        setLayout(new GridLayout(1, 2, 10, 10));
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        add(buildStudentsPanel());
        add(buildGroupsPanel());

        refreshAll();
    }

   private JPanel buildStudentsPanel() {
		JPanel panel = new JPanel(new BorderLayout(5, 5));
		panel.setBorder(BorderFactory.createTitledBorder("Students"));

		studentTableModel = new DefaultTableModel(
            new String[]{"ID", "Name", "Email"}, 0) {

			@Override
			public boolean isCellEditable(int row, int column) {
				return false;
			}
		};

		studentTable = new JTable(studentTableModel);
		panel.add(new JScrollPane(studentTable), BorderLayout.CENTER);

		JPanel buttonPanel = new JPanel(new GridLayout(1, 3, 5, 5));

		JButton addButton = new JButton("Add Student");
		addButton.addActionListener(e -> addStudent());

		JButton editButton = new JButton("Edit Student");
		editButton.addActionListener(e -> editStudent());

		JButton deleteButton = new JButton("Delete Student");
		deleteButton.addActionListener(e -> deleteStudent());

		buttonPanel.add(addButton);
		buttonPanel.add(editButton);
		buttonPanel.add(deleteButton);

		panel.add(buttonPanel, BorderLayout.SOUTH);

		return panel;
	}

   private JPanel buildGroupsPanel() {
		JPanel panel = new JPanel(new BorderLayout(5, 5));
		panel.setBorder(BorderFactory.createTitledBorder("Groups"));

		groupListModel = new DefaultListModel<>();
		groupList = new JList<>(groupListModel);

		groupList.addListSelectionListener(e -> {
			if (!e.getValueIsAdjusting()) {
				refreshMemberList();
			}
		});

		memberListModel = new DefaultListModel<>();
		memberList = new JList<>(memberListModel);

		JPanel listsPanel = new JPanel(new GridLayout(1, 2, 5, 5));

		JPanel groupSide = new JPanel(new BorderLayout());
		groupSide.add(new JLabel("Groups:"), BorderLayout.NORTH);
		groupSide.add(new JScrollPane(groupList), BorderLayout.CENTER);

		listsPanel.add(groupSide);

		JPanel memberSide = new JPanel(new BorderLayout());
		memberSide.add(
            new JLabel("Members of selected group:"),
            BorderLayout.NORTH
		);
		memberSide.add(
            new JScrollPane(memberList),
            BorderLayout.CENTER
		);

		listsPanel.add(memberSide);

		panel.add(listsPanel, BorderLayout.CENTER);

    // Group buttons
		JPanel groupButtonPanel =
            new JPanel(new GridLayout(1, 3, 5, 5));

		JButton addGroupButton =
            new JButton("Add Group");

		addGroupButton.addActionListener(
            e -> addGroup()
		);

		JButton editGroupButton =
            new JButton("Edit Group");

		editGroupButton.addActionListener(
            e -> editGroup()
		);

		JButton deleteGroupButton =
            new JButton("Delete Group");

		deleteGroupButton.addActionListener(
            e -> deleteGroup()
		);

		groupButtonPanel.add(addGroupButton);
		groupButtonPanel.add(editGroupButton);
		groupButtonPanel.add(deleteGroupButton);
	
    // Member buttons
		JPanel memberButtonPanel =
            new JPanel(new GridLayout(1, 2, 5, 5));

		JButton addMemberButton =
            new JButton("Add Selected Student to Selected Group");

		addMemberButton.addActionListener(
            e -> addMemberToGroup()
		);

		JButton removeMemberButton =
            new JButton("Remove Member");

		removeMemberButton.addActionListener(
            e -> removeMemberFromGroup()
		);

		memberButtonPanel.add(addMemberButton);
		memberButtonPanel.add(removeMemberButton);

		JPanel buttonPanel =
            new JPanel(new GridLayout(2, 1, 5, 5));

		buttonPanel.add(groupButtonPanel);
		buttonPanel.add(memberButtonPanel);

		panel.add(buttonPanel, BorderLayout.SOUTH);

		return panel;
	}
	private void removeMemberFromGroup() {
		Group selectedGroup = groupList.getSelectedValue();
		int selectedMemberIndex = memberList.getSelectedIndex();

		if (selectedGroup == null) {
			JOptionPane.showMessageDialog(
                this,
                "Select a group first.",
                "Nothing Selected",
                JOptionPane.WARNING_MESSAGE
			);
			return;
		}

		if (selectedMemberIndex == -1) {
			JOptionPane.showMessageDialog(
                this,
                "Select a member to remove.",
                "Nothing Selected",
                JOptionPane.WARNING_MESSAGE
			);
			return;
		}

		Student studentToRemove = selectedGroup.getMembers().get(selectedMemberIndex);

		selectedGroup.removeMember(studentToRemove);

		refreshMemberList();
	}

    private void addStudent() {

		JTextField idField = new JTextField();
		JTextField nameField = new JTextField();
		JTextField emailField = new JTextField();

		JPanel panel = new JPanel(new GridLayout(3, 2, 5, 5));

		panel.add(new JLabel("Student ID:"));
		panel.add(idField);

		panel.add(new JLabel("Name:"));
		panel.add(nameField);

		panel.add(new JLabel("Email:"));
		panel.add(emailField);

		int result = JOptionPane.showConfirmDialog(
            this,
            panel,
            "Add Student",
            JOptionPane.OK_CANCEL_OPTION,
            JOptionPane.PLAIN_MESSAGE
		);

		if (result != JOptionPane.OK_OPTION) {
			return;
		}

		String id = idField.getText().trim();
		String name = nameField.getText().trim();
		String email = emailField.getText().trim();

		if (id.isEmpty() || name.isEmpty()) {
			JOptionPane.showMessageDialog(
                this,
                "Student ID and Name are required.",
                "Missing Information",
                JOptionPane.WARNING_MESSAGE
			);
			return;
		}

		dataManager.addStudent(new Student(id, name, email));

		refreshStudentTable();
	}
	private void deleteStudent() {
		int selectedRow = studentTable.getSelectedRow();

		if (selectedRow == -1) {
			JOptionPane.showMessageDialog(
				this,
                "Select a student first.",
                "Nothing Selected",
                JOptionPane.WARNING_MESSAGE
			);
			return;
		}

		String studentId = (String) studentTableModel.getValueAt(selectedRow, 0);
		String studentName = (String) studentTableModel.getValueAt(selectedRow, 1);

		int confirm = JOptionPane.showConfirmDialog(
            this,
            "Delete student: " + studentName + "?",
            "Confirm Delete",
            JOptionPane.YES_NO_OPTION
		);

		if (confirm == JOptionPane.YES_OPTION) {

			boolean deleted = studentController.deleteStudent(studentId);

			if (deleted) {
				refreshAll();

				JOptionPane.showMessageDialog(
                    this,
                    "Student deleted successfully."
				);
			}
		}
	}
	private void editStudent() {
		int selectedRow = studentTable.getSelectedRow();

		if (selectedRow == -1) {
			JOptionPane.showMessageDialog(
                this,
                "Select a student to edit.",
                "Nothing Selected",
                JOptionPane.WARNING_MESSAGE
			);
			return;
		}

		String oldStudentId =
            (String) studentTableModel.getValueAt(selectedRow, 0);

		Student student = findStudentById(oldStudentId);

		if (student == null) {
			return;
		}

		JTextField idField = new JTextField(student.getStudentId());
		JTextField nameField = new JTextField(student.getName());
		JTextField emailField = new JTextField(student.getEmail());

		JPanel panel = new JPanel(new GridLayout(3, 2, 5, 5));

		panel.add(new JLabel("Student ID:"));
		panel.add(idField);

		panel.add(new JLabel("Name:"));
		panel.add(nameField);

		panel.add(new JLabel("Email:"));
		panel.add(emailField);

		int result = JOptionPane.showConfirmDialog(
            this,
            panel,
            "Edit Student",
            JOptionPane.OK_CANCEL_OPTION,
            JOptionPane.PLAIN_MESSAGE
		);

		if (result != JOptionPane.OK_OPTION) {
			return;
		}

		String newStudentId = idField.getText().trim();
		String newName = nameField.getText().trim();
		String newEmail = emailField.getText().trim();

		if (newStudentId.isEmpty() || newName.isEmpty()) {
			JOptionPane.showMessageDialog(
                this,
                "Student ID and name are required.",
                "Invalid Input",
                JOptionPane.WARNING_MESSAGE
			);
			return;
		}

		student.setId(newStudentId);
		student.setName(newName);
		student.setEmail(newEmail);

		refreshStudentTable();
	}

   private void addGroup() {

		JTextField nameField = new JTextField();
		JTextField descriptionField = new JTextField();

		JPanel panel = new JPanel(new GridLayout(2, 2, 5, 5));

		panel.add(new JLabel("Group Name:"));
		panel.add(nameField);

		panel.add(new JLabel("Description:"));
		panel.add(descriptionField);

		int result = JOptionPane.showConfirmDialog(
            this,
            panel,
            "Add Group",
            JOptionPane.OK_CANCEL_OPTION,
            JOptionPane.PLAIN_MESSAGE
		);

		if (result != JOptionPane.OK_OPTION) {
			return;
		}

		String name = nameField.getText().trim();
		String description = descriptionField.getText().trim();

		if (name.isEmpty()) {
			JOptionPane.showMessageDialog(
                this,
                "Group name is required.",
                "Missing Information",
                JOptionPane.WARNING_MESSAGE
			);
			return;
		}

		dataManager.addGroup(new Group(name, description));

		refreshGroupList();
	}
	private void deleteGroup() {
		Group selectedGroup = groupList.getSelectedValue();

		if (selectedGroup == null) {
			JOptionPane.showMessageDialog(
                this,
                "Select a group first.",
                "Nothing Selected",
                JOptionPane.WARNING_MESSAGE
			);
			return;
		}

		String groupName = selectedGroup.getGroupName();

		int confirm = JOptionPane.showConfirmDialog(
            this,
            "Delete group: " + groupName + "?",
            "Confirm Delete",
            JOptionPane.YES_NO_OPTION
		);

		if (confirm == JOptionPane.YES_OPTION) {

			boolean deleted = groupController.deleteGroup(groupName);

			if (deleted) {
				refreshAll();

				JOptionPane.showMessageDialog(
                    this,
                    "Group deleted successfully."
				);
			}
		}
	}
	private void editGroup() {
		Group selectedGroup = groupList.getSelectedValue();

		if (selectedGroup == null) {
			JOptionPane.showMessageDialog(
                this,
                "Select a group to edit.",
                "Nothing Selected",
                JOptionPane.WARNING_MESSAGE
			);
			return;
		}

		JTextField nameField =
            new JTextField(selectedGroup.getGroupName());

		JTextField descriptionField =
            new JTextField(selectedGroup.getDescription());

		JPanel panel = new JPanel(new GridLayout(2, 2, 5, 5));

		panel.add(new JLabel("Group Name:"));
		panel.add(nameField);

		panel.add(new JLabel("Description:"));
		panel.add(descriptionField);

		int result = JOptionPane.showConfirmDialog(
            this,
            panel,
            "Edit Group",
            JOptionPane.OK_CANCEL_OPTION,
            JOptionPane.PLAIN_MESSAGE
		);

		if (result != JOptionPane.OK_OPTION) {
			return;
		}

		String newGroupName = nameField.getText().trim();
		String newDescription = descriptionField.getText().trim();

		if (newGroupName.isEmpty()) {
			JOptionPane.showMessageDialog(
                this,
                "Group name is required.",
                "Invalid Input",
                JOptionPane.WARNING_MESSAGE
			);
			return;
		}

		selectedGroup.setGroupName(newGroupName);
		selectedGroup.setDescription(newDescription);

		refreshGroupList();
	}
	
    private void addMemberToGroup() {
        int studentRow = studentTable.getSelectedRow();
        Group selectedGroup = groupList.getSelectedValue();

        if (studentRow == -1 || selectedGroup == null) {
            JOptionPane.showMessageDialog(this, "Select a student and a group first.",
                    "Nothing Selected", JOptionPane.WARNING_MESSAGE);
            return;
        }

        String studentId = (String) studentTableModel.getValueAt(studentRow, 0);
        Student student = findStudentById(studentId);

        if (student != null) {
            selectedGroup.addMember(student);
            refreshMemberList();
        }
    }

    private Student findStudentById(String id) {
        for (Student s : dataManager.getStudents()) {
            if (s.getStudentId().equals(id)) {
                return s;
            }
        }
        return null;
    }

    public void refreshStudentTable() {
        studentTableModel.setRowCount(0);
        for (Student s : dataManager.getStudents()) {
            studentTableModel.addRow(new Object[]{s.getStudentId(), s.getName(), s.getEmail()});
        }
    }

    public void refreshGroupList() {
        Group previouslySelected = groupList.getSelectedValue();
        groupListModel.clear();
        for (Group g : dataManager.getGroups()) {
            groupListModel.addElement(g);
        }
        if (previouslySelected != null) {
            groupList.setSelectedValue(previouslySelected, true);
        }
    }

    private void refreshMemberList() {
        memberListModel.clear();
        Group selected = groupList.getSelectedValue();
        if (selected != null) {
            for (Student s : selected.getMembers()) {
                memberListModel.addElement(s.getName() + " (" + s.getStudentId() + ")");
            }
        }
    }

    public void refreshAll() {
        refreshStudentTable();
        refreshGroupList();
        refreshMemberList();
    }
}
