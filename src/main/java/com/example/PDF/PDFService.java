package com.example.pdf;

import org.jspecify.annotations.Nullable;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.time.LocalDate;
import java.util.List;

@Service
public class PDFService {

    private final PDFRepository pdfRepository;

    public PDFService(PDFRepository pdfRepository) {
        this.pdfRepository = pdfRepository;
    }

    @Transactional
    public void createPDF(String description, @Nullable LocalDate creationDate) {
        String fileName = (description != null && !description.isBlank()) ? description + ".pdf" : "generated.pdf";
        Instant now = (creationDate != null) ? creationDate.atStartOfDay().atZone(java.time.ZoneId.systemDefault()).toInstant() : Instant.now();
        byte[] content = generatePDFBytes(description != null ? description : "PDF Example");
        PDF pdf = new PDF(fileName, now, content);
        pdfRepository.saveAndFlush(pdf);
    }

    @Transactional(readOnly = true)
    public List<PDF> list(Pageable pageable) {
        return pdfRepository.findAllBy(pageable).toList();
    }

    public byte[] generatePDFBytes(String content) {
        try (var out = new java.io.ByteArrayOutputStream();
             var document = new org.apache.pdfbox.pdmodel.PDDocument()) {
            var page = new org.apache.pdfbox.pdmodel.PDPage();
            document.addPage(page);
            try (var contentStream = new org.apache.pdfbox.pdmodel.PDPageContentStream(document, page)) {
                contentStream.beginText();
                contentStream.setFont(org.apache.pdfbox.pdmodel.font.PDType1Font.HELVETICA, 12);
                contentStream.newLineAtOffset(50, 700);
                contentStream.showText(content);
                contentStream.endText();
            }
            document.save(out);
            return out.toByteArray();
        } catch (Exception e) {
            throw new RuntimeException("Error generating PDF bytes", e);
        }
    }

    @Transactional
    public void printPDF(String filePath, String content) {
        try (var document = new org.apache.pdfbox.pdmodel.PDDocument()) {
            var page = new org.apache.pdfbox.pdmodel.PDPage();
            document.addPage(page);
            try (var contentStream = new org.apache.pdfbox.pdmodel.PDPageContentStream(document, page)) {
                contentStream.beginText();
                contentStream.setFont(org.apache.pdfbox.pdmodel.font.PDType1Font.HELVETICA, 12);
                contentStream.newLineAtOffset(50, 700);
                contentStream.showText(content);
                contentStream.endText();
            }
            document.save(filePath);
            System.out.println("PDF created and saved at: " + filePath);
        } catch (Exception e) {
            throw new RuntimeException("Error while creating PDF", e);
        }
    }
}

