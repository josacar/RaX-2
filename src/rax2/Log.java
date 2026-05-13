package rax2;

import java.awt.Component;
import java.awt.event.AdjustmentEvent;
import java.awt.event.AdjustmentListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
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
import io.grpc.StatusRuntimeException;
import rax2.proto.RssaniServiceGrpc;
import rax2.proto.LogRequest;
import rax2.proto.LogResponse;

class DateRenderer extends DefaultTableCellRenderer {

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

public class Log extends javax.swing.JDialog {

    class MyAdjustmentListener implements AdjustmentListener {

        JScrollBar verticalScrollBar;

        MyAdjustmentListener(JScrollBar v) {
            verticalScrollBar = v;
        }

        @Override
        public void adjustmentValueChanged(AdjustmentEvent e) {
            String aux;
            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");

            DefaultTableModel model = (DefaultTableModel) getJTable1().getModel();
            if (e.getValue() == (verticalScrollBar.getMaximum() - verticalScrollBar.getVisibleAmount()) || verticalScrollBar.getMaximum() == verticalScrollBar.getVisibleAmount()) {
                if (max == e.getValue() && verticalScrollBar.getMaximum() != verticalScrollBar.getVisibleAmount()) {
                    return;
                }
                try {
                    LogRequest request = LogRequest.newBuilder()
                            .setIni(offset)
                            .setFin(offset + 20)
                            .build();
                    LogResponse result = _stub.verLog(request);
                    for (int i = 0; i < result.getLinesCount(); ++i) {
                        aux = result.getLines(i);
                        java.util.Date fecha = sdf.parse(aux.split("[|]")[0].substring(2));
                        model.addRow(new Object[]{fecha, aux.split("[|]")[1]});
                    }
                    offset += 20;
                    max = e.getValue();
                } catch (ParseException ex) {
                    Logger.getLogger(RaX2View.class.getName()).log(Level.SEVERE, null, ex);
                } catch (StatusRuntimeException ex) {
                    GrpcErrorHandler.showErrorMessage(Log.this, ex, "Error");
                }
            }
        }
    }
    JScrollBar verticalScrollBar;
    JScrollBar horizontalScrollBar;
    private RssaniServiceGrpc.RssaniServiceBlockingStub _stub;
    int offset = 20;
    int max = 0;

    public Log(java.awt.Frame parent, RssaniServiceGrpc.RssaniServiceBlockingStub stub) {
        super(parent, true);
        initComponents();
        _stub = stub;
        verticalScrollBar = jScrollPaneLog.getVerticalScrollBar();
        horizontalScrollBar = jScrollPaneLog.getHorizontalScrollBar();
        jTable1.getColumnModel().getColumn(0).setMinWidth(90);
        jTable1.getColumnModel().getColumn(0).setMaxWidth(90);

        try {
            LogRequest request = LogRequest.newBuilder()
                    .setIni(0)
                    .setFin(20)
                    .build();
            final LogResponse result = _stub.verLog(request);
            java.awt.EventQueue.invokeLater(new Runnable() {

                DefaultTableModel model;
                java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("dd/MM/yyyy HH:mm:ss");

                @Override
                synchronized public void run() {

                    verticalScrollBar.addAdjustmentListener(new MyAdjustmentListener(verticalScrollBar));
                    model = (DefaultTableModel) jTable1.getModel();
                    String aux;

                    for (int i = 0; i < result.getLinesCount(); ++i) {
                        try {
                            aux = result.getLines(i);
                            java.util.Date fecha = sdf.parse(aux.split("[|]")[0].substring(2));
                            model.addRow(new Object[]{fecha, aux.split("[|]")[1]});
                        } catch (ParseException ex) {
                            Logger.getLogger(RaX2View.class.getName()).log(Level.SEVERE, null, ex);
                        }
                    }
                    offset = 20;
                    max = 0;
                }
            });

        } catch (StatusRuntimeException ex) {
            GrpcErrorHandler.showErrorMessage(this, ex, "Error");
        }
    }

    @SuppressWarnings("unchecked")
    private void initComponents() {

        jScrollPaneLog = new javax.swing.JScrollPane();
        jTable1 = new javax.swing.JTable();
        jLabel1 = new javax.swing.JLabel();
        jTextFieldFiltro = new javax.swing.JTextField();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Log");
        setName("Form");

        jScrollPaneLog.setName("jScrollPaneLog");

        jTable1.setModel(new DefaultTableModel(
            new Object[][]{},
            new String[]{
                "Date", "Title"
            }) {

                public Class<?> getColumnClass(int column) {
                    Class<?> returnValue;
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
            jTable1.setName("jTable1");
            jTable1.setToolTipText("Server log entries");
            sorter = new TableRowSorter<TableModel>(jTable1.getModel());
            jTable1.setRowSorter(sorter);
            jTable1.setDefaultRenderer(Date.class, new DateRenderer());
            jScrollPaneLog.setViewportView(jTable1);

            jLabel1.setLabelFor(jTextFieldFiltro);
            jLabel1.setText("Filter:");
            jLabel1.setName("jLabel1");

            jTextFieldFiltro.setText("");
            jTextFieldFiltro.setName("jTextFieldFiltro");
            jTextFieldFiltro.setToolTipText("Type to filter log entries by text or regex");

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
        }

    private javax.swing.JLabel jLabel1;
    private javax.swing.JScrollPane jScrollPaneLog;
    private javax.swing.JTable jTable1;
    private TableRowSorter<TableModel> sorter;
    private javax.swing.JTextField jTextFieldFiltro;

    public JScrollPane getJScrollPaneLog() {
        return jScrollPaneLog;
    }

    public void setJScrollPaneLog(JScrollPane jScrollPaneLog) {
        this.jScrollPaneLog = jScrollPaneLog;
    }

    public JTable getJTable1() {
        return jTable1;
    }

    public void setJTable1(JTable jTable1) {
        this.jTable1 = jTable1;
    }
}