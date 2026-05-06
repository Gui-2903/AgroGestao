package com.agrogestao.api.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import java.time.LocalDate;

@Entity
@Data
public class Documento {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "O título do documento é obrigatório")
    private String titulo;

    private String tipo; // Ex: ESCRITURA, RECIBO, CONTRATO

    private String nomeArquivo; // O nome gerado no disco (ex: 171096_contrato.pdf)

    private String urlArquivo; // O caminho completo ou link

    private LocalDate dataUpload = LocalDate.now();

    private LocalDate dataValidade; // Útil para avisos futuros

    @ManyToOne
    @JoinColumn(name = "farm_id")
    private Farm farm; // Conexão com a fazenda dona do documento
}