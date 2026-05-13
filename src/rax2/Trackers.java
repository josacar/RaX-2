package rax2;

import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;
import io.grpc.StatusRuntimeException;
import rax2.proto.RssaniServiceGrpc;
import rax2.proto.EmptyRequest;
import rax2.proto.AuthListResponse;
import rax2.proto.AnadirAuthRequest;
import rax2.proto.BorrarAuthRequest;
import rax2.proto.BoolResponse;

public class Trackers extends javax.swing.JDialog {

    private RssaniServiceGrpc.RssaniServiceBlockingStub _stub;
    @SuppressWarnings("rawtypes")
    private DefaultTableModel model;

    public Trackers(java.awt.Frame parent, RssaniServiceGrpc.RssaniServiceBlockingStub stub) {
        super(parent, true);
        initComponents();
        tablaRegexp.getColumnModel().getColumn(1).setMinWidth(60);
        tablaRegexp.getColumnModel().getColumn(1).setMaxWidth(60);
        _stub = stub;
        try {
            EmptyRequest empty = EmptyRequest.getDefaultInstance();
            AuthListResponse result = _stub.listaAuths(empty);

            for (int i = 0; i < result.getEntriesCount(); ++i) {
                var entry = result.getEntries(i);
                TrackerAuth auth = new TrackerAuth(
                        entry.getTracker(),
                        entry.getUid(),
                        entry.getPass(),
                        entry.getPasskey());
                model.addRow(new Object[]{auth.getTracker(), auth.getUid(), auth.getPass(), auth.getPasskey()});
            }

        } catch (StatusRuntimeException ex) {
            GrpcErrorHandler.showErrorMessage(this, ex, "Error");
        }
    }

    @SuppressWarnings("unchecked")
    private void initComponents() {

        jScrollPane1 = new javax.swing.JScrollPane();
        tablaRegexp = new javax.swing.JTable();
        jButtonAnadir = new javax.swing.JButton();
        jButtonBorrar = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Trackers");
        setName("Form");

        jScrollPane1.setName("jScrollPane1");

        model=new DefaultTableModel(){
            public Class<?> getColumnClass(int columnIndex) {
                if (columnIndex==0){
                    return String.class;
                }else if (columnIndex==1){
                    return String.class;
                } else if (columnIndex==2){
                    return String.class;
                }
                return Object.class;
            }
            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return false;
            }
        };

        model.addColumn("Tracker");
        model.addColumn("UID");
        model.addColumn("Password");
        model.addColumn("Passkey");
        tablaRegexp.setModel(model);
        tablaRegexp.setAlignmentX(1.0F);
        tablaRegexp.setAlignmentY(1.0F);
        tablaRegexp.setAutoscrolls(false);
        tablaRegexp.setName("tablaRegexp");
        tablaRegexp.setToolTipText("Tracker credentials (right-click context menu for table options)");
        jScrollPane1.setViewportView(tablaRegexp);
        tablaRegexp.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        tablaRegexp.setShowVerticalLines(false);
        jScrollPane1.setViewportView(tablaRegexp);

        jButtonAnadir.setText("Add");
        jButtonAnadir.setToolTipText("Add a new tracker authentication entry");
        jButtonAnadir.setMnemonic(java.awt.event.KeyEvent.VK_A);
        jButtonAnadir.setName("jButtonAnadir");
        jButtonAnadir.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonAnadirActionPerformed(evt);
            }
        });

        jButtonBorrar.setText("Remove");
        jButtonBorrar.setToolTipText("Remove selected tracker authentication");
        jButtonBorrar.setMnemonic(java.awt.event.KeyEvent.VK_R);
        jButtonBorrar.setName("jButtonBorrar");
        jButtonBorrar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonBorrarActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 762, Short.MAX_VALUE)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jButtonAnadir)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jButtonBorrar)))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 213, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jButtonAnadir)
                    .addComponent(jButtonBorrar))
                .addContainerGap())
        );

        pack();
    }

    private void jButtonBorrarActionPerformed(java.awt.event.ActionEvent evt) {
        if (tablaRegexp.getSelectedRow() == -1) {
            return;
        }
        int si = JOptionPane.showConfirmDialog(this, "Are you sure you want to delete " + tablaRegexp.getModel().getValueAt(tablaRegexp.convertRowIndexToModel(tablaRegexp.getSelectedRow()), 0) + "?", "Confirm", javax.swing.JOptionPane.YES_NO_OPTION);
        if (si == 1) {
            return;
        }
        try {
            String tracker = tablaRegexp.getModel().getValueAt(tablaRegexp.convertRowIndexToModel(tablaRegexp.getSelectedRow()), 0).toString();
            BorrarAuthRequest request = BorrarAuthRequest.newBuilder()
                    .setTracker(tracker)
                    .build();
            _stub.borrarAuth(request);

            model.removeRow(tablaRegexp.convertRowIndexToModel(tablaRegexp.getSelectedRow()));

        } catch (StatusRuntimeException ex) {
            GrpcErrorHandler.showErrorMessage(this, ex, "Error");
        }
    }

    private void jButtonAnadirActionPerformed(java.awt.event.ActionEvent evt) {
        String tracker = JOptionPane.showInputDialog(this, "Enter the tracker URL", "http://");
        if (tracker == null || tracker.equals("") || tracker.equals("http://")) {
            return;
        }

        String uid = JOptionPane.showInputDialog(this, "Enter the UID");
        if (uid == null) {
            return;
        }

        String pass = JOptionPane.showInputDialog(this, "Enter the password hash");

        if (pass == null) {
            return;
        }

        String passkey = JOptionPane.showInputDialog(this, "Enter the passkey");

        if (passkey == null) {
            return;
        }
        try {
            AnadirAuthRequest request = AnadirAuthRequest.newBuilder()
                    .setTracker(tracker)
                    .setUid(uid)
                    .setPassword(pass)
                    .setPasskey(passkey)
                    .build();

            _stub.anadirAuth(request);

            model.addRow(new Object[]{tracker, uid, pass, passkey});

        } catch (StatusRuntimeException ex) {
            GrpcErrorHandler.showErrorMessage(this, ex, "Error");
        }
    }

    private javax.swing.JButton jButtonAnadir;
    private javax.swing.JButton jButtonBorrar;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTable tablaRegexp;
}