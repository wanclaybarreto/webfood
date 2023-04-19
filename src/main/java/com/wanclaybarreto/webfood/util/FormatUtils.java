package com.wanclaybarreto.webfood.util;

import java.math.BigDecimal;
import java.text.NumberFormat;
import java.util.Locale;

public class FormatUtils {

    private static final Locale LOCALE_BR = new Locale("pt", "BR");

    //Método para retornar um formatador de números flutuantes no formato Brasileiro
    public static NumberFormat newNumberFormatBR() {
        NumberFormat nf = NumberFormat.getNumberInstance(LOCALE_BR);
        nf.setMaximumFractionDigits(2);
        nf.setMinimumFractionDigits(2);
        nf.setGroupingUsed(false); //false para não utilizar os separadores de milhar

        return nf;
    }

    //Método que recebe um BigDecimal e retona ele convertido para String
    public static String formatCurrency(BigDecimal number) {
        if (number == null) {
            return null;
        }

        return newNumberFormatBR().format(number);
    }

}
