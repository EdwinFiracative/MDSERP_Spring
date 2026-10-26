package com.proelectricos.mdserp.config;

import org.junit.jupiter.api.Test;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestClient;

import java.net.ServerSocket;
import java.net.Socket;
import java.time.Duration;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class BitrixConfigTest {

    // Servidor que acepta la conexion y nunca responde: sin read timeout la llamada quedaria colgada
    @Test
    void cortaLaLecturaCuandoBitrixNoResponde() throws Exception {
        try (ServerSocket server = new ServerSocket(0)) {
            Thread acceptor = new Thread(() -> {
                try (Socket ignored = server.accept()) {
                    Thread.sleep(10_000);
                } catch (Exception ignored) {
                    // el test cierra el socket al terminar
                }
            });
            acceptor.setDaemon(true);
            acceptor.start();

            RestClient client = new BitrixConfig().bitrixRestClient(
                    "http://localhost:" + server.getLocalPort() + "/rest/1/token",
                    Duration.ofSeconds(1), Duration.ofMillis(300));

            long start = System.nanoTime();
            assertThatThrownBy(() -> client.post().uri("profile.json").retrieve().toBodilessEntity())
                    .isInstanceOf(ResourceAccessException.class);
            assertThat(Duration.ofNanos(System.nanoTime() - start)).isLessThan(Duration.ofSeconds(5));
        }
    }
}
