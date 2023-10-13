package br.edu.unime.paciente.apiPaciente.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;


@Data
@NoArgsConstructor
@AllArgsConstructor
public class Endereco {

    @NotBlank(message = "Logradouro não pode estar em branco.")
    private String Logradouro;


    @NotNull(message = "Numero não pode estar em branco.")
    private Integer Numero;

    @NotBlank(message = "Bairro não pode estar em branco.")
    private String Bairro;

    @NotBlank(message = "CEP não pode estar em branco.")
    private String CEP;

    @NotBlank(message = "Municipio não pode estar em branco.")
    private String Municipio;

    @NotBlank(message = "Estado não pode estar em branco.")
    private String Estado;



}
