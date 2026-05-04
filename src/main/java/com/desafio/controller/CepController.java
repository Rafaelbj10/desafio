package com.desafio.controller;

import com.desafio.infra.response.CepResponse;
import com.desafio.service.CepService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/ceps")
public class CepController {

    private final CepService cepService;

    public CepController(CepService cepService) {
        this.cepService = cepService;
    }

    @GetMapping("/{cep}")
    public CepResponse buscarCep(@PathVariable String cep) {
        return cepService.buscarCep(cep);
    }
}
