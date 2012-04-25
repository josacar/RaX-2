/*
 * RaX2App.java
 */
package rax2;

import org.jdesktop.application.Application;
import org.jdesktop.application.SingleFrameApplication;
import com.jgoodies.looks.plastic.Plastic3DLookAndFeel;
import com.jgoodies.looks.plastic.PlasticLookAndFeel;
import com.jgoodies.looks.plastic.PlasticXPLookAndFeel;
import com.jgoodies.looks.plastic.theme.DesertBlue;
import com.jgoodies.looks.plastic.theme.ExperienceBlue;
import javax.swing.UIManager;

/**
 * The main class of the application.
 */
public class RaX2App extends SingleFrameApplication {

    /**
     * At startup create and show the main frame of the application.
     */
    @Override
    protected void startup() {
        show(new RaX2View(this));
    }

    /**
     * This method is to initialize the specified window by injecting resources.
     * Windows shown in our application come fully initialized from the GUI
     * builder, so this additional configuration is not needed.
     */
    @Override
    protected void configureWindow(java.awt.Window root) {
    }

    /**
     * A convenient static getter for the application instance.
     * @return the instance of RaX2App
     */
    public static RaX2App getApplication() {
        return Application.getInstance(RaX2App.class);
    }

    /**
     * Main method launching the application.
     */
    public static void main(String[] args) {
        //    java.lang.reflect.Constructor[] cons = null;
        launch(RaX2App.class, args);
        try {
            PlasticLookAndFeel.setPlasticTheme(new ExperienceBlue());
            UIManager.setLookAndFeel(new Plastic3DLookAndFeel());
        } catch (Exception e) {
            System.out.println("No se encontro el Look and Feel: " + e.toString());
        }
    /*
    try {
    Class feel = Class.forName("com.sun.java.swing.plaf.gtk.GTKLookAndFeel");
    cons = feel.getConstructors();
    UIManager.setLookAndFeel((LookAndFeel) cons[0].newInstance());
    } catch (UnsupportedLookAndFeelException ex) {
    System.out.println("Look and Feel no compatible: " + ex.toString());
    } catch (InstantiationException ex) {
    } catch (IllegalAccessException ex) {
    } catch (IllegalArgumentException ex) {
    } catch (InvocationTargetException ex) {
    } catch (ClassNotFoundException ex) {
    System.out.println("No se encontro el Look and Feel: " + ex.toString());
    }
     */
    }
}
