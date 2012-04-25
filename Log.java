/*
 * Log.java
 *
 * Created on 12 de abril de 2008, 14:03
 */
package rax2;

import java.awt.Component;
import java.awt.Point;
import java.awt.event.AdjustmentEvent;
import java.awt.event.AdjustmentListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.PatternSyntaxException;
import javax.swing.JOptionPane;
import javax.swing.JScrollBar;
import javax.swing.JTable;
import javax.swing.JScrollPane;
import javax.swing.RowFilter;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;
import javax.swing.table.TableRowSorter;
import org.apache.xmlrpc.XmlRpcException;
import org.apache.xmlrpc.client.XmlRpcClient;

/**
 *
 * @author  Usuario
 */
class DateRenderer extends DefaultTableCellRenderer {
    /* Clase que renderiza la fecha en la tabla del log */

    @Override
    public Component getTableCellRendererComponent(
            JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
        super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);

        Date fecha = (Date) value;
        NumberFormat nf = NumberFormat.getInstance();
        nf.setMinimumIntegerDigits(2);
        GregorianCalendar calendar = new GregorianCalendar();
        calendar.setTime(fecha);

        setText(nf.format(calendar.get(Calendar.DAY_OF_MONTH)) + "/" + nf.format(calendar.get(Calendar.MONTH) + 1) + " " + nf.format(calendar.get(Calendar.HOUR_OF_DAY)) + ":" + nf.format(calendar.get(Calendar.MINUTE)));
        return this;
    }
}

public class Log extends javax.swing.JFrame {

    class MyAdjustmentListener implements AdjustmentListener {
        // Clase que imlementa el cambio de la scrollbar vertical

        JScrollBar verticalScrollBar;

        MyAdjustmentListener(JScrollBar v) {
            verticalScrollBar = v;
        }

        @Override
        public void adjustmentValueChanged(AdjustmentEvent e) {
            String aux;
            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss"); //Las M en mayúsculas o interpretará minutos!!

            DefaultTableModel model = (DefaultTableModel) getJTable1().getModel();
            // Solo cuando sea el maximo de la scroll o maximicemos
            if (e.getValue() == (verticalScrollBar.getMaximum() - verticalScrollBar.getVisibleAmount()) || verticalScrollBar.getMaximum() == verticalScrollBar.getVisibleAmount()) {
                if (max == e.getValue() && verticalScrollBar.getMaximum() != verticalScrollBar.getVisibleAmount()) {
                    return; // Para que volver a llamar
                }
                try { // Llamada al RPC y añadimos al final
                    Object[] params = new Object[]{offset, offset + 20};
                    Object[] result = (Object[]) _client.execute("rssani.verLog", params);
                    for (int i = 0; i < result.length; ++i) {
                        aux = (String) result[i];
                        java.util.Date fecha = sdf.parse(aux.split("[|]")[0].substring(2));
                        model.addRow(new Object[]{fecha, aux.split("[|]")[1]});
                    }
                    offset += 20;
                    max = e.getValue();
                } catch (ParseException ex) {
                    Logger.getLogger(RaX2View.class.getName()).log(Level.SEVERE, null, ex);
                } catch (XmlRpcException ex) {
                    JOptionPane.showMessageDialog(null, ex, "ola?", JOptionPane.ERROR_MESSAGE);
                }
            }
        }
    }
    JScrollBar verticalScrollBar;
    JScrollBar horizontalScrollBar;
    private XmlRpcClient _client;
    int offset = 20;
    int max = 0;

    /** Creates new form Log */
    public Log(XmlRpcClient client) {
        initComponents();
        _client = client;
        verticalScrollBar = jScrollPaneLog.getVerticalScrollBar();
        horizontalScrollBar = jScrollPaneLog.getHorizontalScrollBar();
        jTable1.getColumnModel().getColumn(0).setMinWidth(90);
        jTable1.getColumnModel().getColumn(0).setMaxWidth(90);

        try {
            Object[] params = new Object[]{0, 20};
            final Object[] result = (Object[]) client.execute("rssani.verLog", params);
            java.awt.EventQueue.invokeLater(new Runnable() {

                DefaultTableModel model;
                java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("dd/MM/yyyy HH:mm:ss"); //Las M en mayúsculas o interpretará minutos!!

                @Override
                synchronized public void run() {

                    verticalScrollBar.addAdjustmentListener(new MyAdjustmentListener(verticalScrollBar));
                    model = (DefaultTableModel) jTable1.getModel();
                    String aux;

                    for (int i = 0; i < result.length; ++i) {
                        try {
                            //log.getJTextAreaLog().setCaretPosition(0);
                            aux = (String) result[i];
                            java.util.Date fecha = sdf.parse(aux.split("[|]")[0].substring(2));
                            model.addRow(new Object[]{fecha, aux.split("[|]")[1]});
                        } catch (ParseException ex) {
                            Logger.getLogger(RaX2View.class.getName()).log(Level.SEVERE, null, ex);
                        }
                    }
                    // Inicializamos los valores
                    offset = 20;
                    max = 0;
                }
            });

        } catch (XmlRpcException ex) {
            JOptionPane.showMessageDialog(null, ex, "ola?", JOptionPane.ERROR_MESSAGE);
        }
    }

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jScrollPane1 = new javax.swing.JScrollPane();
        jTree1 = new javax.swing.JTree();
        jScrollPaneLog = new javax.swing.JScrollPane();
        jTable1 = new javax.swing.JTable();
        jLabel1 = new javax.swing.JLabel();
        jTextFieldFiltro = new javax.swing.JTextField();

        jScrollPane1.setName("jScrollPane1"); // NOI18N

        jTree1.setName("jTree1"); // NOI18N
        jScrollPane1.setViewportView(jTree1);

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        org.jdesktop.application.ResourceMap resourceMap = org.jdesktop.application.Application.getInstance(rax2.RaX2App.class).getContext().getResourceMap(Log.class);
        setTitle(resourceMap.getString("Form.title")); // NOI18N
        setName("Form"); // NOI18N

        jScrollPaneLog.setName("jScrollPaneLog"); // NOI18N

        jTable1.setModel(new DefaultTableModel(
            new Object[][]{},
            new String[]{
                "Fecha", "Titulo"
            }) {

                public Class getColumnClass(int column) {
                    Class returnValue;
                    if ((column >= 0) && (column < getColumnCount())) {
                        returnValue = getValueAt(0, column).getClass();
                    } else {
                        returnValue = Object.class;
                    }
                    return returnValue;
                }

                public boolean isCellEditable(int rowIndex, int columnIndex) {
                    return false;
                }
            });
            jTable1.setName("jTable1"); // NOI18N
            sorter = new TableRowSorter<TableModel>(jTable1.getModel());
            jTable1.setRowSorter(sorter);
            jTable1.setDefaultRenderer(Date.class, new DateRenderer() );
            jScrollPaneLog.setViewportView(jTable1);

            jLabel1.setText(resourceMap.getString("jLabel1.text")); // NOI18N
            jLabel1.setName("jLabel1"); // NOI18N

            jTextFieldFiltro.setText(resourceMap.getString("jTextFieldFiltro.text")); // NOI18N
            jTextFieldFiltro.setName("jTextFieldFiltro"); // NOI18N

            javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
            getContentPane().setLayout(layout);
            layout.setHorizontalGroup(
                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(layout.createSequentialGroup()
                    .addContainerGap()
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(jScrollPaneLog, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 612, Short.MAX_VALUE)
                        .addGroup(layout.createSequentialGroup()
                            .addComponent(jLabel1)
                            .addGap(18, 18, 18)
                            .addComponent(jTextFieldFiltro, javax.swing.GroupLayout.PREFERRED_SIZE, 213, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addContainerGap())
            );
            layout.setVerticalGroup(
                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(layout.createSequentialGroup()
                    .addContainerGap()
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jLabel1)
                        .addComponent(jTextFieldFiltro, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                    .addComponent(jScrollPaneLog, javax.swing.GroupLayout.DEFAULT_SIZE, 203, Short.MAX_VALUE)
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

            pack();
        }// </editor-fold>//GEN-END:initComponents

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel jLabel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPaneLog;
    private javax.swing.JTable jTable1;
    private TableRowSorter<TableModel> sorter;
    private javax.swing.JTextField jTextFieldFiltro;
    private javax.swing.JTree jTree1;
    // End of variables declaration//GEN-END:variables

    public JScrollPane getJScrollPaneLog() {
        return jScrollPaneLog;
    }

    public void setJScrollPaneLog(JScrollPane jScrollPaneLog) {
        this.jScrollPaneLog = jScrollPaneLog;
    }
    // End of variables declaration

    public JTable getJTable1() {
        return jTable1;
    }

    public void setJTable1(JTable jTable1) {
        this.jTable1 = jTable1;
    }
}
