package gui;

import com.formdev.flatlaf.extras.FlatSVGIcon;
import org.example.*;

import javax.swing.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class FormTerminal extends JFrame {
    public JPanel panel1;
    private JLabel label1;
    private JTextPane textPane1;
    String alias, key_path;
    SuccessListener listener;

    public FormTerminal(SuccessListener listener, ParentModel model) {
        this.listener = listener;
        textPane1.setEditable(false);
        runTerminal(model);
        alias = model.getAlias();
        key_path = model.getKey_store();
    }

    private void runTerminal(ParentModel model) {
        new Thread(() -> {
            boolean result;
            if (model.getClass().equals(KeyModel.class))
                result = new Commands(textPane1).generateKey(model);
            else if (model.getClass().equals(SignModel.class))
                result = new Commands(textPane1).sign(model);
            else
                result = false;

            result(result);
            onClick(result);
        }).start();
    }

    private void result(boolean success) {
        label1.setText("");
        FlatSVGIcon ic;
        if (success) {
            ic = new FlatSVGIcon("icons8-ok.svg", 24, 24);
            Config.aliasName = alias;
            Config.keyStorePath = key_path;
        } else {
            ic = new FlatSVGIcon("icons8-close.svg", 24, 24);
        }
        label1.setIcon(ic);
    }

    private void onClick(boolean a) {
        label1.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                FormTerminal.this.dispose();
                if (a)
                    listener.OnSuccess();
            }
        });
    }
}
