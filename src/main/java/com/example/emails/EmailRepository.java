package com.example.emails;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface EmailRepository extends JpaRepository<Email, Long>, JpaSpecificationExecutor<Email> {

    // Retorna um Slice de emails, útil para paginação sem contar o total de registros
    Slice<Email> findAllBy(Pageable pageable);
}
