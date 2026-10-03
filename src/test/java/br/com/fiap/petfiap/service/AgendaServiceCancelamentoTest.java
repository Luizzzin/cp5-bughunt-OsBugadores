package br.com.fiap.petfiap.service;

import br.com.fiap.petfiap.exception.StatusInvalidoException;
import br.com.fiap.petfiap.model.Banho;
import br.com.fiap.petfiap.repository.AtendimentoRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

// Teste unitario: NAO sobe o Spring e NAO conecta no Oracle (Aula 15).
@ExtendWith(MockitoExtension.class)
public class AgendaServiceCancelamentoTest {

    @Mock
    private AtendimentoRepository repository;

    @InjectMocks
    private AgendaService service;

    @Test
    public void deveRecusarCancelamentoDeAtendimentoJaConcluido() {
        // Arrange: atendimento que ja foi realizado
        Banho jaConcluido = new Banho(1, "Rex", "PEQUENO", "Ana", LocalDateTime.now().plusDays(1));
        jaConcluido.setStatus("CONCLUIDO");
        when(repository.findById(1L)).thenReturn(Optional.of(jaConcluido));

        // Act + Assert
        assertThrows(StatusInvalidoException.class, () -> service.cancelar(1L));

        // Nada e salvo quando a operacao e recusada
        verify(repository, never()).save(any());
    }
}