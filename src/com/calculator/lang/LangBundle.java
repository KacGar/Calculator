package com.calculator.lang;

import javax.swing.*;
import java.util.Locale;
import java.util.ResourceBundle;
import java.util.logging.Logger;
import java.util.prefs.Preferences;

public class LangBundle {

    private LangBundle(){}

    /**
     * Method to retrieve resource bundle with language properties which uses {@link Logger} to log errors, {@link Preferences} node to check what language to return
     * and {@link JFrame} component upon which dialog with errors are displayed.
     * @param node Main preferences object with user node
     * @return {@link ResourceBundle} object with language properties
     */
    public static ResourceBundle getResBundle(Preferences node){
        ResourceBundle bundle = null;

        try {
            if (node.get("language", "en").equalsIgnoreCase("pl")){
                return bundle = ResourceBundle.getBundle("com.calculator.lang.GUILanguage_pl_PL");
            } else {
                return bundle = ResourceBundle.getBundle("com.calculator.lang.GUILanguage_en_US");
            }
        } catch (NullPointerException ex){
            String msg;
            if (Locale.getDefault().getDisplayLanguage().equalsIgnoreCase("polski")){
                msg = "Odnośnik do pliku językowego nie istnieje. Prawdopodobnie plik property w folderze 'lang' nie istnieje.";
                JOptionPane.showMessageDialog(null,msg);
            }
            else {
                msg = "Pointer to language file don't exists. Probably property file in 'lang' folder don't exist.";
                JOptionPane.showMessageDialog(null,msg);
            }
            System.exit(0);

        } catch (java.util.MissingResourceException ex){
            String msg;
            if (Locale.getDefault().getDisplayLanguage().equalsIgnoreCase("polski")){
                msg = "Odnośnik do zasobu językowego nie istnieje. Prawdopodobnie adres zasobu jest błędny";
                JOptionPane.showMessageDialog(null,msg);
            }
            else {
                msg = "Pointer to package language is missing. Probably pathname to bundle is invalid.";
                JOptionPane.showMessageDialog(null,msg);
            }
            System.exit(0);
        }
        return bundle;
    }
}
