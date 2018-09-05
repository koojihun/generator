package com.newSystem;

import com.mommoo.flat.layout.linear.LinearLayout;
import com.mommoo.flat.layout.linear.Orientation;
import com.mommoo.flat.layout.linear.constraints.LinearConstraints;
import com.mommoo.flat.layout.linear.constraints.LinearSpace;
import com.newSystem.Dialogs.AddressViewDialog;
import com.newSystem.Dialogs.TrackDialog;
import com.newSystem.Dialogs.TransactionDialog;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.PopupMenuEvent;
import javax.swing.event.PopupMenuListener;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;
import javax.swing.table.TableColumnModel;
import javax.swing.text.DefaultCaret;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.List;
import java.util.Map;

public class MidPanel extends JPanel {
    ///////////////////////////////////////////////////////////////////////////////////////////////////////////////
    private JPanel resultTablePanel; // Result
    private JPanel findPanel; // Result Page 맨 밑 find button 이 있을 공간
    private JPanel sendPanel; // Result Page 맨 밑 send button 이 있을 공간
    private ButtonListener buttonListener;
    private MyCellEditor myCellEditor;
    ////////////////////////////
    static private DefaultTableModel productTableModel;
    static private JTable productTable;
    ////////////////////////////
    private JLabel find_product_label;
    private JTextField find_product_textfield;
    private JButton find_product_button;
    ////////////////////////////
    private JLabel addressLabel;
    private JTextField addressTextField;
    private JButton sendButton;
    ////////////////////////////
    private String address;
    ///////////////////////////////////////////////////////////////////////////////////////////////////////////////
    private JPanel bitcoindPanel; // bincoind
    static private JTextArea bincoindTextArea;
    ///////////////////////////////////////////////////////////////////////////////////////////////////////////////
    private JPanel mempoolPanel; // mempool
    static private DefaultTableModel mempoolTableModel;
    static private JTable mempoolTable;
    ///////////////////////////////////////////////////////////////////////////////////////////////////////////////
    public MidPanel() {
        setLayout(new LinearLayout(Orientation.VERTICAL));
        JTabbedPane tabbedPane = new JTabbedPane();
        myCellEditor = new MyCellEditor();

        tabbedPane.setFont(Settings.Font14);
        add(tabbedPane, new LinearConstraints().setWeight(1).setLinearSpace(LinearSpace.MATCH_PARENT));

        makeResultTablePanel();
        makeBitcoindPanel();
        makeMempoolPanel();

        tabbedPane.add("Result", resultTablePanel);
        tabbedPane.add("Bincoind", bitcoindPanel);
        tabbedPane.add("Mempool", mempoolPanel);

        // mempool 영역을 누를 때 마다 mempool 내용을 새로 받아오기 위해서
        // mempool 탭에 리스너 등록.
        ChangeListener changeListener = new ChangeListener() {
            public void stateChanged(ChangeEvent changeEvent) {
                JTabbedPane sourceTabbedPane = (JTabbedPane) changeEvent.getSource();
                int index = sourceTabbedPane.getSelectedIndex();
                if (index == 2) {
                    java.util.List<String> mempool = Main.bitcoinJSONRPCClient.getRawMemPool();
                    DefaultTableModel mempoolModel = MidPanel.getMempoolTableModel();
                    mempoolModel.setNumRows(0);
                    for (String element : mempool) {
                        String[] row = {element};
                        mempoolModel.addRow(row);
                    }
                }
            }
        };
        tabbedPane.addChangeListener(changeListener);

        // product table colum width control.
        setWidthAsPercentages(productTable, 10, 50, 20, 20);
    }
    private void setWidthAsPercentages(JTable table,
                                               double... percentages) {
        final double factor = 10000;

        TableColumnModel model = table.getColumnModel();
        for (int columnIndex = 0; columnIndex < percentages.length; columnIndex++) {
            TableColumn column = model.getColumn(columnIndex);
            column.setPreferredWidth((int) (percentages[columnIndex] * factor));
        }
    }
    public void makeResultTablePanel() {
        resultTablePanel = new JPanel(new LinearLayout(Orientation.VERTICAL, 0));

        String[] col = new String[4];
        col[0] = "No";
        col[1] = "ID";
        col[2] = "Production Date";
        col[3] = "Expiration Date";

        productTableModel = new DefaultTableModel(col, 0);
        productTable = new JTable(productTableModel);
        productTable.setRowHeight(30);
        productTable.getTableHeader().setFont(Settings.Font19);
        productTable.getTableHeader().setPreferredSize(new Dimension(0, 40));
        productTable.setFont(Settings.Font14);
        productTable.setRowSelectionAllowed(true);
        productTable.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);

        productTable.setDefaultEditor(Object.class, myCellEditor);
        setWidthAsPercentages(productTable, 10, 50, 20, 20);
        setColumnAlignment(productTable, JLabel.CENTER);
        ///////////////////////////////////////////////////////////////////////////////////////////////
        JPopupMenu popupMenu = new JPopupMenu();
        JMenuItem detailsItem = new JMenuItem("details");
        detailsItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String pid = productTableModel.getValueAt(productTable.getSelectedRow(), 1).toString();
                new TrackDialog(pid, true);
            }
        });
        popupMenu.add(detailsItem);
        productTable.setComponentPopupMenu(popupMenu);
        popupMenu.addPopupMenuListener(new PopupMenuListener() {

            @Override
            public void popupMenuWillBecomeVisible(PopupMenuEvent e) {
                SwingUtilities.invokeLater(new Runnable() {
                    @Override
                    public void run() {
                        int rowAtPoint = productTable.rowAtPoint(SwingUtilities.convertPoint(popupMenu, new Point(0, 0), productTable));
                        if (rowAtPoint > -1) {
                            productTable.setRowSelectionInterval(rowAtPoint, rowAtPoint);
                        }
                    }
                });
            }

            @Override
            public void popupMenuWillBecomeInvisible(PopupMenuEvent e) {
                // TODO Auto-generated method stub

            }

            @Override
            public void popupMenuCanceled(PopupMenuEvent e) {
                // TODO Auto-generated method stub

            }
        });
        ///////////////////////////////////////////////////////////////////////////////////////////////
        JScrollPane list = new JScrollPane(productTable);
        list.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        ///////////////////////////////////////////////////////////////////////////////////////////////
        buttonListener = new ButtonListener();
        makeFindPanel();
        makeSendPanel();
        ///////////////////////////////////////////////////////////////////////////////////////////////
        resultTablePanel.add(findPanel, new LinearConstraints().setWeight(1).setLinearSpace(LinearSpace.MATCH_PARENT));
        resultTablePanel.add(list, new LinearConstraints().setWeight(10).setLinearSpace(LinearSpace.MATCH_PARENT));
        resultTablePanel.add(sendPanel, new LinearConstraints().setWeight(1).setLinearSpace(LinearSpace.MATCH_PARENT));
    }

    public void makeMempoolPanel() {
        mempoolPanel = new JPanel(new LinearLayout(Orientation.VERTICAL));

        String[] col = {"Transactions In Mempool"}; // dummy header. this will not be shown.
        mempoolTableModel = new DefaultTableModel(col, 0);
        mempoolTable = new JTable(mempoolTableModel);
        mempoolTable.setRowHeight(30);
        mempoolTable.getTableHeader().setFont(Settings.Font19);
        mempoolTable.getTableHeader().setPreferredSize(new Dimension(0, 40));
        mempoolTable.setFont(Settings.Font14);
        mempoolTable.setDefaultEditor(Object.class, null);

        JScrollPane list = new JScrollPane(mempoolTable);
        list.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));

        mempoolTable.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                if (evt.getClickCount() == 2) {
                    String txID = (String) mempoolTable.getValueAt(mempoolTable.getSelectedRow(), 0);
                    new TransactionDialog(txID);
                }
            }
        });
        mempoolPanel.add(list, new LinearConstraints().setWeight(1).setLinearSpace(LinearSpace.MATCH_PARENT));
    }

    public static DefaultTableModel getMempoolTableModel() {
        return mempoolTableModel;
    }

    // find dialog를 통해서 검색 결과로 나오는 row에 highlight하기 위해 필요함.
    public static JTable getProductTable() {
        return productTable;
    }

    public void makeFindPanel() {
        findPanel = new JPanel(new LinearLayout(Orientation.HORIZONTAL, 10));
        findPanel.setBorder(BorderFactory.createEmptyBorder(0, 20, 0, 20));
        find_product_label = new JLabel("ID");
        find_product_label.setHorizontalAlignment(SwingConstants.RIGHT);
        find_product_label.setFont(Settings.Font14);
        find_product_textfield = new JTextField(50);
        find_product_textfield.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    findProductInProductTable();
                }
            }
        });
        find_product_button = new JButton("FIND");
        find_product_button.setFont(Settings.Font14);
        find_product_button.setFocusPainted(false);
        find_product_button.addActionListener(buttonListener);

        findPanel.add(find_product_label, new LinearConstraints().setWeight(1).setLinearSpace(LinearSpace.WRAP_CENTER_CONTENT));
        findPanel.add(find_product_textfield, new LinearConstraints().setWeight(7).setLinearSpace(LinearSpace.WRAP_CENTER_CONTENT));
        findPanel.add(find_product_button, new LinearConstraints().setWeight(2).setLinearSpace(LinearSpace.WRAP_CENTER_CONTENT));
    }

    public void makeSendPanel() {
        sendPanel = new JPanel(new LinearLayout(Orientation.HORIZONTAL, 10));
        sendPanel.setBorder(BorderFactory.createEmptyBorder(0, 20, 0, 20));
        addressLabel = new JLabel("ADDRESS");
        addressLabel.setHorizontalAlignment(SwingConstants.RIGHT);
        addressLabel.setFont(Settings.Font14);
        addressTextField = new JTextField(50);
        sendButton = new JButton("SEND");
        sendButton.setFont(Settings.Font14);
        sendButton.addActionListener(buttonListener);
        sendButton.setFocusPainted(false);

        sendPanel.add(addressLabel, new LinearConstraints().setWeight(1).setLinearSpace(LinearSpace.WRAP_CENTER_CONTENT));
        sendPanel.add(addressTextField, new LinearConstraints().setWeight(7).setLinearSpace(LinearSpace.WRAP_CENTER_CONTENT));
        sendPanel.add(sendButton, new LinearConstraints().setWeight(2).setLinearSpace(LinearSpace.WRAP_CENTER_CONTENT));
    }

    private void findProductInProductTable() {
        getCurrentProducts();
        String target_string = find_product_textfield.getText();

        if (target_string.equals(""))
            return;

        int resultRow = -1;
        for (int r = 0; r < productTable.getRowCount(); r++) {
            if (productTableModel.getValueAt(r, 1).toString().contains(target_string)) {
                resultRow = r;
                break;
            }
        }
        if (resultRow == -1) {
            JOptionPane.showMessageDialog(null, "There is no " + target_string,
                    "Message", JOptionPane.WARNING_MESSAGE);
            return;
        }
        productTable.setRowSelectionInterval(resultRow, resultRow);
        productTable.scrollRectToVisible(productTable.getCellRect(resultRow, productTable.getColumnCount(), true));
    }

    public void makeBitcoindPanel() {
        bitcoindPanel = new JPanel();
        bitcoindPanel.setLayout(new LinearLayout(Orientation.VERTICAL));
        bincoindTextArea = new JTextArea();
        bincoindTextArea.setEditable(false);
        bincoindTextArea.setFont(Settings.Font14);
        /////////////////////////////////////////////////////////////////////
        // AUTO SCROLL
        DefaultCaret caret = (DefaultCaret) bincoindTextArea.getCaret();
        caret.setUpdatePolicy(DefaultCaret.ALWAYS_UPDATE);
        /////////////////////////////////////////////////////////////////////
        JScrollPane bincoindScrollPane = new JScrollPane(bincoindTextArea);
        bincoindTextArea.setBackground(Color.BLACK);
        bincoindTextArea.setForeground(Color.green);
        bitcoindPanel.add(bincoindScrollPane, new LinearConstraints().setWeight(1).setLinearSpace(LinearSpace.MATCH_PARENT));
    }

    static public JTextArea getBitcoindArea() {
        return bincoindTextArea;
    }

    public static void getCurrentProducts() {
        List<Map> maps = Main.bitcoinJSONRPCClient.get_current_products();
        productTableModel.setNumRows(0);
        int count = 0;
        for (Map map : maps) {
            count++;
            String[] row = {
                    String.valueOf(count),
                    String.valueOf(map.get("PID")),
                    String.valueOf(map.get("production date")),
                    String.valueOf(map.get("expiration date")),
            };
            productTableModel.addRow(row);
        }
    }

    private void setColumnAlignment(JTable table, int Alignment) {
        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(Alignment);
        for (int cnt = 0; cnt < table.getColumnCount(); cnt++)
            table.getColumnModel().getColumn(cnt).setCellRenderer(centerRenderer);
    }

    private class ButtonListener implements ActionListener {
        public void actionPerformed(ActionEvent event) {
            JButton clicked = (JButton) event.getSource();
            if (clicked.getText().equals("FIND")) {
                findProductInProductTable();
            } else if (clicked.getText().equals("SEND")) {
                int[] selectedRowsIndex = productTable.getSelectedRows();
                // 선택된 row가 없음 --> 그냥 종료.
                if (selectedRowsIndex.length == 0)
                    return;
                // 주소 칸에 입력 x --> 주소록에서 address 선택.
                if (addressTextField.getText().length() == 0) {
                    // 단일 전송.
                    if (selectedRowsIndex.length == 1) {
                        String product = productTableModel.getValueAt(selectedRowsIndex[0], 1).toString();
                        new AddressViewDialog(product, false);
                        // 다중 전송.
                    } else {
                        String products = "";
                        for (int i = 0; i < selectedRowsIndex.length - 1; i++)
                            products = products + productTableModel.getValueAt(selectedRowsIndex[i], 1).toString() + "/";
                        products = products + productTableModel.getValueAt(selectedRowsIndex[selectedRowsIndex.length - 1], 1).toString();
                        new AddressViewDialog(products, true);
                    }
                } else {
                    if (selectedRowsIndex.length == 1) {
                        // 단일 전송.
                        String id = productTableModel.getValueAt(selectedRowsIndex[0], 1).toString();
                        address = addressTextField.getText();
                        // 새로 만들어진 send_to_address는 product ID만 있으면 됨.
                        Main.bitcoinJSONRPCClient.send_to_address(address, id);

                    } else {
                        // 다중 전송.
                        String products = "";
                        for (int i = 0; i < selectedRowsIndex.length - 1; i++) {
                            products = products + productTableModel.getValueAt(selectedRowsIndex[i], 1).toString() + "/";
                        }
                        products = products + productTableModel.getValueAt(selectedRowsIndex[selectedRowsIndex.length - 1], 1).toString();
                        address = addressTextField.getText();
                        Main.bitcoinJSONRPCClient.send_many(address, products);
                    }
                    // 보낸 상품 table에서 지우기 위해 get_current_products()
                    getCurrentProducts();
                    // 정상적으로 보내졌으면 address textField가 빈칸으로 변해야함.
                    addressTextField.setText("");
                }
            }
        }
    }
    //////////////////////////////////////////////////////////////////////////////////////////
    // product table의 내용물을 edit해도 focus를 잃으면 원래 값으로 돌아가도록 하기 위한 CellEditor 조작.
    private class MyCellEditor extends DefaultCellEditor {
        private Object originalValue;
        public MyCellEditor() {
            super(new JTextField());
        }
        public Component getTableCellEditorComponent(JTable table, Object value,
                                                     boolean isSelected,
                                                     int row, int column) {
            JTextField editor = (JTextField) super.getTableCellEditorComponent(table, value, isSelected,
                    row, column);
            originalValue = value;
            return editor;
        }

        @Override
        public Object getCellEditorValue() {
            return originalValue;
        }
    }
    //////////////////////////////////////////////////////////////////////////////////////////
}




