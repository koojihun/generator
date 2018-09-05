package com.newSystem;

import com.mommoo.flat.layout.linear.LinearLayout;
import com.mommoo.flat.layout.linear.Orientation;
import com.mommoo.flat.layout.linear.constraints.LinearConstraints;
import com.mommoo.flat.layout.linear.constraints.LinearSpace;
import com.newSystem.Bitcoins.Bitcoind;
import com.bitcoinClient.javabitcoindrpcclient.BitcoinJSONRPCClient;
import javax.swing.*;
import java.net.MalformedURLException;

public class MainFrame extends JFrame {
    private JPanel mainPanel;
    static private TopPanel topPanel;
    static private MidPanel midPanel;
    private MenuBar menuBar;

    MainFrame() {
        super.setSize(1200, 800);
        setLocationRelativeTo(null); // 화면 중앙에 생기게 하는 옵션.

        setTitle("MSNet");

        // Icon 설정
        setIconImage(Settings.icon);

        menuBar = new MenuBar(this); // 메뉴바 생성
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // 종료 버튼 누를 시 종료 되되록 하는 옵션.

        // When this program is killed, bitcoind.exe should be killed also.
        // Add window listener to kill the bincoind.exe when alt + f4 or close button is pressed.
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowClosing(java.awt.event.WindowEvent windowEvent) {
                try {
                    if (Bitcoind.isBitcoindRunning())
                        Bitcoind.killBitcoind();
                } catch (Exception e) {
                    System.out.println("window closing event listener error");
                }
            }
        });

        mainPanel = new JPanel();
        mainPanel.setLayout(new LinearLayout(Orientation.VERTICAL, 0));
        mainPanel.add(topPanel = new TopPanel(), new LinearConstraints().setLinearSpace(LinearSpace.MATCH_PARENT));
        topPanel.setSize(0, 90);
        mainPanel.add(midPanel = new MidPanel(), new LinearConstraints().setWeight(1).setLinearSpace(LinearSpace.MATCH_PARENT));
        add(mainPanel);

        setVisible(true);
    }

    public static MidPanel getMidPanel() {
        return midPanel;
    }
}