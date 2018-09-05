package com.newSystem.Dialogs;

import com.mommoo.flat.layout.linear.LinearLayout;
import com.mommoo.flat.layout.linear.Orientation;
import com.mommoo.flat.layout.linear.constraints.LinearConstraints;
import com.mommoo.flat.layout.linear.constraints.LinearSpace;
import com.newSystem.Settings;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

public class ProductListDialog extends JDialog {
    private JPanel mainPanel;
    public static DialogDefaultPanel upperPanel;
    private JScrollPane underPanel;
    public static DefaultTableModel productListTableModel;
    public static JTable productListTable;

    public ProductListDialog() {
        setTitle("Product List");
        setLocation(700, 200);
        setSize(500, 500);
        // Icon 설정
        setIconImage(Settings.icon);

        mainPanel = new JPanel();
        mainPanel.setLayout(new LinearLayout(Orientation.VERTICAL, 0));
        upperPanel = new DialogDefaultPanel(20, DialogDefaultPanel.DIALOG.PRODUCTLIST);

        String[] col = {"No", "Product ID"};
        productListTableModel = new DefaultTableModel(col, 0);
        productListTable = new JTable(productListTableModel);
        productListTable.setDefaultEditor(Object.class, null);
        productListTable.getColumnModel().getColumn(0).setPreferredWidth(1);
        productListTable.getColumnModel().getColumn(1).setPreferredWidth(300);
        productListTable.addMouseListener(new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if(e.getClickCount() == 2){
                    int productIdx = productListTable.getSelectedRow();
                    String pid = productListTable.getValueAt(productIdx, 1).toString();
                    new TrackDialog(pid, true);
                }
            }

            @Override
            public void mousePressed(MouseEvent e) {

            }

            @Override
            public void mouseReleased(MouseEvent e) {

            }

            @Override
            public void mouseEntered(MouseEvent e) {

            }

            @Override
            public void mouseExited(MouseEvent e) {

            }
        });

        underPanel = new JScrollPane(productListTable);
        mainPanel.add(upperPanel, new LinearConstraints().setWeight(1).setLinearSpace(LinearSpace.MATCH_PARENT));
        mainPanel.add(underPanel, new LinearConstraints().setWeight(5).setLinearSpace(LinearSpace.MATCH_PARENT));
        add(mainPanel);
        setVisible(true);
    }

    public static DefaultTableModel getProductListTableModel() {
        return productListTableModel;
    }

    public static JTable getProductListTable() {
        return productListTable;
    }

    public static DialogDefaultPanel getUpperPanel() {
        return upperPanel;
    }
}
