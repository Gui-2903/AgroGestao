package com.agrogestao.api.repository;

import com.agrogestao.api.model.StockItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface StockItemRepository extends JpaRepository<StockItem, Long> {
    // Método customizado para buscar itens apenas de uma fazenda específica
    List<StockItem> findByFarmId(Long farmId);
}