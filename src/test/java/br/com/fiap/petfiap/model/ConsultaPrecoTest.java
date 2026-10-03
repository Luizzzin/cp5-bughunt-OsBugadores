package br.com.fiap.petfiap.model;

import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class ConsultaPrecoTest {

    @Test
    public void deveCobrar150ReaisIndependenteDoPorte() {
        // Arrange
        LocalDateTime dataHora = LocalDateTime.of(2026, 10, 1, 14, 0);
        ConsultaVeterinaria pequeno = new ConsultaVeterinaria(1, "Rex", "PEQUENO", "Ana", dataHora);
        ConsultaVeterinaria medio = new ConsultaVeterinaria(2, "Mimi", "MEDIO", "Bruno", dataHora);
        ConsultaVeterinaria grande = new ConsultaVeterinaria(3, "Thor", "GRANDE", "Ana", dataHora);

        // Act + Assert
        assertEquals(150.0, pequeno.calcularPreco(), 0.001);
        assertEquals(150.0, medio.calcularPreco(), 0.001);
        assertEquals(150.0, grande.calcularPreco(), 0.001);
    }
}