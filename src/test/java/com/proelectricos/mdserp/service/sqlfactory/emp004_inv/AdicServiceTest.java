package com.proelectricos.mdserp.service.sqlfactory.emp004_inv;

import org.junit.jupiter.api.Test;
import java.util.stream.StreamSupport;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class AdicServiceTest {

    @Autowired
    private AdicService adicService;

    @Test
    void findAllAdics() {
        var results = adicService.findAllAdics();
        StreamSupport.stream(results.spliterator(), false)
                .limit(10)
                .forEach(System.out::println);
    }
}
