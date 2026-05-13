package rax2;

import java.awt.Dimension;
import java.util.prefs.Preferences;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.GroupLayout;
import javax.swing.JPanel;

public class AddServerDialog extends javax.swing.JDialog {

    private Preferences propiedades;
    private JComboBox<String> hostComboBox;
    private JTextField portField;
    private boolean confirmed = false;

    public AddServerDialog(java.awt.Frame parent, Preferences propiedades, JComboBox<String> comboBox, JTextField portField) {
        super(parent, true);
        this.propiedades = propiedades;
        this.hostComboBox = comboBox;
        this.portField = portField;
        initComponents();

        jTextFieldPort.setText(portField.getText().isEmpty() ? "50051" : portField.getText());

        if (comboBox.getSelectedItem() != null && !comboBox.getSelectedItem().toString().isEmpty()) {
            String existingHost = comboBox.getSelectedItem().toString();
            jTextFieldHost.setText(existingHost);
            jTextFieldHost.setSelectionStart(0);
            jTextFieldHost.setSelectionEnd(existingHost.length());
            String user = propiedades.get("rpcUser" + existingHost, "");
            String pass = propiedades.get("rpcPass" + existingHost, "");
            if (!user.isEmpty()) {
                jTextFieldUser.setText(user);
            }
            if (!pass.isEmpty()) {
                jTextFieldPass.setText(pass);
            }
        }
    }

    public boolean isConfirmed() {
        return confirmed;
    }

    public String getHost() {
        return jTextFieldHost.getText().trim();
    }

    public String getPort() {
        return jTextFieldPort.getText().trim();
    }

    public String getUser() {
        return jTextFieldUser.getText().trim();
    }

    public String getPassword() {
        return new String(jTextFieldPass.getPassword());
    }

    private void initComponents() {
        jPanel1 = new JPanel();
        jLabel1 = new JLabel();
        jLabel2 = new JLabel();
        jLabel3 = new JLabel();
        jLabel4 = new JLabel();
        jTextFieldHost = new JTextField();
        jTextFieldPort = new JTextField();
        jTextFieldUser = new JTextField();
        jTextFieldPass = new JPasswordField();
        jButtonAdd = new JButton();
        jButtonCancel = new JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Add Server");
        setResizable(false);
        setName("Form");

        jPanel1.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        jPanel1.setName("jPanel1");

        jLabel1.setLabelFor(jTextFieldHost);
        jLabel1.setText("Host:");
        jLabel1.setDisplayedMnemonic(java.awt.event.KeyEvent.VK_H);

        jLabel2.setLabelFor(jTextFieldPort);
        jLabel2.setText("Port:");
        jLabel2.setDisplayedMnemonic(java.awt.event.KeyEvent.VK_O);

        jLabel3.setLabelFor(jTextFieldUser);
        jLabel3.setText("User:");
        jLabel3.setDisplayedMnemonic(java.awt.event.KeyEvent.VK_U);

        jLabel4.setLabelFor(jTextFieldPass);
        jLabel4.setText("Password:");
        jLabel4.setDisplayedMnemonic(java.awt.event.KeyEvent.VK_W);

        jTextFieldHost.setToolTipText("Server hostname or IP address");
        jTextFieldPort.setToolTipText("Server port number (default: 50051)");
        jTextFieldPort.setText("50051");
        jTextFieldUser.setToolTipText("RPC username for authentication");
        jTextFieldPass.setToolTipText("RPC password for authentication");

        jButtonAdd.setText("Add");
        jButtonAdd.setMnemonic(java.awt.event.KeyEvent.VK_A);
        jButtonAdd.setToolTipText("Save server configuration and add to list");
        jButtonAdd.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonAddActionPerformed(evt);
            }
        });

        jButtonCancel.setText("Cancel");
        jButtonCancel.setMnemonic(java.awt.event.KeyEvent.VK_C);
        jButtonCancel.setToolTipText("Discard changes and close");
        jButtonCancel.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonCancelActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel1)
                    .addComponent(jLabel2)
                    .addComponent(jLabel3)
                    .addComponent(jLabel4))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jTextFieldHost, javax.swing.GroupLayout.DEFAULT_SIZE, 220, Short.MAX_VALUE)
                    .addComponent(jTextFieldPort)
                    .addComponent(jTextFieldUser)
                    .addComponent(jTextFieldPass))
                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1)
                    .addComponent(jTextFieldHost, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel2)
                    .addComponent(jTextFieldPort, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel3)
                    .addComponent(jTextFieldUser, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel4)
                    .addComponent(jTextFieldPass, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addComponent(jButtonAdd)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jButtonCancel)))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jButtonAdd)
                    .addComponent(jButtonCancel))
                .addContainerGap())
        );

        pack();
    }

    private void jButtonAddActionPerformed(java.awt.event.ActionEvent evt) {
        String host = jTextFieldHost.getText().trim();
        String port = jTextFieldPort.getText().trim();
        if (host.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Host cannot be empty", "Validation Error", JOptionPane.WARNING_MESSAGE);
            return;
        }
        if (port.isEmpty()) {
            port = "50051";
            jTextFieldPort.setText(port);
        }
        try {
            Integer.parseInt(port);
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Invalid port number", "Validation Error", JOptionPane.WARNING_MESSAGE);
            return;
        }

        propiedades.put("rpcUser" + host, jTextFieldUser.getText().trim());
        propiedades.put("rpcPass" + host, new String(jTextFieldPass.getPassword()));

        boolean found = false;
        for (int i = 0; i < hostComboBox.getModel().getSize(); i++) {
            if (hostComboBox.getModel().getElementAt(i).equals(host)) {
                found = true;
                break;
            }
        }
        if (!found) {
            hostComboBox.addItem(host);
            int items = propiedades.getInt("items", 0);
            items++;
            propiedades.putInt("items", items);
            propiedades.put("item" + items, host);
        }

        hostComboBox.setSelectedItem(host);
        confirmed = true;
        dispose();
    }

    private void jButtonCancelActionPerformed(java.awt.event.ActionEvent evt) {
        dispose();
    }

    private JPanel jPanel1;
    private JButton jButtonAdd;
    private JButton jButtonCancel;
    private JLabel jLabel1;
    private JLabel jLabel2;
    private JLabel jLabel3;
    private JLabel jLabel4;
    private JTextField jTextFieldHost;
    private JTextField jTextFieldPort;
    private JTextField jTextFieldUser;
    private JPasswordField jTextFieldPass;
}