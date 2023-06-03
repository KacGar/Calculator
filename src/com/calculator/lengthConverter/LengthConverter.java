package com.calculator.lengthConverter;

import com.calculator.CalcGUI;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.KeyEvent;

/**
 * LengthConverter is a sub app handling conversions from chosen SI systems.To receive object of class, public static method should be used. All fields in class are static.
 */
public class LengthConverter extends JPanel {

    /**
     * Reference to only object of this class (followed with private constructor)
     */
    private static final JPanel lConvInstance = new LengthConverter();

    /**
     * Simple label where input is displayed.
     */
    private static JLabel labelInput;

    /**
     * Simple label where results are displayed.
     */
    private static JLabel labelResult;

    /**
     * Constructor which handles creating on every component, layout and setting all properties.
     */
    private LengthConverter(){
        //labels
        labelInput = new JLabel("0");
        labelInput.setFont(labelInput.getFont().deriveFont(35f));
        labelInput.setBorder(new EmptyBorder(15,15,15,0));

        labelResult = new JLabel("0");
        labelResult.setFont(labelResult.getFont().deriveFont(35f));
        labelResult.setBorder(new EmptyBorder(15,15,15,0));

        //combo boxes
        JComboBox<SISystem> choice1 = new JComboBox<>();
        JComboBox<SISystem> choice2 = new JComboBox<>();

        for (SISystem e : SISystem.values()){
            choice1.addItem(e);
            choice2.addItem(e);
        }
        LenConvController.setMetric1(choice1);
        LenConvController.setMetric2(choice2);

        choice1.addItemListener(LenConvController.itemListener());
        choice2.addItemListener(LenConvController.itemListener());

        //buttons
        JButton[] numberButtons = CalcGUI.getNumBtns(20f,LenConvController.action());
        CalcGUI.setKeybindsForNumBtns(this,LenConvController.action(), numberButtons);

        var lang = CalcGUI.getLangBundle();

        var btnClear = new JButton(lang.getString("clear"));
        setBasicPropBtn(btnClear,16f,"clear");
        CalcGUI.setKeybind(this,KeyEvent.VK_ESCAPE,"clear",btnClear);

        var btnDelete = new JButton(lang.getString("delete"));
        setBasicPropBtn(btnDelete,16f,"delete");
        CalcGUI.setKeybind(this,KeyEvent.VK_BACK_SPACE,"delete", btnDelete);

        JButton btnDot = new JButton(".");
        btnDot.setFocusable(false);
        btnDot.addActionListener(LenConvController.action());
        CalcGUI.setDotKeyBind(this, LenConvController.action());

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

        setLayout(new GridBagLayout());
        var gc = new GridBagConstraints();
        // adding everything to this class object
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
     * Returns instance of LengthConverter object.
     * @return JPanel object of LengthConverter class
     */
    public static JPanel instance(){ return lConvInstance; }

    /**
     * Returns label that holds input from user.
     * @return JLabel object
     */
    public static JLabel getLabelInput() { return labelInput; }

    /**
     * Returns label where result are (should) be displayed.
     * @return JLabel object.
     */
    public static JLabel getLabelResult(){ return labelResult; }

    /**
     * Sets few properties of given button, such as font, action command, focus and listener.
     * @param button JButton object to set properties.
     * @param fontSize Size of font displayed in given button
     * @param actionCmd Text that will be set as action command.
     */
    private static void setBasicPropBtn(JButton button, float fontSize, String actionCmd){
        button.setFont(button.getFont().deriveFont(fontSize));
        button.setActionCommand(actionCmd);
        button.setFocusable(false);
        button.addActionListener(LenConvController.action());
    }


}
