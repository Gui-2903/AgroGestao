package com.agrogestao.api.controller;

import com.agrogestao.api.model.StockItem;
import com.agrogestao.api.service.StockItemService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/fazendas/{farmId}/estoque")
public class StockItemController {

    @Autowired
    private StockItemService stockItemService;

    // 1. Endpoint para LISTAR o estoque de uma fazenda específica
    @GetMapping
    public List<StockItem> listar(@PathVariable Long farmId) {
        return stockItemService.listarEstoqueDaFazenda(farmId);
    }

    // 2. Endpoint para ADICIONAR um item ao estoque de uma fazenda
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public StockItem adicionar(@PathVariable Long farmId, @Valid @RequestBody StockItem item) {
        return stockItemService.adicionarItem(farmId, item);
    }

    // Atualizar apenas a quantidade (Ex: /api/fazendas/1/estoque/1)
    @PatchMapping("/{itemId}")
    public StockItem atualizarQtd(@PathVariable Long itemId, @RequestBody Double quantidade) {
        return stockItemService.atualizarQuantidade(itemId, quantidade);
    }

    // Deletar um item (Ex: /api/fazendas/1/estoque/1)
    @DeleteMapping("/{itemId}")
    public void remover(@PathVariable Long itemId) {
        stockItemService.deletarItem(itemId);
    }
}