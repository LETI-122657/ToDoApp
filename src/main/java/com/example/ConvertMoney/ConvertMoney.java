package com.example.ConvertMoney;

import java.math.BigDecimal;
import java.time.LocalDate;

/** DTO simples para pedidos/resultados de conversão. */
public class ConvertMoney {
    private String from;
    private String to;
    private BigDecimal amount;
    private BigDecimal rate;
    private BigDecimal result;
    private LocalDate date; // data da taxa

    public ConvertMoney() {}

    public ConvertMoney(String from, String to, BigDecimal amount,
                        BigDecimal rate, BigDecimal result, LocalDate date) {
        this.from = from; this.to = to; this.amount = amount;
        this.rate = rate; this.result = result; this.date = date;
    }

    // getters & setters
    public String getFrom() { return from; }
    public void setFrom(String from) { this.from = from; }
    public String getTo() { return to; }
    public void setTo(String to) { this.to = to; }
    public BigDecimal getAmount() { return amount; }
    public void setAmount(BigDecimal amount) { this.amount = amount; }
    public BigDecimal getRate() { return rate; }
    public void setRate(BigDecimal rate) { this.rate = rate; }
    public BigDecimal getResult() { return result; }
    public void setResult(BigDecimal result) { this.result = result; }
    public LocalDate getDate() { return date; }
    public void setDate(LocalDate date) { this.date = date; }
}
