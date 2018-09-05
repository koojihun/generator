package com.newSystem.Dialogs;

import com.mommoo.flat.layout.linear.LinearLayout;
import com.mommoo.flat.layout.linear.Orientation;
import com.mommoo.flat.layout.linear.constraints.LinearConstraints;
import com.mommoo.flat.layout.linear.constraints.LinearSpace;
import com.newSystem.Main;
import com.newSystem.MainFrame;
import com.newSystem.MidPanel;
import com.newSystem.Settings;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

public class AddressViewDialog extends JDialog {
    private DefaultTableModel savedAddressTableModel;
    private JTable savedAddressTable;
    private JButton okBtn;
    private JButton cancelBtn;
    private ClickListener clickListener;
    String ids;
    boolean many;

    public AddressViewDialog(String products, boolean isMany) {
        // Icon 설정
        setIconImage(Settings.icon);
        setTitle("Select Address to send");
        ids = products;
        many = isMany;
        JPanel mainPanel = new JPanel(new LinearLayout(Orientation.VERTICAL, 10));
        setSize(600, 800);
        setLocation(200, 200);
        add(mainPanel);
        clickListener = new ClickListener();
        //////////////////////////////////////////////////////////////////////////////////////////////////////
        // 상단 버튼 패널 생성.
        JPanel btnPanel = new JPanel(new LinearLayout(Orientation.HORIZONTAL, 10));
        btnPanel.setBorder(BorderFactory.createEmptyBorder(10, 20, 0, 20));
        okBtn = new JButton("SEND");
        okBtn.setFont(Settings.Font18);
        okBtn.setFocusPainted(false);
        okBtn.addActionListener(clickListener);
        cancelBtn = new JButton("CANCEL");
        cancelBtn.setFont(Settings.Font18);
        cancelBtn.setFocusPainted(false);
        cancelBtn.addActionListener(clickListener);
        btnPanel.add(okBtn, new LinearConstraints().setWeight(1).setLinearSpace(LinearSpace.MATCH_PARENT));
        btnPanel.add(cancelBtn, new LinearConstraints().setWeight(1).setLinearSpace(LinearSpace.MATCH_PARENT));
        //////////////////////////////////////////////////////////////////////////////////////////////////////
        // 상단 버튼 패널 등록.
        mainPanel.add(btnPanel, new LinearConstraints().setWeight(1).setLinearSpace(LinearSpace.MATCH_PARENT));
        //////////////////////////////////////////////////////////////////////////////////////////////////////
        String[] col = {"Name", "Address"};
        savedAddressTableModel = new DefaultTableModel(col, 0);
        savedAddressTable = new JTable(savedAddressTableModel);
        savedAddressTable.setRowHeight(30);
        savedAddressTable.getTableHeader().setFont(Settings.Font19);
        savedAddressTable.setFont(Settings.Font14);
        //////////////////////////////////////////////////////////////////////////////////////////////////////
        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(JLabel.CENTER);
        savedAddressTable.getColumnModel().getColumn(0).setCellRenderer(centerRenderer);
        savedAddressTable.getColumnModel().getColumn(1).setCellRenderer(centerRenderer);
        savedAddressTable.setDefaultEditor(Object.class, new MyCellEditor());
        ///////////////////////////////////////////////////////////////////////////////////////////////////////////////
        JScrollPane scrollPane = new JScrollPane(savedAddressTable);
        scrollPane.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        mainPanel.add(scrollPane, new LinearConstraints().setWeight(15).setLinearSpace(LinearSpace.MATCH_PARENT));

        if(AddressDialog.isThereAddressTxt()) {
            ArrayList<String> names_addresses = AddressDialog.readNameAndAddressFromFile();
            for (int cnt = 0; cnt < names_addresses.size(); cnt += 2) {
                String[] row = {names_addresses.get(cnt), names_addresses.get(cnt + 1)};
                savedAddressTableModel.addRow(row);
            }
        }
        setVisible(true);
    }
    private class ClickListener implements ActionListener {
        ClickListener() {}
        @Override
        public void actionPerformed(ActionEvent e) {
            JButton clicked = (JButton) e.getSource();
            if (clicked == okBtn) {
                if (savedAddressTable.getSelectedRow() == -1)
                    return;
                String address = savedAddressTable.getValueAt(savedAddressTable.getSelectedRow(), 1).toString();
                if (many)
                    Main.bitcoinJSONRPCClient.send_many(address, ids);
                else
                    Main.bitcoinJSONRPCClient.send_to_address(address, ids);
                MidPanel.getCurrentProducts();
                SwingUtilities.getWindowAncestor(clicked).dispose();
            } else if (clicked == cancelBtn) {
                SwingUtilities.getWindowAncestor(clicked).dispose();
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
