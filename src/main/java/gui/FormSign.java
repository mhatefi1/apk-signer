package gui;

import com.formdev.flatlaf.extras.FlatSVGIcon;
import org.example.Config;
import org.example.SuccessListener;
import org.example.SignModel;

import javax.swing.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;

public class FormSign extends JFrame implements SuccessListener {
    public JPanel panel1;
    private JTextField t_key_store_path;
    private JTextField t_alias_name;
    private JTextField t_apk_path;
    private JLabel l_key_store_path;
    private JLabel l_apk_path;
    private JLabel t_sign;
    private JLabel l_back;
    private final JFrame parent;

    public FormSign(JFrame parent) {
        this.parent = parent;
        FlatSVGIcon ic_back = new FlatSVGIcon("icons8-back.svg", 16, 16);
        FlatSVGIcon ic_folder = new FlatSVGIcon("icons8-folder.svg", 16, 16);
        FlatSVGIcon ic_create = new FlatSVGIcon("icons8-create.svg", 24, 24);
        l_apk_path.setIcon(ic_folder);
        l_key_store_path.setIcon(ic_folder);
        t_sign.setIcon(ic_create);
        l_back.setIcon(ic_back);
        t_alias_name.setText(Config.aliasName);
        t_key_store_path.setText(Config.keyStorePath);
        t_apk_path.setText(System.getProperty("user.dir"));
        l_back.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                new Config().Close(parent, FormSign.this);
            }
        });
        l_key_store_path.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                JFileChooser j = new JFileChooser(System.getProperty("user.dir"));
                j.setFileSelectionMode(JFileChooser.FILES_ONLY);
                int r = j.showDialog(null, "select");
                if (r == JFileChooser.APPROVE_OPTION) {
                    t_key_store_path.setText(j.getSelectedFile().getAbsolutePath());
                }
            }
        });
        l_apk_path.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                JFileChooser j = new JFileChooser(System.getProperty("user.dir"));
                j.setFileSelectionMode(JFileChooser.FILES_ONLY);
                int r = j.showDialog(null, "select");
                if (r == JFileChooser.APPROVE_OPTION) {
                    t_apk_path.setText(j.getSelectedFile().getAbsolutePath());
                }
            }
        });
        t_sign.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                boolean flag = true;
                if (!new File(t_key_store_path.getText()).isFile()) {
                    FormMessage formDialogue = new FormMessage("please select key store file");
                    new Config().InitialiseDialogue(null, formDialogue, formDialogue.panel1);
                    flag = false;
                } else if (t_alias_name.getText().isEmpty()) {
                    FormMessage formDialogue = new FormMessage("please enter alias name");
                    new Config().InitialiseDialogue(null, formDialogue, formDialogue.panel1);
                    flag = false;
                } else if (!new File(t_apk_path.getText()).isFile()) {
                    FormMessage formDialogue = new FormMessage("please select apk file");
                    new Config().InitialiseDialogue(null, formDialogue, formDialogue.panel1);
                    flag = false;
                }
                if (flag) {
                    SignModel signModel = new SignModel(t_alias_name.getText(), t_key_store_path.getText(), t_apk_path.getText());
                    FormTerminal formTerminal = new FormTerminal(FormSign.this, signModel);
                    new Config().InitialiseTerminal(formTerminal, formTerminal.panel1);
                }
            }
        });
    }

    @Override
    public void OnSuccess() {
        new Config().Close(parent, FormSign.this);
    }
}
