package com.example.pdf;

import jakarta.persistence.*;
import org.jspecify.annotations.Nullable;

import java.time.Instant;

@Entity
@Table(name = "pdf")
public class PDF {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column(name = "pdf_id")
    private Long id;

    @Column(name = "file_name", nullable = false, length = 255)
    private String fileName = "";

    @Column(name = "creation_date", nullable = false)
    private Instant creationDate;

    @Lob
    @Column(name = "content", nullable = false)
    private byte[] content;

    protected PDF() {
    }

    public PDF(String fileName, Instant creationDate, byte[] content) {
        this.fileName = fileName;
        this.creationDate = creationDate;
        this.content = content;
    }

    public @Nullable Long getId() {
        return id;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public Instant getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(Instant creationDate) {
        this.creationDate = creationDate;
    }

    public byte[] getContent() {
        return content;
    }

    public void setContent(byte[] content) {
        this.content = content;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null || !getClass().isAssignableFrom(obj.getClass())) {
            return false;
        }
        if (obj == this) {
            return true;
        }
        PDF other = (PDF) obj;
        return getId() != null && getId().equals(other.getId());
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
