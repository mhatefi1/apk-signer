package org.example;

import gui.FormMain;

public class Main {
    public static void main(String[] args) {
        FormMain formMain = new FormMain();
        new Config().Initialise(formMain,formMain.panel1);
    }
}