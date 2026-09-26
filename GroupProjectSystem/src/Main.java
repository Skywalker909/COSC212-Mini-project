import model.DataManager;
import controller.ProgressController;
import view.MainFrame;

import javax.swing.SwingUtilities;

/**
 * Entry point for the GUI application (Phase 5).
 *
 * TestProgress.java still exists as your console-based test harness
 * for the model/controller layer - this class just launches the Swing
 * front end on top of that same layer.
 */
public class Main {
    public static void main(String[] args) {
        DataManager dataManager = new DataManager();
        ProgressController progressController = new ProgressController(dataManager);

        SwingUtilities.invokeLater(() -> {
            MainFrame frame = new MainFrame(dataManager, progressController);
            frame.setVisible(true);
        });
    }
}
