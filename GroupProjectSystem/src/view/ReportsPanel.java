package view;

import controller.ProgressController;
import model.DataManager;
import model.Group;
import model.ProgressReport;
import model.Student;

import javax.swing.*;
import java.awt.*;

/**
 * Tab 3: Reports.
 *
 * Pure read-only view: pick a student or a group, click "View Report",
 * and it prints the ProgressReport that ProgressController builds for
 * that student/group. This panel does no math itself - it just calls
 * the controller and formats what comes back.
 */
public class ReportsPanel extends JPanel {

    private DataManager dataManager;
    private ProgressController progressController;

    private JComboBox<Student> studentCombo;
    private JComboBox<Group> groupCombo;

    private JTextArea memberReportArea;
    private JTextArea groupReportArea;
	private JTextArea taskReportArea;

    public ReportsPanel(DataManager dataManager, ProgressController progressController) {
        this.dataManager = dataManager;
        this.progressController = progressController;

        setLayout(new GridLayout(1, 3, 10, 10));
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        add(buildMemberReportPanel());
        add(buildGroupReportPanel());
		add(buildTaskPanel());

        refreshDropdowns();
    }

    private JPanel buildMemberReportPanel() {
        JPanel panel = new JPanel(new BorderLayout(5, 5));
        panel.setBorder(BorderFactory.createTitledBorder("Member Progress"));

        studentCombo = new JComboBox<>();
        studentCombo.setRenderer(new DefaultListCellRenderer() {
            @Override
            public Component getListCellRendererComponent(JList<?> list, Object value, int index,
                                                            boolean isSelected, boolean cellHasFocus) {
                super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
                if (value instanceof Student) {
                    setText(((Student) value).getName());
                }
                return this;
            }
        });

        JButton viewButton = new JButton("View Report");
        viewButton.addActionListener(e -> showMemberReport());

        JPanel topPanel = new JPanel(new BorderLayout(5, 5));
        topPanel.add(studentCombo, BorderLayout.CENTER);
        topPanel.add(viewButton, BorderLayout.EAST);

        memberReportArea = new JTextArea();
        memberReportArea.setEditable(false);
        memberReportArea.setFont(new Font(Font.MONOSPACED, Font.PLAIN, 13));

        panel.add(topPanel, BorderLayout.NORTH);
        panel.add(new JScrollPane(memberReportArea), BorderLayout.CENTER);
        return panel;
    }

    private JPanel buildGroupReportPanel() {
        JPanel panel = new JPanel(new BorderLayout(5, 5));
        panel.setBorder(BorderFactory.createTitledBorder("Group Progress"));

        groupCombo = new JComboBox<>();

        JButton viewButton = new JButton("View Report");
        viewButton.addActionListener(e -> showGroupReport());

        JPanel topPanel = new JPanel(new BorderLayout(5, 5));
        topPanel.add(groupCombo, BorderLayout.CENTER);
        topPanel.add(viewButton, BorderLayout.EAST);

        groupReportArea = new JTextArea();
        groupReportArea.setEditable(false);
        groupReportArea.setFont(new Font(Font.MONOSPACED, Font.PLAIN, 13));

        panel.add(topPanel, BorderLayout.NORTH);
        panel.add(new JScrollPane(groupReportArea), BorderLayout.CENTER);
        return panel;
    }
	private void showMemberReport() {
		Student student = (Student) studentCombo.getSelectedItem();

		if (student == null) {
			JOptionPane.showMessageDialog(
                this,
                "Select a student first.",
                "Nothing Selected",
                JOptionPane.WARNING_MESSAGE
			);
			return;
		}

		ProgressReport report =
            progressController.getMemberProgressReport(student);

		StringBuilder text = new StringBuilder();

		text.append("Student: ")
            .append(student.getName())
            .append("\n\n");

		text.append("Assigned Tasks:\n");

		boolean hasTasks = false;

		for (model.Responsibility responsibility :
            dataManager.getResponsibilities()) {

			if (responsibility.getAssignedMember() == student) {

				hasTasks = true;

				text.append("- ")
                    .append(responsibility.getTitle())
                    .append(" : ")
                    .append(responsibility.getStatus())
                    .append("\n");
			}
		}

		if (!hasTasks) {
			text.append("No assigned tasks.\n");
		}

		text.append("\n");
		text.append("Total Responsibilities: ")
            .append(report.getTotalResponsibilities())
            .append("\n");

		text.append("Completed:              ")
            .append(report.getCompletedResponsibilities())
            .append("\n");

		text.append("Pending:                ")
            .append(report.getPendingResponsibilities())
            .append("\n");

		text.append("Contribution:           ")
            .append(String.format("%.1f",
                    report.getCompletionPercentage()))
            .append("%");

		memberReportArea.setText(text.toString());
	}

    

    private void showGroupReport() {
		Group group = (Group) groupCombo.getSelectedItem();

		if (group == null) {
			JOptionPane.showMessageDialog(
                this,
                "Select a group first.",
                "Nothing Selected",
                JOptionPane.WARNING_MESSAGE
			);
			return;
		}

		ProgressReport report =
            progressController.getGroupProgressReport(group);

		StringBuilder text = new StringBuilder();

		text.append("Group: ")
            .append(group.getGroupName())
            .append("\n\n");

		text.append("Members:\n");

		if (group.getMembers().isEmpty()) {
			text.append("No members.\n");
		} else {
			for (Student student : group.getMembers()) {
				text.append("- ")
                    .append(student.getName())
                    .append(" (")
                    .append(student.getStudentId())
                    .append(")\n");
			}
		}

		text.append("\n");

		text.append("Total Responsibilities: ")
            .append(report.getTotalResponsibilities())
            .append("\n");

		text.append("Completed:              ")
            .append(report.getCompletedResponsibilities())
            .append("\n");

		text.append("Pending:                ")
            .append(report.getPendingResponsibilities())
            .append("\n");

		text.append("Overall Progress:       ")
            .append(String.format("%.1f",
                    report.getCompletionPercentage()))
            .append("%");

		groupReportArea.setText(text.toString());
	}

    public void refreshDropdowns() {
        Student previousStudent = (Student) studentCombo.getSelectedItem();
        studentCombo.removeAllItems();
        for (Student s : dataManager.getStudents()) {
            studentCombo.addItem(s);
        }
        if (previousStudent != null) {
            studentCombo.setSelectedItem(previousStudent);
        }

        Group previousGroup = (Group) groupCombo.getSelectedItem();
        groupCombo.removeAllItems();
        for (Group g : dataManager.getGroups()) {
            groupCombo.addItem(g);
        }
        if (previousGroup != null) {
            groupCombo.setSelectedItem(previousGroup);
        }
    }
	private JPanel buildTaskPanel() {
		JPanel panel = new JPanel(new BorderLayout(5, 5));
		panel.setBorder(BorderFactory.createTitledBorder("Task Status Report"));

		taskReportArea = new JTextArea();
		taskReportArea.setEditable(false);

		JButton viewButton = new JButton("View Task Report");

		viewButton.addActionListener(e -> {
			StringBuilder report = new StringBuilder();

			for (model.Responsibility responsibility : dataManager.getResponsibilities()) {

				model.Student student = responsibility.getAssignedMember();

				String studentName = "Unassigned";
				String groupName = "No Group";

				if (student != null) {
					studentName = student.getName();

					for (model.Group group : dataManager.getGroups()) {
						if (group.getMembers().contains(student)) {
							groupName = group.getGroupName();
							break;
						}
					}
				}

				report.append("Task: ")
                    .append(responsibility.getTitle())
                    .append("\n");

				report.append("Group: ")
                    .append(groupName)
                    .append("\n");

				report.append("Assigned To: ")
                    .append(studentName)
                    .append("\n");

				report.append("Status: ")
                    .append(responsibility.getStatus())
                    .append("\n");

				report.append("-------------------------\n");
			}

			if (report.length() == 0) {
				taskReportArea.setText("No tasks available.");
			} else {
				taskReportArea.setText(report.toString());
			}
		});

		panel.add(viewButton, BorderLayout.NORTH);
		panel.add(new JScrollPane(taskReportArea), BorderLayout.CENTER);

		return panel;
	}
}
