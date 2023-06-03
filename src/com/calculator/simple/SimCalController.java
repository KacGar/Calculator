package com.calculator.simple;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import static com.calculator.simple.SimpleCalculator.*;

/**
 * Main controller class handling calculations in SimpleCalculator module. By extending {@link AbstractAction} class, this class can be used as a action listener object.
 */
public class SimCalController extends AbstractAction{

    private enum OPERATION {
        ADD("+"){
            public double apply(double x, double y) { return x + y; }
        },
        SUBTRACT("-"){
            public double apply(double x, double y) { return x - y; }
        },
        MULTI("*"){
            public double apply(double x, double y) { return x * y; }
        },
        DIV("/"){
            public double apply(double x, double y) { return x / y;}
        };

        private final String symbol;
        OPERATION(String op) { symbol = op; }
        @Override public String toString() { return symbol;}
        public abstract double apply(double x, double y);
    }

    private enum OPERATION_SQR{

        SqrROOT('\u221A' + "x"){
            @Override
            public double sqrRoot(double x) { return super.sqrRoot(x); }
        },
        SqrPOWER("X" + '\u00B2'){
            @Override
            public double sqrPower(double x) { return super.sqrPower(x); }
        };

        private final String symbol;
        OPERATION_SQR(String op) { symbol = op; }
        @Override public String toString() { return symbol;}
        public double sqrRoot(double x) { return Math.sqrt(x); }
        public double sqrPower(double x) { return x * x; }
    }

    /**
     * Single instance of SimCalController class (coupled with private constructor)
     */
    private static Action INSTANCE = new SimCalController();
    /**
     * Variables used in calculations. Few conditions and certain actions are depended on state of those variables. Changing / assigning values should be treated with caution.
     */
    private static double x = 0, y = 0, result = 0;
    /**
     * Operators of type OPERATION enum, used to perform specific operations.
     * Few conditions and certain actions are depended on state of those variables. Changing / assigning values should be treated with caution.
     */
    private static OPERATION tempOp;
    /**
     * Field that holds compiled once pattern for dot checking in textfield.
     */
    private final Pattern pattern = Pattern.compile(".*[.]+.*");

    /**
     * Returns reference to controller object of type Action. Used mostly for assigning action listener.
     * @return Reference to controller class of type {@link Action}
     */
    public static Action action() { return INSTANCE; }

    /**
     * Private constructor prevents constructing new object of controller class.
     */
    private SimCalController(){}

    String nums = "0123456789";
    @Override
    public void actionPerformed(ActionEvent e) {
        String s = e.getActionCommand();

        // checks if number was pressed, if not one of the rest btns was clicked
        if ( nums.contains(s) ) {
            if (!(getInputField().getText().equals(""))){
                if (getInputField().getText().charAt(0) == '0' && !(getInputField().getText().contains("."))){
                    getInputField().setText("");
                }
                getInputField().setText(getInputField().getText().concat(s));
            }
            if (getInputField().getText().equals("")){
                getInputField().setText(getInputField().getText().concat(s));
            }
        } else {
            switch (s){
                case "+" -> { if ( !(getInputField().getText().equals("")) ){ tempCalcs(OPERATION.ADD); } }
                case "-" -> { if ( !(getInputField().getText().equals("")) ){ tempCalcs(OPERATION.SUBTRACT); } }
                case "*" -> { if ( !(getInputField().getText().equals("")) ){ tempCalcs(OPERATION.MULTI); } }
                case "/" -> { if ( !(getInputField().getText().equals("")) ){ tempCalcs(OPERATION.DIV); } }
                case "=" -> {
                    tempCalcs(tempOp);
                    getInputField().setText(getLabel().getText());
                    getLabel().setText("0");
                    result = 0;
                    x = 0;
                    y = 0;
                }
                case "sqrPower" ->  sqrCalcs(OPERATION_SQR.SqrPOWER);
                case "sqrRoot" ->  sqrCalcs(OPERATION_SQR.SqrROOT);
                case "flip" ->  flipSgn();
                case "percent" -> {
                    double temp = Double.parseDouble(getInputField().getText());
                    if (tempOp == OPERATION.ADD || tempOp == OPERATION.SUBTRACT){
                        double base = Double.parseDouble(getLabel().getText());
                        temp = (base * temp) / 100;
                    }else {
                        temp = temp / 100;
                    }
                    getInputField().setText(String.valueOf(temp));
                }
                case "," -> {
                    if ( !(getInputField().getText().equals("")) ){
                        Matcher m = pattern.matcher(getInputField().getText());
                        boolean b = m.matches();
                        if ( !b ) { getInputField().setText(getInputField().getText().concat(".")); }
                    }
                }
                case "delete", "Cofnij" -> {
                    String input = getInputField().getText();
                    if (input.length() != 0){ input = input.substring(0, input.length() - 1); }
                    if (input.length() != 0 && input.charAt(input.length()-1) == '.'){ input = input.substring(0, input.length() - 1); }
                    if (input.length() == 0) { getInputField().setText("0"); }
                    else { getInputField().setText(input); }
                }
                case "clear", "Wyczyść" -> {
                    x = 0;
                    y = 0;
                    getInputField().setText("0");
                    getLabel().setText("0.0");
                    result = 0;
                }
            }
        }
    }

    /**
     * Method that performs temporal calculation whenever functional button is used (like add, subtract, etc.).
     * Performs operations on two values at once, therefore, prompting two values creates a result that will be used with next value prompted later.
     * For example, if user wants to prompt x + y + z, hitting second time ADD button will perform math and shown result of (x+y) in label and wait for user to input z value,
     * after that user can choose another functional button (which will do same work like with x,y but now with (result + z) or hit EQUALS.
     * @param operator Operator of OPERATION enum type chosen to perform calculations.
     */
    private void tempCalcs(OPERATION operator){
        //do work only if input isnt empty
        if (!(getInputField().getText().equals(""))){
            //cover situation at start of application
            if (result == 0 && (Double.parseDouble(getLabel().getText()) == 0) ){
                x = Double.parseDouble(getInputField().getText());
                getInputField().setText("");
                getLabel().setText(String.valueOf(x));
                result = x;
                tempOp = operator;
            }
            else if (tempOp == null){
                getLabel().setText(String.valueOf(Double.valueOf(getInputField().getText())));
                getInputField().setText("");
            }
            else {
                //perform math
                y = Double.parseDouble(getInputField().getText());
                switch (tempOp){
                    case ADD -> x = OPERATION.ADD.apply(x,y);
                    case SUBTRACT -> x = OPERATION.SUBTRACT.apply(x,y);
                    case MULTI -> x = OPERATION.MULTI.apply(x,y);
                    case DIV -> x = OPERATION.DIV.apply(x,y);
                }
                y = 0;
                tempOp = operator;
                //display result
                DecimalFormat df = new DecimalFormat("#.####");
                df.setRoundingMode(RoundingMode.HALF_DOWN);
                String s = String.valueOf(df.format(x));
                s = s.replace(",",".");
                getLabel().setText(s);
                getInputField().setText("0");
            }
        }
    }

    /**
     * Method that handles two operation - root and power square. Calculations are performed on prompted values,
     * if input is empty but temporal value is present then temporal value will be used for calculations.
     * @param operator Operator object of OPERATION enum. Square root and power should be only used. Other types won't work.
     */
    private void sqrCalcs(OPERATION_SQR operator){
        double temp;
        //retrevie value
        if ( getInputField().getText().equals("")){
            temp = Double.parseDouble(getLabel().getText());
        } else {
            temp = Double.parseDouble(getInputField().getText());
        }
        //make calculation
        switch (operator){
            case SqrROOT -> temp = OPERATION_SQR.SqrROOT.sqrRoot(temp);
            case SqrPOWER -> temp = OPERATION_SQR.SqrPOWER.sqrPower(temp);
        }
        //display result
        if ( getInputField().getText().equals("")){
            getLabel().setText(String.valueOf(temp));
        } else {
            getInputField().setText(String.valueOf(temp));
        }
    }

    /**
     * "Flips" signs of prompted value (plus to minus or minus to plus). If input is empty and temporal values is not, then temporal value will be used.
     */
    private void flipSgn(){
        double temp;
        //retrieve value
        if ( getInputField().getText().equals("")){
            temp = Double.parseDouble(getLabel().getText());
        } else {
            temp = Double.parseDouble(getInputField().getText());
        }
        //flip sign
        if (temp > 0){
            temp = Double.parseDouble("-" + temp);
        } else {
            long rest = (int) (temp / 10) * 10;
            long num = (int) temp;
            num = Math.negateExact(num);
            String flipped = num + "." + rest;
            temp = Double.parseDouble(flipped);
        }
        //display result
        if ( getInputField().getText().equals("")){
            getLabel().setText(String.valueOf(temp));
            x = temp;
            result = temp;
        } else {
            getInputField().setText(String.valueOf(temp));
        }
    }

}
