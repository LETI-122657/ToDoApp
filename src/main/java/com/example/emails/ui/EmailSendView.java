package com.example.emails.ui;

import com.example.base.ui.component.ViewToolbar;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.NotificationVariant;
import com.vaadin.flow.component.textfield.TextArea;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.Menu;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.component.html.Main;
import com.vaadin.flow.theme.lumo.LumoUtility;
import com.example.emails.EmailService;

@Route("send-email")
@PageTitle("Enviar Email")
@Menu(order = 1, icon = "vaadin:envelope", title = "Enviar Email")
public class EmailSendView extends Main {

    private final EmailService emailService;

    final TextField toField;
    final TextField subjectField;
    final TextArea bodyField;
    final Button sendBtn;

    public EmailSendView(EmailService emailService) {
        this.emailService = emailService;

        toField = new TextField("Para");
        subjectField = new TextField("Assunto");
        bodyField = new TextArea("Mensagem");

        sendBtn = new Button("Enviar", event -> sendEmail());

        setSizeFull();
        addClassNames(LumoUtility.BoxSizing.BORDER, LumoUtility.Display.FLEX, LumoUtility.FlexDirection.COLUMN,
                LumoUtility.Padding.MEDIUM, LumoUtility.Gap.SMALL);

        add(new ViewToolbar("Enviar Email", ViewToolbar.group(toField, subjectField, bodyField, sendBtn)));
    }

    private void sendEmail() {
        emailService.sendEmail(toField.getValue(), subjectField.getValue(), bodyField.getValue());
        toField.clear();
        subjectField.clear();
        bodyField.clear();
        Notification.show("Email enviado", 3000, Notification.Position.BOTTOM_END)
                .addThemeVariants(NotificationVariant.LUMO_SUCCESS);
    }
}
