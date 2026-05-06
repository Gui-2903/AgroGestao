package com.agrogestao.api.service;

import com.agrogestao.api.exception.EntidadeNaoEncontradaException;
import com.agrogestao.api.model.StockItem;
import com.agrogestao.api.repository.StockItemRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class StockItemService {

    @Autowired
    private StockItemRepository stockItemRepository;

    @Autowired
    private FarmService farmService; // Usamos o seu service de fazenda para validar

    public StockItem adicionarItem(Long farmId, StockItem item) {
        // 1. Buscamos a fazenda (se não existir, o farmService já lança o erro 404 que criamos)
        var farm = farmService.buscarPorId(farmId);

        // 2. Vinculamos o item à fazenda encontrada
        item.setFarm(farm);

        // 3. Salvamos o item no estoque
        return stockItemRepository.save(item);
    }

    public List<StockItem> listarEstoqueDaFazenda(Long farmId) {
        farmService.buscarPorId(farmId); // Valida se a fazenda existe antes
        return stockItemRepository.findByFarmId(farmId);
    }
    public StockItem atualizarQuantidade(Long id, Double novaQuantidade) {
        StockItem item = stockItemRepository.findById(id)
                .orElseThrow(() -> new EntidadeNaoEncontradaException("Item de estoque não encontrado."));

        item.setQuantidade(novaQuantidade);
        return stockItemRepository.save(item);
    }

    public void deletarItem(Long id) {
        if (!stockItemRepository.existsById(id)) {
            throw new EntidadeNaoEncontradaException("Item não encontrado para exclusão.");
        }
        stockItemRepository.deleteById(id);
    }
}