package com.desafio.infra.client;

import com.desafio.exception.CepNotFoundException;
import feign.Response;
import feign.codec.ErrorDecoder;

public class FeignErrorDecoder implements ErrorDecoder {

    @Override
    public Exception decode(String methodKey, Response response) {
        if (response.status() == 404) {
            String cep = extractCepFromUrl(response.request().url());
            return new CepNotFoundException(cep);
        }
        return new Default().decode(methodKey, response);
    }

    private String extractCepFromUrl(String url) {
        String[] parts = url.split("/");
        for (String part : parts) {
            if (part.matches("\\d{8}")) {
                return part;
            }
        }
        return "desconhecido";
    }
}