package com.desafio.service;

import com.desafio.exception.CepNotFoundException;
import com.desafio.infra.response.CepResponse;
import com.desafio.infra.client.ViaCepClient;
import org.springframework.stereotype.Service;

@Service
public class CepService {

    private final ViaCepClient viaCepClient;
    private final CepLogService logService;

    public CepService(ViaCepClient viaCepClient, CepLogService logService) {
        this.viaCepClient = viaCepClient;
        this.logService = logService;
    }

    public CepResponse findCep(String cep) {
        validCep(cep);

        String cepSanitizado = clearCep(cep);

        if (cepSanitizado.length() != 8) {
            throw new IllegalArgumentException("CEP deve conter 8 dígitos");
        }

        CepResponse response = viaCepClient.buscarCep(cepSanitizado);

        if (response.getCep() == null) {
            throw new CepNotFoundException("CEP não encontrado");
        }

        logService.save(cep, response);

        return response;
    }

    private void validCep(String cep) {
        if (cep == null || cep.trim().isBlank()) {
            throw new IllegalArgumentException("CEP inválido");
        }

        if (!cep.trim().matches("\\d{8}|\\d{5}-\\d{3}")) {
            throw new IllegalArgumentException("Formato de CEP inválido. Use 00000000 ou 00000-000");
        }
    }

    private String clearCep(String cep) {
        return cep.replaceAll("[^0-9]", "");
    }
}