package com.newSystem;

import com.mommoo.flat.layout.linear.LinearLayout;
import com.mommoo.flat.layout.linear.Orientation;

import javax.swing.*;
import javax.swing.border.LineBorder;
import java.awt.*;


public class TopPanel extends JPanel {
    private TopPanelButtons btns;
    TopPanel() {
        setLayout(new LinearLayout(Orientation.HORIZONTAL, 0));
        btns = new TopPanelButtons(this);
        btns.addBtns();
        setVisible(true);
        setBorder(new LineBorder(Color.GRAY, 1, false));
    }
}
