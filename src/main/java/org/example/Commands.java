package org.example;

import javax.swing.*;
import javax.swing.text.BadLocationException;
import javax.swing.text.Style;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyledDocument;
import java.awt.*;
import java.io.File;
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
        return runCommand(cm, true);
    }

    public boolean sign(ParentModel signModel) {
        String apk_path = makeArguments(signModel.getApk_path());
        String key_alias = makeArguments(signModel.getAlias());
        String key_store_path = makeArguments(signModel.getKey_store());
        String out_put_apk_path = getZipAlignedApkPath(apk_path);
        String cm;
        boolean success;
        if (isWindows()) {
            cm = getZipAlignPath() + " -p -f -v 4 " + apk_path + " " + out_put_apk_path + "\"";
            success = runCommand(cm, false);
            if (success) {
                cm = "java -jar " + "\"" + getApkSignerPath() + "\"" + " sign --ks "
                        + "\"" + key_store_path + "\"" + " --ks-key-alias " + key_alias +
                        " --ks-pass pass:password --key-pass pass:password " + out_put_apk_path + "\"";
                success = runCommand(cm, false);
                if (success) {
                    cm = "java -jar " + "\"" + getApkSignerPath() + "\"" + " verify -v -v4-signature-file "
                            + out_put_apk_path + ".idsig" + "\"" + " " + out_put_apk_path + "\"";
                    success = runCommand(cm, false);
                }
            }
        } else {
            cm = getZipAlignPath() + " -p -f -v 4 " + apk_path + " " + out_put_apk_path;
            success = runCommand(cm, false);
            if (success) {
                cm = "java -jar " + getApkSignerPath() + " sign --ks "
                        + key_store_path + " --ks-key-alias " + key_alias +
                        " --ks-pass pass:password --key-pass pass:password " + out_put_apk_path;
                success = runCommand(cm, false);
                if (success) {
                    cm = "java -jar " + getApkSignerPath() + " verify -v -v4-signature-file "
                            + out_put_apk_path + ".idsig " + out_put_apk_path;
                    success = runCommand(cm, false);
                }
            }
        }
        print(cm);
        paneStyle(jLabel, cm + "\n", "", "", "");
        return success;
    }

    private boolean runCommand(String cm, boolean gen_key) {
        print(cm);
        Process p;
        try {
            p = Runtime.getRuntime().exec(cm);
        } catch (Exception e) {
            paneStyle(jLabel, "", "", "", e.toString());
            return false;
        }
        try {
            if (gen_key) {
                return logOutputCommand(p.getInputStream(), true);
            } else {
                logOutputCommand(p.getInputStream(), false);
                return logOutputError(p.getErrorStream());
            }
        } catch (Exception e) {
            paneStyle(jLabel, "", "", "", e.toString());
            return false;
        }
    }

    private void paneStyle(JTextPane pane, String cm, String code_execution, String warning, String error) {
        StyledDocument doc = pane.getStyledDocument();
        Style style = pane.addStyle("name", null);

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

    private String getApkSignerPath() {
        if (isWindows())
            return "bin\\apksigner.jar";
        else
            return "bin/apksigner.jar";
    }

    private String getZipAlignedApkPath(String apk_path) {
        File apk_file = new File(apk_path);
        String apk_path_parent = apk_file.getParent();
        String apk_name = apk_file.getName().split(".apk")[0];
        String apk_zip_aligned = apk_name + "-zip-aligned" + ".apk";
        if (isWindows())
            return apk_path_parent + "\\" + apk_zip_aligned;
        else
            return apk_path_parent + "/" + apk_zip_aligned;
    }

    private boolean isWindows() {
        return System.getProperty("os.name").toLowerCase().contains("windows");
    }

    private String makeArguments(String s) {
        if (s.isEmpty())
            return "Unknown";
        else
            return "\"" + s + "\"";
    }

    private boolean logOutputError(InputStream inputStream) {
        StringBuilder sb = new StringBuilder();
        boolean successful = true;
        try {
            for (int ch; (ch = inputStream.read()) != -1; ) {
                successful = false;
                sb.append((char) ch);
            }
            if (!successful) {
                String r = sb.toString();
                print(r);
                String first_chars = r.substring(0, 10).toLowerCase();
                if (first_chars.contains("warning")) {
                    paneStyle(jLabel, "", "", r, "");
                } else {
                    paneStyle(jLabel, "", "", "", r);
                }
            }
        } catch (Exception ignored) {

        }
        return successful;
    }

    private boolean logOutputCommand(InputStream inputStream, boolean gen_key) throws IOException {
        StringBuilder sb = new StringBuilder();
        for (int ch; (ch = inputStream.read()) != -1; ) {
            sb.append((char) ch);
        }
        String output = sb.toString();
        if (gen_key) {
            if (output.contains("keytool error:")) {
                paneStyle(jLabel, "", "", "", output);
                return false;
            }
        } else {
            paneStyle(jLabel, "", sb.toString(), "", "");
        }
        return true;
    }
}
