package view;

import controller.SystemController;
import model.Group;
import model.Responsibility;
import model.Student;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class ReportsPanel extends JPanel {
    private SystemController controller;
    private JTextArea reportTextArea;

    public ReportsPanel(SystemController controller) {
        this.controller = controller;
        setLayout(new BorderLayout(5, 5));
        setBorder(BorderFactory.createTitledBorder("Progress & Summary Reports"));

        reportTextArea = new JTextArea();
        reportTextArea.setEditable(false);
        reportTextArea.setFont(new Font("Monospaced", Font.PLAIN, 12));
        add(new JScrollPane(reportTextArea), BorderLayout.CENTER);

        JPanel btnPanel = new JPanel(new FlowLayout());
        JButton groupReportBtn = new JButton("Group Summary");
        JButton memberReportBtn = new JButton("Member Contributions");

        groupReportBtn.addActionListener(e -> generateGroupReport());
        memberReportBtn.addActionListener(e -> generateMemberReport());

        btnPanel.add(groupReportBtn);
        btnPanel.add(memberReportBtn);
        add(btnPanel, BorderLayout.SOUTH);
    }

    public void generateGroupReport() {
        StringBuilder sb = new StringBuilder();
        sb.append("==================================================\n");
        sb.append("               GROUP SUMMARY REPORT               \n");
        sb.append("==================================================\n\n");

        for (Group g : controller.getDataStore().getGroups()) {
            sb.append("Group Name: ").append(g.getName()).append("\n");
            sb.append("Description: ").append(g.getDescription()).append("\n");
            sb.append("Members: ").append(g.getMembers().size()).append("\n");

            double progress = controller.calculateGroupProgress(g);
            sb.append(String.format("Overall Progress: %.1f%%\n", progress));
            sb.append("--------------------------------------------------\n");
        }

        reportTextArea.setText(sb.toString());
    }

    public void generateMemberReport() {
        StringBuilder sb = new StringBuilder();
        sb.append("==================================================\n");
        sb.append("            MEMBER CONTRIBUTION REPORT            \n");
        sb.append("==================================================\n\n");

        for (Student s : controller.getDataStore().getStudents()) {
            sb.append("Member: ").append(s.getName()).append(" (ID: ").append(s.getId()).append(")\n");
            Group g = controller.getDataStore().getGroupForStudent(s);
            sb.append("Group: ").append(g != null ? g.getName() : "Unassigned").append("\n");

            double score = controller.calculateMemberContribution(s);
            sb.append(String.format("Contribution Score: %.1f%%\n", score));
            sb.append("--------------------------------------------------\n");
        }

        reportTextArea.setText(sb.toString());
    }
}