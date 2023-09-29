package gui;

import com.formdev.flatlaf.extras.FlatSVGIcon;

import javax.swing.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class FormMessage extends JFrame{
    public JPanel panel1;
    private JLabel label1;
    private JLabel label2;

    public FormMessage(String s){
        label1.setText(s);
        FlatSVGIcon ic_ok = new FlatSVGIcon("icons8-ok.svg", 16, 16);
        label2.setIcon(ic_ok);
        label2.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                FormMessage.this.dispose();
            }
        });
    }

}
