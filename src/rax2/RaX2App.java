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
 * The main class of the application.
 */
public class RaX2App {

    /**
     * At startup create and show the main frame of the application.
     */
    protected void startup() {
        new RaX2View();
    }

    /**
     * This method is to initialize the specified window by injecting resources.
     * Windows shown in our application come fully initialized from the GUI
     * builder, so this additional configuration is not needed.
     */
    protected void configureWindow(java.awt.Window root) {
    }

    /**
     * Main method launching the application.
     * @param args
     */
    public static void main(String[] args) {
        //    java.lang.reflect.Constructor[] cons = null;
        RaX2App raX2App = new RaX2App();
        
        RaX2App app = new RaX2App();
        app.startup();
       
        try {
            PlasticLookAndFeel.setPlasticTheme(new ExperienceBlue());
            UIManager.setLookAndFeel(new Plastic3DLookAndFeel());
        } catch (UnsupportedLookAndFeelException e) {
            System.out.println("No se encontro el Look and Feel: " + e.toString());
        }
    }
}