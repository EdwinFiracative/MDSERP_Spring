package com.proelectricos.mdserp.chatbot.telegram;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class TelegramFormatterTest {

    @Test
    void convierteNegritasVinetasYEscapaHtml() {
        String html = TelegramFormatter.html("## Pedido 49594\n- **Cliente:** A & B <SAS>\n- `TOP 20`");

        assertThat(html).isEqualTo("<b>Pedido 49594</b>\n• <b>Cliente:</b> A &amp; B &lt;SAS&gt;\n• <code>TOP 20</code>");
    }

    @Test
    void renderizaTablasAlineadasEnPre() {
        String html = TelegramFormatter.html("Referencias:\n| Item | Código |\n|---|---|\n| 1 | **ABC-1** |\nFin");

        assertThat(html).isEqualTo("Referencias:\n<pre>Item | Código\n-----+-------\n1    | ABC-1\n</pre>\nFin");
    }

    @Test
    void parteTextosLargosEnSaltosDeLinea() {
        assertThat(TelegramFormatter.partir("aaaa\nbbbb\ncccc", 10)).containsExactly("aaaa\nbbbb", "cccc");
    }
}
