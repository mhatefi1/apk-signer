package gui;

import com.formdev.flatlaf.extras.FlatSVGIcon;
import org.example.Config;

import javax.swing.*;
import java.awt.event.*;

public class FormMain extends JFrame{
    public JPanel panel1;
    private JLabel icon;
    private JLabel key;
    private JLabel sign;

    public FormMain() {
        FlatSVGIcon ic_key = new FlatSVGIcon("icons8-key.svg", 16, 16);
        FlatSVGIcon ic_sign = new FlatSVGIcon("icons8-sign.svg", 20, 20);
        FlatSVGIcon ic_icon = new FlatSVGIcon("icons8-icon.svg", 128, 128);
        icon.setIcon(ic_icon);
        key.setIcon(ic_key);
        sign.setIcon(ic_sign);
        key.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                FormKeyGenerator f = new FormKeyGenerator(FormMain.this);
                new Config().Initialise(FormMain.this,f,f.panel1);
            }
        });

        sign.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                FormSign f = new FormSign(FormMain.this);
                new Config().Initialise(FormMain.this,f,f.panel1);
            }
        });
    }
}
