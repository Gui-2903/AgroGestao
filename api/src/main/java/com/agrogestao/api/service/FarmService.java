package com.agrogestao.api.service;

import com.agrogestao.api.exception.EntidadeNaoEncontradaException;
import com.agrogestao.api.exception.NegocioException;
import com.agrogestao.api.model.Farm;
import com.agrogestao.api.repository.FarmRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class FarmService {

    @Autowired
    private FarmRepository farmRepository;

    public Farm salvar(Farm farm) {
        if (farmRepository.existsByNome(farm.getNome())) {
            throw new NegocioException("Já existe uma fazenda cadastrada com o nome: " + farm.getNome());
        }
        return farmRepository.save(farm);
    }

    public Farm buscarPorId(Long id) {
        return farmRepository.findById(id)
                .orElseThrow(() -> new EntidadeNaoEncontradaException("Fazenda com ID " + id + " não encontrada."));
    }

    public void deletar(Long id) {
        Farm farm = buscarPorId(id); // Já lança 404 se não existir
        farmRepository.delete(farm);
    }

    public List<Farm> listarTodas() {
        return farmRepository.findAll(); // Adicione esta linha
    }

    public Farm atualizar(Long id, Farm novosDados) {
        Farm farmExistente = buscarPorId(id); // Já usa a lógica de erro que criamos

        farmExistente.setNome(novosDados.getNome());
        farmExistente.setLocalizacao(novosDados.getLocalizacao());
        farmExistente.setTamanhoHectares(novosDados.getTamanhoHectares());

        return farmRepository.save(farmExistente);
    }
}