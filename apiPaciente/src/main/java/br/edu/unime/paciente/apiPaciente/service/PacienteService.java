package br.edu.unime.paciente.apiPaciente.service;

import br.edu.unime.paciente.apiPaciente.entity.Paciente;
import br.edu.unime.paciente.apiPaciente.repository.PacienteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class PacienteService {
    private final CacheManager cacheManager;
    @Autowired
    PacienteRepository pacienteRepository;

    @Autowired
    public PacienteService(CacheManager cacheManager, PacienteRepository pacienteRepository) {
        this.cacheManager = cacheManager;
        this.pacienteRepository = pacienteRepository;
    }

    @Cacheable("pacienteCache")
    public List<Paciente> obterTodos(){
        return pacienteRepository.findAll();
    }

    @Cacheable("pacienteCache")
    public Paciente encontrarPaciente(String id) throws Exception {
        Cache cache = cacheManager.getCache("pacienteCache");

        if (cache != null){
            Cache.ValueWrapper valorBuscaId = cache.get(id);
            if (valorBuscaId != null) {
                Paciente paciente = (Paciente) valorBuscaId.get();
                return paciente;
            }
        }

        Optional<Paciente> pacienteOptional = pacienteRepository.findById(id);

        if (pacienteOptional.isEmpty()) {
            throw new Exception("Paciente não encontrado!");
        }
        return pacienteOptional.get();
    }

    public void inserir(Paciente paciente){
        pacienteRepository.insert(paciente);
    }

    public Paciente atualizar(String id, Paciente paciente) throws Exception {
        Paciente pacienteAntigo = encontrarPaciente(id);

        pacienteAntigo.setNome(paciente.getNome());
        pacienteAntigo.setSobrenome(paciente.getSobrenome());
        pacienteAntigo.setCpf(paciente.getSobrenome());
        pacienteAntigo.setDataDeNascimento(paciente.getDataDeNascimento());
        pacienteAntigo.setGenero(paciente.getGenero());
        pacienteAntigo.setContatos(paciente.getContatos());
        pacienteAntigo.setEnderecos(paciente.getEnderecos());

        pacienteRepository.save(pacienteAntigo);

        return pacienteAntigo;
    }

    public void deletar(String id) throws Exception {
        Paciente paciente = encontrarPaciente(id);

        pacienteRepository.delete(paciente);

    }

}
