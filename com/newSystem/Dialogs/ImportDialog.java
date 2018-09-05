package com.newSystem.Dialogs;

import com.mommoo.flat.layout.linear.LinearLayout;
import com.mommoo.flat.layout.linear.Orientation;
import com.mommoo.flat.layout.linear.constraints.LinearConstraints;
import com.mommoo.flat.layout.linear.constraints.LinearSpace;
import com.newSystem.Main;
import com.newSystem.MainFrame;
import com.newSystem.Settings;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Iterator;
import java.util.Map;

public class ImportDialog extends JDialog {
    private JButton importAddressBtn;
    public ImportDialog() {
        setTitle("Imported Addresses (Only For Server)");
        JPanel mainPanel = new JPanel(new LinearLayout(Orientation.VERTICAL, 10));
        setSize(600, 800);
        setLocation(200, 200);
        // Icon 설정
        setIconImage(Toolkit.getDefaultToolkit().getImage(getClass().getClassLoader().getResource("icon.png")));
        this.add(mainPanel);

        importAddressBtn = new JButton("Import Address");
        mainPanel.add(importAddressBtn, new LinearConstraints().setWeight(1).setLinearSpace(LinearSpace.MATCH_PARENT));
        importAddressBtn.setFont(Settings.Font14);
        importAddressBtn.setFocusPainted(false);
        importAddressBtn.addActionListener(new ClickListener());
        ///////////////////////////////////////////////////////////////////////////////////////////////////////////////
        String[] col = { "Account", "Address" };
        DefaultTableModel importedAddressTableModel = new DefaultTableModel(col, 0);
        JTable importedAddressTable = new JTable(importedAddressTableModel);
        importedAddressTable.setRowHeight(30);
        importedAddressTable.getTableHeader().setFont(Settings.Font19);
        importedAddressTable.setFont(Settings.Font14);
        importedAddressTable.setDefaultEditor(Object.class, new MyCellEditor());
        java.util.List<Map> maps = Main.bitcoinJSONRPCClient.enumerate_account_info(); {
            for (Map map : maps) {
                String account = (String) map.get("account");
                if (account.length() == 0)
                    account = "\"DEFAULT\"";
                java.util.List<String> addresses = (java.util.List<String>) map.get("addresses");
                Iterator<String> it = addresses.iterator();
                String[] eachFirstRow = { account, it.next() };
                importedAddressTableModel.addRow(eachFirstRow);
                while (it.hasNext()) {
                    String[] eachRow = { "", it.next() };
                    importedAddressTableModel.addRow(eachRow);
                }
            }
        }
        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(JLabel.CENTER);
        importedAddressTable.getColumnModel().getColumn(0).setCellRenderer(centerRenderer);
        importedAddressTable.getColumnModel().getColumn(1).setCellRenderer(centerRenderer);
        ///////////////////////////////////////////////////////////////////////////////////////////////////////////////
        JScrollPane scrollPane = new JScrollPane(importedAddressTable);
        scrollPane.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        ///////////////////////////////////////////////////////////////////////////////////////////////////////////////
        mainPanel.add(scrollPane, new LinearConstraints().setWeight(15).setLinearSpace(LinearSpace.MATCH_PARENT));
        setVisible(true);
    }
    private class ClickListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            JButton clicked = (JButton) e.getSource();
            if (clicked == importAddressBtn) {
                new ImportAddressDialog();
                dispose();
            }
        }
    }
    private class ImportAddressDialog extends JDialog {
        public ImportAddressDialog() {
            setTitle("Import an Address");
            setLocation(200, 200);
            setSize(500, 250);
            // Icon 설정
            setIconImage(Toolkit.getDefaultToolkit().getImage(getClass().getClassLoader().getResource("icon.png")));
            DialogDefaultPanel mainPanel = new DialogDefaultPanel(20, DialogDefaultPanel.DIALOG.IMPORTADDRESS);
            ///////////////////////////////////////////////////////////////////////////////////////////////////////////////
            JLabel explaination = new JLabel("Import an Address as account");
            explaination.setHorizontalAlignment(SwingConstants.CENTER);
            explaination.setFont(Settings.Font14);
            explaination.setBorder(BorderFactory.createEmptyBorder(30, 0, 0, 0));
            mainPanel.add(explaination, new LinearConstraints().setWeight(1).setLinearSpace(LinearSpace.MATCH_PARENT));
            mainPanel.makeNonEmptyLine("Account", "", true);
            mainPanel.makeNonEmptyLine("Address", "", true);
            mainPanel.makeButtonLine();
            add(mainPanel);
            ///////////////////////////////////////////////////////////////////////////////////////////////////////////////
            setVisible(true);
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
