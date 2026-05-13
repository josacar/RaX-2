package rax2;

import io.grpc.StatusRuntimeException;
import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
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
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));

        JLabel msgLabel = new JLabel("<html><b>" + escapeHtml(message) + "</b></html>");
        msgLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        panel.add(msgLabel);

        panel.add(Box.createVerticalStrut(12));

        JTextArea detailsArea = new JTextArea();
        detailsArea.setEditable(false);
        detailsArea.setTabSize(2);
        StringWriter sw = new StringWriter();
        ex.printStackTrace(new PrintWriter(sw));
        detailsArea.setText(sw.toString());

        JScrollPane scrollPane = new JScrollPane(detailsArea);
        scrollPane.setPreferredSize(new Dimension(520, 180));
        scrollPane.setVisible(false);

        JButton toggleButton = new JButton("Show Details \u25BC");
        toggleButton.addActionListener((ActionEvent e) -> {
            boolean visible = !scrollPane.isVisible();
            scrollPane.setVisible(visible);
            toggleButton.setText(visible ? "Hide Details \u25B2" : "Show Details \u25BC");
            toggleButton.revalidate();
            toggleButton.repaint();
            // Force parent dialog to resize
            Component top = panel.getTopLevelAncestor();
            if (top instanceof javax.swing.JDialog) {
                ((javax.swing.JDialog) top).pack();
            } else if (top instanceof javax.swing.JFrame) {
                ((javax.swing.JFrame) top).pack();
            }
        });
        toggleButton.setAlignmentX(Component.LEFT_ALIGNMENT);
        toggleButton.setBackground(null);
        toggleButton.setBorderPainted(false);

        panel.add(toggleButton);
        panel.add(scrollPane);

        JOptionPane.showMessageDialog(parent, panel, title, JOptionPane.ERROR_MESSAGE);
    }

    private static String escapeHtml(String s) {
        return s.replace("&", "&amp;").replace("<", "&lt;").replace(">", "&gt;")
                .replace("\n", "<br>");
    }
}