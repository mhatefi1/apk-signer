package gui;

import com.formdev.flatlaf.extras.FlatSVGIcon;
import org.example.Config;
import org.example.KeyModel;
import org.example.SuccessListener;

import javax.swing.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;

public class FormKeyGenerator extends JFrame implements SuccessListener {
    public JPanel panel1;
    private JLabel back;
    private JTextField t_alias;
    private JTextField t_name;
    private JTextField t_organization;
    private JTextField t_city;
    private JTextField t_state;
    private JTextField t_code;
    private JTextField t_path;
    private JLabel chooser;
    private JTextField t_unit;
    private JLabel t_create;
    private JRadioButton createNewKeyStoreRadioButton;
    private JTextField t_keystore_name;
    private JLabel l_keystore_name;
    private JLabel l_jks_format;
    private final JFrame parent;

    public FormKeyGenerator(JFrame parent) {
        this.parent = parent;
        t_keystore_name.setVisible(createNewKeyStoreRadioButton.isSelected());
        l_keystore_name.setVisible(createNewKeyStoreRadioButton.isSelected());
        l_jks_format.setVisible(createNewKeyStoreRadioButton.isSelected());
        FlatSVGIcon ic_back = new FlatSVGIcon("icons8-back.svg", 16, 16);
        FlatSVGIcon ic_folder = new FlatSVGIcon("icons8-folder.svg", 16, 16);
        FlatSVGIcon ic_create = new FlatSVGIcon("icons8-create.svg", 24, 24);
        back.setIcon(ic_back);
        chooser.setIcon(ic_folder);
        t_create.setIcon(ic_create);
        t_path.setText(Config.keyStorePath);
        back.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                new Config().Close(parent, FormKeyGenerator.this);
            }
        });
        chooser.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                JFileChooser j = new JFileChooser(System.getProperty("user.dir"));
                j.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);
                int r = j.showDialog(null, "select");
                if (r == JFileChooser.APPROVE_OPTION) {
                    t_path.setText(j.getSelectedFile().getAbsolutePath());
                }
            }
        });

        t_create.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                boolean isChecked = createNewKeyStoreRadioButton.isSelected();
                boolean flag = true;

                if (t_alias.getText().isEmpty()) {
                    FormMessage formDialogue = new FormMessage("please enter alias name");
                    new Config().InitialiseDialogue(null, formDialogue, formDialogue.panel1);
                    flag = false;
                } else if (isChecked) {
                    if (new File(t_path.getText()).isFile()) {
                        FormMessage formDialogue = new FormMessage("please select a directory path");
                        new Config().InitialiseDialogue(null, formDialogue, formDialogue.panel1);
                        flag = false;
                    }
                } else {
                    if (new File(t_path.getText()).isDirectory()) {
                        FormMessage formDialogue = new FormMessage("please select key store file or create new");
                        new Config().InitialiseDialogue(null, formDialogue, formDialogue.panel1);
                        flag = false;
                    }
                }
                if (flag) {
                    String key_store;
                    if (isChecked)
                        key_store = new File(t_path.getText(), t_keystore_name.getText() + ".jks").getPath();
                    else
                        key_store = t_path.getText();

                    KeyModel keyModel = new KeyModel(key_store, t_alias.getText(), t_name.getText(), t_organization.getText(),
                            t_unit.getText(), t_city.getText(), t_state.getText(), t_code.getText());
                    FormTerminal formTerminal = new FormTerminal(FormKeyGenerator.this, keyModel);
                    new Config().InitialiseTerminal(formTerminal, formTerminal.panel1);
                }
            }
        });

        createNewKeyStoreRadioButton.addChangeListener(changeEvent -> {
            t_keystore_name.setVisible(createNewKeyStoreRadioButton.isSelected());
            l_keystore_name.setVisible(createNewKeyStoreRadioButton.isSelected());
            l_jks_format.setVisible(createNewKeyStoreRadioButton.isSelected());
        });
    }

    @Override
    public void OnSuccess() {
        new Config().Close(parent, FormKeyGenerator.this);
    }
}
