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

    public ReportsPanel(DataManager dataManager, ProgressController progressController) {
        this.dataManager = dataManager;
        this.progressController = progressController;

        setLayout(new GridLayout(1, 2, 10, 10));
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        add(buildMemberReportPanel());
        add(buildGroupReportPanel());

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
            JOptionPane.showMessageDialog(this, "Select a student first.",
                    "Nothing Selected", JOptionPane.WARNING_MESSAGE);
            return;
        }

        ProgressReport report = progressController.getMemberProgressReport(student);
        memberReportArea.setText(
                "Student: " + student.getName() + "\n\n" +
                "Total Responsibilities: " + report.getTotalResponsibilities() + "\n" +
                "Completed:              " + report.getCompletedResponsibilities() + "\n" +
                "Pending:                " + report.getPendingResponsibilities() + "\n" +
                "Completion:             " + String.format("%.1f", report.getCompletionPercentage()) + "%"
        );
    }

    private void showGroupReport() {
        Group group = (Group) groupCombo.getSelectedItem();
        if (group == null) {
            JOptionPane.showMessageDialog(this, "Select a group first.",
                    "Nothing Selected", JOptionPane.WARNING_MESSAGE);
            return;
        }

        ProgressReport report = progressController.getGroupProgressReport(group);
        groupReportArea.setText(
                "Group: " + group.getGroupName() + "\n\n" +
                "Total Responsibilities: " + report.getTotalResponsibilities() + "\n" +
                "Completed:              " + report.getCompletedResponsibilities() + "\n" +
                "Pending:                " + report.getPendingResponsibilities() + "\n" +
                "Completion:             " + String.format("%.1f", report.getCompletionPercentage()) + "%"
        );
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
}
