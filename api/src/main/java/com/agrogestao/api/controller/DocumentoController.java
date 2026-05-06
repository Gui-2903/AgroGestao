package com.agrogestao.api.controller;

import com.agrogestao.api.model.Documento;
import com.agrogestao.api.service.DocumentoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import io.swagger.v3.oas.annotations.Parameter;


import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/fazendas/{farmId}/documentos")
@Tag(name = "Documentos", description = "Gerenciamento de arquivos e alertas de vencimento") // Organiza em grupos
public class DocumentoController {

    @Autowired
    private DocumentoService documentoService;

    // 1. Endpoint para fazer o UPLOAD do arquivo
    @Operation(summary = "Realiza o upload de um novo documento",
               description = "Envia um arquivo físico e salva os metadados com data de validade.")
    @PostMapping("/upload")
    @ResponseStatus(HttpStatus.CREATED)
    public Documento upload(
            @PathVariable Long farmId,
            @RequestParam("titulo") String titulo,
            @RequestParam("tipo") String tipo,
            @RequestParam(value = "dataValidade", required = false) String dataValidadeStr, // Novo campo
            @RequestParam("arquivo") MultipartFile arquivo) {

        LocalDate dataValidade = null;
        if (dataValidadeStr != null && !dataValidadeStr.isEmpty()) {
            dataValidade = LocalDate.parse(dataValidadeStr); // Converte texto "2026-12-31" para Data
        }

        return documentoService.salvarArquivoComValidade(farmId, titulo, tipo, dataValidade, arquivo);
    }


    @Operation(summary = "Listar todos os documentos",
            description = "Retorna uma lista simples de todos os documentos de uma fazenda.")
    @GetMapping
    public List<Documento> listar(@PathVariable Long farmId) {
        return documentoService.listarPorFazenda(farmId);
    }

    @Operation(summary = "Download de arquivo")
    @GetMapping("/{id}/download")
// Adicionamos o farmId aqui para combinar com o @RequestMapping da classe
    public ResponseEntity<Resource> download(@PathVariable Long farmId, @PathVariable Long id) {
        Documento doc = documentoService.buscarPorIdNoBanco(id);
        Resource arquivo = documentoService.buscarArquivo(id);

        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + doc.getNomeArquivo() + "\"")
                .body(arquivo);
    }

    @Operation(summary = "Lista alertas de vencimento")
    @GetMapping("/alertas-vencimento")
    public Page<Documento> listarAlertas(
            @PathVariable Long farmId,
            @Parameter(hidden = true) @PageableDefault(size = 10, sort = "dataValidade") Pageable pageable) { // @Parameter aqui!

        return documentoService.listarAlertasPaginados(farmId, pageable);
    }

}