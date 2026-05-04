package com.desafio.infra.repository;

import com.desafio.model.CepLog;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CepLogRepository extends JpaRepository<CepLog, Long> {
}
