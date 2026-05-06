package com.agrogestao.api.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Entity
@Data
public class Farm {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "O nome não pode estar vazio")
    private String nome;

    @NotBlank(message = "A localização é obrigatória")
    private String localizacao;

    @Min(value = 0, message = "O tamanho não pode ser negativo")
    private Double tamanhoHectares;
}