package com.example.pdf;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface PDFRepository extends JpaRepository<PDF, Long>, JpaSpecificationExecutor<PDF> {
    Slice<PDF> findAllBy(Pageable pageable);
}
