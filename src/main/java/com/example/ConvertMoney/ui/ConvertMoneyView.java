package com.example.ConvertMoney.ui;

import com.example.ConvertMoney.ConvertMoneyService;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.NumberField;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;

import java.math.BigDecimal;

@PageTitle("Convert Money")
@Route("convert-money") // http://localhost:8080/convert-money
public class ConvertMoneyView extends VerticalLayout {

    private final ConvertMoneyService service = new ConvertMoneyService();

    public ConvertMoneyView() {
        setPadding(true); setSpacing(true);

        H2 title = new H2("Câmbio de Moedas");
        ComboBox<String> from = new ComboBox<>("De");
        ComboBox<String> to   = new ComboBox<>("Para");
        NumberField amount   = new NumberField("Montante");
        amount.setMin(0); amount.setStep(1); amount.setValue(100.0);

        try {
            String[] currencies = service.listCurrencies();
            from.setItems(currencies); to.setItems(currencies);
            from.setValue("EUR");      to.setValue("USD");
        } catch (RuntimeException ex) {
            Notification.show("Falha a carregar moedas: " + ex.getMessage());
        }

        Button convert = new Button("Converter", e -> {
            if (from.getValue() == null || to.getValue() == null || amount.getValue() == null) {
                Notification.show("Preenche todos os campos."); return;
            }
            try {
                var res = service.convert(from.getValue(), to.getValue(),
                        BigDecimal.valueOf(amount.getValue()));
                Notification.show(String.format(
                        "%s %.2f → %s %.2f (taxa: %s)",
                        res.getFrom(), res.getAmount(), res.getTo(),
                        res.getResult(), res.getRate()
                ));
            } catch (RuntimeException ex) {
                Notification.show("Erro: " + ex.getMessage());
            }
        });

        add(title, from, to, amount, convert);
    }
}
