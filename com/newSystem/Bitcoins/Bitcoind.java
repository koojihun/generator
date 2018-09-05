package com.newSystem.Bitcoins;

import javax.swing.*;
import java.awt.*;
import java.io.*;

public class Bitcoind extends Thread {

    private static String fileName = "bincoind.exe";
    private static Process process_bincoind;
    private JTextArea bincoind_screen;

    public Bitcoind(JTextArea bincoind_screen) {
        this.bincoind_screen = bincoind_screen;
    }

    public void run() {
        try {
            if (isBitcoindRunning())
                killBitcoind();
            String filePath = "C:\\Users\\" +
                    System.getProperty("user.name") +
                    "\\AppData\\Roaming\\Bitcoin\\bincoind.exe";
            process_bincoind = Runtime.getRuntime().exec(filePath);
        } catch (IOException e) {
            System.err.println("Bitcoind Execute error");
            System.exit(1);
        } catch (Exception e) {
            System.err.println("Already bitcoind process kill error");
            System.exit(1);
        }

        // BitcoindWriter class (= Thread) will write bitcoind result into JTextArea in infinite loop.
        BitcoindWriter writer = new BitcoindWriter(new BufferedReader(new InputStreamReader(process_bincoind.getInputStream())));
        writer.start();
    }

    public static boolean isBitcoindRunning() throws Exception {
        Process p = Runtime.getRuntime().exec("TASKLIST");
        BufferedReader reader = new BufferedReader(new InputStreamReader(p.getInputStream()));
        String line;
        while ((line = reader.readLine()) != null)
            if (line.contains(fileName))
                return true;
        return false;
    }

    public static void killBitcoind() throws Exception {
        Process p = Runtime.getRuntime().exec("TASKKILL /F /IM bincoind.exe");
        /* !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
           Under this line, should wait for killing already running bitcoind.
           !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!! */
        p.waitFor();
        System.out.println("running bitcoind is killed");
    }

    // BitcoinWriter class (= thread) will write bitcoind result into JTextArea in infinite loop.
    public class BitcoindWriter extends Thread {
        private BufferedReader bufferedReader;

        public BitcoindWriter(BufferedReader bufferedReader) {
            this.bufferedReader = bufferedReader;
        }

        @Override
        public void run() {
            String s;
            try {
                while ((s = bufferedReader.readLine()) != null)
                    bincoind_screen.append(s + "\n");
            } catch (IOException e) {
                System.err.println("redirection error!!");
                System.exit(1);
            }
        }
    }
}