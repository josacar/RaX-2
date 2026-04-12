package rax2;

import java.awt.Component;
import javax.swing.JOptionPane;
import org.apache.xmlrpc.XmlRpcException;

/**
 * Utility for handling XML-RPC exceptions in GUI dialogs.
 */
final class XmlRpcErrorHandler {

    private XmlRpcErrorHandler() {
    }

    /**
     * Shows an error dialog for an XML-RPC exception.
     * @param parent the parent component for the dialog
     * @param ex the XML-RPC exception
     * @param title the dialog title
     */
    static void showErrorMessage(Component parent, XmlRpcException ex, String title) {
        JOptionPane.showMessageDialog(parent, ex, title, JOptionPane.ERROR_MESSAGE);
    }

    /**
     * Shows a connection error dialog with special handling for connection failures.
     * @param parent the parent component for the dialog
     * @param ex the XML-RPC exception
     */
    static void showConnectionError(Component parent, XmlRpcException ex) {
        if (ex.code == 0) {
            JOptionPane.showMessageDialog(parent, "Could not connect to server",
                    "Connection Error", JOptionPane.ERROR_MESSAGE);
        } else {
            JOptionPane.showMessageDialog(parent, "Error: " + ex.code + " " + ex,
                    "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}
