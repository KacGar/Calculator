package com.calculator.currencyExchange;

public enum Currency {
    DOLLAR_USA ("USD", 1),
    PLN ("PLN", 1),
    EURO ("EUR" ,1),
    YEN ("JPY" ,100),
    YUAN ("CNY" ,1),
    BAT ("THB", 1),
    DOLLAR_AU ("AUD",1),
    DOLLAR_HK ("HKD", 1),
    DOLLAR_CAD ("CAD", 1),
    DOLLAR_NZD ("NZD", 1),
    DOLLAR_SGD ("SGD",1),
    FORINT ("HUF", 100),
    SWISS_FRANC ("CHF", 1),
    POUND_GB ("GBP",1),
    HRYVNIA ("UAH",1 ),
    CROWN_CZ ("CZK", 1),
    CROWN_DK ("DKK",1),
    CROWN_ISL ("ISK", 100),
    CROWN_NOR ("NOK",1),
    CROWN_SWE ("SEK",1),
    LEU_ROMANIAN ("RON",1),
    LEV_BULGARIA ("BGN",1),
    LIRA_TURKEY ("TRY",1),
    SHEKEL_ISRAEL ("ILS",1),
    PESO_CHIL ("CLP",100),
    PESO_PHIL ("PHP", 1),
    PESO_MEXICO ("MXN",1),
    RAND_RPA ("ZAR",1),
    REAL_BRAZIL ("BRL",1),
    RINGGIT_MALESIA ("MYR", 1),
    RUPEE_INDONESIA ("IDR", 10000),
    RUPEE_INDIA ("INR", 100),
    WON_SKOREA ("KRW",100),
    SDR_MFW ("XDR", 1);


    private final String code;
    private final double amount;

    // constructor
    Currency(String code, double amount) {
        this.code = code;
        this.amount = amount;
    }

    public final String getCode() { return code; }

    public double getAmount() {return amount;}
}
