package com.newSystem.Dialogs;

import com.mommoo.flat.layout.linear.LinearLayout;
import com.mommoo.flat.layout.linear.Orientation;
import com.mommoo.flat.layout.linear.constraints.LinearConstraints;
import com.mommoo.flat.layout.linear.constraints.LinearSpace;
import com.newSystem.Settings;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;

public class TrackLocationDialog extends JDialog {
    private JPanel mainPanel;
    private DialogDefaultPanel upperPanel;
    private JScrollPane underPanel;
    public static DefaultTableModel trackLocationModel;
    public static JTable trackLocationTable;

    public TrackLocationDialog() {
        setTitle("Tracking Location");
        setLocation(200, 200);
        setSize(500, 500);
        setIconImage(Settings.icon);

        mainPanel = new JPanel();
        mainPanel.setLayout(new LinearLayout(Orientation.VERTICAL, 0));

        upperPanel = new DialogDefaultPanel(20, DialogDefaultPanel.DIALOG.TRACKLOCATION);
        upperPanel.makeNonEmptyLine("File name", null, true);
        upperPanel.makeButtonLine();
        String[] col = {"No", "Company", "Number of products"};
        trackLocationModel = new DefaultTableModel(col, 0);
        trackLocationTable = new JTable(trackLocationModel);
        trackLocationTable.setRowSelectionAllowed(true);
        trackLocationTable.setDefaultEditor(Object.class, null);
        trackLocationTable.getColumnModel().getColumn(0).setPreferredWidth(10);
        trackLocationTable.getColumnModel().getColumn(1).setPreferredWidth(100);
        trackLocationTable.getColumnModel().getColumn(2).setPreferredWidth(200);
        underPanel = new JScrollPane(trackLocationTable);

        mainPanel.add(upperPanel, new LinearConstraints().setWeight(1).setLinearSpace(LinearSpace.MATCH_PARENT));
        mainPanel.add(underPanel, new LinearConstraints().setWeight(5).setLinearSpace(LinearSpace.MATCH_PARENT));

        add(mainPanel);
        setVisible(true);
    }

    public static DefaultTableModel getTrackLocationTableModel() {
        return trackLocationModel;
    }

    public static JTable getTrackLocationTable(){
        return trackLocationTable;
    }
}
