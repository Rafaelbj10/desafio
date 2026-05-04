package com.desafio.infra.response;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@RequiredArgsConstructor
public class CepResponse {

    private String cep;
    private String logradouro;
    private String bairro;
    private String localidade;
    private String uf;

}
