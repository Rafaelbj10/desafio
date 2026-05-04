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

    public CepResponse buscarCep(String cep) {
        if (cep == null || cep.trim().isBlank()) {
            throw new IllegalArgumentException("CEP inválido");
        }

        String cepSanitizado = cep.replaceAll("[^0-9]", "");

        if (cepSanitizado.length() != 8) {
            throw new IllegalArgumentException("CEP deve conter 8 dígitos");
        }

        CepResponse response = viaCepClient.buscarCep(cepSanitizado);

        if (response.getCep() == null) {
            throw new CepNotFoundException("CEP não encontrado");
        }

        logService.salvar(cep, response);

        return response;
    }
}