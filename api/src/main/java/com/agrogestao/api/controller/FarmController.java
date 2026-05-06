package com.agrogestao.api.controller;

import com.agrogestao.api.model.Farm;
import com.agrogestao.api.service.FarmService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/fazendas")
public class FarmController {

    @Autowired
    private FarmService farmService; // Agora chamamos o Service, não o Repository

    @GetMapping
    public List<Farm> listar() {
        return farmService.listarTodas();
    }

    @PostMapping
    public Farm salvar(@Valid @RequestBody Farm farm) { // Mantenha apenas aqui
        return farmService.salvar(farm);
    }

    @PutMapping("/{id}")
    public Farm atualizar(@PathVariable Long id, @RequestBody Farm farm) {
        return farmService.atualizar(id, farm);
    }

    @DeleteMapping("/{id}")
    public String deletar(@PathVariable Long id) {
        farmService.deletar(id);
        return "Fazenda removida com sucesso!";
    }
}