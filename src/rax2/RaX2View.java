/*
 * RaX2View.java
 */
package rax2;

import java.awt.AWTException;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Image;
import java.awt.MenuItem;
import java.awt.Point;
import java.awt.PopupMenu;
import java.awt.SystemTray;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.FileInputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.Vector;
import javax.swing.JOptionPane;
import org.apache.xmlrpc.XmlRpcException;
import org.apache.xmlrpc.client.XmlRpcClient;
import org.apache.xmlrpc.client.XmlRpcClientConfigImpl;
import java.util.prefs.Preferences;
import java.util.regex.PatternSyntaxException;
import javax.swing.DefaultCellEditor;
import javax.swing.JCheckBox;
import javax.swing.RowFilter;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;
import javax.swing.table.TableRowSorter;
import java.awt.TrayIcon;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import javax.swing.JFrame;
import javax.swing.JPopupMenu;
import javax.swing.JMenuItem;
import java.awt.Toolkit;
import java.awt.datatransfer.StringSelection;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.SwingUtilities;

/**
 * The application's main frame.
 */
public class RaX2View extends javax.swing.JFrame {

    XmlRpcClientConfigImpl config;
    XmlRpcClient client;
    Preferences propiedades;
    String FICHERO_CONFIGURACION = "Configuracion.properties";
    int items;
    String auxitem;
    DefaultTableModel model;
    String version = "0.7.1";
    Log log;
    Vector <String>trackers;
    TrayIcon trayIcon;

    class MyWindowListener implements WindowListener {

        @Override
        public void windowClosing(WindowEvent arg0) {
        }

        @Override
        public void windowOpened(WindowEvent arg0) {
        }

        @Override
        public void windowClosed(WindowEvent arg0) {
        }

        @Override
        public void windowIconified(WindowEvent arg0) {
            hideApplication();
        }

        @Override
        public void windowDeiconified(WindowEvent arg0) {
        }

        @Override
        public void windowActivated(WindowEvent arg0) {
        }

        @Override
        public void windowDeactivated(WindowEvent arg0) {
        }
    }

    class JPopupMenuShower extends MouseAdapter {

        private JPopupMenu popup;

        public JPopupMenuShower(JPopupMenu popup) {
            this.popup = popup;
        }

        private void showIfPopupTrigger(MouseEvent mouseEvent) {
            if (popup.isPopupTrigger(mouseEvent)) {
                int row = jTable1.convertRowIndexToModel(jTable1.rowAtPoint(new Point(mouseEvent.getX(), mouseEvent.getY())));
                jTable1.setRowSelectionInterval(row, row);
                popup.show(mouseEvent.getComponent(), mouseEvent.getX(), mouseEvent.getY());
            }
        }

        @Override
        public void mousePressed(MouseEvent mouseEvent) {
            showIfPopupTrigger(mouseEvent);
        }

        @Override
        public void mouseReleased(MouseEvent mouseEvent) {
            showIfPopupTrigger(mouseEvent);
        }
    }

    private void creaPopup() {
        JPopupMenu popupMenu = new JPopupMenu();

        JMenuItem copyMenuItem = new JMenuItem("Copiar",new javax.swing.ImageIcon(getClass().getResource("/rax2/resources/edit-copy.png")));
        copyMenuItem.addActionListener(new java.awt.event.ActionListener() {

            @Override
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                String regexp = (String) jTable1.getModel().getValueAt(jTable1.convertRowIndexToModel(jTable1.getSelectedRow()), 0);
                Toolkit.getDefaultToolkit().getSystemClipboard().setContents(new StringSelection(regexp), null);
            }
        });
        popupMenu.add(copyMenuItem);

        popupMenu.addSeparator();

        JMenuItem editMenuItem = new JMenuItem("Editar", new javax.swing.ImageIcon(getClass().getResource("/rax2/resources/edit.png")));
        editMenuItem.addActionListener(new java.awt.event.ActionListener() {

            @Override
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonEditActionPerformed(evt);
            }
        });
        popupMenu.add(editMenuItem);

        JMenuItem delMenuItem = new JMenuItem("Borrar", new javax.swing.ImageIcon(getClass().getResource("/rax2/resources/edit_remove.png")));
        delMenuItem.addActionListener(new java.awt.event.ActionListener() {

            @Override
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonRemoveActionPerformed(evt);
            }
        });
        popupMenu.add(delMenuItem);

        JMenuItem actdesMenuItem = new JMenuItem("Act/Des", new javax.swing.ImageIcon(getClass().getResource("/rax2/resources/actdes.png")));
        actdesMenuItem.addActionListener(new java.awt.event.ActionListener() {

            @Override
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonActDesActionPerformed(evt);
            }
        });
        popupMenu.add(actdesMenuItem);

        MouseListener mouseListener = new JPopupMenuShower(popupMenu);

        jTable1.addMouseListener(mouseListener);

    }

    public RaX2View() {
        super();
        this.setTitle(getTitle() + ' ' + version);
        this.setMinimumSize(new Dimension(600, 470));
        FileInputStream f = null;
        config = new XmlRpcClientConfigImpl();
        client = new XmlRpcClient();
        client.setConfig(config);
        propiedades = Preferences.userNodeForPackage(this.getClass());
        items = propiedades.getInt("items", 0);
        System.out.println("Items: " + items);
        System.out.println("lastItem: " + propiedades.get("lastItem", "ERROR"));
        initComponents();
        jTable1.getColumnModel().getColumn(5).setMaxWidth(40);
        jTable1.getColumnModel().getColumn(5).setMinWidth(40);
        jTable1.getColumnModel().getColumn(4).setMaxWidth(40);
        jTable1.getColumnModel().getColumn(4).setMinWidth(40);
        jTable1.getColumnModel().getColumn(2).setMaxWidth(40);
        jTable1.getColumnModel().getColumn(2).setMinWidth(40);
        jTable1.getColumnModel().getColumn(1).setMaxWidth(100);
        jTable1.getColumnModel().getColumn(1).setMinWidth(100);
        for (int i = 1; i <= items; i++) {
            jComboBoxIP.addItem(propiedades.get("item" + i, "ERROR"));
        }
        jComboBoxIP.setSelectedItem(propiedades.get("lastItem", ""));

        crearBandeja();
        this.addWindowListener(new MyWindowListener());
        this.addWindowListener(new MyWindowListener());

        creaPopup();
    }

    private void toggleVisible(){
      if (!this.isVisible()) {
          this.setVisible(true);
      } else {
          this.setVisible(false);
      }
    }

     private void hideApplication(){
       this.setVisible(false);
    }

    private void crearBandeja() {
        if (SystemTray.isSupported()) {
            ActionListener exitListener = new ActionListener() {

                @Override
                public void actionPerformed(ActionEvent e) {
                    System.out.println("Exiting...");
                    System.exit(0);
                }
            };

            ActionListener actionListenerB = new ActionListener() {

                @Override
                public void actionPerformed(ActionEvent e) {
                    // trayIcon.displayMessage("Action Event","An Action Event Has Been Performed!",TrayIcon.MessageType.INFO);
                }
            };

            MouseListener mouseListener = new MouseListener() {

                @Override
                public void mouseClicked(MouseEvent e) {
                    // System.out.println("Tray Icon - Mouse clicked!");
                   toggleVisible();
                }

                @Override
                public void mouseEntered(MouseEvent e) {
                    // System.out.println("Tray Icon - Mouse entered!");
                }

                @Override
                public void mouseExited(MouseEvent e) {
                    // System.out.println("Tray Icon - Mouse exited!");
                }

                @Override
                public void mousePressed(MouseEvent e) {
                    // System.out.println("Tray Icon - Mouse pressed!");
                }

                @Override
                public void mouseReleased(MouseEvent e) {
                    // System.out.println("Tray Icon - Mouse released!");
                }
            };

            SystemTray tray = SystemTray.getSystemTray();

            Image image = new javax.swing.ImageIcon(getClass().getResource("/rax2/resources/rax2.png")).getImage();

            PopupMenu popup = new PopupMenu();
            MenuItem defaultItem = new MenuItem("Exit");
            defaultItem.addActionListener(exitListener);
            popup.add(defaultItem);


            trayIcon = new TrayIcon(image, "RaX 2", popup);

            trayIcon.setImageAutoSize(true);
            trayIcon.addActionListener(actionListenerB);
            trayIcon.addMouseListener(mouseListener);

            try {
                tray.add(trayIcon);
            } catch (AWTException e) {
                System.err.println("TrayIcon could not be added.");
            }

        }
    }

    private void estadoBotones(boolean estado) {
        jButtonAdd.setEnabled(estado);
        jButtonDown.setEnabled(estado);
        jButtonKill.setEnabled(estado);
        jButtonLog.setEnabled(estado);
        jButtonSave.setEnabled(estado);
        jButtonTrackers.setEnabled(estado);
        jButtonUp.setEnabled(estado);
        jTextFieldFiltro.setEditable(estado);
    }

    private int Conectar() {
        Boolean mail = false;
        Boolean activa = false;
        Boolean exito = false;
        try {
            // Ponemos los parametros de configuracion
            config.setServerURL(new URL("http://" + jComboBoxIP.getSelectedItem().toString() + ":" + jTextFieldPort.getText() + "/RPC2"));
            config.setBasicUserName(propiedades.get("rpcUser" + jComboBoxIP.getSelectedItem().toString(), ""));
            config.setBasicPassword(propiedades.get("rpcPass" + jComboBoxIP.getSelectedItem().toString(), ""));
            Object[] params = new Object[0]; // Sin parametros de entrada

            HashMap struct = new HashMap(); // Estructura tipo Struct

            Object[] result = (Object[]) client.execute("rssani.listaExpresiones", params);

            // Borramos los datos existentes en la lista de regexps
            int filas = model.getRowCount();
            for (int i = 0; i < filas; ++i) {
                model.removeRow(0);
            }

            // Insertamos las regexps recibidas, haciendo casting
            for (int i = 0; i < result.length; ++i) {
                struct = (HashMap) result[i];
                mail = (Boolean) struct.get("mail");
                activa = (Boolean) struct.get("activa");
                model.addRow(new Object[]{struct.get("nombre"), struct.get("vencimiento"), mail, struct.get("tracker"),struct.get("dias"),activa});
            }

            // Obtenemos el timer y lo colocamos en la app
            Integer result2 = (Integer) client.execute("rssani.verTimer", params);
            jLabelTimer.setText(Integer.toString(result2 / 60000) + " min.");

            // Obtenemos el ultimo get del RSS
            String result3 = (String) client.execute("rssani.verUltimo", params);
            String valores[] = result3.split(" ");
            jLabelUltimoFecha.setText(valores[0]);
            jLabelUltimoHora.setText(valores[1]);

            // Obtenemos el item para ver si estaba en la lista
            String item = (String) jComboBoxIP.getEditor().getItem();
            boolean encontrado = false;

            // Miramos si el elemento estaba ya en la lista
            for (int i = 0; i < jComboBoxIP.getModel().getSize(); ++i) {
                if (item.equals(jComboBoxIP.getModel().getElementAt(i))) {
                    encontrado = true;
                    break;
                }
            }

            // Si no estaba lo añadimos al combo y lo guardamos
            if (!encontrado) {
                jComboBoxIP.addItem(jComboBoxIP.getEditor().getItem());
                items++;
                propiedades.putInt("items", items);
                propiedades.put("item" + items, jComboBoxIP.getEditor().getItem().toString());
            }

            // Guardamos el ultimo item conectado
            propiedades.put("lastItem", jComboBoxIP.getEditor().getItem().toString());

            Object[] result4 = (Object[]) client.execute("rssani.listaAuths", params);

            trackers = new Vector<String>();
            String tracker;

            for (int i = 0; i < result4.length; ++i) {
                struct = (HashMap) result4[i];
                tracker = (String) struct.get("tracker");
                if (!trackers.contains(tracker)) {
                    trackers.add(tracker);
                }
            }

            exito = true;
            estadoBotones(true);
        } catch (XmlRpcException ex) {
            if (ex.code == 0) {
                JOptionPane.showMessageDialog(this, "No se pudo conectar a RSSANI", "Error de conexión", JOptionPane.ERROR_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(this, "Error: " + ex.code + " " + ex, "ola?", JOptionPane.ERROR_MESSAGE);
            }
            Desconectar();
        } catch (MalformedURLException ex2) {
            JOptionPane.showMessageDialog(this, ex2, "ola?", JOptionPane.ERROR_MESSAGE);
        } finally {
            if (exito) {
                return 0;
            } else {
                return -1;
            }
        }
    }

    private void Desconectar() {
        int filas = model.getRowCount();
        for (int i = 0; i < filas; ++i) {
            model.removeRow(0);
        }
        jLabelTimer.setText("N/A");
        jLabelUltimoFecha.setText("N/A");
        jLabelUltimoHora.setText("N/A");
        config = new XmlRpcClientConfigImpl();
        client.setConfig(config);
        estadoBotones(false);
    }

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        mainPanel = new javax.swing.JPanel();
        jLabel2 = new javax.swing.JLabel();
        jTextFieldPort = new javax.swing.JTextField();
        jLabel3 = new javax.swing.JLabel();
        jComboBoxIP = new javax.swing.JComboBox();
        jButtonAdd = new javax.swing.JButton();
        jSeparator1 = new javax.swing.JSeparator();
        jButtonKill = new javax.swing.JButton();
        jLabelUltimoHora = new javax.swing.JLabel();
        jLabelUltimoFecha = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jButtonSave = new javax.swing.JButton();
        jPanel2 = new javax.swing.JPanel();
        jButtonDown = new javax.swing.JButton();
        jButtonUp = new javax.swing.JButton();
        jButtonLog = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTable1 = new javax.swing.JTable();
        jButtonOpciones = new javax.swing.JButton();
        jPanel1 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jLabelTimer = new javax.swing.JLabel();
        jToggleButtonConnect = new javax.swing.JToggleButton();
        jLabel5 = new javax.swing.JLabel();
        jTextFieldFiltro = new javax.swing.JTextField();
        jButtonBorrarHost = new javax.swing.JButton();
        jButtonTrackers = new javax.swing.JButton();

        mainPanel.setName("mainPanel"); // NOI18N

        jLabel2.setText("IP"); // NOI18N
        jLabel2.setName("jLabel2"); // NOI18N

        jTextFieldPort.setText(""); // NOI18N
        jTextFieldPort.setName("jTextFieldPort"); // NOI18N

        jLabel3.setText("Port"); // NOI18N
        jLabel3.setName("jLabel3"); // NOI18N

        jComboBoxIP.setEditable(true);
        jComboBoxIP.setName("jComboBoxIP"); // NOI18N
        jComboBoxIP.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jComboBoxIPActionPerformed(evt);
            }
        });

        jButtonAdd.setIcon(new javax.swing.ImageIcon(getClass().getResource("/rax2/resources/edit_add.png"))); // NOI18N
        jButtonAdd.setText("Add"); // NOI18N
        jButtonAdd.setEnabled(false);
        jButtonAdd.setMaximumSize(new java.awt.Dimension(77, 26));
        jButtonAdd.setMinimumSize(new java.awt.Dimension(77, 26));
        jButtonAdd.setName("jButtonAdd"); // NOI18N
        jButtonAdd.setPreferredSize(new java.awt.Dimension(77, 26));
        jButtonAdd.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonAddActionPerformed(evt);
            }
        });

        jSeparator1.setName("jSeparator1"); // NOI18N

        jButtonKill.setIcon(new javax.swing.ImageIcon(getClass().getResource("/rax2/resources/cancel.png"))); // NOI18N
        jButtonKill.setText("Kill"); // NOI18N
        jButtonKill.setEnabled(false);
        jButtonKill.setName("jButtonKill"); // NOI18N
        jButtonKill.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonKillActionPerformed(evt);
            }
        });

        jLabelUltimoHora.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabelUltimoHora.setText("N/A"); // NOI18N
        jLabelUltimoHora.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        jLabelUltimoHora.setName("jLabelUltimoHora"); // NOI18N

        jLabelUltimoFecha.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabelUltimoFecha.setText("N/A"); // NOI18N
        jLabelUltimoFecha.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        jLabelUltimoFecha.setName("jLabelUltimoFecha"); // NOI18N

        jLabel4.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel4.setText("Last update"); // NOI18N
        jLabel4.setName("jLabel4"); // NOI18N

        jButtonSave.setIcon(new javax.swing.ImageIcon(getClass().getResource("/rax2/resources/filesave.png"))); // NOI18N
        jButtonSave.setText("Save"); // NOI18N
        jButtonSave.setEnabled(false);
        jButtonSave.setName("jButtonSave"); // NOI18N
        jButtonSave.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonSaveActionPerformed(evt);
            }
        });

        jPanel2.setName("jPanel2"); // NOI18N
        jPanel2.setLayout(new java.awt.BorderLayout());

        jButtonDown.setIcon(new javax.swing.ImageIcon(getClass().getResource("/rax2/resources/1downarrow.png"))); // NOI18N
        jButtonDown.setText(""); // NOI18N
        jButtonDown.setAlignmentX(0.5F);
        jButtonDown.setEnabled(false);
        jButtonDown.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jButtonDown.setMaximumSize(new java.awt.Dimension(60, 22));
        jButtonDown.setMinimumSize(new java.awt.Dimension(60, 22));
        jButtonDown.setName("jButtonDown"); // NOI18N
        jButtonDown.setPreferredSize(new java.awt.Dimension(60, 22));
        jButtonDown.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonDownActionPerformed(evt);
            }
        });
        jPanel2.add(jButtonDown, java.awt.BorderLayout.WEST);

        jButtonUp.setIcon(new javax.swing.ImageIcon(getClass().getResource("/rax2/resources/1uparrow.png"))); // NOI18N
        jButtonUp.setText(""); // NOI18N
        jButtonUp.setAlignmentX(0.5F);
        jButtonUp.setEnabled(false);
        jButtonUp.setMaximumSize(new java.awt.Dimension(60, 26));
        jButtonUp.setMinimumSize(new java.awt.Dimension(60, 26));
        jButtonUp.setName("jButtonUp"); // NOI18N
        jButtonUp.setPreferredSize(new java.awt.Dimension(60, 22));
        jButtonUp.setRequestFocusEnabled(false);
        jButtonUp.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonUpActionPerformed(evt);
            }
        });
        jPanel2.add(jButtonUp, java.awt.BorderLayout.EAST);

        jButtonLog.setIcon(new javax.swing.ImageIcon(getClass().getResource("/rax2/resources/openterm.png"))); // NOI18N
        jButtonLog.setText("Log"); // NOI18N
        jButtonLog.setEnabled(false);
        jButtonLog.setName("jButtonLog"); // NOI18N
        jButtonLog.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonLogActionPerformed(evt);
            }
        });

        jScrollPane1.setName("jScrollPane1"); // NOI18N

        //model = new DefaultTableModel();
        model=new DefaultTableModel(){
            public Class getColumnClass(int columnIndex) {
                if (columnIndex==0){
                    return String.class;
                }else if (columnIndex==1){
                    return String.class;
                } else if (columnIndex==2){
                    return Boolean.class;
                } else if (columnIndex==3){
                    return String.class;
                } else if (columnIndex==4){
                    return Integer.class;
                } else if (columnIndex==5){
                    return Boolean.class;
                }
                return Object.class;
            }
            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return false;
            }
        };
        model.addColumn("Nombre");
        model.addColumn("Vencimiento");
        model.addColumn("Mail");
        model.addColumn("Tracker");
        model.addColumn("Dias");
        model.addColumn("Activa");
        jTable1.setModel(model);
        javax.swing.table.TableColumn var_col = jTable1.getColumnModel().getColumn(2);
        javax.swing.table.TableColumn var_col2 = jTable1.getColumnModel().getColumn(5);
        final JCheckBox check = new JCheckBox();
        var_col.setCellEditor(new DefaultCellEditor(check));
        var_col2.setCellEditor(new DefaultCellEditor(check));
        sorter = new TableRowSorter<TableModel>(jTable1.getModel());
        jTable1.setRowSorter(sorter);
        jTable1.setAlignmentX(1.0F);
        jTable1.setAlignmentY(1.0F);
        jTable1.setAutoscrolls(false);
        jTable1.setName("jTable1"); // NOI18N
        jTable1.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        jTable1.setShowVerticalLines(false);
        jScrollPane1.setViewportView(jTable1);

        jButtonOpciones.setIcon(new javax.swing.ImageIcon(getClass().getResource("/rax2/resources/configure.png"))); // NOI18N
        jButtonOpciones.setText("Settings"); // NOI18N
        jButtonOpciones.setName("jButtonOpciones"); // NOI18N
        jButtonOpciones.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonOpcionesActionPerformed(evt);
            }
        });

        jPanel1.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        jPanel1.setName("jPanel1"); // NOI18N
        jPanel1.setLayout(new java.awt.GridBagLayout());

        jLabel1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel1.setText("Timer:"); // NOI18N
        jLabel1.setName("jLabel1"); // NOI18N
        jPanel1.add(jLabel1, new java.awt.GridBagConstraints());

        jLabelTimer.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabelTimer.setText("N/A"); // NOI18N
        jLabelTimer.setName("jLabelTimer"); // NOI18N
        jLabelTimer.setPreferredSize(new java.awt.Dimension(72, 18));
        jPanel1.add(jLabelTimer, new java.awt.GridBagConstraints());

        jToggleButtonConnect.setIcon(new javax.swing.ImageIcon(getClass().getResource("/rax2/resources/connect_creating.png"))); // NOI18N
        jToggleButtonConnect.setText("Connect"); // NOI18N
        jToggleButtonConnect.setName("jToggleButtonConnect"); // NOI18N
        jToggleButtonConnect.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jToggleButtonConnectActionPerformed(evt);
            }
        });

        jLabel5.setText("Filter:"); // NOI18N
        jLabel5.setName("jLabel5"); // NOI18N

        jTextFieldFiltro.setEditable(false);
        jTextFieldFiltro.setName("jTextFieldFiltro"); // NOI18N

        jButtonBorrarHost.setIcon(new javax.swing.ImageIcon(getClass().getResource("/rax2/resources/edit_remove.png"))); // NOI18N
        jButtonBorrarHost.setText("Borrar"); // NOI18N
        jButtonBorrarHost.setName("jButtonBorrarHost"); // NOI18N
        jButtonBorrarHost.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonBorrarHostActionPerformed(evt);
            }
        });

        jButtonTrackers.setIcon(new javax.swing.ImageIcon(getClass().getResource("/rax2/resources/configure.png"))); // NOI18N
        jButtonTrackers.setText("Trackers"); // NOI18N
        jButtonTrackers.setEnabled(false);
        jButtonTrackers.setName("jButtonTrackers"); // NOI18N
        jButtonTrackers.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonTrackersActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout mainPanelLayout = new javax.swing.GroupLayout(mainPanel);
        mainPanel.setLayout(mainPanelLayout);
        mainPanelLayout.setHorizontalGroup(
            mainPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(mainPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(mainPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, mainPanelLayout.createSequentialGroup()
                        .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 500, Short.MAX_VALUE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(mainPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                            .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, 123, Short.MAX_VALUE)
                            .addComponent(jSeparator1, javax.swing.GroupLayout.DEFAULT_SIZE, 123, Short.MAX_VALUE)
                            .addComponent(jPanel1, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 123, Short.MAX_VALUE)
                            .addComponent(jButtonKill, javax.swing.GroupLayout.DEFAULT_SIZE, 123, Short.MAX_VALUE)
                            .addComponent(jLabel4, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 123, Short.MAX_VALUE)
                            .addComponent(jLabelUltimoFecha, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 123, Short.MAX_VALUE)
                            .addComponent(jLabelUltimoHora, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 123, Short.MAX_VALUE)
                            .addComponent(jButtonAdd, javax.swing.GroupLayout.DEFAULT_SIZE, 123, Short.MAX_VALUE)
                            .addComponent(jButtonSave, javax.swing.GroupLayout.DEFAULT_SIZE, 123, Short.MAX_VALUE)
                            .addComponent(jButtonOpciones, javax.swing.GroupLayout.DEFAULT_SIZE, 123, Short.MAX_VALUE)
                            .addComponent(jButtonTrackers, javax.swing.GroupLayout.DEFAULT_SIZE, 123, Short.MAX_VALUE)
                            .addComponent(jButtonLog, javax.swing.GroupLayout.DEFAULT_SIZE, 123, Short.MAX_VALUE)))
                    .addGroup(mainPanelLayout.createSequentialGroup()
                        .addComponent(jLabel2)
                        .addGap(10, 10, 10)
                        .addComponent(jComboBoxIP, javax.swing.GroupLayout.PREFERRED_SIZE, 147, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel3)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jTextFieldPort, javax.swing.GroupLayout.PREFERRED_SIZE, 45, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jToggleButtonConnect)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jButtonBorrarHost, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(26, 26, 26)
                        .addComponent(jLabel5)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jTextFieldFiltro, javax.swing.GroupLayout.DEFAULT_SIZE, 119, Short.MAX_VALUE)))
                .addContainerGap())
        );
        mainPanelLayout.setVerticalGroup(
            mainPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(mainPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(mainPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(mainPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jTextFieldPort, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jToggleButtonConnect)
                        .addComponent(jComboBoxIP, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jLabel3)
                        .addComponent(jLabel2))
                    .addComponent(jButtonBorrarHost, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(mainPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jTextFieldFiltro, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jLabel5)))
                .addGroup(mainPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(mainPanelLayout.createSequentialGroup()
                        .addGap(21, 21, 21)
                        .addComponent(jButtonAdd, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jButtonSave, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jButtonOpciones, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jButtonTrackers, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jButtonLog, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(63, 63, 63)
                        .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jSeparator1, javax.swing.GroupLayout.PREFERRED_SIZE, 2, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel4, javax.swing.GroupLayout.PREFERRED_SIZE, 14, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabelUltimoFecha, javax.swing.GroupLayout.PREFERRED_SIZE, 18, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabelUltimoHora, javax.swing.GroupLayout.PREFERRED_SIZE, 18, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 12, Short.MAX_VALUE)
                        .addComponent(jButtonKill, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(mainPanelLayout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 378, Short.MAX_VALUE)))
                .addContainerGap())
        );

        jTextFieldFiltro.addKeyListener(new KeyListener() {

            public void keyPressed(KeyEvent e) {
            }

            public void keyReleased(KeyEvent e) {
                String text = jTextFieldFiltro.getText();
                if (text.length() == 0) {
                    sorter.setRowFilter(null);
                } else {
                    try {
                        sorter.setRowFilter(
                            RowFilter.regexFilter("(?i)" + text));
                    } catch (PatternSyntaxException pse) {
                        System.err.println("Bad regex pattern");
                    }
                }
            }

            public void keyTyped(KeyEvent e) {
            }
        });

        getContentPane().add(mainPanel);
        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jButtonKillActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonKillActionPerformed
        Boolean result = false;
        // Vemos si existe la configuracion
        if (config.getServerURL() == null) {
            return;
        }

        // Preguntamos como tito bill
        int si = JOptionPane.showConfirmDialog(this, "¿Esta seguro de matar rssani?", "Pregunta", javax.swing.JOptionPane.YES_NO_OPTION);
        if (si == 1) {
            return;
        }

        // Hacemos la llamada xmlrpc
        try {
            Object[] params = new Object[0];
            result = (Boolean) client.execute("rssani.shutdown", params);

            if (!result) {
                JOptionPane.showMessageDialog(this, "No se pudo detener", "Error", JOptionPane.WARNING_MESSAGE);
            }
        } catch (XmlRpcException ex) {
            JOptionPane.showMessageDialog(this, ex, "ola2?", JOptionPane.ERROR_MESSAGE);
        } finally {
            if (result) {
                jToggleButtonConnect.setSelected(false);
                Desconectar();
            }
        }
    }//GEN-LAST:event_jButtonKillActionPerformed

    private void jButtonSaveActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonSaveActionPerformed
        if (config.getServerURL() == null) {
            return;
        }

        try {
            Object[] params = new Object[0];
            Boolean result = (Boolean) client.execute("rssani.guardar", params);

            if (!result) {
                JOptionPane.showMessageDialog(this, "No se pudo guardar", "Error", JOptionPane.WARNING_MESSAGE);
            }
        } catch (XmlRpcException ex) {
            JOptionPane.showMessageDialog(this, ex, "ola?", JOptionPane.ERROR_MESSAGE);
        }
    }//GEN-LAST:event_jButtonSaveActionPerformed

    private void jButtonEditActionPerformed(java.awt.event.ActionEvent evt) {
        // Vemos si existe la configuracion
        if (config.getServerURL() == null) {
            return;
        }

        if (jTable1.getSelectedRow() == -1) {
            return;
        }

        // Cojo la 1 columna
        String cadOrig = (String) jTable1.getModel().getValueAt(jTable1.convertRowIndexToModel(jTable1.getSelectedRow()), 0);
        int filaOrig = -1;
        filaOrig = jTable1.convertRowIndexToModel(jTable1.getSelectedRow());
        if (filaOrig == -1) {
            return;
        }

        // Dialogo de edicion de la regexp
        String cadDest = JOptionPane.showInputDialog("Introduce la nueva RegExp", cadOrig);
        if (cadDest == null || cadDest.equals("")) {
            return;
        }

        // Hacemos la llamada xmlrpc con las dos cadenas
        try {
            Object[] params = new Object[]{filaOrig, cadDest};

            Boolean result = (Boolean) client.execute("rssani.editarRegexpI", params);

            if (result) {
                JOptionPane.showMessageDialog(this, "No se pudo cambiar", "Error", JOptionPane.WARNING_MESSAGE);
            } else {
                Conectar(); // Refrescamos desde el servidor
            }

        } catch (XmlRpcException ex) {
            JOptionPane.showMessageDialog(this, ex, "ola?", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void jButtonActDesActionPerformed(java.awt.event.ActionEvent evt) {
        // Vemos si existe la configuracion
        if (config.getServerURL() == null) {
            return;
        }

        if (jTable1.getSelectedRow() == -1) {
            return;
        }

        // Cojo la 1 columna
        int filaOrig = -1;
        filaOrig = jTable1.convertRowIndexToModel(jTable1.getSelectedRow());
        if (filaOrig == -1) {
            return;
        }

        // Hacemos la llamada xmlrpc con las dos cadenas
        try {
            Object[] params = new Object[]{filaOrig};

            Boolean result = (Boolean) client.execute("rssani.activarRegexp", params);

            if (result) {
                JOptionPane.showMessageDialog(this, "No se pudo cambiar", "Error", JOptionPane.WARNING_MESSAGE);
            } else {
                Conectar(); // Refrescamos desde el servidor
            }

        } catch (XmlRpcException ex) {
            JOptionPane.showMessageDialog(this, ex, "ola?", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void jButtonAddActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonAddActionPerformed
        // Vemos si existe la configuracion
        if (config.getServerURL() == null) {
            return;
        }

        // Dialogo para la nueva regexp
        String cadena = JOptionPane.showInputDialog("Introduce la RegExp a añadir");
        if (cadena == null || cadena.equals("")) {
            return;
        }

        // Dialogo para la fecha
        String vencimiento = JOptionPane.showInputDialog("Introduce el vencimiento (dd-mm-yyyy)", "");
        if (vencimiento == null) {
            return;
        }

        // Dialogo para solo mail
        int mail = JOptionPane.showConfirmDialog(this, "¿Solo mail?", "Pregunta", javax.swing.JOptionPane.YES_NO_OPTION);

        boolean bmail = mail == 0 ? true : false;

        // Dialogo para el tracker
        String tracker = (String) JOptionPane.showInputDialog(this, "Introduce el tracker (solo el host)", "titulo", JOptionPane.DEFAULT_OPTION, null, trackers.toArray(), null);
        if (tracker == null) {
            return;
        }

        // Dialogo para solo mail
        String dias = JOptionPane.showInputDialog("Numero de dias entre descarga?",0);
        if (dias == null) {
            return;
        }

        int ndias = Integer.parseInt(dias);

        // Creamos la xmlrpc con los tres parametros
        try {
            Object[] params = new Object[]{cadena, vencimiento, bmail, tracker,ndias};

            Boolean result = (Boolean) client.execute("rssani.anadirRegexp", params);

            if (result) {
                Conectar(); // Refrescamos

            }

        } catch (XmlRpcException ex) {
            JOptionPane.showMessageDialog(this, ex, "ola?", JOptionPane.ERROR_MESSAGE);
        }
    }//GEN-LAST:event_jButtonAddActionPerformed

    private void jButtonUpActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonUpActionPerformed
        int orig = jTable1.convertRowIndexToModel(jTable1.getSelectedRow());
        if (orig <= 0) {
            return; // Si esta la primera abortamos
        }

        try { // Lo movemos a una posicion menos
            Object[] params = new Object[]{orig, orig - 1};

            Boolean result = (Boolean) client.execute("rssani.moverRegexp", params);

            if (!result) {
                JOptionPane.showMessageDialog(this, "No se pudo cambiar", "Error", JOptionPane.WARNING_MESSAGE);
            } else {
                Conectar();
            }

        } catch (XmlRpcException ex) {
            JOptionPane.showMessageDialog(this, ex, "ola?", JOptionPane.ERROR_MESSAGE);
        }
    }//GEN-LAST:event_jButtonUpActionPerformed

    private void jButtonDownActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonDownActionPerformed
        int orig = jTable1.convertRowIndexToModel(jTable1.getSelectedRow());
        if (orig >= jTable1.getModel().getRowCount() - 1 || orig < 0) {
            return; // Si esta al final abortamos
        }

        try { // Lo movemos a una posicion mas
            Object[] params = new Object[]{orig, orig + 1};

            Boolean result = (Boolean) client.execute("rssani.moverRegexp", params);

            if (!result) {
                JOptionPane.showMessageDialog(this, "No se pudo cambiar", "Error", JOptionPane.WARNING_MESSAGE);
            } else {
                Conectar();
            }

        } catch (XmlRpcException ex) {
            JOptionPane.showMessageDialog(this, ex, "ola?", JOptionPane.ERROR_MESSAGE);
        }
    }//GEN-LAST:event_jButtonDownActionPerformed

    private void jButtonLogActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonLogActionPerformed
        if (config.getServerURL() == null) {
            return;
        }

        log = new Log(client);
        log.setLocationRelativeTo(this);
        log.setVisible(true);
    }//GEN-LAST:event_jButtonLogActionPerformed

    private void jButtonRemoveActionPerformed(java.awt.event.ActionEvent evt) {
        if (config.getServerURL() == null) {
            return;
        }

        if (jTable1.getSelectedRow() == -1) {
            return;
        }

        // Pregunto antes
        int si = JOptionPane.showConfirmDialog(this, "¿Esta seguro de eliminar " + jTable1.getModel().getValueAt(jTable1.convertRowIndexToModel(jTable1.getSelectedRow()), 0) + "?", "Pregunta", javax.swing.JOptionPane.YES_NO_OPTION);
        if (si == 1) {
            return;
        }
        try {
            // Borro la regexp por el nombre
            Object[] params = new Object[]{jTable1.getModel().getValueAt(jTable1.convertRowIndexToModel(jTable1.getSelectedRow()), 0)};
            Boolean result = (Boolean) client.execute("rssani.borrarRegexpS", params);

            if (result) {
                Conectar();
            }

        } catch (XmlRpcException ex) {
            JOptionPane.showMessageDialog(this, ex, "ola?", JOptionPane.ERROR_MESSAGE);
        }
    }

private void jButtonOpcionesActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonOpcionesActionPerformed
    java.awt.EventQueue.invokeLater(new Runnable() {

        @Override
        synchronized public void run() {
            // Creo el dialogo de opciones con sus cosas
            Opciones opc = new Opciones(propiedades, client, jComboBoxIP.getSelectedItem().toString());
            opc.setLocationRelativeTo(null);
            opc.setVisible(true);

        }
    });
}//GEN-LAST:event_jButtonOpcionesActionPerformed

private void jToggleButtonConnectActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jToggleButtonConnectActionPerformed
    if (jToggleButtonConnect.isSelected()) {
        if (Conectar() == 0) {
            jComboBoxIP.setEnabled(false);
        } else {
            jToggleButtonConnect.setSelected(false);
        }
    } else {
        jComboBoxIP.setEnabled(true);
        Desconectar();
    }
}//GEN-LAST:event_jToggleButtonConnectActionPerformed

private void jButtonBorrarHostActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonBorrarHostActionPerformed
    // Eliminamos la ip con su config
    int si = JOptionPane.showConfirmDialog(this, "¿Esta seguro de eliminar " + jComboBoxIP.getSelectedItem() + "?", "Pregunta", javax.swing.JOptionPane.YES_NO_OPTION);
    if (si == 1) {
        return;
    }

    String host = (String) jComboBoxIP.getSelectedItem();

    // Borramos la CFG y el item del combo
    if (host != null) {
        propiedades.remove("rpcUser" + host);
        propiedades.remove("rpcPass" + host);
        jComboBoxIP.removeItem(jComboBoxIP.getSelectedItem());
    }

    boolean cent = false;

    for (int i = 1; i <= items; i++) { // Recorro los items
        if (propiedades.get("item" + i, "ERROR").equals(host)) {
            propiedades.remove("item" + i); // Quito el item
            cent = true;
            continue;
        }
        if (cent) { // Muevo los items siguientes a una posicion menos
            propiedades.put("item" + (i - 1), propiedades.get("item" + i, "ERROR"));
            propiedades.remove("item" + i);
        }
    }
    propiedades.put("items", Integer.toString(jComboBoxIP.getModel().getSize()));
    items = jComboBoxIP.getModel().getSize();
}//GEN-LAST:event_jButtonBorrarHostActionPerformed

private void jButtonTrackersActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonTrackersActionPerformed
    Trackers trk = new Trackers(client);
    trk.setLocationRelativeTo(this);
    trk.setVisible(true);
}//GEN-LAST:event_jButtonTrackersActionPerformed

    private void jComboBoxIPActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jComboBoxIPActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jComboBoxIPActionPerformed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButtonAdd;
    private javax.swing.JButton jButtonBorrarHost;
    private javax.swing.JButton jButtonDown;
    private javax.swing.JButton jButtonKill;
    private javax.swing.JButton jButtonLog;
    private javax.swing.JButton jButtonOpciones;
    private javax.swing.JButton jButtonSave;
    private javax.swing.JButton jButtonTrackers;
    private javax.swing.JButton jButtonUp;
    private javax.swing.JComboBox jComboBoxIP;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabelTimer;
    private javax.swing.JLabel jLabelUltimoFecha;
    private javax.swing.JLabel jLabelUltimoHora;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JSeparator jSeparator1;
    private javax.swing.JTable jTable1;
    private TableRowSorter<TableModel> sorter;
    private javax.swing.JTextField jTextFieldFiltro;
    private javax.swing.JTextField jTextFieldPort;
    private javax.swing.JToggleButton jToggleButtonConnect;
    private javax.swing.JPanel mainPanel;
    // End of variables declaration//GEN-END:variables
}
