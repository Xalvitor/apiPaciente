package br.edu.unime.paciente.apiPaciente;

import br.edu.unime.paciente.apiPaciente.entity.Paciente;
import br.edu.unime.paciente.apiPaciente.repository.PacienteRepository;
import br.edu.unime.paciente.apiPaciente.service.PacienteService;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.InjectMocks;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.cache.support.SimpleValueWrapper;

import static org.junit.jupiter.api.Assertions.assertSame;

@SpringBootTest
public class PacienteServiceTest {

    @InjectMocks
    private PacienteService pacienteService;

    @Mock
    private CacheManager cacheManager;

    @Mock
    private PacienteRepository pacienteRepository;

    @Test
    public void testCache() throws Exception {
        // Simule o comportamento do CacheManager
        Cache cache = Mockito.mock(Cache.class);
        Mockito.when(cacheManager.getCache("pacienteCache")).thenReturn(cache);

        // Simule o valor no cache
        Paciente paciente = new Paciente(); // Crie um paciente de exemplo
        Mockito.when(cache.get("1")).thenReturn(new SimpleValueWrapper(paciente));

        // Primeira chamada, deve buscar do cache
        Paciente cachedPaciente = pacienteService.encontrarPaciente("1");
        assertSame(paciente, cachedPaciente);

        // Segunda chamada, deve buscar do cache novamente, não do repositório
        Paciente cachedPaciente2 = pacienteService.encontrarPaciente("1");
        assertSame(paciente, cachedPaciente2);

        // Certifique-se de que o método do repositório não foi chamado novamente
        Mockito.verify(pacienteRepository, Mockito.times(0)).findById("1");
    }
}