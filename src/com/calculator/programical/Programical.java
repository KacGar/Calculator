package com.calculator.programical;

import com.calculator.CalcGUI;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.MatteBorder;
import java.awt.*;
import java.awt.event.KeyEvent;

/**
 * Programical calculator has calculator and converts inputs / result into 3 different number systems (DECIMAL,HEXADECIMAL,OCTAL,BINARY).
 * Handles only full numbers without fractions. All fields in class are static.
 * To receive object of class, public static method should be used.
 */
public class Programical extends JPanel {

    private static final JPanel INSTANCE = new Programical();
    private static JTextField input;
    private static JTextField decLabel;
    private static JTextField hexLabel;
    private static JTextField octalLabel;
    private static JTextField binaryLabel;
    private static JTextField descLabel;
    private static JTextField tempLabel;
    private static JButton[] numSystemBtns;
    private static JButton[] numberButtons;
    private static JButton[] hexaLetterBtns;

    /**
     * Private constructor of Programical class that handles creation of every component and setting layout.
     */
    private Programical(){

        //labels
        descLabel = new JTextField();
        setTxtFieldProperties(descLabel, SwingConstants.LEFT, 26f);
        descLabel.setBorder(new MatteBorder(0,0,2,0, Color.black));
        descLabel.setText("DEC");

        input = new JTextField();
        setTxtFieldProperties(input, SwingConstants.RIGHT, 26f);
        input.setBorder(new MatteBorder(0,0,2,0, Color.black));
        input.setText("");

        tempLabel = new JTextField();
        setTxtFieldProperties(tempLabel, SwingConstants.RIGHT, 16f);
        tempLabel.setBorder(new EmptyBorder(0,0,10,0));
        tempLabel.setText("0");

        decLabel = new JTextField();
        setTxtFieldProperties(decLabel, SwingConstants.LEFT, 18f);

        hexLabel = new JTextField();
        setTxtFieldProperties(hexLabel, SwingConstants.LEFT, 18f);

        octalLabel = new JTextField();
        setTxtFieldProperties(octalLabel, SwingConstants.LEFT, 18f);

        binaryLabel = new JTextField();
        setTxtFieldProperties(binaryLabel, SwingConstants.LEFT, 18f);

        //buttons
        numSystemBtns = CalcGUI.getNumericSystemBtns(16f,ProgramicalController.action());
        hexaLetterBtns = CalcGUI.getHexadecimalBtns(18f, ProgramicalController.action());
        numberButtons = CalcGUI.getNumBtns(18f, ProgramicalController.action());
        JButton[] arithmeticButtons = CalcGUI.getArithmeticFunctionBtns(18f, ProgramicalController.action());
            //for now percent function button is disabled
        arithmeticButtons[0].setEnabled(false);
            //setting keybindings for buttons
        CalcGUI.setKeybindsForNumBtns(this,ProgramicalController.action(),numberButtons);
        CalcGUI.setKeybindsForAritmBtns(this,ProgramicalController.action(),arithmeticButtons);

        var dotBtn = new JButton(",");
        dotBtn.setEnabled(false);
        dotBtn.setFocusable(false);

        var flipBtn = new JButton(String.valueOf('\u21B1') + '\u21B2');
        flipBtn.setEnabled(false);
        flipBtn.setFocusable(false);
            //language bundle
        var lang = CalcGUI.getLangBundle();

        var delBtn = new JButton(lang.getString("delete"));
        delBtn.setFont(delBtn.getFont().deriveFont(18f));
        delBtn.setFocusable(false);
        delBtn.addActionListener(ProgramicalController.action());

        var clearBtn = new JButton(lang.getString("clear"));
        clearBtn.setFont(delBtn.getFont().deriveFont(18f));
        clearBtn.setFocusable(false);
        clearBtn.addActionListener(ProgramicalController.action());
            //setting keybindings for delete,clear and enter
        CalcGUI.setKeybind(this,KeyEvent.VK_BACK_SPACE,"delete",delBtn);
        CalcGUI.setKeybind(this,KeyEvent.VK_ESCAPE,"clear",clearBtn);
        CalcGUI.setEnterKeybind(this,arithmeticButtons[8]);

        // panel is divided into 3 sections/groups
        // 1st takes buttons with different number system with labels and main label
        // 2nd Abtn, delete, clear buttons
        // 3rd takes all number/hex and action buttons
            //first section
        var section1 = new JPanel();
        section1.setLayout(new GridBagLayout());
        var gcsec1 = new GridBagConstraints();
        // 2 rows with description label and main input and smaller label under it
        gcsec1.fill = GridBagConstraints.HORIZONTAL;
        gcsec1.gridx = 0;
        gcsec1.gridy = 0;
        gcsec1.weightx = 0;
        section1.add(descLabel,gcsec1);
        gcsec1.gridx = 1;
        gcsec1.weightx = 1;
        section1.add(input, gcsec1);
        gcsec1.gridy += 1;
        section1.add(tempLabel,gcsec1);
        // next row
        gcsec1.gridx = 0;
        gcsec1.gridy += 1;
        gcsec1.weightx = 0;
        section1.add(numSystemBtns[0], gcsec1);
        gcsec1.gridx = 1;
        gcsec1.weightx = 1;
        section1.add(decLabel, gcsec1);
        // next row
        gcsec1.gridx = 0;
        gcsec1.gridy += 1;
        gcsec1.weightx = 0;
        section1.add(numSystemBtns[1], gcsec1);
        gcsec1.gridx = 1;
        gcsec1.weightx = 1;
        section1.add(hexLabel, gcsec1);
        // next row
        gcsec1.gridx = 0;
        gcsec1.gridy += 1;
        gcsec1.weightx = 0;
        section1.add(numSystemBtns[2], gcsec1);
        gcsec1.gridx = 1;
        gcsec1.weightx = 1;
        section1.add(octalLabel, gcsec1);
        // next row
        gcsec1.gridx = 0;
        gcsec1.gridy += 1;
        gcsec1.weightx = 0;
        section1.add(numSystemBtns[3], gcsec1);
        gcsec1.gridx = 1;
        gcsec1.weightx = 1;
        section1.add(binaryLabel, gcsec1);
            // 2nd section
        var section2 = new JPanel();
        section2.setLayout(new GridLayout(0,3));
        section2.add(hexaLetterBtns[0]);
        section2.add(clearBtn);
        section2.add(delBtn);
            // 3rd section
        var section3 = new JPanel();
        section3.setLayout(new GridLayout(0,5));
        // 1st row
        section3.add(hexaLetterBtns[1]);
        section3.add(arithmeticButtons[0]);
        section3.add(arithmeticButtons[1]);
        section3.add(arithmeticButtons[2]);
        section3.add(arithmeticButtons[4]);
        // 2nd row
        section3.add(hexaLetterBtns[2]);
        section3.add(numberButtons[7]);
        section3.add(numberButtons[8]);
        section3.add(numberButtons[9]);
        section3.add(arithmeticButtons[5]);
        // 3rd row
        section3.add(hexaLetterBtns[3]);
        section3.add(numberButtons[4]);
        section3.add(numberButtons[5]);
        section3.add(numberButtons[6]);
        section3.add(arithmeticButtons[6]);
        // 4th row
        section3.add(hexaLetterBtns[4]);
        section3.add(numberButtons[1]);
        section3.add(numberButtons[2]);
        section3.add(numberButtons[3]);
        section3.add(arithmeticButtons[7]);
        // 5th row
        section3.add(hexaLetterBtns[5]);
        section3.add(flipBtn);
        section3.add(numberButtons[0]);
        section3.add(dotBtn);
        section3.add(arithmeticButtons[8]);

        setLayout(new GridBagLayout());
        var gc = new GridBagConstraints();
        // adding every section to class object
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
     * Returns reference to static final instance of Programical class object.
     * @return Instance of Programical class.
     */
    public static JPanel instance(){ return INSTANCE; }

    /**
     * Returns reference to JButton array of number buttons.
     * @return JButton array object.
     */
    public static JButton[] getNumberBtns() { return numberButtons; }

    /**
     * Returns reference to JButton array of hexadecimal letter buttons.
     * @return JButton array object.
     */
    public static JButton[] getLetterBtns() { return hexaLetterBtns; }

    /**
     * Returns reference to JButton array of number system buttons, ie. DEC,HEX,OCT,BIN.
     * @return JButton array object.
     */
    public static JButton[] getNumTypeBtns() { return numSystemBtns; }

    /**
     * Returns input textfield object.
     * @return JTextField object
     */
    public static JTextField getInputField(){ return input; }

    /**
     * Returns description label object, ie. what current system number is chosen.
     * @return JTextField object
     */
    public static JTextField getDescLabel() { return descLabel; }

    /**
     * Returns JTextField object that holds decimal numbers that are automatically converted there from given input.
     * @return JTextField object
     */
    public static JTextField getDecLabel() {return decLabel;}

    /**
     * Returns JTextField object that holds hexadecimal numbers that are automatically converted there from given input.
     * @return JTextField object
     */
    public static JTextField getHexLabel() {return hexLabel;}

    /**
     * Returns JTextField object that holds octal numbers that are automatically converted there from given input.
     * @return JTextField object
     */
    public static JTextField getOctalLabel() {return octalLabel;}

    /**
     * Returns JTextField object that holds binary numbers that are automatically converted there from given input.
     * @return JTextField object
     */
    public static JTextField getBinaryLabel() {return binaryLabel;}

    /**
     * Returns JTextField object that holds a result of temporal or final calculation.
     * @return JTextField object
     */
    public static JTextField getTempLabel() {return tempLabel;}

    /**
     * Sets few properties for given JTextField.
     * @param field JTextField object to set properties.
     * @param SCalignment alignment of type int from SwingConstants class
     * @param size Size of text displayed in given JTextField
     */
    private void setTxtFieldProperties(JTextField field,int SCalignment, float size){
        field.setEditable(false);
        field.setFocusable(false);
        field.setHorizontalAlignment(SCalignment);
        field.setFont(field.getFont().deriveFont(size));
    }
}
