package com.newSystem.Dialogs;
import com.bitcoinClient.javabitcoindrpcclient.BitcoindRpcClient;

import javax.swing.*;
import java.awt.*;

import static com.newSystem.Main.bitcoinJSONRPCClient;

public class InfoDialog extends JDialog {
    public InfoDialog() {
        setTitle("Block Chain Information");
        setLocation(200, 200);
        setSize(500, 500);
        // Icon 설정
        setIconImage(Toolkit.getDefaultToolkit().getImage(getClass().getClassLoader().getResource("icon.png")));
        DialogDefaultPanel panel = new DialogDefaultPanel(20, DialogDefaultPanel.DIALOG.INFO);
        BitcoindRpcClient.Info info = bitcoinJSONRPCClient.getInfo();
        panel.makeNonEmptyLine("Version", String.valueOf(info.version()), false);
        panel.makeNonEmptyLine("Protocol Version", String.valueOf(info.protocolVersion()), false);
        panel.makeNonEmptyLine("Blocks", String.valueOf(info.blocks()), false);
        panel.makeNonEmptyLine("Time Offset", String.valueOf(info.timeOffset()), false);
        panel.makeNonEmptyLine("Connections", String.valueOf(info.connections()), false);
        panel.makeNonEmptyLine("Proxy", String.valueOf(info.proxy()), false);
        panel.makeNonEmptyLine("Difficulty", String.valueOf(info.difficulty()), false);
        panel.makeNonEmptyLine("Testnet", String.valueOf(info.testnet()), false);
        panel.makeNonEmptyLine("Keypool Oldest", String.valueOf(info.keyPoolOldest()), false);
        panel.makeNonEmptyLine("Keypool Size", String.valueOf(info.keyPoolSize()), false);
        panel.makeButtonLine();
        add(panel);
        setVisible(true);
    }
}
