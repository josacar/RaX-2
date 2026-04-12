/*
 * RaX2App.java
 */
package rax2;

import com.formdev.flatlaf.FlatLightLaf;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

/**
 * The main class of the RaX-2 application. Launches the main window with
 * FlatLaf Light Look &amp; Feel.
 */
public class RaX2App {

    /**
     * Creates a new RaX2App instance.
     */
    public RaX2App() {
    }

    /**
     * At startup create and show the main frame of the application.
     */
    protected void startup() {
        new RaX2View();
    }

    /**
     * Initializes the specified window by injecting resources.
     * Windows in this application are fully initialized by the GUI builder,
     * so additional configuration is not needed.
     * @param root the window to configure
     */
    protected void configureWindow(java.awt.Window root) {
    }

    /**
     * Main method launching the application.
     * Sets up the FlatLaf Light Look &amp; Feel
     * and starts the main window.
     * @param args command line arguments (unused)
     */
    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(new FlatLightLaf());
        } catch (UnsupportedLookAndFeelException e) {
            System.out.println("Failed to set Look and Feel: " + e.toString());
        }

        RaX2App app = new RaX2App();
        app.startup();
    }
}
