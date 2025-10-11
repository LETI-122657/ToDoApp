package com.example.pdf.ui;

import com.example.base.ui.component.ViewToolbar;
import com.example.pdf.PDF;
import com.example.pdf.PDFRepository;
import com.example.pdf.PDFService;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.datepicker.DatePicker;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.Main;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.NotificationVariant;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.Menu;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.theme.lumo.LumoUtility;

import java.time.Instant;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;

import static com.vaadin.flow.spring.data.VaadinSpringDataHelpers.toSpringPageRequest;

@Route("pdf")
@PageTitle("PDF Printer")
@Menu(order = 1, icon = "vaadin:file-pdf", title = "PDF Printer")
public class pdfView extends Main {

    private final PDFService pdfService;
    private final PDFRepository pdfRepository;

    final TextField description;
    final DatePicker dueDate;
    final Button createBtn;
    final Grid<PDF> pdfGrid;

    public pdfView(PDFService pdfService, PDFRepository pdfRepository) {
        this.pdfService = pdfService;
        this.pdfRepository = pdfRepository;

        description = new TextField();
        description.setPlaceholder("PDF description");
        description.setAriaLabel("PDF description");
        description.setMinWidth("20em");

        dueDate = new DatePicker();
        dueDate.setPlaceholder("Creation date");
        dueDate.setAriaLabel("Creation date");

        createBtn = new Button("Create PDF", event -> createPDF());
        createBtn.addThemeVariants(ButtonVariant.LUMO_PRIMARY);

        var dateTimeFormatter = DateTimeFormatter.ofLocalizedDateTime(FormatStyle.MEDIUM).withLocale(getLocale())
                .withZone(ZoneId.systemDefault());

        pdfGrid = new Grid<>();
        pdfGrid.setItems(query -> pdfRepository.findAll(toSpringPageRequest(query)).stream());
        pdfGrid.addColumn(PDF::getFileName).setHeader("File Name");
        pdfGrid.addColumn(pdf -> dateTimeFormatter.format(pdf.getCreationDate())).setHeader("Creation Date");
        pdfGrid.setSizeFull();

        setSizeFull();
        addClassNames(LumoUtility.BoxSizing.BORDER, LumoUtility.Display.FLEX, LumoUtility.FlexDirection.COLUMN,
                LumoUtility.Padding.MEDIUM, LumoUtility.Gap.SMALL);

        add(new ViewToolbar("PDF Printer", ViewToolbar.group(description, dueDate, createBtn)));
        add(pdfGrid);
    }

    private void createPDF() {
        String desc = description.getValue();
        String fileName = desc != null && !desc.isBlank() ? desc + ".pdf" : "generated.pdf";
        Instant now = Instant.now();
        byte[] content = pdfService.generatePDFBytes(desc != null ? desc : "PDF Example");
        PDF pdf = new PDF(fileName, now, content);
        pdfRepository.save(pdf);
        pdfGrid.getDataProvider().refreshAll();
        description.clear();
        dueDate.clear();
        Notification.show("PDF created and saved", 3000, Notification.Position.BOTTOM_END)
                .addThemeVariants(NotificationVariant.LUMO_SUCCESS);
    }
}
