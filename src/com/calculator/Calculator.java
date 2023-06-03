package com.calculator;

import com.formdev.flatlaf.intellijthemes.FlatNordIJTheme;
import com.formdev.flatlaf.intellijthemes.materialthemeuilite.FlatArcDarkContrastIJTheme;
import com.formdev.flatlaf.intellijthemes.materialthemeuilite.FlatMoonlightContrastIJTheme;

import javax.swing.*;

/**
 * Class that holds main method only. Sets custom LaF and launched application. If custom fails to load, default swing LaF will be used.
 */
public class Calculator {

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            try {
                //loading custom Look and Feel
                //FlatArcDarkContrastIJTheme.setup();
                FlatNordIJTheme.setup();
                JFrame appGui = CalcGUI.getGuiInstance();
                appGui.setVisible(true);
            } catch( Exception ex ) {
                //custom LaF failed, then launch with standard LaF
                JFrame appGui = CalcGUI.getGuiInstance();
                appGui.setVisible(true);
            }
        });
    }
}
