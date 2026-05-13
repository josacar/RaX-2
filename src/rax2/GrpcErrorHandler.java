package rax2;

import io.grpc.StatusRuntimeException;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.net.ConnectException;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

final class GrpcErrorHandler {

    private GrpcErrorHandler() {
    }

    static void showErrorMessage(Component parent, StatusRuntimeException ex, String title) {
        showError(parent, title, ex.getMessage(), ex);
    }

    static void showConnectionError(Component parent, StatusRuntimeException ex) {
        String msg = ex.getStatus().getDescription();
        if (msg == null) {
            msg = ex.getStatus().toString();
        }
        String displayMsg;
        if (ex.getCause() instanceof ConnectException || ex.getStatus().getCode() == io.grpc.Status.Code.UNAVAILABLE) {
            displayMsg = "Could not connect to server";
        } else {
            displayMsg = "Error: " + ex.getStatus().getCode() + " " + msg;
        }
        showError(parent, "Connection Error", displayMsg, ex);
    }

    static void showErrorMessage(Component parent, Exception ex, String title) {
        showError(parent, title, ex.getMessage(), ex);
    }

    private static void showError(Component parent, String title, String message, Throwable ex) {
        String safeMsg = message != null ? message : ex.getClass().getSimpleName();

        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));

        JLabel msgLabel = new JLabel("<html><b>" + escapeHtml(safeMsg) + "</b></html>");
        msgLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        panel.add(msgLabel);

        panel.add(Box.createVerticalStrut(10));

        StringWriter sw = new StringWriter();
        ex.printStackTrace(new PrintWriter(sw));
        JTextArea detailsArea = new JTextArea(sw.toString());
        detailsArea.setEditable(false);
        detailsArea.setTabSize(2);

        JScrollPane scrollPane = new JScrollPane(detailsArea);
        scrollPane.setPreferredSize(new Dimension(520, 180));
        scrollPane.setMinimumSize(new Dimension(520, 180));

        JButton toggleButton = new JButton("Show Details \u25BC");
        toggleButton.addActionListener((ActionEvent e) -> {
            boolean added = toggleButton.getClientProperty("detailsShown") == Boolean.TRUE;
            if (!added) {
                panel.add(scrollPane);
                toggleButton.putClientProperty("detailsShown", Boolean.TRUE);
                toggleButton.setText("Hide Details \u25B2");
            } else {
                panel.remove(scrollPane);
                toggleButton.putClientProperty("detailsShown", Boolean.FALSE);
                toggleButton.setText("Show Details \u25BC");
            }
            panel.revalidate();
            panel.repaint();
            Window w = javax.swing.SwingUtilities.getWindowAncestor(panel);
            if (w != null) {
                w.pack();
            }
        });
        toggleButton.setAlignmentX(Component.LEFT_ALIGNMENT);
        toggleButton.putClientProperty("detailsShown", Boolean.FALSE);

        panel.add(toggleButton);

        JOptionPane.showMessageDialog(parent, panel, title, JOptionPane.ERROR_MESSAGE);
    }

    private static String escapeHtml(String s) {
        return s.replace("&", "&amp;").replace("<", "&lt;").replace(">", "&gt;")
                .replace("\n", "<br>");
    }
}