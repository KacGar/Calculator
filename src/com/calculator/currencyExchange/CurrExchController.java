package com.calculator.currencyExchange;

import com.calculator.lengthConverter.SISystem;
import org.w3c.dom.*;
import org.xml.sax.SAXException;
import javax.swing.*;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.awt.event.ActionEvent;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.io.IOException;
import java.math.RoundingMode;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import static com.calculator.currencyExchange.CurrencyExchange.*;

/**
 * Controller for currency exchange sub app. By extending {@link AbstractAction} class, this class can be used as action listener.
 */
public class CurrExchController extends AbstractAction implements ItemListener{

    /**
     * Instance of controller class
     */
    private static final CurrExchController INSTANCE = new CurrExchController();
    /**
     * instance of document object that holds XML file responded from NBP server. This file holds multiple links to older updates and newest one.
     */
    private static org.w3c.dom.Document document;

    /**
     * Instance of document object that holds XML file with currency exchange rates. URL adress is taken from newest "item" from main document.
     */
    private static org.w3c.dom.Document newestDocument;
    private final Pattern pattern = Pattern.compile(".*[.]+.*");

    /**
     * Combobox filled with values from {@link SISystem} enum. Used as FROM which system conversion starts.
     */
    private static JComboBox<Currency> metric1;

    /**
     * Combobox filled with values from {@link SISystem} enum. Used as FROM which system conversion starts.
     */
    private static JComboBox<Currency> metric2;

    /**
     * Private constructor prevents creation of new objects.
     */
    private CurrExchController() {}

    /**
     *Sets reference to first (FROM) combobox object for later use in convert operations
     * @param metric JComboBox with {@link SISystem} parametrized type.
     */
    public static void setMetric1(JComboBox<Currency> metric){ metric1 = metric;}

    /**
     * Sets reference to first (TO) combobox object for later use in convert operations
     * @param metric JComboBox with {@link SISystem} parametrized type.
     */
    public static void setMetric2(JComboBox<Currency> metric){ metric2 = metric;}

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

    String nums = "0123456789";
    @Override
    public void actionPerformed(ActionEvent e) {
        String s = e.getActionCommand();

        //appends numbers in textfield
        if (nums.contains(s)) {
            if ( getLabelInput().getText().contains(".")){ getLabelInput().setText(getLabelInput().getText().concat(s)); }
            else {
                if ( getLabelInput().getText().charAt(0) == '0' ) { getLabelInput().setText(s); }
                else { getLabelInput().setText(getLabelInput().getText().concat(s)); }
            }
            convert();
        }

        //adds comma if not present
        if ( s.equals(",") || s.equals(".") ){
            if ( !(getLabelInput().getText().equals("")) ){
                Matcher m = pattern.matcher(getLabelInput().getText());
                boolean b = m.matches();
                if (!b){ getLabelInput().setText(getLabelInput().getText().concat(".")); }
                convert();
            }
        }

        //deletes last character (number) from input label
        if ( e.getActionCommand().equalsIgnoreCase("delete") ){
            String input = getLabelInput().getText();
            if( input.length() != 0 ){ input = input.substring(0, input.length() - 1);}
            if (input.length() != 0 && input.charAt(input.length()-1) == '.') { input = input.substring(0, input.length() - 1); }
            if (input.length() == 0) { getLabelInput().setText("0");}
            else { getLabelInput().setText(input); convert();}
        }

        // clears all labels
        if (e.getActionCommand().equalsIgnoreCase("clear")){
            getLabelInput().setText("0");
            getLabelResult().setText("0.0");
        }
    }

    /**
     * Method implemented from {@link ItemListener} interface. Whenever something is chosen in any of the two comboboxes, converion happens.
     * @param e ItemEvent object
     */
    @Override
    public void itemStateChanged(ItemEvent e) { if (e.getStateChange() == ItemEvent.SELECTED){ convert(); } }


    /**
     * Converts value from input from one chosen currency into another chosen currency and displays results.
     */
    private void convert(){
        double number = Double.parseDouble(getLabelInput().getText());
        Currency choice1 = metric1.getItemAt(metric1.getSelectedIndex());
        Currency choice2 = metric2.getItemAt(metric2.getSelectedIndex());
        double result = calculate(choice1, choice2, number);

        DecimalFormat df = new DecimalFormat("#.####");
        df.setRoundingMode(RoundingMode.CEILING);
        getLabelResult().setText(df.format(result));
    }

    /**
     * Calculates value from given input from chosen currency into second chosen currency. Exchange rates are taken from XML file provided from NBP site.
     * @param from Currency type
     * @param to Currency type into which convert
     * @param value Input value
     * @return Converted value of double type.
     */
    private static double calculate(Currency from, Currency to, double value){
        String currencyCodeFrom = from.getCode();
        String currencyCodeTo = to.getCode();
        double rateFrom = 1;
        double rateTo = 1;
        ArrayList<Node> allCurrencies = new ArrayList<>();
        Element root = newestDocument.getDocumentElement();
        NodeList children = root.getChildNodes();
        // looking for childs called 'pozycja' and adding all subchilds to arraylist
        for (int i = 0; i < children.getLength(); i++){
            if (children.item(i) instanceof Element){
                Node child = children.item(i);
                if (child.getNodeName().equals("pozycja")){
                    NodeList subChilds = child.getChildNodes();
                    for (int j = 0; j < subChilds.getLength(); j++){
                        if ( subChilds.item(j) instanceof Element ){ allCurrencies.add(subChilds.item(j)); }
                    }
                }
            }
        }
        // lookin for match in currency codes and parsing correct currency rates
        for (int i = 0; i < allCurrencies.size(); i++) {
            if ( allCurrencies.get(i).getTextContent().contains(currencyCodeFrom) ){
                String s = allCurrencies.get(i + 1).getTextContent();
                s = s.replace(",",".");
                rateFrom = Double.parseDouble(s);
            }
            if( allCurrencies.get(i).getTextContent().contains(currencyCodeTo) ){
                String s = allCurrencies.get(i + 1).getTextContent();
                s = s.replace(",",".");
                rateTo = Double.parseDouble(s);
            }
        }
        return ((value * rateFrom) / rateTo) * to.getAmount();
    }

    /**
     * Receive XML table with links to newest and older updates.
     */
    public static void updateData(){
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        try {
            DocumentBuilder builder = factory.newDocumentBuilder();
            URL url = new URL("http://rss.nbp.pl/kursy/TabelaA.xml");
            document = builder.parse(url.openStream());
            getNewestData(document);
        } catch (ParserConfigurationException | IOException | SAXException e) {
            // remember handle exception properly
        }
    }

    /**
     * Fetches newest XML table with currency exchange rates from XML table provided by NPS server.
     * @param document Document object (from org.w3c.dom.node) as XML file that holds all info (and URLs) about newest and older updates.
     */
    private static void getNewestData(Document document){
        Element root = document.getDocumentElement();
        NodeList content = root.getChildNodes();

        NodeList channel = null;
        for (int i = 0; i < content.getLength(); i++){
            if (content.item(1).getNodeName().equals("channel")){
                channel = content.item(1).getChildNodes();
                break;
            }
            if (content.item(i).getNodeName().equals("channel")) { channel = content.item(i).getChildNodes(); }
        }

        NodeList newest = null;
        for (int i = 0; i < channel.getLength(); i++){
            if (channel.item(i).getNodeName().equals("item")){
                newest = channel.item(i).getChildNodes();
                break;
            }
        }

        URL url = null;
        for (int i = 0; i< newest.getLength(); i++){
            if (newest.item(i).getNodeName().equals("enclosure")){
                NamedNodeMap s = newest.item(i).getAttributes();
                Node urlAttr = s.getNamedItem("url");
                try{
                    DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
                    DocumentBuilder builder = factory.newDocumentBuilder();
                    url = new URL(urlAttr.getTextContent());
                    newestDocument = builder.parse(url.openStream());
                } catch (MalformedURLException | ParserConfigurationException ex){
                    //handle ex properly
                } catch (IOException | SAXException ioException) {
                    ioException.printStackTrace();
                }
            }
        }
    }

}
