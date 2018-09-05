package com.newSystem.Dialogs;

import com.bitcoinClient.javabitcoindrpcclient.BitcoindRpcClient;
import com.newSystem.Main;
import com.newSystem.MainFrame;

import javax.swing.*;
import java.awt.*;
import java.text.SimpleDateFormat;
import java.util.Map;

public class TransactionDialog extends JDialog {
    public TransactionDialog(String txID) {
        setTitle("Transaction Information");
        setLocation(200, 200);
        setSize(950, 500);
        // Icon 설정
        setIconImage(Toolkit.getDefaultToolkit().getImage(getClass().getClassLoader().getResource("icon.png")));
        //////////////////////////////////////////////////////////////////////////////////////////////////////////////
        BitcoindRpcClient.Transaction transaction = Main.bitcoinJSONRPCClient.get_transaction(txID, true);
        java.util.List<Map> maps = transaction.details();
        Map send = maps.get(0);
        Map receive = maps.get(1);
        String sendAddress = (String) send.get("address");
        String sendAccount = (String) send.get("account");
        String receiveAddress = (String) receive.get("address");
        String receiveAccount = (String) receive.get("account");
        Map product = (Map) receive.get("product received");
        //////////////////////////////////////////////////////////////////////////////////////////////////////////////
        DialogDefaultPanel panel = new DialogDefaultPanel(10, DialogDefaultPanel.DIALOG.TXINFO);
        panel.makeNonEmptyLine("Transaction ID", transaction.txId(), false);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd, hh:mm:ss a");
        panel.makeNonEmptyLine("Send Time", sdf.format(transaction.time()), false);
        panel.makeNonEmptyLine("Sender Account", sendAccount, false);
        panel.makeNonEmptyLine("Sender Address", sendAddress, false);
        panel.makeNonEmptyLine("Receive Time", sdf.format(transaction.timeReceived()), false);
        panel.makeNonEmptyLine("Receiver Account", receiveAccount, false);
        panel.makeNonEmptyLine("Receiver Address", receiveAddress, false);
        panel.makeNonEmptyLine("Product Id", product.get("PID").toString(), false);
        panel.makeButtonLine();
        //////////////////////////////////////////////////////////////////////////////////////////////////////////////
        add(panel);
        setVisible(true);
        setVisible(true);
    }
}
