/*
 * RaX2App.java
 */
package rax2;

import com.jgoodies.looks.plastic.Plastic3DLookAndFeel;
import com.jgoodies.looks.plastic.PlasticLookAndFeel;
import com.jgoodies.looks.plastic.PlasticXPLookAndFeel;
import com.jgoodies.looks.plastic.theme.DesertBlue;
import com.jgoodies.looks.plastic.theme.ExperienceBlue;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

/**
 * The main class of the RaX-2 application. Launches the main window with
 * JGoodies Plastic3D Look &amp; Feel.
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
     * Sets up the JGoodies Plastic3D Look &amp; Feel with ExperienceBlue theme
     * and starts the main window.
     * @param args command line arguments (unused)
     */
    public static void main(String[] args) {
        try {
            PlasticLookAndFeel.setPlasticTheme(new ExperienceBlue());
            UIManager.setLookAndFeel(new Plastic3DLookAndFeel());
        } catch (UnsupportedLookAndFeelException e) {
            System.out.println("No se encontro el Look and Feel: " + e.toString());
        }

        RaX2App raX2App = new RaX2App();

        RaX2App app = new RaX2App();
        app.startup();
    }
}
