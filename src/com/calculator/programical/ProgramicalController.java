package com.calculator.programical;

import com.calculator.CalcGUI;
import javax.swing.*;
import java.awt.event.ActionEvent;
import java.util.ResourceBundle;
import static com.calculator.programical.Programical.*;

/**
 * Main controller class handling calculations in ProgramicalCalculator module. By extending {@link AbstractAction} class, this class can be used as an action listener object.
 */
public class ProgramicalController extends AbstractAction {

    /**
     * One and only instance of controller class. (followed with private constructor)
     */
    private static final ProgramicalController INSTANCE = new ProgramicalController();

    private final String sType = "DEC HEX OCT BIN";
    private final String alphaNumeric = "0123456789ABCDEF";
    private final String allOperators = "+-/*";
    private final ResourceBundle lang = CalcGUI.getLangBundle();

    /**
     * Private constructor to prevent creating new objects.
     */
    private ProgramicalController(){}

    /**
     * Returns instance of controller. Used mostly to set action listener.
     * @return Action object
     */
    public static Action action(){ return INSTANCE; }

    @Override
    public void actionPerformed(ActionEvent e) {

        // changes number system to system clicked via button, enables/disables number buttons as necessary
        if (sType.contains(e.getActionCommand())){
            // converts type only if value != 0
            if ( !(getTempLabel().getText().equals("0")) ){
                String input = getTempLabel().getText();
                String operator = input.substring(input.length()-1);
                String num = input.substring(0, input.length()-1);
                switch (e.getActionCommand()){
                    case "DEC" -> {
                        switch (getDescLabel().getText()){
                            case "HEX" -> num = Conversion.hexToDec(num);
                            case "OCT" -> num = Conversion.octalToDec(num);
                            case "BIN" -> num = Conversion.binToDec(num);
                        }
                    }
                    case "HEX" -> {
                        switch (getDescLabel().getText()){
                            case "DEC" -> num = Conversion.decToHex(num);
                            case "OCT" -> {
                                num = Conversion.octalToDec(num);
                                num = Conversion.decToHex(num);
                            }
                            case "BIN" -> {
                                num = Conversion.binToDec(num);
                                num = Conversion.decToHex(num);
                            }
                        }
                    }
                    case "OCT" -> {
                        switch (getDescLabel().getText()){
                            case "DEC" -> num = Conversion.decToBinOc(num,8);
                            case "HEX" -> {
                                num = Conversion.hexToDec(num);
                                num = Conversion.decToBinOc(num,8);
                            }
                            case "BIN" -> {
                                num = Conversion.binToDec(num);
                                num = Conversion.decToBinOc(num,8);
                            }
                        }

                    }
                    case "BIN" -> {
                        switch (getDescLabel().getText()){
                            case "DEC" -> num = Conversion.decToBinOc(num,2);
                            case "HEX" -> {
                                num = Conversion.hexToDec(num);
                                num = Conversion.decToBinOc(num,2);
                            }
                            case "OCT" ->{
                                num = Conversion.octalToDec(num);
                                num = Conversion.decToBinOc(num,2);
                            }
                        }
                    }
                }
                getTempLabel().setText(num + operator);
            }

            // sets proper description, enables proper buttons and if value in input != 0 then sets proper converted value from chosen label
            switch (e.getActionCommand()){
                case "DEC" -> {
                    getDescLabel().setText("DEC");
                    disableBtn(0, Programical.getNumTypeBtns());
                    for (JButton b : getLetterBtns()){
                        b.setEnabled(false);
                    }
                    for (JButton b : getNumberBtns()){
                        b.setEnabled(true);
                    }
                    if ( !(getInputField().getText().equals("")) ) getInputField().setText(getDecLabel().getText());
                }
                case "HEX" -> {
                    getDescLabel().setText("HEX");
                    disableBtn(1, getNumTypeBtns());
                    for (JButton b : getLetterBtns()){
                        b.setEnabled(true);
                    }
                    for (JButton b : getNumberBtns()){
                        b.setEnabled(true);
                    }
                    if ( !(getInputField().getText().equals("")) ) getInputField().setText(getHexLabel().getText());
                }
                case "OCT" -> {
                    getDescLabel().setText("OCT");
                    disableBtn(2, getNumTypeBtns());
                    for (JButton b : getLetterBtns()){
                        b.setEnabled(false);
                    }
                    for (JButton b : getNumberBtns()){
                        b.setEnabled(true);
                    }
                    var numBtns = getNumberBtns();
                    numBtns[8].setEnabled(false);
                    numBtns[9].setEnabled(false);
                    if ( !(getInputField().getText().equals("")) ) getInputField().setText(getOctalLabel().getText());
                }
                case "BIN" -> {
                    getDescLabel().setText("BIN");
                    disableBtn(3, getNumTypeBtns());
                    for (JButton b : getLetterBtns()){
                        b.setEnabled(false);
                    }
                    for (JButton b : getNumberBtns()){
                        b.setEnabled(false);
                    }
                    var numBtns = getNumberBtns();
                    numBtns[0].setEnabled(true);
                    numBtns[1].setEnabled(true);
                    if ( !(getInputField().getText().equals("")) ) getInputField().setText(getBinaryLabel().getText());
                }

            }
        }

        // append numbers in input field and triggers conversion
        if (alphaNumeric.contains(e.getActionCommand())){
            getInputField().setText(
                    getInputField().getText()
                            .concat(e.getActionCommand())
            );
            convert(getInputField().getText());
        }

        // clears inputs
        if (e.getActionCommand().equals(lang.getString("clear"))){
            getInputField().setText("");
            getTempLabel().setText("0");
            getDecLabel().setText("");
            getHexLabel().setText("");
            getOctalLabel().setText("");
            getBinaryLabel().setText("");
        }

        // deletes last character (number) in input field
        if (e.getActionCommand().equals(lang.getString("delete"))){
            String input = getInputField().getText();
            int inputLength = input.length();
            if (inputLength != 0){
                input = input.substring(0, inputLength - 1);
                getInputField().setText(input);
                if (input.length() > 0) convert(input);
            }

        }

        // makes final calculation and triggers conversion
        if (e.getActionCommand().equals("=") || e.getActionCommand().equals("equals")){
            String input = getInputField().getText();
            String tempLabel = getTempLabel().getText();
            if ( !(tempLabel.equals("0")) && !(input.isBlank()) ) {
                String oldOperator = tempLabel.substring(tempLabel.length()-1);
                String num1 = tempLabel.substring(0, tempLabel.length()-1);
                String num2 = input;

                // convert number to decimal for easier calculations
                switch (getDescLabel().getText()){
                    case "DEC" -> {}
                    case "HEX" -> {
                        num1 = Conversion.hexToDec(num1);
                        num2 = Conversion.hexToDec(num2);
                    }
                    case "OCT" -> {
                        num1 = Conversion.octalToDec(num1);
                        num2 = Conversion.octalToDec(num2);
                    }
                    case "BIN" -> {
                        num1 = Conversion.binToDec(num1);
                        num2 = Conversion.binToDec(num2);
                    }
                }

                //calculate new number
                String result = "";
                switch (oldOperator){
                    case "+" -> result = String.valueOf(Double.parseDouble(num1) + Double.parseDouble(num2));
                    case "-" -> result = String.valueOf(Double.parseDouble(num1) - Double.parseDouble(num2));
                    case "*" -> result = String.valueOf(Double.parseDouble(num1) * Double.parseDouble(num2));
                    case "/" -> result = String.valueOf(Double.parseDouble(num1) / Double.parseDouble(num2));
                }

                //get rid of dot
                if (result.contains(".")){
                    int endIndex = result.indexOf(".");
                    result = result.substring(0,endIndex);
                }


                //convert to proper num system
                switch (getDescLabel().getText()){
                    case "DEC" -> {}
                    case "HEX" -> result = Conversion.decToHex(result);
                    case "OCT" -> result = Conversion.decToBinOc(result,8);
                    case "BIN" -> result = Conversion.decToBinOc(result,2);
                }
                //activate conversion
                convert(result);

                //set textfields
                switch (getDescLabel().getText()){
                    case "DEC" -> getInputField().setText(getDecLabel().getText());
                    case "HEX" -> getInputField().setText(getHexLabel().getText());
                    case "OCT" -> getInputField().setText(getOctalLabel().getText());
                    case "BIN" -> getInputField().setText(getBinaryLabel().getText());
                }
                getTempLabel().setText("0");
            }

            if (input.isBlank() && !(tempLabel.equals("0"))){
                String num1 = tempLabel.substring(0, tempLabel.length()-1);
                getInputField().setText(num1);
                getTempLabel().setText("0");
            }
        }

        // square root calculation
        if (e.getActionCommand().equals("sqrRoot")){
            String input = getInputField().getText();
            String tempLabel = getTempLabel().getText();
            String result = "";
            String op = "";

            if ( !(input.isBlank()) && tempLabel.equals("0")){ result = input; }
            if ( !(input.isBlank()) && !(tempLabel.equals("0"))){ result = input; }

            if ( input.isBlank() && !(tempLabel.equals("0"))){
                result = tempLabel.substring(0, tempLabel.length() -1);
                op = tempLabel.substring(tempLabel.length()-1);
            }
            switch (getDescLabel().getText()){
                case "DEC" -> {}
                case "HEX" -> result = Conversion.hexToDec(result);
                case "OCT" -> result = Conversion.octalToDec(result);
                case "BIN" -> result = Conversion.binToDec(result);
            }

            var num = Double.parseDouble(result);
            num = Math.sqrt(num);
            result = String.valueOf(num);
            int end = result.indexOf('.');
            result = result.substring(0, end);
            if ( !(input.isBlank()) && tempLabel.equals("0")){
                getInputField().setText(result);
                convert(result);
            }
            if ( input.isBlank() && !(tempLabel.equals("0"))){
                getTempLabel().setText(result + op);
                convert(result);
            }
            if ( !(input.isBlank()) && !(tempLabel.equals("0")) ){
                getInputField().setText(result);
                convert(result);
            }
        }

        // square power calculation
        if (e.getActionCommand().equals("sqrPower")){
            String input = getInputField().getText();
            String tempLabel = getTempLabel().getText();
            String result = "";
            String op = "";

            if ( !(input.isBlank()) && tempLabel.equals("0")){ result = input; }
            if ( !(input.isBlank()) && !(tempLabel.equals("0"))){ result = input; }

            if ( input.isBlank() && !(tempLabel.equals("0"))){
                result = tempLabel.substring(0, tempLabel.length() -1);
                op = tempLabel.substring(tempLabel.length()-1);
            }
            switch (getDescLabel().getText()){
                case "DEC" -> {}
                case "HEX" -> result = Conversion.hexToDec(result);
                case "OCT" -> result = Conversion.octalToDec(result);
                case "BIN" -> result = Conversion.binToDec(result);
            }

            var num = Double.parseDouble(result);
            num = num * num;
            result = String.valueOf(num);
            int end = result.indexOf('.');
            result = result.substring(0, end);
            if ( !(input.isBlank()) && tempLabel.equals("0")){
                getInputField().setText(result);
                convert(result);
            }
            if ( input.isBlank() && !(tempLabel.equals("0"))){
                getTempLabel().setText(result + op);
                convert(result);
            }
            if ( !(input.isBlank()) && !(tempLabel.equals("0")) ){
                getInputField().setText(result);
                convert(result);
            }
        }

        //covers simple calculations when clicking operational buttons
        if (allOperators.contains(e.getActionCommand())){
            String input = getInputField().getText();
            String tempLabel = getTempLabel().getText();

            // calculate new number only if both fields arent empty
            if ( !(tempLabel.equals("0")) && !(input.isBlank())){
                // get operators and numbers
                String oldOperator = tempLabel.substring(tempLabel.length()-1);
                String newOperator = "";
                switch (e.getActionCommand()){
                    case "+" -> newOperator = "+";
                    case "-" -> newOperator = "-";
                    case "*" -> newOperator = "*";
                    case "/" -> newOperator = "/";
                }
                String num1 = tempLabel.substring(0, tempLabel.length()-1);
                String num2 = input;

                // convert number to decimal for easier calculations
                switch (getDescLabel().getText()){
                    case "DEC" -> {}
                    case "HEX" -> {
                        num1 = Conversion.hexToDec(num1);
                        num2 = Conversion.hexToDec(num2);
                    }
                    case "OCT" -> {
                        num1 = Conversion.octalToDec(num1);
                        num2 = Conversion.octalToDec(num2);
                    }
                    case "BIN" -> {
                        num1 = Conversion.binToDec(num1);
                        num2 = Conversion.binToDec(num2);
                    }
                }

                //calculate new number
                String result = "";
                switch (oldOperator){
                    case "+" -> result = String.valueOf(Double.parseDouble(num1) + Double.parseDouble(num2));
                    case "-" -> result = String.valueOf(Double.parseDouble(num1) - Double.parseDouble(num2));
                    case "*" -> result = String.valueOf(Double.parseDouble(num1) * Double.parseDouble(num2));
                    case "/" -> result = String.valueOf(Double.parseDouble(num1) / Double.parseDouble(num2));
                }

                //get rid of dot
                if (result.contains(".")){
                    int endIndex = result.indexOf(".");
                    result = result.substring(0,endIndex);
                }

                //convert to proper num system
                switch (getDescLabel().getText()){
                    case "DEC" -> {}
                    case "HEX" -> result = Conversion.decToHex(result);
                    case "OCT" -> result = Conversion.decToBinOc(result,8);
                    case "BIN" -> result = Conversion.decToBinOc(result,2);
                }

                //activate conversion
                convert(result);

                //add new operator
                result = result + newOperator;

                //set textfields
                getTempLabel().setText(result);
                getInputField().setText("");
            }

            // init templabel only if input isnt empty and if temp is zero
            if (tempLabel.equals("0") && !(input.isBlank())){
                String newOperator = "";
                getInputField().setText("");
                switch (e.getActionCommand()){
                    case "+" -> newOperator = "+";
                    case "-" -> newOperator = "-";
                    case "*" -> newOperator = "*";
                    case "/" -> newOperator = "/";
                }
                tempLabel = input + newOperator;
                getTempLabel().setText(tempLabel);
            }
        }
    }

    /**
     * Converts given number input to every numeric system. Since numbers are taken as type of String, parameter is also of String type.
     * @param input Number to convert
     */
    private void convert(String input){
        String type = Programical.getDescLabel().getText();
        switch (type){
            case "DEC" -> {
                getDecLabel().setText(input);
                getHexLabel().setText(Conversion.decToHex(input));
                getOctalLabel().setText(Conversion.decToBinOc(input,8));
                getBinaryLabel().setText(Conversion.decToBinOc(input,2));
            }
            case "HEX" -> {
                getDecLabel().setText(Conversion.hexToDec(input));
                getHexLabel().setText(input);
                getOctalLabel().setText(
                        Conversion.decToBinOc(
                                Conversion.hexToDec(input),8)
                );
                getBinaryLabel().setText(
                        Conversion.decToBinOc(
                                Conversion.hexToDec(input),2)
                );
            }
            case "OCT" -> {
                getDecLabel().setText(Conversion.octalToDec(input));
                getHexLabel().setText(
                        Conversion.decToHex(
                                Conversion.octalToDec(input))
                );
                getOctalLabel().setText(input);
                getBinaryLabel().setText(
                        Conversion.decToBinOc(
                                Conversion.octalToDec(input),2)
                );
            }
            case "BIN" -> {
                getDecLabel().setText(Conversion.binToDec(input));
                getHexLabel().setText(
                        Conversion.decToHex(
                                Conversion.binToDec(input))
                );
                getOctalLabel().setText(
                        Conversion.decToBinOc(
                                Conversion.binToDec(input), 8)
                );
                getBinaryLabel().setText(input);
            }
        }
    }

    /**
     * Disables chosen numeric system (button). Takes JButton array containing all numeric systems and disables one from specified index.
     * @param index Index of element to disable
     * @param array Array of JButton containing all numeric systems (ei. DEC,HEX,OCT,BIN)
     */
    private void disableBtn(int index, JButton[] array){
        for (JButton b : array){
            b.setEnabled(true);
        }
        array[index].setEnabled(false);
    }
}
