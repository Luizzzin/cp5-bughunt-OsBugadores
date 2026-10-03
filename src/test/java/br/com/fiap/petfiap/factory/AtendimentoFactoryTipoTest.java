package br.com.fiap.petfiap.factory;

import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class AtendimentoFactoryTipoTest {

    @Test
    public void deveRecusarTipoInexistenteComMensagemClara() {
        // Arrange
        LocalDateTime dataHora = LocalDateTime.of(2026, 10, 1, 10, 0);

        // Act
        IllegalArgumentException erro = assertThrows(IllegalArgumentException.class,
                () -> AtendimentoFactory.criar(1, "VACINA", "Rex", "PEQUENO", "Ana", dataHora));

        // Assert: a mensagem diz qual tipo foi recusado
        assertTrue(erro.getMessage().contains("VACINA"));
    }
}