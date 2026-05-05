package com.desafio.service;

import com.desafio.infra.repository.CepLogRepository;
import com.desafio.infra.sqs.SqsProducer;
import com.desafio.model.CepLog;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class CepLogService {

    private final CepLogRepository repository;
    private final SqsProducer sqsProducer;
    private final ObjectMapper objectMapper;

    public CepLogService(CepLogRepository repository,
                         SqsProducer sqsProducer,
                         ObjectMapper objectMapper) {
        this.repository = repository;
        this.sqsProducer = sqsProducer;
        this.objectMapper = objectMapper;
    }

    public void save(String cep, Object response) {
        try {
            String json = objectMapper.writeValueAsString(response);

            CepLog log = new CepLog();
            log.setCep(cep);
            log.setResponse(json);
            log.setDataConsulta(LocalDateTime.now());

            sqsProducer.sendMessage(json);
            repository.save(log);

        } catch (Exception e) {
            throw new RuntimeException("Erro ao processar log", e);
        }
    }
}