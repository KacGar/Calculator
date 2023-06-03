package com.calculator;

import com.calculator.lang.LangBundle;
import com.calculator.simple.SimpleCalculator;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.time.Instant;
import java.util.ResourceBundle;
import java.util.prefs.Preferences;

/**
 * Main GUI class of application, which handles creation of frame window containing only one JPanel acting as a wrapper.
 * Content if application such as simple calculator or currency exchange is created in seperate modules which use few static methods from this class,
 * those methods handles creation of common components like number buttons or setting keybinds for number buttons.
 */
public final class CalcGUI extends JFrame {

    /**
     * Field of Preferences object calling root node for the calling user used to save few settings.
     */
    private static final Preferences root = Preferences.userRoot();
    /**
     * Main node of application which holds information about launch time of application and current language.
     */
    private static final Preferences node = root.node("com.calculator.CalcGUI");
    /**
     * Language bundle instance. Fot the moment, there are 2 languages available (english (with US) and polish).
     */
    private static final ResourceBundle langBundle = LangBundle.getResBundle(node);
    /**
     * Static field object of {@link MenuController} class to instantiate only one menu object for application.
     */
    private static final MenuController menuListener = new MenuController();
    /**
     * Wrapper panel field which holds content such as simple calculator.
     */
    private static JPanel wrapper = new JPanel();
    /**
     * Static GUI object to ensure that only one object of {@link JFrame} object is created (followed with private constructor)
     */
    private static final JFrame guiInstance = new CalcGUI();

    /**
     * Private GUI constructor to ensure only one object is created (followed with final statc field)
     */
    private CalcGUI(){
        // frame settings
        setSize(new Dimension(370,500));
        setMinimumSize(new Dimension(270,350));
        setLocationRelativeTo(null);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setTitle(langBundle.getString("title"));
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                Instant instant = Instant.now();
                node.put("appLaunchDate", instant.toString());
                // appLaunchDate key is mostly used in currency exchange module which connects to XML file by internet.
                // Therefore controller makes sure that application is connecting to server after fixed period of time, to avoid being blocked by said server.
            }
        });

        //menu  (type of calculator)
        JMenu menu1 = new JMenu(langBundle.getString("menu1"));
        JMenuItem sm11 = new JMenuItem(langBundle.getString("sm11"));
        sm11.setEnabled(false);
        JMenuItem sm13 = new JMenuItem(langBundle.getString("sm13"));
        JMenuItem sm14 = new JMenuItem(langBundle.getString("sm14"));
        JMenuItem sm15 = new JMenuItem(langBundle.getString("sm15"));
        menu1.add(sm11);
        menu1.add(sm13);
        menu1.add(sm14);
        menu1.add(sm15);
            // changing language menu
        JMenu menu2 = new JMenu(langBundle.getString("menu2"));
        JMenuItem sm21 = new JMenuItem(langBundle.getString("sm21"));
        menu2.add(sm21);

        JMenu[] menus = {menu1,menu2};
        setupMenuListeners(menus);

        //setting menu to frame
        JMenuBar jMenuBar = new JMenuBar();
        jMenuBar.add(menu1);
        jMenuBar.add(menu2);
        setJMenuBar(jMenuBar);

        // adding simple calculator object as default to wrapper upon launching application
        wrapper.setLayout(new BorderLayout());
        wrapper.add(SimpleCalculator.instance(), BorderLayout.CENTER);

        setLayout(new BorderLayout());
        add(wrapper, BorderLayout.CENTER);

    }

    /**
     * Returns main frame of application. Bear in mind that object is static and final. Therefore, multiple callers get same object reference.
     * @return GUI object of type JFrame.
     */
    public static JFrame getGuiInstance(){ return guiInstance; }

    /**
     * Returns language bundle used in application. Language bundle is determined on launch of application (upon creating GUI object).
     * @return Language bundle of type ResourceBundle.
     */
    public static ResourceBundle getLangBundle() { return langBundle; }

    /**
     * Returns main wrapper panel which holds all content currently shown to the user.
     * @return JPanel object
     */
    public static JPanel getContentPanel(){ return wrapper; }

    /**
     * Returns node object of type Preferences used in application.
     * @return Node object of type Preferences.
     */
    public static Preferences getNode(){ return node; }

    /**
     * Assigns action commands and action listeners to every menu item with provided JMenu object as parameter.
     * Action commands are basically text that JMenuItem holds and action listener is a object of {@link MenuController} class.
     * @param menus
     */
    private void setupMenuListeners(JMenu[] menus){
        for (JMenu menu : menus) {
            for (int j = 0; j < menu.getItemCount(); j++) {
                String s = menu.getItem(j).getText();
                menu.getItem(j).setActionCommand(s);
                menu.getItem(j).addActionListener(menuListener);
            }
        }
    }

    /**
     * Creates array of number buttons (0-9) with provided text size and action object as parameter that will handle events.
     * @param fontSize Font size value displayed inside buttons.
     * @param action Action object with defined actionPerformed method.
     * @return Array of type JButton.
     */
    public static JButton[] getNumBtns(float fontSize, Action action){
        JButton[] btns = new JButton[10];
        for (int i = 0; i < btns.length; i++){
            btns[i] = new JButton(String.valueOf(i));
            btns[i].setFocusable(false);
            btns[i].setFont(btns[i].getFont().deriveFont(fontSize));
            btns[i].addActionListener(action);
        }
        return btns;
    }

    /**
     * Creates array of hexadecimal buttons (only A-F, without numbers) with provided text size and action object as parameter that will handle events.
     * @param fontsize Font size value displayed inside buttons.
     * @param action Action object with defined actionPerformed method.
     * @return Array of type JButton.
     */
    public static JButton[] getHexadecimalBtns(float fontsize, Action action){
        JButton[] hexaBtns = new JButton[6];
        String[] letters = {"A","B","C","D","E","F"};
        for (int i = 0; i < hexaBtns.length; i++){
            hexaBtns[i] = new JButton(letters[i]);
            hexaBtns[i].setFont(hexaBtns[i].getFont().deriveFont(fontsize));
            hexaBtns[i].setFocusable(false);
            hexaBtns[i].setEnabled(false);
            hexaBtns[i].addActionListener(action);
        }
        return hexaBtns;
    }

    /**
     * Creates array of numeric system buttons (DECimal, HEXadecimal, OCTal, BINary) with provided text size and action object as parameter that will handle events.
     * Text displayed in buttons are 3 first letters of specific numeric system, like DEC,HEX,OCT,BIN.
     * @param fontSize Font size value displayed inside buttons.
     * @param action Action object with defined actionPerformed method.
     * @return Array of type JButton.
     */
    public static JButton[] getNumericSystemBtns(float fontSize, Action action){
        JButton[] nsBtns = new JButton[4];
        String[] names = {"DEC","HEX","OCT","BIN"};
        for (int i = 0; i< nsBtns.length; i++){
            nsBtns[i] = new JButton(names[i]);
            nsBtns[i].setFocusable(false);
            nsBtns[i].setFont(nsBtns[i].getFont().deriveFont(fontSize));
            nsBtns[i].addActionListener(action);
        }
        return nsBtns;
    }

    /**
     * Creates array of arithmetic functions buttons (percent, sqrRoot, sqrPower, flipSign, divide, multiply, subtract, add, equals)
     * with provided text size and action object as parameter that will handle events. Every symbol (text) displayed in buttons is created with UNICODE codes.
     * @param fontsize Font size value displayed inside buttons.
     * @param action Action object with defined actionPerformed method.
     * @return Array of type JButton.
     */
    public static JButton[] getArithmeticFunctionBtns(float fontsize, Action action){
        JButton[] aritmBtns = new JButton[9];
        // symbols -> % , sqrRoot, sqrPower, flipsign , / , * , - , + ,  =
        char[] symbols = {'\u0025','\u221A','\u00B2','\u21B1','\u00F7','\u00D7','\u2212','\u002B','\u003D'};
        for (int i = 0; i < aritmBtns.length; i++){
            aritmBtns[i] = new JButton(String.valueOf(symbols[i]));
            aritmBtns[i].setFont(aritmBtns[i].getFont().deriveFont(fontsize));
            aritmBtns[i].setFocusable(false);
            aritmBtns[i].addActionListener(action);
            aritmBtns[i].setActionCommand(aritmBtns[i].getText());
        }
        aritmBtns[1].setText(aritmBtns[1].getText() + "x");
        aritmBtns[2].setText("x" + aritmBtns[2].getText());
        aritmBtns[3].setText(aritmBtns[3].getText().concat(String.valueOf('\u21B2')));

        String[] actionCommands = {"percent","sqrRoot","sqrPower","flip","/","*","-","+","="};
        for (int i = 0; i < actionCommands.length; i++){
            aritmBtns[i].setActionCommand(actionCommands[i]);
        }
        return  aritmBtns;
    }

    /**
     * Sets keybinds to numpads for every number (0-9) from provided array. Note that, array should contain 10 elements :) (can be less)
     * @param panel JPanel object upon which input/action map will be called.
     * @param action Action object handling events.
     * @param buttons Array of JButton type with numbers in it (0-9)
     */
    public static void setKeybindsForNumBtns(JPanel panel, Action action, JButton[] buttons){
        InputMap inputMap = panel.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW);
        for (JButton b : buttons){
            inputMap.put(KeyStroke.getKeyStroke("NUMPAD" + b.getText()), b.getText());
            inputMap.put(KeyStroke.getKeyStroke(b.getText()), b.getText());
            panel.getActionMap().put(b.getText(), action);
        }
    }

    /**
     * Sets keybinds for five arithmetic functions ie. divide,multiply,subtract,add located on numpad keyboard and equals button.
     * @param panel JPanel object upon which input/action map will be called.
     * @param action Action object handling events.
     * @param buttons Array of JButton type with numbers in it (0-9)
     */
    public static void setKeybindsForAritmBtns(JPanel panel, Action action, JButton[] buttons){
        InputMap inputMap = panel.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW);
        int[] keyCodes = {KeyEvent.VK_DIVIDE,KeyEvent.VK_MULTIPLY,KeyEvent.VK_SUBTRACT,KeyEvent.VK_ADD,KeyEvent.VK_EQUALS};
        for (int i = 4; i < buttons.length; i++){
            inputMap.put(KeyStroke.getKeyStroke(keyCodes[i-4],0),buttons[i].getText());
            panel.getActionMap().put(buttons[i].getText(), action);
        }
    }

    /**
     * Sets keybind for button.
     * @param panel JPanel object upon which input/action map will be called.
     * @param keycode Keycode of int type (KeyEvent enum) upon which keystroke is get.
     * @param keyName Action map key name of String type
     * @param button Button upon which doClick method will be called through keystroke.
     */
    public static void setKeybind(JPanel panel, int keycode, String keyName, JButton button){
        button.setFocusable(false);
        InputMap inputMap = panel.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW);
        inputMap.put(KeyStroke.getKeyStroke(keycode,0),keyName);
        panel.getActionMap().put(keyName, new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) { button.doClick();}
        });
    }

    /**
     * Specialised mehtod to set keybinds for dot in application. Comma,period and period in numpad fire same button action in application. Not for use outside this API.
     * @param panel JPanel object upon which input/action map will be called.
     * @param action Action object handling events.
     */
    public static void setDotKeyBind(JPanel panel, Action action){
        InputMap inputMap = panel.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW);
        inputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_COMMA,0),",");
        inputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_PERIOD,0),",");
        inputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_DECIMAL,0),",");
        panel.getActionMap().put(",", action);
    }

    /**
     * Specialised method ot set enter keystroke in numpad. Not for use outside this API.
     * @param panel JPanel object upon which input/action map will be called.
     * @param button Button object which will call doClick method while pressing enter.
     */
    public static void setEnterKeybind(JPanel panel, JButton button){
        button.setActionCommand("=");
        button.setFocusable(false);
        InputMap inputMap = panel.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW);
        inputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_ENTER, 0), "equals");
        panel.getActionMap().put("equals", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) { button.doClick();}
        });
    }

}
