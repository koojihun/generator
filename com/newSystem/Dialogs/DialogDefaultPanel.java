package com.newSystem.Dialogs;

import com.mommoo.flat.layout.linear.LinearLayout;
import com.mommoo.flat.layout.linear.Orientation;
import com.mommoo.flat.layout.linear.constraints.LinearConstraints;
import com.mommoo.flat.layout.linear.constraints.LinearSpace;
import com.newSystem.Bitcoins.Bitcoind;
import com.newSystem.MainFrame;
import com.newSystem.MidPanel;
import com.newSystem.Settings;
import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.*;
import java.net.HttpURLConnection;
import java.util.*;
import java.net.URL;
import java.util.List;

import static com.newSystem.Main.bitcoinJSONRPCClient;

public class DialogDefaultPanel extends JPanel {
    public enum DIALOG {
        ADDPRODUCT, ADDMANYPRODUCT, ADDADDRESS, ADDPEER, INFO, MINING, FIND, TRACK, IMPORTADDRESS, TXINFO, SAVEADDRESS,
        SENDADDRESSTOSERVER, TRACKLOCATION, PRODUCTLIST, SIGNUP
    }

    public ArrayList<JPanel> eachLine; // if total = 4 Lines, 4th line will contain ok & cancel buttons.
    public ArrayList<JLabel> eachLabel;
    public ArrayList<JTextField> eachText;
    JButton okBtn;
    JButton cancelBtn;
    JButton trackBtn;
    int padding;
    DIALOG dialog;
    ClickListener clickListener;

    DialogDefaultPanel(int padding, DIALOG dialog) {
        this.padding = padding;
        eachLine = new ArrayList<JPanel>();
        eachLabel = new ArrayList<JLabel>();
        eachText = new ArrayList<JTextField>();
        this.dialog = dialog;
        setLayout(new LinearLayout(Orientation.VERTICAL, 10));
        clickListener = new ClickListener();
    }

    public void makeEmptyLine() {
        JPanel targetLine;
        eachLine.add(targetLine = new JPanel(new LinearLayout(Orientation.HORIZONTAL, 10)));
        eachLabel.add(new JLabel());
        eachText.add(new JTextField());
        targetLine.setBorder(BorderFactory.createEmptyBorder(padding, padding, padding, padding));
        add(targetLine, new LinearConstraints().setWeight(1).setLinearSpace(LinearSpace.MATCH_PARENT));
    }

    public void makePasswordLine(String l) {
        JPanel targetLine;
        eachLine.add(targetLine = new JPanel(new LinearLayout(Orientation.HORIZONTAL, 10)));
        JLabel targetLabel;
        eachLabel.add(targetLabel = new JLabel(l));

        JPasswordField targetText = new JPasswordField();
        eachText.add(targetText);

        targetLabel.setHorizontalAlignment(SwingConstants.RIGHT);
        targetLabel.setVerticalAlignment(SwingConstants.CENTER);
        targetLabel.setFont(Settings.Font14);
        targetText.setFont(Settings.Font14);

        targetLine.add(targetLabel, new LinearConstraints().setWeight(4).setLinearSpace(LinearSpace.WRAP_CENTER_CONTENT));
        targetLine.add(targetText, new LinearConstraints().setWeight(6).setLinearSpace(LinearSpace.WRAP_CENTER_CONTENT));
        targetLine.setBorder(BorderFactory.createEmptyBorder(padding + 10, padding, padding, padding));
        add(targetLine, new LinearConstraints().setWeight(1).setLinearSpace(LinearSpace.MATCH_PARENT));
    }
    public void makeExplainLine(String s) {
        JPanel targetLine;
        JLabel targetLabel;
        eachLine.add(targetLine = new JPanel(new LinearLayout(Orientation.VERTICAL, 10)));
        eachLabel.add(targetLabel = new JLabel(s));
        eachText.add(new JTextField());
        targetLabel.setHorizontalAlignment(SwingConstants.CENTER);
        targetLabel.setVerticalAlignment(SwingConstants.CENTER);
        targetLabel.setFont(Settings.Font18);
        targetLine.add(targetLabel, new LinearConstraints().setWeight(1).setLinearSpace(LinearSpace.WRAP_CENTER_CONTENT));
        targetLine.setBorder(BorderFactory.createEmptyBorder(padding + 10, padding, padding, padding));
        add(targetLine, new LinearConstraints().setWeight(1).setLinearSpace(LinearSpace.MATCH_PARENT));
    }
    public void makeNonEmptyLine(String l, String t, boolean textFieldChange) {
        JPanel targetLine;
        eachLine.add(targetLine = new JPanel(new LinearLayout(Orientation.HORIZONTAL, 10)));
        JLabel targetLabel;
        eachLabel.add(targetLabel = new JLabel(l));
        JTextField targetText;
        eachText.add(targetText = new JTextField());

        targetLabel.setHorizontalAlignment(SwingConstants.RIGHT);
        targetLabel.setVerticalAlignment(SwingConstants.CENTER);
        targetLabel.setFont(Settings.Font14);
        targetText.setFont(Settings.Font14);
        targetText.setText(t);

        if (!textFieldChange)
            targetText.setEditable(false);

        targetLine.add(targetLabel, new LinearConstraints().setWeight(4).setLinearSpace(LinearSpace.WRAP_CENTER_CONTENT));
        targetLine.add(targetText, new LinearConstraints().setWeight(6).setLinearSpace(LinearSpace.WRAP_CENTER_CONTENT));
        targetLine.setBorder(BorderFactory.createEmptyBorder(padding + 10, padding, padding, padding));
        add(targetLine, new LinearConstraints().setWeight(1).setLinearSpace(LinearSpace.MATCH_PARENT));
    }

    public void makeButtonLine() {
        JPanel targetLine;
        eachLine.add(targetLine = new JPanel(new LinearLayout(Orientation.HORIZONTAL, 10)));

        targetLine.setBorder(BorderFactory.createEmptyBorder(padding, padding, padding, padding));
        targetLine.add(okBtn = new JButton("OK"), new LinearConstraints().setWeight(1).setLinearSpace(LinearSpace.WRAP_CENTER_CONTENT));
        okBtn.setFont(Settings.Font14);
        okBtn.setFocusPainted(false);
        okBtn.addActionListener(clickListener);

        if (dialog != DIALOG.INFO && dialog != DIALOG.MINING && dialog != DIALOG.FIND && dialog != DIALOG.TRACK && dialog != DIALOG.TXINFO) {
            targetLine.add(cancelBtn = new JButton("CANCEL"), new LinearConstraints().setWeight(1).setLinearSpace(LinearSpace.WRAP_CENTER_CONTENT));
            cancelBtn.setFont(Settings.Font14);
            cancelBtn.setFocusPainted(false);
            cancelBtn.addActionListener(clickListener);
        }
        add(targetLine, new LinearConstraints().setWeight(1).setLinearSpace(LinearSpace.MATCH_PARENT));
    }

    public void makeTrackButtonLine() {
        JPanel targetLine;
        eachLine.add(targetLine = new JPanel(new LinearLayout(Orientation.HORIZONTAL, 10)));
        targetLine.setBorder(BorderFactory.createEmptyBorder(0, padding, 0, padding));
        targetLine.add(trackBtn = new JButton("Track"), new LinearConstraints().setWeight(4).setLinearSpace(LinearSpace.WRAP_CENTER_CONTENT));
        trackBtn.setFont(Settings.Font14);
        trackBtn.setFocusPainted(false);
        trackBtn.addActionListener(clickListener);
        add(targetLine, new LinearConstraints().setWeight(1).setLinearSpace(LinearSpace.MATCH_PARENT));
    }

    class ClickListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            JButton clicked = (JButton) e.getSource();
            // Info 와 Mining 창은 버튼이 1개이므로 눌리면 바로 종료.
            if (clicked == cancelBtn || dialog == DIALOG.INFO || dialog == DIALOG.MINING || dialog == DIALOG.TXINFO)
                // 해당 다이얼로그 종료.
                SwingUtilities.getWindowAncestor(clicked).dispose();
            else if (clicked == okBtn) {
                if (dialog == DIALOG.ADDADDRESS) {
                    // add 창에서 address 추가인 경우 첫번째라인은 비어 있음.
                    String account = eachText.get(0).getText();
                    bitcoinJSONRPCClient.get_new_address(account);
                    SwingUtilities.getWindowAncestor(clicked).dispose();
                } else if (dialog == DIALOG.ADDPRODUCT) {
                    // add 창에서 product 추가인 경우.
                    String prodDate = eachText.get(0).getText();
                    String expDate = eachText.get(1).getText();
                    String count = eachText.get(2).getText();
                    // Product info가 빈칸일 때 경고 메시지.
                    if (prodDate.equals("") || expDate.equals("") || count.equals("")) {
                        JOptionPane.showMessageDialog(null,
                                "Insert product information.",
                                "Message", JOptionPane.WARNING_MESSAGE);
                    } else if (prodDate.length() != 15 || expDate.length() != 15) {
                        JOptionPane.showMessageDialog(null,
                                "Please match the format(ex 20180821T101621).",
                                "Message", JOptionPane.WARNING_MESSAGE);
                    } else {
                        try {
                            FileWriter fw = new FileWriter("C:\\Users\\" + Settings.getUserNmae() + "\\AppData\\Roaming\\Bitcoin\\ProductList.txt", true);
                            for (int cnt = 0; cnt < Integer.valueOf(count); cnt++) {
                                String tmpPID = bitcoinJSONRPCClient.gen_new_product(prodDate, expDate);
                                fw.write(tmpPID + "\r\n");
                            }
                            fw.close();
                        } catch (IOException e1) {
                            e1.printStackTrace();
                        }
                        SwingUtilities.getWindowAncestor(clicked).dispose();
                    }
                } else if (dialog == DIALOG.IMPORTADDRESS) {
                    String account = eachText.get(0).getText();
                    String address = eachText.get(1).getText();
                    // account 또는 address가 빈칸일 때 경고 메시지.
                    if (account.equals("") || address.equals("")) {
                        JOptionPane.showMessageDialog(null, "Insert Account and Address.", "Message", JOptionPane.WARNING_MESSAGE);
                    } else {
                        bitcoinJSONRPCClient.importAddress(address, account, true);
                        SwingUtilities.getWindowAncestor(clicked).dispose();
                        new ImportDialog();
                    }
                } else if (dialog == DIALOG.ADDPEER) {
                    // 아무것도 입력하지 않았을 때 경고 메시지.
                    if (eachText.get(1).getText().equals("")) {
                        JOptionPane.showMessageDialog(null, "Insert IP address.", "Message", JOptionPane.WARNING_MESSAGE);
                    } else {
                        try {
                            ////////////////////////////////////////////////////////////////////////////////////
                            String fileName = "C:\\Users\\" +
                                    System.getProperty("user.name") +
                                    "\\AppData\\Roaming\\Bitcoin\\bitcoin.conf";
                            BufferedWriter fw = new BufferedWriter(new FileWriter(fileName, true));
                            // 파일안에 문자열 쓰기
                            fw.newLine();
                            fw.write("addnode=" + eachText.get(1).getText());
                            fw.newLine();
                            fw.flush();
                            // 객체 닫기
                            fw.close();
                            ////////////////////////////////////////////////////////////////////////////////////
                            try {
                                Bitcoind.killBitcoind();
                                new Bitcoind(MidPanel.getBitcoindArea()).start();
                            } catch (Exception exception) {
                                System.err.println("Error : kill bitcoind.exe in add peer.");
                            }
                            ////////////////////////////////////////////////////////////////////////////////////
                            SwingUtilities.getWindowAncestor(clicked).dispose();
                        } catch (IOException ex) {
                            System.err.println("Error : Add Node Error.");
                        }
                    }
                } else if (dialog == DIALOG.SAVEADDRESS) {
                    String name = eachText.get(1).getText();
                    String address = eachText.get(2).getText();
                    if (name.length() == 0 || address.length() == 0)
                        return;
                    String[] addRow = {name, address};
                    AddressDialog.savedAddressTableModel.addRow(addRow);
                    SwingUtilities.getWindowAncestor(clicked).dispose();
                } else if (dialog == DIALOG.SENDADDRESSTOSERVER) {
                    String companyName = eachText.get(1).getText();
                    if (companyName.length() == 0) {
                        JOptionPane.showMessageDialog(null, "Insert name of Company.", "Message", JOptionPane.WARNING_MESSAGE);
                    } else {
                        String url = "http://166.104.126.21:9999/?method=0&account=" + companyName + "&address=" + bitcoinJSONRPCClient.get_account_address("");
                        try {
                            URL obj = new URL(url);
                            HttpURLConnection con = (HttpURLConnection) obj.openConnection();
                            con.setRequestMethod("GET");
                            //add request header 헤더를 만들어주는것.
                            con.setRequestProperty("User-Agent", "Chrome/version");
                            con.setRequestProperty("Accept-Charset", "UTF-8");
                            con.setRequestProperty("Content-Type", "text/plain; charset=utf-8");
                            int responseCode = con.getResponseCode();
                            BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
                            String inputLine;
                            StringBuffer response = new StringBuffer();
                            while ((inputLine = in.readLine()) != null) {
                                response.append(inputLine);
                            }
                            in.close();
                        } catch (IOException e1) {
                            e1.printStackTrace();
                        }
                        SwingUtilities.getWindowAncestor(clicked).dispose();
                    }
                } else if (dialog == DIALOG.TRACKLOCATION) {
                    HashMap<String, Integer> map = new HashMap<String, Integer>();
                    HashMap<String, List<String>> mapPID = new HashMap<String, List<String>>();
                    String filename = eachText.get(0).getText();

                    try {
                        BufferedReader br = new BufferedReader(new FileReader("C:\\Users\\" + Settings.getUserNmae() + "\\AppData\\Roaming\\Bitcoin\\" + filename));
                        while (true) {
                            String pID = br.readLine();
                            if (pID == null) {
                                break;
                            } else {
                                List<Map> track_prouct_Result = bitcoinJSONRPCClient.track_product(pID);
                                ArrayList<String> tmp;
                                if (track_prouct_Result.size() != 0) {
                                    String userID = track_prouct_Result.get(0).get("Where").toString();
                                    if (map.containsKey(userID)) {
                                        map.put(userID, map.get(userID) + 1);
                                        mapPID.get(userID).add(pID);
                                    } else {
                                        map.put(track_prouct_Result.get(0).get("Where").toString(), 1);
                                        tmp = new ArrayList<>();
                                        mapPID.put(userID, tmp);
                                        mapPID.get(userID).add(pID);
                                    }
                                }
                            }
                        }
                        br.close();
                    } catch (IOException e1) {
                        e1.printStackTrace();
                    }
                    //가운데정렬
                    DefaultTableCellRenderer celAlignCenter = new DefaultTableCellRenderer();
                    celAlignCenter.setHorizontalAlignment(JLabel.CENTER);

                    TrackLocationDialog.getTrackLocationTableModel().setNumRows(0);
                    Iterator<String> itr = map.keySet().iterator();
                    String[] row = new String[3];
                    int count = 0;

                    while (itr.hasNext()) {
                        String key = (String) itr.next();
                        int value = map.get(key);
                        count++;
                        row[0] = String.valueOf(count);
                        row[1] = key;
                        row[2] = String.valueOf(value);
                        TrackLocationDialog.trackLocationTable.getColumnModel().getColumn(0).setCellRenderer(celAlignCenter);
                        TrackLocationDialog.trackLocationTable.getColumnModel().getColumn(1).setCellRenderer(celAlignCenter);
                        TrackLocationDialog.trackLocationTable.getColumnModel().getColumn(2).setCellRenderer(celAlignCenter);
                        TrackLocationDialog.getTrackLocationTableModel().addRow(row);
                    }

                    TrackLocationDialog.getTrackLocationTable().addMouseListener(new MouseListener() {
                        @Override
                        public void mouseClicked(MouseEvent e) {
                            if (e.getClickCount() == 2) {

                                int productIdx = TrackLocationDialog.getTrackLocationTable().getSelectedRow();
                                String company = TrackLocationDialog.getTrackLocationTable().getValueAt(productIdx, 1).toString();

                                new ProductListDialog();
                                ProductListDialog.getUpperPanel().makeNonEmptyLine("Company", company, false);
                                for (int i = 0; i < mapPID.get(company).size(); i++) {
                                    String[] row = new String[2];
                                    row[0] = (i + 1) + "";
                                    row[1] = mapPID.get(company).get(i);
                                    ProductListDialog.productListTable.getColumnModel().getColumn(0).setCellRenderer(celAlignCenter);
                                    ProductListDialog.productListTable.getColumnModel().getColumn(1).setCellRenderer(celAlignCenter);
                                    ProductListDialog.getProductListTableModel().addRow(row);
                                }
                            }
                        }

                        @Override
                        public void mousePressed(MouseEvent e) {
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
                }
            } else if (clicked == trackBtn) {
                // table을 초기화 시켜주기 위해서
                int rowCount = TrackDialog.getTrackTableModel().getRowCount();
                for (int i = rowCount - 1; i >= 0; i--) {
                    TrackDialog.getTrackTableModel().removeRow(i);
                }
                String id = eachText.get(0).getText();
                // Product ID를 빈칸으로 뒀을 때 경고 메시지.
                if (id.equals("")) {
                    JOptionPane.showMessageDialog(null, "Insert Product ID.", "Message", JOptionPane.WARNING_MESSAGE);
                } else {
                    try {
                        List<Map> result = bitcoinJSONRPCClient.track_product(id);
                        int resultSize = result.size();
                        int count = 0;
                        String[][] rows = new String[result.size()][3];
                        for (Map map : result) {
                            rows[count][0] = String.valueOf(resultSize);
                            rows[count][1] = String.valueOf(map.get("Where"));
                            String time_tmp = String.valueOf(map.get("Time"));
                            String time = time_tmp.substring(0,4) + "-" + time_tmp.substring(4, 6) + "-" + time_tmp.substring(6, 8) + " " + time_tmp.substring(9, 11) + ":" + time_tmp.substring(11, 13) + ":" + time_tmp.substring(13, 15);
                            rows[count][2] = time;

                            count++;
                            resultSize--;
                        }
                        for (int i = result.size() - 1; i >= 0; i--)
                            TrackDialog.getTrackTableModel().addRow(rows[i]);
                    } catch (ClassCastException err) {
                        JOptionPane.showMessageDialog(null, "There is no product " + id, "Message", JOptionPane.WARNING_MESSAGE);
                    }
                }
            }
        }
    }
}
