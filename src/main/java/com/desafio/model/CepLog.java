package com.desafio.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import java.time.LocalDateTime;

@Getter
@Setter
@Entity
@Table(name = "cep_log")
public class CepLog {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String cep;

    @Column(columnDefinition = "TEXT")
    private String response;

    private LocalDateTime dataConsulta;

    public static CepLog of(String cep, String response) {
        CepLog log = new CepLog();
        log.setCep(cep);
        log.setResponse(response);
        log.setDataConsulta(LocalDateTime.now());
        return log;
    }
}