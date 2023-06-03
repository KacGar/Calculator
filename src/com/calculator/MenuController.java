package com.calculator;

import com.calculator.currencyExchange.CurrencyExchange;
import com.calculator.lengthConverter.LengthConverter;
import com.calculator.programical.Programical;
import com.calculator.simple.SimpleCalculator;
import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ResourceBundle;
import static com.calculator.CalcGUI.*;

/**
 * MenuController class handles every events fire by JMenuItems in application.
 * Main purpose is to display chosen panel (sub app) from menu and hide others.
 * Changing language is handled with {@link JOptionPane} object.
 */
public class MenuController implements ActionListener {


    private final ResourceBundle bundle = CalcGUI.getLangBundle();

    /**
     * All text in menu, menu items and buttons are different for every language.
     * Action command text is compared with a specified key-value from language bundle
     * @param e ActionEvent object
     */
    @Override
    public void actionPerformed(ActionEvent e) {
        String s = e.getActionCommand();

        // simple calculator
        if ( s.equalsIgnoreCase(bundle.getString("sm11"))){
            disableMenu(0);
            var panel = SimpleCalculator.instance();
            switchPanel(panel);
        }

        //programical calculator
        if ( s.equalsIgnoreCase(bundle.getString("sm13"))){
            disableMenu(1);
            var panel = Programical.instance();
            switchPanel(panel);
        }

        //currency exchange
        if ( s.equalsIgnoreCase(bundle.getString("sm14"))){
            disableMenu(2);
            var panel = CurrencyExchange.instance();
            switchPanel(panel);
        }

        // lenght converter
        if ( s.equalsIgnoreCase(bundle.getString("sm15"))){
            disableMenu(3);
            var panel = LengthConverter.instance();
            switchPanel(panel);
        }

        // change language
        if ( s.equalsIgnoreCase(bundle.getString("sm21"))){
            String msg = bundle.getString("language");
            int choice = JOptionPane.showConfirmDialog(CalcGUI.getGuiInstance(), msg);
            if (choice == JOptionPane.YES_OPTION){
                String key = bundle.getString("currentLang");
                if (key.equalsIgnoreCase("pl")) CalcGUI.getNode().put("language", "en");
                else CalcGUI.getNode().put("language", "pl");
            }
            JOptionPane.showMessageDialog(CalcGUI.getGuiInstance(),bundle.getString("changed"));
        }


    }

    /**
     * Method which takes every JMenuItem from first menu that holds text reference to every sub app and disables JMenuItem with provided index as parameter.
     * Therefore, if user chooses first option - that element will be disabled and rest options will be enabled.
     * @param index Position index that corresponds to placement in first menu. (Starts with zero)
     */
    private void disableMenu(int index){
        JMenu menu = CalcGUI.getGuiInstance().getJMenuBar().getMenu(0);
        for (int i = 0; i < menu.getItemCount(); i++){
            menu.getItem(i).setEnabled(true);
        }
        menu.getItem(index).setEnabled(false);
    }

    /**
     * Method that handles "switching" panels that should be displayed. Displays provided panel and removes all others from wrapper panel.
     * @param panel JPanel object to display.
     */
    private void switchPanel(JPanel panel){
        getContentPanel().removeAll();
        getContentPanel().add(panel);
        getContentPanel().updateUI();
    }
}
