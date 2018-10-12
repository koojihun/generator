package com.newSystem;

import com.alee.laf.WebLookAndFeel;
import com.bitcoinClient.javabitcoindrpcclient.BitcoinJSONRPCClient;
import com.newSystem.Bitcoins.Bitcoind;
import com.newSystem.Bitcoins.bitcoinServer;
import com.newSystem.DB.CompanyDB;
import com.newSystem.DB.TrackingDB;

import javax.swing.*;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.net.MalformedURLException;
import java.util.HashMap;

public class Main {
    public static HashMap<String, String> companyIPs;
    public static HashMap<String, String> companyAddresses;
    static public TrackingDB trackingDB;
    static public CompanyDB companyDB;
    static public BitcoinJSONRPCClient bitcoinJSONRPCClient;

    public static void main(String[] args) throws Exception {
        UIManager.setLookAndFeel(new WebLookAndFeel());

        companyIPs = new HashMap<>();
        companyAddresses = new HashMap<>();

        // DB
        trackingDB = new TrackingDB();
        companyDB = new CompanyDB();

        new Settings();

        companyDB.getCompanyNamesAndAddresses();

        MainFrame mainFrame = new MainFrame();

        // bincoind 실행 쓰레드
        new Bitcoind(MidPanel.getBitcoindArea()).start();

        // bincoind로 rpc 명령을 전달하는 서버를 돌리는 쓰레드.
        new bitcoinServer().start();

        try {
            bitcoinJSONRPCClient = new BitcoinJSONRPCClient(Settings.getRpcUser(), Settings.getRpcPassword());
        } catch (MalformedURLException e) {
            System.err.println("BitcoinJSONRPCClient Constructor Error");
        }

        if (Settings.companyAddress == null) {
            while (Settings.companyAddress == null) {
                try {
                    Settings.companyAddress = bitcoinJSONRPCClient.get_new_address(Settings.companyName);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            String filePath = "C:\\Users\\" + Settings.getUserNmae() + "\\AppData\\Roaming\\Bitcoin\\bitcoin.conf";
            BufferedWriter bw = new BufferedWriter(new FileWriter(filePath, true));
            bw.newLine();
            bw.write("companyAddress=" + Settings.companyAddress);
            bw.close();
            companyDB.insertAddress(Settings.companyName, Settings.companyAddress);
        }
    }
}
