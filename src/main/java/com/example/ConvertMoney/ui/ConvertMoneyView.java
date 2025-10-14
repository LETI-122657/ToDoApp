package com.example.ConvertMoney.ui;

import com.example.ConvertMoney.ConvertMoneyService;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.html.Main;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.NotificationVariant;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.NumberField;
import com.vaadin.flow.router.Menu;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.router.RouteAlias;
import com.vaadin.flow.theme.lumo.LumoUtility;

// toolbar base do projeto
import com.example.base.ui.component.ViewToolbar;

import java.math.BigDecimal;

@Route("convert-money")
@RouteAlias("fx") // opcional: /fx também abre a vista
@PageTitle("Convert Money")
@Menu(order = 1, icon = "vaadin:exchange", title = "Convert Money")
public class ConvertMoneyView extends Main {

    private final ConvertMoneyService service = new ConvertMoneyService();

    // Controlo(s) de entrada
    final ComboBox<String> from;
    final ComboBox<String> to;
    final NumberField amount;
    final Button convertBtn;

    // Área de resultado
    final Span result;

    public ConvertMoneyView() {
        // ---- UI: campos
        from = new ComboBox<>("De");
        to   = new ComboBox<>("Para");
        amount = new NumberField("Montante");
        amount.setMin(0);
        amount.setStep(1);
        amount.setValue(100.0);

        convertBtn = new Button("Converter", e -> convert());
        convertBtn.addThemeVariants(ButtonVariant.LUMO_PRIMARY);

        // ---- Carregar moedas
        try {
            String[] currencies = service.listCurrencies();
            from.setItems(currencies);
            to.setItems(currencies);
            from.setValue("EUR");
            to.setValue("USD");
        } catch (RuntimeException ex) {
            Notification.show("Falha a carregar moedas: " + ex.getMessage(), 4000, Notification.Position.BOTTOM_END)
                    .addThemeVariants(NotificationVariant.LUMO_ERROR);
        }

        // ---- Resultado
        result = new Span();
        result.getElement().getThemeList().add("badge");
        result.addClassNames(LumoUtility.FontSize.MEDIUM);

        // ---- Layout da página no mesmo estilo do teu pdfView
        setSizeFull();
        addClassNames(
                LumoUtility.BoxSizing.BORDER,
                LumoUtility.Display.FLEX,
                LumoUtility.FlexDirection.COLUMN,
                LumoUtility.Padding.MEDIUM,
                LumoUtility.Gap.SMALL
        );

        // Barra superior (toolbar) com os controlos
        add(new ViewToolbar("Convert Money",
                ViewToolbar.group(from, to, amount, convertBtn)
        ));

        // Corpo (só um container simples para mostrar o resultado)
        var content = new VerticalLayout(result);
        content.setPadding(true);
        content.setSpacing(false);
        content.setSizeFull();

        add(content);
    }

    private void convert() {
        if (from.getValue() == null || to.getValue() == null || amount.getValue() == null) {
            Notification.show("Preenche todos os campos.", 2500, Notification.Position.BOTTOM_END)
                    .addThemeVariants(NotificationVariant.LUMO_CONTRAST);
            return;
        }
        try {
            var dto = service.convert(
                    from.getValue(),
                    to.getValue(),
                    BigDecimal.valueOf(amount.getValue())
            );

            // Mostra o resultado na badge e também uma notificação
            result.setText(String.format(
                    "%s %.2f → %s %.2f  (taxa: %s, data: %s)",
                    dto.getFrom(), dto.getAmount(), dto.getTo(), dto.getResult(), dto.getRate(), dto.getDate()
            ));
            Notification.show("Conversão efetuada com sucesso", 2000, Notification.Position.BOTTOM_END)
                    .addThemeVariants(NotificationVariant.LUMO_SUCCESS);
        } catch (RuntimeException ex) {
            Notification.show("Erro: " + ex.getMessage(), 4000, Notification.Position.BOTTOM_END)
                    .addThemeVariants(NotificationVariant.LUMO_ERROR);
        }
    }
}
