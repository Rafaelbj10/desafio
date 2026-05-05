package com.desafio.service;

import com.desafio.exception.CepNotFoundException;
import com.desafio.exception.InvalidCepException;
import com.desafio.infra.client.ViaCepClient;
import com.desafio.infra.response.CepResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CepServiceTest {

    @Mock
    private ViaCepClient viaCepClient;

    @Mock
    private CepLogService logService;

    @InjectMocks
    private CepService cepService;

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
    void deveRetornarCepQuandoEncontrado() {
        when(viaCepClient.buscarCep("06130040")).thenReturn(cepResponse);

        CepResponse resultado = cepService.findCep("06130040");

        assertNotNull(resultado);
        assertEquals("06130040", resultado.getCep());
        verify(logService, times(1)).save("06130040", cepResponse);
    }

    @Test
    void deveAceitarCepComTraco() {
        when(viaCepClient.buscarCep("06130040")).thenReturn(cepResponse);

        CepResponse resultado = cepService.findCep("06130-040");

        assertNotNull(resultado);
        verify(viaCepClient, times(1)).buscarCep("06130040");
    }

    @Test
    void deveLancarInvalidCepExceptionQuandoCepNulo() {
        InvalidCepException exception = assertThrows(InvalidCepException.class,
                () -> cepService.findCep(null));

        assertTrue(exception.getMessage().contains("CEP inválido"));
        verify(viaCepClient, never()).buscarCep(anyString());
    }

    @Test
    void deveLancarInvalidCepExceptionQuandoCepVazio() {
        InvalidCepException exception = assertThrows(InvalidCepException.class,
                () -> cepService.findCep("   "));

        assertTrue(exception.getMessage().contains("CEP inválido"));
        verify(viaCepClient, never()).buscarCep(anyString());
    }

    @Test
    void deveLancarInvalidCepExceptionQuandoFormatoInvalido() {
        InvalidCepException exception = assertThrows(InvalidCepException.class,
                () -> cepService.findCep("1234"));

        assertTrue(exception.getMessage().contains("Formato de CEP inválido"));
        verify(viaCepClient, never()).buscarCep(anyString());
    }

    @Test
    void deveLancarCepNotFoundExceptionQuandoCepNaoEncontrado() {
        CepResponse respostaVazia = new CepResponse();
        when(viaCepClient.buscarCep("06130040")).thenReturn(respostaVazia);

        CepNotFoundException exception = assertThrows(CepNotFoundException.class,
                () -> cepService.findCep("06130040"));

        assertTrue(exception.getMessage().contains("06130040"));
        verify(logService, never()).save(anyString(), any());
    }

    @Test
    void naoDeveSalvarLogQuandoCepNaoEncontrado() {
        CepResponse respostaVazia = new CepResponse();
        when(viaCepClient.buscarCep("06130040")).thenReturn(respostaVazia);

        assertThrows(CepNotFoundException.class, () -> cepService.findCep("06130040"));

        verify(logService, never()).save(anyString(), any());
    }
}