package rax2;

import io.grpc.StatusRuntimeException;
import java.awt.Component;
import java.net.ConnectException;
import javax.swing.JOptionPane;

/**
 * Utility for handling gRPC exceptions in GUI dialogs.
 */
final class GrpcErrorHandler {

    private GrpcErrorHandler() {
    }

    /**
     * Shows an error dialog for a gRPC exception.
     * @param parent the parent component for the dialog
     * @param ex the gRPC exception
     * @param title the dialog title
     */
    static void showErrorMessage(Component parent, StatusRuntimeException ex, String title) {
        JOptionPane.showMessageDialog(parent, ex.getMessage(), title, JOptionPane.ERROR_MESSAGE);
    }

    /**
     * Shows a connection error dialog with special handling for connection failures.
     * @param parent the parent component for the dialog
     * @param ex the gRPC exception
     */
    static void showConnectionError(Component parent, StatusRuntimeException ex) {
        String msg = ex.getStatus().getDescription();
        if (msg == null) {
            msg = ex.getStatus().toString();
        }
        if (ex.getCause() instanceof ConnectException || ex.getStatus().getCode() == io.grpc.Status.Code.UNAVAILABLE) {
            JOptionPane.showMessageDialog(parent, "Could not connect to server",
                    "Connection Error", JOptionPane.ERROR_MESSAGE);
        } else {
            JOptionPane.showMessageDialog(parent, "Error: " + ex.getStatus().getCode() + " " + msg,
                    "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    /**
     * Shows a generic error dialog for any exception.
     * @param parent the parent component for the dialog
     * @param ex the exception
     * @param title the dialog title
     */
    static void showErrorMessage(Component parent, Exception ex, String title) {
        JOptionPane.showMessageDialog(parent, ex.getMessage(), title, JOptionPane.ERROR_MESSAGE);
    }
}
