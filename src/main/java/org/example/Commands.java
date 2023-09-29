package org.example;

import javax.swing.*;
import javax.swing.text.BadLocationException;
import javax.swing.text.Style;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyledDocument;
import java.awt.*;
import java.io.IOException;
import java.io.InputStream;

import static org.example.Config.print;

public class Commands {
    JTextPane jLabel;

    public Commands(JTextPane jLabel) {
        this.jLabel = jLabel;
    }

    public boolean generateKey(ParentModel keyModel) {
        String key_store_path = keyModel.getKey_store();
        String alias = makeArguments(keyModel.getAlias());
        String name = makeArguments(keyModel.getName());
        String organization_unit_name = makeArguments(keyModel.getOrgan_unit());
        String organization_name = makeArguments(keyModel.getUnit());
        String city = makeArguments(keyModel.getCity());
        String state = makeArguments(keyModel.getState());
        String country_code = makeArguments(keyModel.getCode());
        String cm;
        if (isWindows()) {
            cm = "keytool -genkey -alias " + alias
                    + " -keyalg RSA -keystore " + "\"" + key_store_path + "\"" + " -keypass password -storepass password -dname CN=" + name +
                    ",OU=" + organization_unit_name + ",O=" + organization_name + ",L=" + city + ",ST=" +
                    state + ",C=" + country_code + " --debug";
        } else {
            cm = "keytool -genkey -alias " + alias
                    + " -keyalg RSA -keystore " + key_store_path + " -keypass password -storepass password -dname CN=" + name +
                    ",OU=" + organization_unit_name + ",O=" + organization_name + ",L=" + city + ",ST=" +
                    state + ",C=" + country_code + " --debug";
        }
        print(cm);
        paneStyle(jLabel, cm + "\n", "", "", "");
        return runCommand(cm);
    }

    public boolean sign(ParentModel signModel) {
        String apk_path = makeArguments(signModel.getApk_path());
        String key_alias = makeArguments(signModel.getAlias());
        String key_store_path = makeArguments(signModel.getKey_store());
        String cm;
        if (isWindows()) {
            cm = "java -jar " + "\"" + getUberPath() + "\"" + " -a " + "\"" + apk_path + "\"" + " --ks "
                    + "\"" + key_store_path + "\"" + " --ksAlias " + key_alias +
                    " --ksKeyPass password --ksPass password --zipAlignPath " + "\"" + getZipAlignPath() + "\"" + " --debug";
        } else {
            cm = "java -jar " + getUberPath() + " -a " + apk_path + " --ks "
                    + key_store_path + " --ksAlias " + key_alias +
                    " --ksKeyPass password --ksPass password --zipAlignPath " + getZipAlignPath() + " --debug";
        }
        print(cm);
        paneStyle(jLabel, cm + "\n", "", "", "");
        return runCommand(cm);
    }

    private boolean runCommand(String cm) {
        Process p;
        try {
            p = Runtime.getRuntime().exec(cm);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        try {
            logOutputCommand(p.getInputStream());
            print(p.getErrorStream());
            return logOutputError(p.getErrorStream());

        } catch (IOException e) {
            jLabel.setText(e.toString());
            e.printStackTrace();
            return false;
        }
    }

    private void paneStyle(JTextPane pane, String cm, String code_execution, String warning, String error) {
        StyledDocument doc = pane.getStyledDocument();
        Style style = pane.addStyle("I'm a Style", null);

        StyleConstants.setForeground(style, Color.WHITE);
        try {
            doc.insertString(doc.getLength(), cm, style);
        } catch (BadLocationException e) {
            throw new RuntimeException(e);
        }

        StyleConstants.setForeground(style, Color.WHITE);
        try {
            doc.insertString(doc.getLength(), code_execution, style);
        } catch (BadLocationException e) {
            throw new RuntimeException(e);
        }

        StyleConstants.setForeground(style, Color.YELLOW);
        try {
            doc.insertString(doc.getLength(), warning, style);
        } catch (BadLocationException e) {
            throw new RuntimeException(e);
        }

        StyleConstants.setForeground(style, Color.RED);
        try {
            doc.insertString(doc.getLength(), error, style);
        } catch (BadLocationException e) {
            throw new RuntimeException(e);
        }
    }

    private String getZipAlignPath() {
        return getBinPath() + "zipalign";
    }

    private String getBinPath() {
        if (isWindows())
            return "bin\\windows\\";
        else
            return "bin/linux/";
    }

    private String getUberPath() {
        if (isWindows())
            return "bin\\uber-apk-signer.jar";
        else
            return "bin/uber-apk-signer.jar";
    }

    private boolean isWindows() {
        return System.getProperty("os.name").toLowerCase().contains("windows");
    }

    private String makeArguments(String s) {
        if (s.isEmpty())
            return "Unknown";
        else
            return "\""+s+"\"";
    }

    private boolean logOutputError(InputStream inputStream) throws IOException {
        StringBuilder sb = new StringBuilder();
        boolean successful = true;
        for (int ch; (ch = inputStream.read()) != -1; ) {
            successful = false;
            sb.append((char) ch);
        }
        try {
            if (!successful) {
                String r = sb.toString();
                print(r);
                if (r.substring(0, 10).toLowerCase().contains("warning")) {
                    paneStyle(jLabel, "", "", r, "");
                } else if (r.substring(0, 10).toLowerCase().contains("exception")) {
                    paneStyle(jLabel, "", "", "", r);
                } else {
                    paneStyle(jLabel, "", r, "", "");
                    successful = true;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return successful;
    }

    private void logOutputCommand(InputStream inputStream) throws IOException {
        for (int ch; (ch = inputStream.read()) != -1; ) {
            paneStyle(jLabel, "", String.valueOf((char) ch), "", "");
        }
    }
}
