package view;

import model.DataManager;
import controller.ProgressController;

import javax.swing.*;
import java.awt.*;

/**
 * The main application window.
 *
 * This class ONLY builds the frame shell: the menu bar and the tabbed
 * pane that holds the three feature panels. It does not know anything
 * about students, groups, or responsibilities directly - that logic
 * lives in the individual panel classes and the model/controller
 * classes they call.
 */
public class MainFrame extends JFrame {

    private DataManager dataManager;
    private ProgressController progressController;

    private StudentsGroupsPanel studentsGroupsPanel;
    private TasksAssignmentsPanel tasksAssignmentsPanel;
    private ReportsPanel reportsPanel;

    public MainFrame(DataManager dataManager, ProgressController progressController) {
        this.dataManager = dataManager;
        this.progressController = progressController;

        setTitle("Group Project Management System");
        setSize(950, 650);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        setJMenuBar(buildMenuBar());
        add(buildTabbedPane(), BorderLayout.CENTER);
    }

    private JTabbedPane buildTabbedPane() {
        studentsGroupsPanel = new StudentsGroupsPanel(dataManager);
        tasksAssignmentsPanel = new TasksAssignmentsPanel(dataManager);
        reportsPanel = new ReportsPanel(dataManager, progressController);

        JTabbedPane tabbedPane = new JTabbedPane();
        tabbedPane.addTab("Students & Groups", studentsGroupsPanel);
        tabbedPane.addTab("Tasks & Assignments", tasksAssignmentsPanel);
        tabbedPane.addTab("Reports", reportsPanel);

        // The three tabs all read from the same shared DataManager, but
        // each one only loads data into its own tables/combo boxes when
        // it is built. This listener re-syncs a tab every time the user
        // switches to it, so (for example) a student added on tab 1
        // shows up in the "Assigned To" dropdown on tab 2.
        tabbedPane.addChangeListener(e -> {
            int index = tabbedPane.getSelectedIndex();
            if (index == 0) {
                studentsGroupsPanel.refreshAll();
            } else if (index == 1) {
                tasksAssignmentsPanel.refreshAll();
            } else if (index == 2) {
                reportsPanel.refreshDropdowns();
            }
        });

        return tabbedPane;
    }

    private JMenuBar buildMenuBar() {
        JMenuBar menuBar = new JMenuBar();

        JMenu fileMenu = new JMenu("File");
        JMenuItem newItem = new JMenuItem("New");
        JMenuItem exitItem = new JMenuItem("Exit");

        newItem.addActionListener(e -> startNewProject());
        exitItem.addActionListener(e -> System.exit(0));

        fileMenu.add(newItem);
        fileMenu.addSeparator();
        fileMenu.add(exitItem);

        JMenu helpMenu = new JMenu("Help");
        JMenuItem aboutItem = new JMenuItem("About");
        aboutItem.addActionListener(e -> JOptionPane.showMessageDialog(
                this,
                "Group Project Management System\nCOSC212 Mini Project",
                "About",
                JOptionPane.INFORMATION_MESSAGE
        ));
        helpMenu.add(aboutItem);

        menuBar.add(fileMenu);
        menuBar.add(helpMenu);

        return menuBar;
    }

    private void startNewProject() {
        int confirm = JOptionPane.showConfirmDialog(
                this,
                "Start a new project? All current data will be cleared.",
                "New Project",
                JOptionPane.YES_NO_OPTION
        );

        if (confirm == JOptionPane.YES_OPTION) {
            dataManager.getStudents().clear();
            dataManager.getGroups().clear();
            dataManager.getResponsibilities().clear();

            studentsGroupsPanel.refreshAll();
            tasksAssignmentsPanel.refreshAll();
            reportsPanel.refreshDropdowns();

            JOptionPane.showMessageDialog(this, "New project started.");
        }
    }
}
