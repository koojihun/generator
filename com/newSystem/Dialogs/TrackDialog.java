package com.newSystem.Dialogs;

import com.mommoo.flat.layout.linear.LinearLayout;
import com.mommoo.flat.layout.linear.Orientation;
import com.mommoo.flat.layout.linear.constraints.LinearConstraints;
import com.mommoo.flat.layout.linear.constraints.LinearSpace;
import com.newSystem.Settings;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

public class TrackDialog extends JDialog {

    private JPanel mainPanel;
    private DialogDefaultPanel upperPanel;
    private JScrollPane underPanel;
    private static DefaultTableModel trackTableModel;
    private JTable trackTable;

    public TrackDialog() {
        setTitle("Tracking Product");
        setLocation(200, 200);
        setSize(500, 500);
        // Icon 설정
        setIconImage(Settings.icon);

        mainPanel = new JPanel();
        mainPanel.setLayout(new LinearLayout(Orientation.VERTICAL, 0));

        upperPanel = new DialogDefaultPanel(20, DialogDefaultPanel.DIALOG.TRACK);
        upperPanel.makeNonEmptyLine("Product ID", null, true);
        upperPanel.makeTrackButtonLine();

        String[] col = {"No", "Receiver","Time"};
        trackTableModel = new DefaultTableModel(col, 0);
        trackTable = new JTable(trackTableModel);

        //가운데정렬
        DefaultTableCellRenderer celAlignCenter = new DefaultTableCellRenderer();
        celAlignCenter.setHorizontalAlignment(JLabel.CENTER);
        trackTable.getColumnModel().getColumn(0).setCellRenderer(celAlignCenter);
        trackTable.getColumnModel().getColumn(1).setCellRenderer(celAlignCenter);
        trackTable.getColumnModel().getColumn(2).setCellRenderer(celAlignCenter);

        trackTable.getColumnModel().getColumn(0).setPreferredWidth(10);
        trackTable.getColumnModel().getColumn(1).setPreferredWidth(100);
        trackTable.getColumnModel().getColumn(2).setPreferredWidth(200);
        trackTable.setDefaultEditor(Object.class, new MyCellEditor());
        underPanel = new JScrollPane(trackTable);
        mainPanel.add(upperPanel, new LinearConstraints().setWeight(1).setLinearSpace(LinearSpace.MATCH_PARENT));
        mainPanel.add(underPanel, new LinearConstraints().setWeight(5).setLinearSpace(LinearSpace.MATCH_PARENT));

        add(mainPanel);
        setVisible(true);
    }
    public TrackDialog(String id, boolean auto) {
        setTitle("Tracking Product");
        setLocation(200, 200);
        setSize(500, 500);
        // Icon 설정
        setIconImage(Settings.icon);

        mainPanel = new JPanel();
        mainPanel.setLayout(new LinearLayout(Orientation.VERTICAL, 0));

        upperPanel = new DialogDefaultPanel(20, DialogDefaultPanel.DIALOG.TRACK);
        upperPanel.makeNonEmptyLine("Product ID", id, true);
        upperPanel.makeTrackButtonLine();


        String[] col = {"No", "Receiver", "Time"};
        trackTableModel = new DefaultTableModel(col, 0);
        trackTable = new JTable(trackTableModel);

        //가운데정렬
        DefaultTableCellRenderer celAlignCenter = new DefaultTableCellRenderer();
        celAlignCenter.setHorizontalAlignment(JLabel.CENTER);
        trackTable.getColumnModel().getColumn(0).setCellRenderer(celAlignCenter);
        trackTable.getColumnModel().getColumn(1).setCellRenderer(celAlignCenter);
        trackTable.getColumnModel().getColumn(2).setCellRenderer(celAlignCenter);

        trackTable.getColumnModel().getColumn(0).setPreferredWidth(10);
        trackTable.getColumnModel().getColumn(1).setPreferredWidth(100);
        trackTable.getColumnModel().getColumn(2).setPreferredWidth(200);
        trackTable.setDefaultEditor(Object.class, new MyCellEditor());
        underPanel = new JScrollPane(trackTable);
        mainPanel.add(upperPanel, new LinearConstraints().setWeight(1).setLinearSpace(LinearSpace.MATCH_PARENT));
        mainPanel.add(underPanel, new LinearConstraints().setWeight(5).setLinearSpace(LinearSpace.MATCH_PARENT));

        add(mainPanel);
        setVisible(true);

        if (auto)
            upperPanel.trackBtn.doClick();
    }
    public static DefaultTableModel getTrackTableModel() {
        return trackTableModel;
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
