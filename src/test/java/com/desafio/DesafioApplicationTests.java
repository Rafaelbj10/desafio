package com.desafio;

import com.desafio.infra.client.ViaCepClient;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

@SpringBootTest(properties = {
        "via-cep.url=https://viacep.com.br/ws"
})
class DesafioApplicationTests {

    @Test
    void contextLoads() {
    }

    @MockBean
    private ViaCepClient viaCepClient;

}
