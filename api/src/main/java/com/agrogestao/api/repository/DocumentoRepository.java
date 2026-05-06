package com.agrogestao.api.repository;

import com.agrogestao.api.model.Documento;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface DocumentoRepository extends JpaRepository<Documento, Long> {
    List<Documento> findByFarmId(Long farmId); // Para listar docs de uma fazenda específica
    // Busca documentos de uma fazenda que vencem entre duas datas
    // Nova versão otimizada com Paginação
    @Query("SELECT d FROM Documento d WHERE d.farm.id = :farmId AND d.dataValidade <= :dataLimite")
    Page<Documento> buscarAlertasPaginados(
            @Param("farmId") Long farmId,
            @Param("dataLimite") LocalDate dataLimite,
            Pageable pageable
    );
}