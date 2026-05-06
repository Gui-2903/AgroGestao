package com.agrogestao.api.repository;

import com.agrogestao.api.model.Farm;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FarmRepository extends JpaRepository<Farm, Long> {
    // O Spring gera o SQL automaticamente: SELECT * FROM farm WHERE nome = ?
    boolean existsByNome(String nome);
}