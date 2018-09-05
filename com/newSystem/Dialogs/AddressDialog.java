package com.newSystem.Dialogs;

import com.mommoo.flat.layout.linear.LinearLayout;
import com.mommoo.flat.layout.linear.Orientation;
import com.mommoo.flat.layout.linear.constraints.LinearConstraints;
import com.mommoo.flat.layout.linear.constraints.LinearSpace;
import com.newSystem.MidPanel;
import com.newSystem.Settings;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.util.ArrayList;

public class AddressDialog extends JDialog {
    private JButton addBtn;
    private JButton deleteBtn;
    private ClickListener clickListener;
    static public DefaultTableModel savedAddressTableModel;
    static public JTable savedAddressTable;
    public AddressDialog() {
        setTitle("Address Book");
        JPanel mainPanel = new JPanel(new LinearLayout(Orientation.VERTICAL, 10));
        setSize(600, 800);
        setLocation(200, 200);
        setIconImage(Settings.icon);
        add(mainPanel);
        clickListener = new ClickListener();
        //////////////////////////////////////////////////////////////////////////////////////////////////////
        // 상단 버튼 패널 생성.
        JPanel btnPanel = new JPanel(new LinearLayout(Orientation.HORIZONTAL, 10));
        btnPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 0, 10));
        addBtn = new JButton("ADD");
        addBtn.setFont(Settings.Font18);
        addBtn.setFocusPainted(false);
        addBtn.addActionListener(clickListener);
        deleteBtn = new JButton("Delete");
        deleteBtn.setFont(Settings.Font18);
        deleteBtn.setFocusPainted(false);
        deleteBtn.addActionListener(clickListener);
        btnPanel.add(addBtn, new LinearConstraints().setWeight(1).setLinearSpace(LinearSpace.MATCH_PARENT));
        btnPanel.add(deleteBtn, new LinearConstraints().setWeight(1).setLinearSpace(LinearSpace.MATCH_PARENT));
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
        savedAddressTable.setDefaultEditor(Object.class, new MyCellEditor());
        //////////////////////////////////////////////////////////////////////////////////////////////////////
        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(JLabel.CENTER);
        savedAddressTable.getColumnModel().getColumn(0).setCellRenderer(centerRenderer);
        savedAddressTable.getColumnModel().getColumn(1).setCellRenderer(centerRenderer);
        ///////////////////////////////////////////////////////////////////////////////////////////////////////////////
        JScrollPane scrollPane = new JScrollPane(savedAddressTable);
        scrollPane.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        ///////////////////////////////////////////////////////////////////////////////////////////////////////////////
        mainPanel.add(scrollPane, new LinearConstraints().setWeight(15).setLinearSpace(LinearSpace.MATCH_PARENT));
        ///////////////////////////////////////////////////////////////////////////////////////////////////////////////
        if(isThereAddressTxt()) {
            ArrayList<String> names_addresses = readNameAndAddressFromFile();
            for (int cnt = 0; cnt < names_addresses.size(); cnt += 2) {
                String[] row = {names_addresses.get(cnt), names_addresses.get(cnt + 1)};
                savedAddressTableModel.addRow(row);
            }
        }
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowClosing(java.awt.event.WindowEvent windowEvent){
                try {
                    writeNameAndAddressToFile();
                } catch (Exception e) {
                    System.err.println("Save Address Dialog closing event listener error");
                }
            }});
        setVisible(true);
    }
    static public boolean isThereAddressTxt() {
        File addresses_txt = new File(
                "C:\\Users\\"
                        + Settings.getUserNmae()
                        + "\\AppData\\Roaming\\Bitcoin\\addresses.txt");
        if (addresses_txt.exists())
            return true;
        else
            return false;
    }
    static public void writeNameAndAddressToFile() {
        try {
            FileWriter writer = new FileWriter("" +
                    "C:\\Users\\"
                    + Settings.getUserNmae()
                    + "\\AppData\\Roaming\\Bitcoin\\addresses.txt"); // 텍스트 파일이 없으면 새로 생성함!
            BufferedWriter bufferedWriter = new BufferedWriter(writer);
            for (int cnt = 0; cnt < savedAddressTable.getRowCount(); cnt++) {
                String name = savedAddressTable.getValueAt(cnt, 0).toString();
                String address = savedAddressTable.getValueAt(cnt, 1).toString();
                bufferedWriter.write(name);
                bufferedWriter.newLine();
                bufferedWriter.write(address);
                bufferedWriter.newLine();
            }
            bufferedWriter.close();
            writer.close();
        } catch (IOException e) {
            System.err.println("Error : write addresses.txt");
        }
    }
    static public ArrayList<String> readNameAndAddressFromFile() {
        File addresses_txt = new File(
                "C:\\Users\\"
                        + Settings.getUserNmae()
                        + "\\AppData\\Roaming\\Bitcoin\\addresses.txt");
        try {
            FileReader fileReader = new FileReader(addresses_txt);
            BufferedReader bufferedReader = new BufferedReader(fileReader);
            ArrayList<String> names_addresses = new ArrayList<String>();
            String line = "";

            while ((line = bufferedReader.readLine()) != null)
                names_addresses.add(line);

            bufferedReader.close();
            fileReader.close();
            return names_addresses;
        } catch (FileNotFoundException e) {
            System.err.println("Error : addresses.txt not found");
        } catch (IOException e) {
            System.err.println("Error : addresses.txt readline error");
        }
        return null;
    }
    class ClickListener implements ActionListener {
        ClickListener() {}
        @Override
        public void actionPerformed(ActionEvent e) {
            JButton clicked = (JButton) e.getSource();
            if (clicked == addBtn) {
                new AddAddressDialog();
            } else if (clicked == deleteBtn) {
                int selectedRow = savedAddressTable.getSelectedRow();
                if (selectedRow != -1)
                    savedAddressTableModel.removeRow(selectedRow);
            }
        }
    }
    private class AddAddressDialog extends JDialog {
        AddAddressDialog() {
            setTitle("Add Address");
            setLocation(200, 200);
            setSize(500, 300);
            DialogDefaultPanel panel = new DialogDefaultPanel(20, DialogDefaultPanel.DIALOG.SAVEADDRESS);
            panel.makeEmptyLine();
            panel.makeNonEmptyLine("Name ", null, true);
            panel.makeNonEmptyLine("Address ", null, true);
            panel.makeButtonLine();
            add(panel);
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
