package rax2;

import java.util.prefs.Preferences;
import javax.swing.JOptionPane;
import javax.swing.JPasswordField;
import io.grpc.StatusRuntimeException;
import rax2.proto.RssaniServiceGrpc;
import rax2.proto.EmptyRequest;
import rax2.proto.OpcionesResponse;
import rax2.proto.PonerOpcionesRequest;
import rax2.proto.PonerCredencialesRequest;
import rax2.proto.BoolResponse;

public class Opciones extends javax.swing.JDialog {

    private RssaniServiceGrpc.RssaniServiceBlockingStub stub;
    private Preferences preferences;
    private String host;

    public Opciones(java.awt.Frame parent, Preferences opciones, RssaniServiceGrpc.RssaniServiceBlockingStub stub, String host) {
        super(parent, true);
        initComponents();
        this.stub = stub;
        preferences = opciones;
        this.host = host;
        iniciaValores();
    }

    private void iniciaValores() {
        if (stub != null) {
            try {
                EmptyRequest empty = EmptyRequest.getDefaultInstance();
                OpcionesResponse options = stub.verOpciones(empty);

                jTextFieldMailFrom.setText(options.getFromMail());
                jTextFieldMailTo.setText(options.getToMail());
                jTextFieldRuta.setText(options.getPath());
            } catch (StatusRuntimeException ex) {
                GrpcErrorHandler.showErrorMessage(this, ex, "Error");
            }
        }
        String user = preferences.get("rpcUser" + host, "");
        if (!user.equals("")) {
            jTextFieldRpcUser.setText(user);
        }
        String pass = preferences.get("rpcPass" + host, "");
        if (!pass.equals("")) {
            jTextFieldRpcPass.setText(pass);
        }
    }

    @SuppressWarnings("unchecked")
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jTextFieldRpcUser = new javax.swing.JTextField();
        jLabel2 = new javax.swing.JLabel();
        jTextFieldRpcPass = new JPasswordField();
        jButtonModificar = new javax.swing.JButton();
        jButtonSaveAuth = new javax.swing.JButton();
        jButtonCancelar = new javax.swing.JButton();
        jButtonAceptar = new javax.swing.JButton();
        jPanel2 = new javax.swing.JPanel();
        jLabel5 = new javax.swing.JLabel();
        jTextFieldRuta = new javax.swing.JTextField();
        jTextFieldMailFrom = new javax.swing.JTextField();
        jLabel6 = new javax.swing.JLabel();
        jTextFieldMailTo = new javax.swing.JTextField();
        jLabel7 = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Options");
        setName("Form");

        jPanel1.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        jPanel1.setName("jPanel1");

        jLabel1.setLabelFor(jTextFieldRpcUser);
        jLabel1.setText("User");
        jLabel1.setDisplayedMnemonic(java.awt.event.KeyEvent.VK_U);
        jLabel1.setName("jLabel1");

        jTextFieldRpcUser.setText("rssani-rpc");
        jTextFieldRpcUser.setToolTipText("RPC username for the rssani server");
        jTextFieldRpcUser.setName("jTextFieldRpcUser");

        jLabel2.setLabelFor(jTextFieldRpcPass);
        jLabel2.setText("Password");
        jLabel2.setDisplayedMnemonic(java.awt.event.KeyEvent.VK_P);
        jLabel2.setName("jLabel2");

        jTextFieldRpcPass.setText("rssanipass-rpc");
        jTextFieldRpcPass.setToolTipText("RPC password for the rssani server");
        jTextFieldRpcPass.setName("jTextFieldRpcPass");

        jButtonModificar.setText("Update credentials");
        jButtonModificar.setToolTipText("Update credentials on the remote server");
        jButtonModificar.setMnemonic(java.awt.event.KeyEvent.VK_D);
        jButtonModificar.setName("jButtonModificar");
        jButtonModificar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonModificarActionPerformed(evt);
            }
        });

        jButtonSaveAuth.setText("Save credentials");
        jButtonSaveAuth.setToolTipText("Save credentials locally for auto-connect");
        jButtonSaveAuth.setMnemonic(java.awt.event.KeyEvent.VK_S);
        jButtonSaveAuth.setName("jButtonSaveAuth");
        jButtonSaveAuth.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonSaveAuthActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel2)
                            .addComponent(jLabel1))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jTextFieldRpcUser, javax.swing.GroupLayout.DEFAULT_SIZE, 223, Short.MAX_VALUE)
                            .addComponent(jTextFieldRpcPass, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 223, Short.MAX_VALUE)))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                        .addComponent(jButtonSaveAuth)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 39, Short.MAX_VALUE)
                        .addComponent(jButtonModificar)))
                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1)
                    .addComponent(jTextFieldRpcUser, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel2)
                    .addComponent(jTextFieldRpcPass, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 25, Short.MAX_VALUE)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jButtonModificar)
                    .addComponent(jButtonSaveAuth))
                .addContainerGap())
        );

        jButtonCancelar.setText("Cancel");
        jButtonCancelar.setToolTipText("Discard changes and close");
        jButtonCancelar.setMnemonic(java.awt.event.KeyEvent.VK_C);
        jButtonCancelar.setName("jButtonCancelar");
        jButtonCancelar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonCancelarActionPerformed(evt);
            }
        });

        jButtonAceptar.setText("Accept");
        jButtonAceptar.setToolTipText("Apply server options and close");
        jButtonAceptar.setMnemonic(java.awt.event.KeyEvent.VK_O);
        jButtonAceptar.setName("jButtonAceptar");
        jButtonAceptar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonAceptarActionPerformed(evt);
            }
        });

        jPanel2.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        jPanel2.setName("jPanel2");

        jLabel5.setLabelFor(jTextFieldRuta);
        jLabel5.setText("Torrents path:");
        jLabel5.setDisplayedMnemonic(java.awt.event.KeyEvent.VK_T);
        jLabel5.setName("jLabel5");

        jTextFieldRuta.setText("");
        jTextFieldRuta.setToolTipText("Path where torrent files are saved");
        jTextFieldRuta.setName("jTextFieldRuta");

        jTextFieldMailFrom.setText("");
        jTextFieldMailFrom.setToolTipText("Sender email address");
        jTextFieldMailFrom.setName("jTextFieldMailFrom");

        jLabel6.setLabelFor(jTextFieldMailFrom);
        jLabel6.setText("Mail from:");
        jLabel6.setDisplayedMnemonic(java.awt.event.KeyEvent.VK_F);
        jLabel6.setName("jLabel6");

        jTextFieldMailTo.setText("");
        jTextFieldMailTo.setToolTipText("Recipient email address");
        jTextFieldMailTo.setName("jTextFieldMailTo");

        jLabel7.setLabelFor(jTextFieldMailTo);
        jLabel7.setText("Mail to");
        jLabel7.setDisplayedMnemonic(java.awt.event.KeyEvent.VK_M);
        jLabel7.setName("jLabel7");

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel5)
                    .addComponent(jLabel6)
                    .addComponent(jLabel7))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jTextFieldMailTo, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 201, Short.MAX_VALUE)
                    .addComponent(jTextFieldMailFrom, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 201, Short.MAX_VALUE)
                    .addComponent(jTextFieldRuta, javax.swing.GroupLayout.DEFAULT_SIZE, 201, Short.MAX_VALUE))
                .addGap(26, 26, 26))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jTextFieldRuta, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel5))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jTextFieldMailFrom, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel6))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jTextFieldMailTo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel7))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addComponent(jButtonAceptar)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jButtonCancelar)))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 11, Short.MAX_VALUE)
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jButtonCancelar)
                    .addComponent(jButtonAceptar))
                .addContainerGap())
        );

        pack();
    }

private void jButtonSaveAuthActionPerformed(java.awt.event.ActionEvent evt) {
    preferences.put("rpcUser" + host, jTextFieldRpcUser.getText());
    preferences.put("rpcPass" + host, new String(jTextFieldRpcPass.getPassword()));
}

private void jButtonCancelarActionPerformed(java.awt.event.ActionEvent evt) {
    this.dispose();
}

private void jButtonAceptarActionPerformed(java.awt.event.ActionEvent evt) {
    try {
        PonerOpcionesRequest request = PonerOpcionesRequest.newBuilder()
                .setFromMail(jTextFieldMailFrom.getText())
                .setToMail(jTextFieldMailTo.getText())
                .setPath(jTextFieldRuta.getText())
                .build();

        Boolean result = stub.ponerOpciones(request).getValue();

        if (!result) {
            JOptionPane.showMessageDialog(this, "Could not apply changes", "Error", JOptionPane.WARNING_MESSAGE);
        } else {
            JOptionPane.showMessageDialog(this, "Options updated successfully", "Success", JOptionPane.INFORMATION_MESSAGE);
            jButtonSaveAuthActionPerformed(null);
        }
    } catch (StatusRuntimeException ex) {
        GrpcErrorHandler.showErrorMessage(this, ex, "Error");
    }
    this.dispose();
}

private void jButtonModificarActionPerformed(java.awt.event.ActionEvent evt) {
    try {
        PonerCredencialesRequest request = PonerCredencialesRequest.newBuilder()
                .setUser(jTextFieldRpcUser.getText())
                .setPassword(new String(jTextFieldRpcPass.getPassword()))
                .build();

        Boolean result = stub.ponerCredenciales(request).getValue();

        if (!result) {
            JOptionPane.showMessageDialog(this, "Could not apply changes", "Error", JOptionPane.WARNING_MESSAGE);
        } else {
            JOptionPane.showMessageDialog(this, "Credentials updated successfully", "Success", JOptionPane.INFORMATION_MESSAGE);
            jButtonSaveAuthActionPerformed(null);
        }

    } catch (StatusRuntimeException ex) {
        GrpcErrorHandler.showErrorMessage(this, ex, "Error");
    }
}
    private javax.swing.JButton jButtonAceptar;
    private javax.swing.JButton jButtonCancelar;
    private javax.swing.JButton jButtonModificar;
    private javax.swing.JButton jButtonSaveAuth;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JTextField jTextFieldMailFrom;
    private javax.swing.JTextField jTextFieldMailTo;
    private JPasswordField jTextFieldRpcPass;
    private javax.swing.JTextField jTextFieldRpcUser;
    private javax.swing.JTextField jTextFieldRuta;
}