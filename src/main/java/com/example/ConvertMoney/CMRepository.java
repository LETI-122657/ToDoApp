package com.example.ConvertMoney;

import java.math.BigDecimal;

public interface CMRepository {
    /** Lista os códigos de moeda suportados, ordenados alfabeticamente. */
    String[] listCurrencies();

    /** Obtém a taxa direta FROM→TO (ex.: EUR→USD). */
    BigDecimal rate(String from, String to);
}
