package br.com.fiap.petfiap.model;

import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class TosaDuracaoTest {

    @Test
    public void deveDurar60MinutosQuandoUsadaComoAtendimento() {
        // Arrange: referencia da classe base, como o sistema usa (polimorfismo)
        Atendimento tosa = new Tosa(1, "Rex", "PEQUENO", "Ana", LocalDateTime.of(2026, 10, 1, 10, 0));

        // Act
        int duracao = tosa.getDuracaoMinutos();

        // Assert
        assertEquals(60, duracao);
    }
}