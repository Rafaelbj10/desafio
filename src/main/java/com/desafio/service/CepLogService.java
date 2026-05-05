package com.desafio.service;

import com.desafio.exception.CepLogException;
import com.desafio.infra.repository.CepLogRepository;
import com.desafio.infra.sqs.SqsProducer;
import com.desafio.model.CepLog;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Service;

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
        String json;
        try {
            json = objectMapper.writeValueAsString(response);
        } catch (JsonProcessingException e) {
            throw new CepLogException("Falha ao serializar resposta para CEP: " + cep, e);
        }

        try {
            sqsProducer.sendMessage(json);
        } catch (Exception e) {
            throw new CepLogException("Falha ao enviar mensagem SQS para CEP: " + cep, e);
        }

        CepLog log = CepLog.of(cep, json);
        repository.save(log);
    }


}