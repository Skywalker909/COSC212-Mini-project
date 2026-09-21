package view;

import controller.SystemController;

import javax.swing.*;
import java.awt.*;

public class MainFrame extends JFrame {
    private SystemController controller;
    private StudentsGroupsPanel studentsGroupsPanel;
    private TasksPanel tasksPanel;
    private ReportsPanel reportsPanel;

    public MainFrame(SystemController controller) {
        this.controller = controller;

        setTitle("Group Project Management System - COSC 212");
        setSize(950, 650);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        setJMenuBar(createMenuBar());

        JTabbedPane tabbedPane = new JTabbedPane();

        studentsGroupsPanel = new StudentsGroupsPanel(controller);
        tasksPanel = new TasksPanel(controller);
        reportsPanel = new ReportsPanel(controller);

        tabbedPane.addTab("Students & Groups", studentsGroupsPanel);
        tabbedPane.addTab("Tasks & Assignment", tasksPanel);
        tabbedPane.addTab("Reports", reportsPanel);

        tabbedPane.addChangeListener(e -> refreshAllTabs());

        add(tabbedPane, BorderLayout.CENTER);
    }

    private JMenuBar createMenuBar() {
        JMenuBar menuBar = new JMenuBar();

        JMenu fileMenu = new JMenu("File");
        JMenuItem exitItem = new JMenuItem("Exit");
        exitItem.addActionListener(e -> System.exit(0));
        fileMenu.add(exitItem);

        JMenu helpMenu = new JMenu("Help");
        JMenuItem aboutItem = new JMenuItem("About");
        aboutItem.addActionListener(e -> 
            JOptionPane.showMessageDialog(this, 
                "Group Project Management System\nCOSC 212 Assignment\nBuilt with Java Swing/AWT", 
                "About", JOptionPane.INFORMATION_MESSAGE));
        helpMenu.add(aboutItem);

        menuBar.add(fileMenu);
        menuBar.add(helpMenu);
        return menuBar;
    }

    public void refreshAllTabs() {
        studentsGroupsPanel.refresh();
        tasksPanel.refresh();
        reportsPanel.generateGroupReport();
    }
}