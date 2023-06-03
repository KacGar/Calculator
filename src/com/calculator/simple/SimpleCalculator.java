package com.calculator.simple;

import com.calculator.CalcGUI;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.KeyEvent;

/**
 * Standard, simple calculator where user can do basic calculations. As for now, brackets or higher mathematical functions are not handled.
 * SimpleCalculator extends JPanel class, so object of this class should be treated such as. Only one instance of this class exists and can be called with public static method.
 */
public class SimpleCalculator extends JPanel {

    /**
     * Main textfield which displays prompts from user. Values from this field are used in calculations.
     */
    private static JTextField input;
    /**
     * Smaller textfield which displays temporary prompted values after clicking function button. It is used also to display result upon hitting enter or clicking equals button.
     */
    private static JLabel tempResultField;
    /**
     * Instance of simple calculator. Since only one object is needed, this field is static and final (followed with private constructor in class)
     */
    private static final JPanel simpleCalcInstance = new SimpleCalculator();

    /**
     * Private constructor that creates one instance of SimpleCalculator class. All elements and layout are defined here.
     */
    private SimpleCalculator(){
        //textfields
        input = new JTextField();
        input.setEditable(false);
        input.setFocusable(false);
        input.setHorizontalAlignment(SwingConstants.RIGHT);
        input.setFont(input.getFont().deriveFont(22f));
        input.setBorder(new EmptyBorder(0,0,0,0));
        input.setText("0");
        tempResultField = new JLabel("0.0");
        tempResultField.setFont(tempResultField.getFont().deriveFont(30f));
        tempResultField.setHorizontalAlignment(SwingConstants.RIGHT);

        //buttons
        JButton[] numberButtons = CalcGUI.getNumBtns(20f, SimCalController.action());
        JButton[] arithmeticButtons = CalcGUI.getArithmeticFunctionBtns(20f, SimCalController.action());

        CalcGUI.setKeybindsForNumBtns(this, SimCalController.action(),numberButtons);
        CalcGUI.setKeybindsForAritmBtns(this,SimCalController.action(),arithmeticButtons);
        CalcGUI.setEnterKeybind(this,arithmeticButtons[8]);

            //language bundle
        var lang = CalcGUI.getLangBundle();

        var delBtn = new JButton(lang.getString("delete"));
        delBtn.addActionListener(SimCalController.action());
        delBtn.setFont(delBtn.getFont().deriveFont(18f));
        CalcGUI.setKeybind(this,KeyEvent.VK_BACK_SPACE,"delete",delBtn);

        var clearBtn = new JButton(lang.getString("clear"));
        clearBtn.addActionListener(SimCalController.action());
        clearBtn.setFont(delBtn.getFont().deriveFont(18f));
        CalcGUI.setKeybind(this,KeyEvent.VK_ESCAPE,"clear",clearBtn);

        var dotBtn = new JButton(",");
        dotBtn.addActionListener(SimCalController.action());
        dotBtn.setFocusable(false);
        CalcGUI.setDotKeyBind(this,SimCalController.action());

        // panel is divided into 3 sections/groups
            // 1st takes text field and label
            // 2nd clear and delete buttons to make them wider since 3rd section is divided into 4 columns
            // 3rd takes all number and action buttons
        var section1 = new JPanel();
        section1.setLayout(new GridLayout(0,1));
        section1.add(input);
        section1.add(tempResultField);

        var section2 = new JPanel();
        section2.setLayout(new GridLayout(0,2));
        section2.add(clearBtn);
        section2.add(delBtn);

        var section3 = new JPanel();
        section3.setLayout(new GridLayout(0,4));
        // 1st row
        section3.add(arithmeticButtons[0]);
        section3.add(arithmeticButtons[1]);
        section3.add(arithmeticButtons[2]);
        section3.add(arithmeticButtons[4]);
        // 2nd row
        section3.add(numberButtons[7]);
        section3.add(numberButtons[8]);
        section3.add(numberButtons[9]);
        section3.add(arithmeticButtons[5]);
        // 3rd row
        section3.add(numberButtons[4]);
        section3.add(numberButtons[5]);
        section3.add(numberButtons[6]);
        section3.add(arithmeticButtons[6]);
        // 4th row
        section3.add(numberButtons[1]);
        section3.add(numberButtons[2]);
        section3.add(numberButtons[3]);
        section3.add(arithmeticButtons[7]);
        // 5th row
        section3.add(arithmeticButtons[3]);
        section3.add(numberButtons[0]);
        section3.add(dotBtn);
        section3.add(arithmeticButtons[8]);

        setLayout(new GridBagLayout());
        var gc = new GridBagConstraints();

        // adding every section to our main panel
        gc.gridx = 0;
        gc.gridy = 0;
        gc.weightx = 1;
        gc.weighty = 0.3;
        gc.fill = GridBagConstraints.BOTH;
        add(section1,gc);

        gc.gridy = 1;
        gc.weighty = 0.1;
        gc.fill = GridBagConstraints.BOTH;
        add(section2,gc);

        gc.gridy = 2;
        gc.weighty = 1;
        gc.fill = GridBagConstraints.BOTH;
        add(section3,gc);
    }

    /**
     * Returns reference to static final reference of SimpleCalculator class object.
     * @return Reference to object instance (type JPanel,SimpleCalculator)
     */
    public static JPanel instance(){ return simpleCalcInstance; }

    /**
     * Returns JTextField object that holds values propted by user.
     * @return JTextField object.
     */
    public static JTextField getInputField(){ return input; }

    /**
     * Returns JLabel object that holds temporary values used in calculations.
     * @return JLabel object
     */
    public static JLabel getLabel(){ return tempResultField; }
}
