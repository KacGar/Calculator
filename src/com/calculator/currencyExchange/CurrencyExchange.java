package com.calculator.currencyExchange;

import com.calculator.CalcGUI;
import com.calculator.lengthConverter.LenConvController;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.time.Duration;
import java.time.Instant;

/**
 * Currency exchange sub app that shows currency exchange rates. Data with exchange rates is taken from XML file on NBP site (Narodowy Bank Polski).
 * Keeping mind that NBP is providing data, sometimes said data can be not up-to-date since they update XML file once a day or even longer.
 */
public class CurrencyExchange extends JPanel {

    /**
     * Instance of currency exchange object (followed with private constructor)
     */
    private static final JPanel currExchangeInstance = new CurrencyExchange();

    /**
     * Label that holds input
     */
    private static JLabel labelInput;

    /**
     * Label that results are shown
     */
    private static JLabel labelResult;


    private CurrencyExchange(){

        //getting data from NBP
        CurrExchController.updateData();

        setLayout(new GridBagLayout());
        var gc = new GridBagConstraints();

        //labels
        labelInput = new JLabel("0");
        labelInput.setFont(labelInput.getFont().deriveFont(35f));
        labelInput.setBorder(new EmptyBorder(15,15,15,0));

        labelResult = new JLabel("0");
        labelResult.setFont(labelResult.getFont().deriveFont(35f));
        labelResult.setBorder(new EmptyBorder(15,15,15,0));

        //comboboxes
        JComboBox<Currency> choice1 = new JComboBox<>();
        JComboBox<Currency> choice2 = new JComboBox<>();
        for (Currency e : Currency.values()){
            choice1.addItem(e);
            choice2.addItem(e);
        }
        CurrExchController.setMetric1(choice1);
        CurrExchController.setMetric2(choice2);

        choice1.addItemListener(CurrExchController.itemListener());
        choice2.addItemListener(CurrExchController.itemListener());

        //buttons
        JButton[] numberButtons = CalcGUI.getNumBtns(20f,CurrExchController.action());
        CalcGUI.setKeybindsForNumBtns(this,CurrExchController.action(), numberButtons);

        var lang = CalcGUI.getLangBundle();

        var btnClear = new JButton(lang.getString("clear"));
        btnClear.addActionListener(CurrExchController.action());
        setBasicPropBtn(btnClear,16f,"clear");
        CalcGUI.setKeybind(this,KeyEvent.VK_ESCAPE,"clear",btnClear);

        var btnDelete = new JButton(lang.getString("delete"));
        btnDelete.addActionListener(CurrExchController.action());
        setBasicPropBtn(btnDelete,16f,"delete");
        CalcGUI.setKeybind(this,KeyEvent.VK_BACK_SPACE,"delete", btnDelete);

        JButton btnDot = new JButton(".");
        btnDot.setFocusable(false);
        btnDot.addActionListener(CurrExchController.action());
        CalcGUI.setDotKeyBind(this, CurrExchController.action());

        // application is divided into 2 sections, one with label and comboboxes and second with buttons
        var section1 = new JPanel();
        section1.setLayout(new GridBagLayout());

        var gsec1 = new GridBagConstraints();
        gsec1.gridx = 0;
        gsec1.gridy = 0;
        gsec1.weightx = 1;
        gsec1.fill = GridBagConstraints.HORIZONTAL;
        section1.add(labelInput, gsec1);
        gsec1.gridy += 1;
        section1.add(choice1, gsec1);
        gsec1.gridy += 1;
        section1.add(labelResult,gsec1);
        gsec1.gridy += 1;
        section1.add(choice2,gsec1);

        var section2 = new JPanel();
        section2.setLayout(new GridLayout(0,3));
        section2.setBorder(new EmptyBorder(30,0,0,0));

        section2.add(new JLabel(""));
        section2.add(btnClear);
        section2.add(btnDelete);
        section2.add(numberButtons[7]);
        section2.add(numberButtons[8]);
        section2.add(numberButtons[9]);
        section2.add(numberButtons[4]);
        section2.add(numberButtons[5]);
        section2.add(numberButtons[6]);
        section2.add(numberButtons[1]);
        section2.add(numberButtons[2]);
        section2.add(numberButtons[3]);
        section2.add(new JLabel(""));
        section2.add(numberButtons[0]);
        section2.add(btnDot);

        gc.gridx = 0;
        gc.gridy = 0;
        gc.weightx = 1;
        gc.weighty = 0.1;
        gc.fill = GridBagConstraints.HORIZONTAL;
        add(section1,gc);
        gc.gridy = 1;
        gc.weighty = 0.9;
        gc.fill = GridBagConstraints.BOTH;
        add(section2,gc);
    }

    /**
     * Returns a reference to currency exchange object.
     * @return JPanel object of {@link CurrencyExchange} class
     */
    public static JPanel instance(){ return currExchangeInstance; }

    /**
     * Returns JLabel object that holds input from used.
     * @return JLable object.
     */
    public static JLabel getLabelInput() { return labelInput; }

    /**
     * Returns JLabel object that result are (should be) shown
     * @return
     */
    public static JLabel getLabelResult(){ return labelResult; }

    private static boolean isTimeToUpdate(){
        System.out.println(CalcGUI.getNode().get("appLaunchDate",""));
        Instant lastClosed = Instant.parse(
                CalcGUI.getNode().get("appLaunchDate","")
        );
        Instant today = Instant.now();
        Duration duration = Duration.between(today,lastClosed);
        //update only once per 4 hours
        return duration.toHoursPart() > 4;
    }

    /**
     * Sets common set of properties for given button, like font, action command, focusable and action listener.
     * @param button JButton which properties will be set.
     * @param fontSize Size of text displayed inside given JButton
     * @param actionCmd Text that will be set as action command.
     */
    private static void setBasicPropBtn(JButton button, float fontSize, String actionCmd){
        button.setFont(button.getFont().deriveFont(fontSize));
        button.setActionCommand(actionCmd);
        button.setFocusable(false);
        button.addActionListener(LenConvController.action());
    }


}
