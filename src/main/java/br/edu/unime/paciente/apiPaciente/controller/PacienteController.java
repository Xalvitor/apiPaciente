    package br.edu.unime.paciente.apiPaciente.controller;

    import br.edu.unime.paciente.apiPaciente.entity.Paciente;
    import br.edu.unime.paciente.apiPaciente.service.PacienteService;
    import br.edu.unime.paciente.apiPaciente.testing.ListaPaciente;
    import org.springframework.beans.factory.annotation.Autowired;
    import org.springframework.context.support.DefaultMessageSourceResolvable;
    import org.springframework.http.HttpStatus;
    import org.springframework.http.ResponseEntity;
    import org.springframework.validation.BindingResult;
    import org.springframework.web.bind.annotation.*;

    import java.util.*;

    import lombok.Data;

    import javax.servlet.http.HttpServletResponse;
    import javax.validation.Valid;

    @Data

    @RestController
    @RequestMapping("/pacientes")
    public class PacienteController {

        @Autowired
        PacienteService pacienteService;

        @GetMapping
        public ResponseEntity<List<Paciente>> obterTodos() {
            int statusCode = HttpServletResponse.SC_OK;
            pacienteService.registrarLog("GET", "Buscar Pacientes", pacienteService.obterTodos().toString(), statusCode);
            return ResponseEntity.ok().body(pacienteService.obterTodos());
        }

        @GetMapping("/{id}")
        public ResponseEntity<?> encontrarPaciente(@PathVariable String id) {
            try {
                Paciente paciente = pacienteService.encontrarPaciente(id);
                int statusCode = HttpServletResponse.SC_OK;
                pacienteService.registrarLog("GET", "Buscar Paciente pelo id", id, statusCode);
                return ResponseEntity.ok().body(paciente);

            } catch (Exception e) {

                Map<String, String> resposta = new HashMap<>();

                resposta.put("mensagem", e.getMessage());
                int statusCode = HttpServletResponse.SC_NOT_FOUND;
                pacienteService.registrarLog("GET", "Buscar Paciente pelo id", "Erro ao buscar Paciente pelo id: " + id, statusCode);
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(resposta);
            }

        }

        @PostMapping
        public ResponseEntity<?> inserir(@RequestBody @Valid Paciente paciente, BindingResult bindingResult) {

            if (bindingResult.hasErrors()) {

                List<String> erros = bindingResult
                        .getAllErrors()
                        .stream()
                        .map(DefaultMessageSourceResolvable::getDefaultMessage)
                        .toList();
                int statusCode = HttpServletResponse.SC_BAD_REQUEST;
                pacienteService.registrarLog("POST", "Adicionar Paciente", "Objeto: " + paciente, statusCode);

                return ResponseEntity.badRequest().body(erros.toArray());
            }
            pacienteService.inserir(paciente);

            int statusCode = HttpServletResponse.SC_CREATED;
            pacienteService.registrarLog("POST", "Adicionar Paciente", "Objeto: " + paciente, statusCode);

            return ResponseEntity.created(null).body(paciente);


        }

        @PutMapping("/{id}")
        public ResponseEntity<?> atualizar(
                @PathVariable String id,
                @Valid @RequestBody Paciente paciente,
                BindingResult bindingResult
        ) {
            if (bindingResult.hasErrors()) {

                List<String> erros = bindingResult
                        .getAllErrors()
                        .stream()
                        .map(DefaultMessageSourceResolvable::getDefaultMessage)
                        .toList();
                int statusCode = HttpServletResponse.SC_BAD_REQUEST;
                pacienteService.registrarLog("PUT", "Atualizar cadastro do Paciente", "Objeto: " + paciente, statusCode);

                return ResponseEntity.badRequest().body(erros.toArray());
            }
            try {
                Paciente pacienteAtualizado = pacienteService.atualizar(id, paciente);
                if (pacienteAtualizado != null) {
                    int statusCode = HttpServletResponse.SC_OK;
                    pacienteService.registrarLog("PUT", "Atualizar cadastro do Paciente", "Erro ao adicionar o paciente: " + paciente + " pelo id: " + id, statusCode);

                    return ResponseEntity.ok().body(pacienteAtualizado);
                } else {
                    String mensagemDeErro = "Paciente com ID " + id + " não encontrado.";
                    int statusCode = HttpServletResponse.SC_NOT_FOUND;
                    pacienteService.registrarLog("PUT", "Atualizar cadastro do Paciente", "Paciente não foi encontrado pelo id " + paciente + " pelo id: " + id, statusCode);

                    Map<String, String> resposta = new HashMap<>();
                    resposta.put("mensagem", mensagemDeErro);
                    return ResponseEntity.status(HttpStatus.NOT_FOUND).body(resposta);
                }

            } catch (Exception e) {
                Map<String, String> resposta = new HashMap<>();

                resposta.put("mensagem", e.getMessage());
                int statusCode = HttpServletResponse.SC_NOT_FOUND;
                pacienteService.registrarLog("PUT", "Atualizar cadastro do Paciente", "Erro ao atualizar o paciente " + paciente + " pelo id: " + id, statusCode);

                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(resposta);
            }
        }

        @DeleteMapping("/{id}")
        public ResponseEntity<?> excluir(
                @PathVariable String id
        ) {
            try {
                if (pacienteService.encontrarPaciente(id) == null) {
                    String mensagemDeErro = "Paciente com ID " + id + " não encontrado.";
                    int statusCode = HttpServletResponse.SC_NOT_FOUND;
                    pacienteService.registrarLog("DELETE", "Deletar Paciente por id", "Paciente não foi encontrado pelo id:" + id, statusCode);

                    Map<String, String> resposta = new HashMap<>();
                    resposta.put("mensagem", mensagemDeErro);
                    return ResponseEntity.status(HttpStatus.NOT_FOUND).body(resposta);
                }
                pacienteService.deletar(id);

                int statusCode = HttpServletResponse.SC_NO_CONTENT;
                pacienteService.registrarLog("DELETE", "Deletar Paciente por id", "Id do Paciente: " + id, statusCode);

                return ResponseEntity.noContent().build();
            } catch (Exception e) {
                Map<String, String> resposta = new HashMap<>();
                resposta.put("mensagem", e.getMessage());

                int statusCode = HttpServletResponse.SC_NOT_FOUND;
                pacienteService.registrarLog("DELETE", "Deletar Paciente por id", "Id do Paciente " + id, statusCode);

                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(resposta);
            }
        }

        @PostMapping("/adicionar-pacientes")
        public ResponseEntity<?> inserirPacientesPredefinidosAoBanco() {
            try {
                ListaPaciente pacientesPredefinidos = new ListaPaciente();

                List<Paciente> pacientes = pacientesPredefinidos.ListaPaciente();

                for (Paciente paciente : pacientes) {

                    pacienteService.inserir(paciente);

                    int statusCode = HttpServletResponse.SC_BAD_REQUEST;


                    pacienteService.registrarLog("POST", "Adicionar pacientes predefinidos", "Quantidade de pacientes: " + pacientes.size(), statusCode);

                }
                return ResponseEntity.status(HttpStatus.CREATED).body("Pacientes inseridos");

            } catch (Exception e) {

                Map<String, String> resposta = new HashMap<>();
                resposta.put("mensagem", e.getMessage());

                int statusCode = HttpServletResponse.SC_BAD_REQUEST;
                pacienteService.registrarLog("POST", "Adicionar pacientes predefinidos " ,
                        "Erro ao tentar adicionar pacientes predefinidos", statusCode);

                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(resposta);
            }

        }
    }