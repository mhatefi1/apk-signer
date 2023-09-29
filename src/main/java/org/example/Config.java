package org.example;

import com.formdev.flatlaf.extras.FlatSVGIcon;
import gui.FormMessage;

import javax.swing.*;

public class Config {
    public static int width = 500;
    public static int height = 500;
    public static String aliasName = "";
    public static String keyStorePath = System.getProperty("user.dir");



    public void frameIcon(JFrame jFrame){
        FlatSVGIcon ic_key = new FlatSVGIcon("icons8-icon_small.svg", 24, 24);
        jFrame.setIconImage(ic_key.getImage());
    }
    public void Initialise(JFrame parent,JFrame jFrame,JPanel panel){
        frameIcon(jFrame);
        jFrame.setContentPane(panel);
        jFrame.setVisible(true);
        jFrame.setBounds(600,200, Config.width,Config.height);
        jFrame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        if (parent!=null)
            parent.setVisible(false);
    }

    public void InitialiseDialogue(JFrame parent, FormMessage jFrame, JPanel panel){
        frameIcon(jFrame);
        jFrame.setContentPane(panel);
        jFrame.setVisible(true);
        jFrame.setBounds(600+(Config.width/2),200+(Config.height/2), 400,150);
        jFrame.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        if (parent!=null)
            parent.setVisible(false);
    }

    public void Initialise(JFrame jFrame,JPanel panel){
        frameIcon(jFrame);
        jFrame.setContentPane(panel);
        jFrame.setVisible(true);
        jFrame.setBounds(600,200, Config.width,Config.height);
        jFrame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
    }

    public void InitialiseTerminal(JFrame jFrame,JPanel panel){
        frameIcon(jFrame);
        jFrame.setContentPane(panel);
        jFrame.setVisible(true);
        jFrame.setBounds(600,200, 700,Config.height);
        jFrame.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
    }

    public void Close(JFrame parent,JFrame jFrame){
        jFrame.dispose();
        if (parent!=null)
            parent.setVisible(true);
    }

    public static void print(Object s) {
        System.out.println(s+"");
    }

}
