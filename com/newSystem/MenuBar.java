package com.newSystem;

import com.newSystem.Bitcoins.Bitcoind;
import com.QRCode.QRMaker;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.*;
import java.util.List;
import java.util.Map;

/* menu bar를 관장하는 클래스 */
class MenuBar {
    private JMenuBar menuBar;

    private JMenu menuFile;
    private JMenu menuWindow;
    private JMenu menuHelp;
    // [File]
    private JMenuItem menuItem_File_qr;
    private JMenuItem menuItem_File_Open;
    private JMenuItem menuItem_File_BitcoindRestart;
    private JMenuItem menuItem_File_Exit;
    // [Window]
    private JMenuItem menuItem_Window_Preference;
    // [Help]
    private JMenuItem menuItem_Help_Licenses;

    MenuBar(JFrame mainFrame) {
        menuBar = new JMenuBar();
        menuFile = new JMenu("File");
        menuFile.setFont(Settings.Font14);
        menuWindow = new JMenu("Window");
        menuWindow.setFont(Settings.Font14);
        menuHelp = new JMenu("Help");
        menuHelp.setFont(Settings.Font14);
        menuBar.add(menuFile);
        menuBar.add(menuWindow);
        menuBar.add(menuHelp);

        // File Menu
        // [File - Open...]: Open bitcoin folder
        menuItem_File_Open = new JMenuItem("Open...");
        menuItem_File_Open.setFont(Settings.Font14);
        menuFile.add(menuItem_File_Open);

        menuItem_File_Open.addMouseListener(new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent e) {
            }

            @Override
            public void mousePressed(MouseEvent e) {
                String str = "C:\\Users\\" + Settings.getUserNmae() + "\\AppData\\Roaming\\Bitcoin";
                File confFile = new File(str);
                Desktop desktop = Desktop.getDesktop();
                if (confFile.exists()) {
                    try {
                        desktop.open(confFile);
                    } catch (IOException e1) {
                        e1.printStackTrace();
                    }
                } else {
                    System.out.println("not exist");
                }
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
        // [File - Bitcoind Restart]: Bitcoind restart
        menuItem_File_BitcoindRestart = new JMenuItem("Bitcoind Restart");
        menuItem_File_BitcoindRestart.setFont(Settings.Font14);
        menuFile.add(menuItem_File_BitcoindRestart);
        menuItem_File_BitcoindRestart.addMouseListener(new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent e) {

            }

            @Override
            public void mousePressed(MouseEvent e) {
                try {
                    Bitcoind.killBitcoind();
                    new Bitcoind(MainFrame.getMidPanel().getBitcoindArea()).start();
                } catch (Exception e1) {
                    e1.printStackTrace();
                }
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
        // File Menu
        // [File - QR Code Generate]: Create QR Codes.
        menuItem_File_qr = new JMenuItem("QR Code generate");
        menuItem_File_qr.setFont(Settings.Font14);
        menuFile.add(menuItem_File_qr);
        menuItem_File_qr.addMouseListener(new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent e) {

            }

            @Override
            public void mousePressed(MouseEvent e) {
                QRMaker qrMaker = new QRMaker(400, 400);
                List<Map> maps = Main.bitcoinJSONRPCClient.get_current_products();
                for (Map m : maps) {
                    String pid = String.valueOf(m.get("PID"));
                    qrMaker.makeQR(pid, pid); // file name, content
                }
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

        // [File - Exit]: End program
        menuItem_File_Exit = new JMenuItem("Exit");
        menuItem_File_Exit.setFont(Settings.Font14);
        menuFile.add(menuItem_File_Exit);
        menuItem_File_Exit.addMouseListener(new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent e) {

            }

            @Override
            public void mousePressed(MouseEvent e) {
                try {
                    Bitcoind.killBitcoind();
                    System.exit(0);
                } catch (Exception e1) {
                    e1.printStackTrace();
                }
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

        // Window Menu
        menuItem_Window_Preference = new JMenuItem("Preference");
        menuItem_Window_Preference.setFont(Settings.Font14);
        menuWindow.add(menuItem_Window_Preference);

        // Help Menu
        menuItem_Help_Licenses = new JMenuItem("Licenses");
        menuItem_Help_Licenses.setFont(Settings.Font14);
        menuHelp.add(menuItem_Help_Licenses);
        menuItem_Help_Licenses.addMouseListener(new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent e) {
            }

            @Override
            public void mousePressed(MouseEvent e) {
                LicensesDialog licensesDialog = new LicensesDialog();
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

        //menuItem = new JMenuItem(“메뉴항목1”, KeyEvent.VK_T); ->키보드 단축키 설정 가능
        mainFrame.setJMenuBar(menuBar);                            //프레임에 메뉴바 설정
    }
    class LicensesDialog extends JDialog {

        public LicensesDialog() {
            setTitle("Licenses");
            setLocation(200, 200);
            setSize(700, 600);
            setBackground(Color.BLACK);

            JTextArea textArea = new JTextArea();
            textArea.setEditable(false);
            textArea.setBackground(Color.BLACK);
            textArea.setForeground(Color.WHITE);
            JScrollPane scrollPane = new JScrollPane(textArea);
            add(scrollPane);

            try {
                BufferedReader br = new BufferedReader(new FileReader("C:\\Users\\" + Settings.getUserNmae() + "\\AppData\\Roaming\\Bitcoin\\Licenses.txt"));
                while (true) {
                    String line = br.readLine();
                    if (line == null) {
                        break;
                    } else {
                        textArea.append(line + "\n");
                    }
                }
                br.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            setVisible(true);
        }
    }
}