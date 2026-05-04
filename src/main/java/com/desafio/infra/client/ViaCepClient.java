package com.desafio.infra.client;

import com.desafio.infra.response.CepResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(
        name = "viaCepClient",
        url = "${via-cep.url}"
)
public interface ViaCepClient {

    @GetMapping("/{cep}/json/")
    CepResponse buscarCep(@PathVariable String cep);
}