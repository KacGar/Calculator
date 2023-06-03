package com.calculator.programical;

/**
 * Conversion class is responded to convert numbers (values) from one given system to other.
 * Object of this class can't be created. Holds only public static methods.
 */
public class Conversion {

    /**
     * Private constructor prevents creating objects.
     */
    private Conversion(){}

    /**
     * Method that converts DECIMAL number to BINARY or OCTAL number. Number is taken mostly from textfields therefore parameter is of String type.
     * Returns value in String type for easier use for displaying in textfields/labels.
     * Conditions corresponds on what system decimal number should be convert into. Conditions are 2 for BINARY and 8 for OCTAL.
     * @param s Decimal number.
     * @param condition Number system to convert to (2-BIN,8-OCTAL)
     * @return Number value of type String.
     */
    public static String decToBinOc( String s, int condition){
        int base = 1;

        if ( condition == 2 ){ base = 2; }        // binary
        else if ( condition == 8 ){ base = 8; }   // octal

        long decNumber = Long.parseLong(s);
        StringBuilder result = new StringBuilder();

        while ( decNumber != 0 ) {
            result.append(decNumber%base);
            decNumber = decNumber/base;
        }
        return result.reverse().toString();
    }

    /**
     * Converts given DECIMAL numbet to HEXADECIMAL number. Number is taken mostly from textfields therefore parameter is of String type.
     * Returns value in String type for easier use for displaying in textfields/labels.
     * @param s Decimal number.
     * @return Hexadecimal number of type String.
     */
    public static String decToHex (String s){
        String digits = "0123456789ABCDEF";
        int decNumber = Integer.parseInt(s);
        int reminder;
        StringBuilder result = new StringBuilder();

        while ( decNumber !=0 ){
            reminder = decNumber % 16;
            decNumber /= 16;
            result.append(digits.charAt(reminder));
        }
        return result.reverse().toString();
    }

    /**
     * Converts BINARY number to DECIMAL. Number is taken mostly from textfields therefore parameter is of String type.
     * Returns value in String type for easier use for displaying in textfields/labels.
     * @param s Binary number.
     * @return Decimal number of type String.
     */
    public static String binToDec (String s){
        long binNumber = Long.parseLong(s);
        long remainder, decNumber = 0, i = 1;

        while ( binNumber !=0 ){
            remainder = binNumber %10;
            decNumber = decNumber + remainder * i;
            i = i*2;
            binNumber = binNumber/10;
        }
        return String.valueOf(decNumber);
    }

    /**
     * Converts HEXADECIMAL number into DECIAML. Number is taken mostly from textfields therefore parameter is of String type.
     * Returns value in String type for easier use for displaying in textfields/labels.
     * @param s Hexadecimal number.
     * @return Decimal number of type String.
     */
    public static String hexToDec (String s){
        String digits = "0123456789ABCDEF";
        s = s.toUpperCase();
        int val = 0;

        for (int i = 0; i < s.length(); i++){
            char c = s.charAt(i);
            int d = digits.indexOf(c);
            val = 16*val + d;
        }
        return String.valueOf(val);
    }

    /**
     * Converts OCTAL number to DECIMAL number.Number is taken mostly from textfields therefore parameter is of String type.
     * Returns value in String type for easier use for displaying in textfields/labels.
     * @param s Octal number.
     * @return Decimal number of type String.
     */
    public static String octalToDec (String s){
        int i = 0;
        long decNumber = 0 ;
        long octal = Long.parseLong(s);

        while ( octal != 0 ) {
            decNumber = (long) (decNumber + (octal % 10) * Math.pow(8, i++));
            octal = octal / 10;
        }
        return String.valueOf(decNumber);
    }

}
