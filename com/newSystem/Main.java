package com.newSystem;

import com.alee.laf.WebLookAndFeel;
import com.bitcoinClient.javabitcoindrpcclient.BitcoinJSONRPCClient;
import com.newSystem.Bitcoins.Bitcoind;
import com.newSystem.Bitcoins.bitcoinServer;
import com.newSystem.DB.TrackingDB;

import javax.swing.*;
import java.net.MalformedURLException;
import java.util.HashMap;

public class Main {
    public static TrackingDB db;
    public static HashMap<String, String> companyIPs;
    static public TrackingDB trackingDb;
    static public BitcoinJSONRPCClient bitcoinJSONRPCClient;

    public static void main(String[] args) throws Exception {
        UIManager.setLookAndFeel(new WebLookAndFeel());

        new Settings();
        companyIPs = new HashMap<>();

        MainFrame mainFrame = new MainFrame();

        // bincoind 실행 쓰레드
        new Bitcoind(MidPanel.getBitcoindArea()).start();

        // DB
        db = new TrackingDB();

        // bincoind로 rpc 명령을 전달하는 서버를 돌리는 쓰레드.
        new bitcoinServer().start();

        try {
            bitcoinJSONRPCClient = new BitcoinJSONRPCClient(Settings.getRpcUser(), Settings.getRpcPassword());
        } catch (MalformedURLException e) {
            System.err.println("BitcoinJSONRPCClient Constructor Error");
        }
    }
}
