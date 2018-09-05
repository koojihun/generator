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
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class MiningDialog extends JDialog {
    JButton oneBlock;
    JButton autoMining;
    DialogDefaultPanel mainPanel;

    public MiningDialog() {
        setTitle("Mining");
        setLocation(200, 200);
        setSize(500, 300);
        // Icon 설정
        setIconImage(Toolkit.getDefaultToolkit().getImage(getClass().getClassLoader().getResource("icon.png")));
        mainPanel = new DialogDefaultPanel(20, DialogDefaultPanel.DIALOG.MINING);

        ClickListener miningClick = new ClickListener();
        JPanel buttonPanel = new JPanel(new LinearLayout(Orientation.HORIZONTAL, 10));
        oneBlock = new JButton("One Block");
        buttonPanel.add(oneBlock, new LinearConstraints().setWeight(1).setLinearSpace(LinearSpace.MATCH_PARENT));
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 0, 20));
        oneBlock.setFont(Settings.Font14);
        oneBlock.setFocusPainted(false);
        oneBlock.addActionListener(miningClick);
        autoMining = new JButton("Auto Mining");
        autoMining.setFont(Settings.Font14);
        autoMining.setFocusPainted(false);
        autoMining.addActionListener(miningClick);
        buttonPanel.add(autoMining, new LinearConstraints().setWeight(1).setLinearSpace(LinearSpace.MATCH_PARENT));
        mainPanel.add(buttonPanel, new LinearConstraints().setWeight(1).setLinearSpace(LinearSpace.MATCH_PARENT));
        mainPanel.makeNonEmptyLine("Seconds", "", true);
        mainPanel.makeButtonLine();
        add(mainPanel);
        setVisible(true);
    }

    class ClickListener implements ActionListener {
        ClickListener() {}
        @Override
        public void actionPerformed(ActionEvent e) {
            JButton clicked = (JButton) e.getSource();
            if (clicked == oneBlock) {
                if (Main.bitcoinJSONRPCClient.getRawMemPool().size() == 0
                        && Main.bitcoinJSONRPCClient.get_pending_products().size() == 0) {
                    // transaction이 없을 때는 mining이 되지 않도록 막아 놓음
                    JOptionPane.showMessageDialog(null, "There are no transactions", "Message", JOptionPane.WARNING_MESSAGE);
                } else {
                    Main.bitcoinJSONRPCClient.set_generate();
                    MidPanel.getCurrentProducts();
                    dispose();
                }
            } else if (clicked == autoMining) {
                String seconds = mainPanel.eachText.get(0).getText();
                // Tx size 또는 Seconds가 빈칸일 때 경고 메시지.
                if (seconds.equals("")) {
                    JOptionPane.showMessageDialog(null, "Mining block every 100 seconds", "Message", JOptionPane.INFORMATION_MESSAGE);
                    Main.bitcoinJSONRPCClient.auto_set_generate(100);
                } else {
                    JOptionPane.showMessageDialog(null, "Mining block every " + seconds + " seconds", "Message", JOptionPane.INFORMATION_MESSAGE);
                    Main.bitcoinJSONRPCClient.auto_set_generate(Integer.parseInt(seconds));
                    dispose();
                }
            }
        }
    }
}
