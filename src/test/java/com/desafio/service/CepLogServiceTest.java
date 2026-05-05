package com.desafio.service;

import com.desafio.exception.CepLogException;
import com.desafio.infra.repository.CepLogRepository;
import com.desafio.infra.response.CepResponse;
import com.desafio.infra.sqs.SqsProducer;
import com.desafio.model.CepLog;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CepLogServiceTest {

    @Mock
    private CepLogRepository repository;

    @Mock
    private SqsProducer sqsProducer;

    @Mock
    private ObjectMapper objectMapper;

    @InjectMocks
    private CepLogService cepLogService;

    private CepResponse cepResponse;

    @BeforeEach
    void setUp() {
        cepResponse = new CepResponse();
        cepResponse.setCep("06130040");
        cepResponse.setLogradouro("Rua Teste");
        cepResponse.setBairro("Bairro Teste");
        cepResponse.setLocalidade("São Paulo");
        cepResponse.setUf("SP");
    }

    @Test
    void deveSalvarLogComSucessoQuandoTudoFunciona() throws JsonProcessingException {
        when(objectMapper.writeValueAsString(cepResponse)).thenReturn("{\"cep\":\"06130040\"}");

        cepLogService.save("06130040", cepResponse);

        verify(sqsProducer, times(1)).sendMessage(anyString());
        verify(repository, times(1)).save(any(CepLog.class));
    }

    @Test
    void deveLancarCepLogExceptionQuandoSerializacaoFalhar() throws JsonProcessingException {
        when(objectMapper.writeValueAsString(cepResponse))
                .thenThrow(new JsonProcessingException("Erro de serialização") {});

        CepLogException exception = assertThrows(CepLogException.class,
                () -> cepLogService.save("06130040", cepResponse));

        assertTrue(exception.getMessage().contains("Falha ao serializar resposta para CEP: 06130040"));
        verify(sqsProducer, never()).sendMessage(anyString());
        verify(repository, never()).save(any());
    }

    @Test
    void deveLancarCepLogExceptionQuandoSqsFalhar() throws JsonProcessingException {
        when(objectMapper.writeValueAsString(cepResponse)).thenReturn("{\"cep\":\"06130040\"}");
        doThrow(new RuntimeException("SQS indisponível")).when(sqsProducer).sendMessage(anyString());

        CepLogException exception = assertThrows(CepLogException.class,
                () -> cepLogService.save("06130040", cepResponse));

        assertTrue(exception.getMessage().contains("Falha ao enviar mensagem SQS para CEP: 06130040"));
        verify(repository, never()).save(any());
    }

    @Test
    void naoDeveSalvarNoBancoQuandoSqsFalhar() throws JsonProcessingException {
        when(objectMapper.writeValueAsString(cepResponse)).thenReturn("{\"cep\":\"06130040\"}");
        doThrow(new RuntimeException("SQS indisponível")).when(sqsProducer).sendMessage(anyString());

        assertThrows(CepLogException.class, () -> cepLogService.save("06130040", cepResponse));

        verify(repository, never()).save(any());
    }
}
