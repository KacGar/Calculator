package com.calculator.lengthConverter;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import static com.calculator.lengthConverter.LengthConverter.*;

/**
 * Controller for LengthConverter object. By extending {@link AbstractAction} class, this class can be used as action listener object.
 * To get instance of this class public static methods should be used. No new object of this class can be created.
 */
public class LenConvController extends AbstractAction implements ItemListener{

    /**
     * The only onstance of this class (followed with private constructor)
     */
    private static final LenConvController INSTANCE = new LenConvController();
    private final Pattern pattern = Pattern.compile(".*[.]+.*");

    /**
     * Combobox filled with values from {@link SISystem} enum. Used as FROM which system conversion starts.
     */
    private static JComboBox<SISystem> metric1;

    /**
     *Combobox filled with values from {@link SISystem} enum. Used as TO which system convert into
     */
    private static JComboBox<SISystem> metric2;

    /**
     * Returns instance of controler as ItemListener type.
     * @return ItemListnener object
     */
    public static ItemListener itemListener(){ return INSTANCE;}

    /**
     * Returns instance of controller class, used mostly for setting action listeners.
     * @return Action object.
     */
    public static Action action(){ return INSTANCE;}

    /**
     * Sets reference to first (FROM) combobox object for later use in convert operations
     * @param metric JComboBox with {@link SISystem} parametrized type.
     */
    public static void setMetric1(JComboBox<SISystem> metric){ metric1 = metric;}

    /**
     * Sets reference to first (TO) combobox object for later use in convert operations
     * @param metric JComboBox with {@link SISystem} parametrized type.
     */
    public static void setMetric2(JComboBox<SISystem> metric){ metric2 = metric;}

    /**
     * Private construcotr prevents creating new objects.
     */
    private LenConvController(){}

    /**
     * Method implemented from {@link ItemListener} interface. Whenever something is chosen in any of the two comboboxes, converion happens.
     * @param e ItemEvent object
     */
    @Override
    public void itemStateChanged(ItemEvent e) { if (e.getStateChange() == ItemEvent.SELECTED){ convert(); } }

    String nums = "0123456789";
    @Override
    public void actionPerformed(ActionEvent e) {
        String s = e.getActionCommand();

        // appends number in input field
        if (nums.contains(s)) {
            if ( getLabelInput().getText().contains(".")){
                getLabelInput().setText(getLabelInput().getText().concat(s));
            } else {
                if ( getLabelInput().getText().charAt(0) == '0' ) {
                    getLabelInput().setText(s);
                } else {
                    getLabelInput().setText(getLabelInput().getText().concat(s));
                }
            }
            convert();
        }

        //check if dot exist in text, if not adds it at the end
        if ( s.equals(",") || s.equals(".") ){
            if ( !(getLabelInput().getText().equals("")) ){
                Matcher m = pattern.matcher(getLabelInput().getText());
                boolean b = m.matches();
                if (!b){ getLabelInput().setText(getLabelInput().getText().concat(".")); }
                convert();
            }

        }

        //removes last character from text until length > 0
        if ( s.equals("delete") ){
            String input = getLabelInput().getText();
            if( input.length() != 0 ){ input = input.substring(0, input.length() - 1);}
            if (input.length() != 0 && input.charAt(input.length()-1) == '.') { input = input.substring(0, input.length() - 1); }
            if (input.length() == 0) { getLabelInput().setText("0");}
            else { getLabelInput().setText(input); convert();}
        }

        if (s.equals("clear")){
            getLabelInput().setText("0");
            getLabelResult().setText("0.0");
        }
    }

    /**
     * Converts value from input from one chosen system into another chosen system and displays results.
     */
    private void convert(){
        double number = Double.parseDouble(getLabelInput().getText());
        SISystem choice1 = metric1.getItemAt(metric1.getSelectedIndex());
        SISystem choice2 = metric2.getItemAt(metric2.getSelectedIndex());

        double num1 = number / choice1.rate();
        double num2 = num1 * choice2.rate();

        getLabelResult().setText(String.valueOf(num2));
    }
}
