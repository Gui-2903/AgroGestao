package com.agrogestao.api.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Entity
@Data
public class StockItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "O nome do item é obrigatório")
    private String nome;

    @NotNull(message = "A quantidade é obrigatória")
    @Min(value = 0, message = "A quantidade não pode ser negativa")
    private Double quantidade;

    @NotBlank(message = "A unidade de medida é obrigatória (ex: Sacas, Kg, Litros)")
    private String unidadeMedida;

    // RELACIONAMENTO: Muitos itens para uma Fazenda
    @ManyToOne
    @JoinColumn(name = "farm_id")
    @JsonProperty(access = JsonProperty.Access.READ_ONLY) // O Java só envia para o usuário, não exige no POST
    private Farm farm;
}