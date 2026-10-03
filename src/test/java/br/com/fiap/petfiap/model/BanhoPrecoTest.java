package br.com.fiap.petfiap.model;

import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class BanhoPrecoTest {

    @Test
    public void deveCobrarPrecoDoBanhoConformeOPorte() {
        // Arrange
        LocalDateTime dataHora = LocalDateTime.of(2026, 10, 1, 10, 0);
        Banho pequeno = new Banho(1, "Rex", "PEQUENO", "Ana", dataHora);
        Banho medio = new Banho(2, "Mimi", "MEDIO", "Ana", dataHora);
        Banho grande = new Banho(3, "Thor", "GRANDE", "Ana", dataHora);

        // Act + Assert
        assertEquals(60.0, pequeno.calcularPreco(), 0.001);
        assertEquals(80.0, medio.calcularPreco(), 0.001);
        assertEquals(100.0, grande.calcularPreco(), 0.001);
    }
}