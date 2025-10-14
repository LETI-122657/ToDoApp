package com.example.ConvertMoney;

import com.google.gson.Gson;
import java.io.IOException;
import java.math.BigDecimal;
import java.net.URI;
import java.net.http.*;
import java.time.LocalDate;
import java.util.Map;
import java.util.Objects;

/** Serviço: fala com a API pública Frankfurter e implementa CMRepository. */
public class ConvertMoneyService implements CMRepository {
    private static final String API_BASE = "https://api.frankfurter.app";
    private final HttpClient http;
    private final Gson gson = new Gson();

    public ConvertMoneyService() { this(HttpClient.newHttpClient()); }
    public ConvertMoneyService(HttpClient http) { this.http = http; }

    @Override
    public String[] listCurrencies() {
        String url = API_BASE + "/currencies";
        try {
            var res = http.send(HttpRequest.newBuilder(URI.create(url)).GET().build(),
                    HttpResponse.BodyHandlers.ofString());
            if (res.statusCode() != 200) throw new RuntimeException("FX HTTP " + res.statusCode());
            Map<String, String> map = gson.fromJson(res.body(), Map.class);
            return map.keySet().stream().sorted().toArray(String[]::new);
        } catch (IOException | InterruptedException e) {
            throw new RuntimeException("Falha a obter moedas", e);
        }
    }

    @Override
    public BigDecimal rate(String from, String to) {
        Objects.requireNonNull(from); Objects.requireNonNull(to);
        if (from.equalsIgnoreCase(to)) return BigDecimal.ONE;

        String url = API_BASE + "/latest?from=" + from + "&to=" + to;
        try {
            var res = http.send(HttpRequest.newBuilder(URI.create(url)).GET().build(),
                    HttpResponse.BodyHandlers.ofString());
            if (res.statusCode() != 200) throw new RuntimeException("FX HTTP " + res.statusCode());
            RatesResponse body = gson.fromJson(res.body(), RatesResponse.class);
            BigDecimal r = body.rates.get(to.toUpperCase());
            if (r == null) throw new RuntimeException("Sem taxa para " + to);
            return r;
        } catch (IOException | InterruptedException e) {
            throw new RuntimeException("Falha a obter taxa", e);
        }
    }

    /** Converte um montante e devolve o DTO preenchido. */
    public ConvertMoney convert(String from, String to, BigDecimal amount) {
        Objects.requireNonNull(amount);
        BigDecimal r = rate(from, to);
        BigDecimal result = amount.multiply(r);
        return new ConvertMoney(from, to, amount, r, result, LocalDate.now());
    }

    // --- DTO interno para /latest
    static class RatesResponse {
        String base;
        String date;
        Map<String, BigDecimal> rates;
    }
}
