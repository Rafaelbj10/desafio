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

        if (cep == null || cep.isBlank()) {
            throw new IllegalArgumentException("CEP inválido");
        }

        CepResponse response = viaCepClient.buscarCep(cep);

        logService.salvar(cep, response);

        if (response.getCep() == null) {
            throw new CepNotFoundException("CEP não encontrado");
        }

        return response;
    }
}