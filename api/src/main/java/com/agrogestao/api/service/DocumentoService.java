package com.agrogestao.api.service;

import com.agrogestao.api.exception.EntidadeNaoEncontradaException;
import com.agrogestao.api.exception.NegocioException;
import com.agrogestao.api.model.Documento;
import com.agrogestao.api.repository.DocumentoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;

import java.net.MalformedURLException;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Service
public class DocumentoService {

    @Autowired
    private DocumentoRepository documentoRepository;

    @Autowired
    private FarmService farmService;

    // Puxa o caminho que você configurou no application.properties
    @Value("${upload.dir}")
    private String uploadDir;

    public Documento salvarArquivo(Long farmId, String titulo, String tipo, MultipartFile arquivo) {
        // 1. Valida se a fazenda existe
        var farm = farmService.buscarPorId(farmId);

        try {
            // 2. Cria a pasta se ela não existir
            Path diretorioPath = Paths.get(uploadDir);
            if (!Files.exists(diretorioPath)) {
                Files.createDirectories(diretorioPath);
            }

            // 3. Gera um nome único para o arquivo (evita duplicidade no Windows)
            String nomeOriginal = arquivo.getOriginalFilename();
            String nomeFinal = UUID.randomUUID().toString() + "_" + nomeOriginal;
            Path caminhoCompleto = diretorioPath.resolve(nomeFinal);

            // 4. Copia o arquivo físico para a pasta
            Files.copy(arquivo.getInputStream(), caminhoCompleto);

            // 5. Salva os metadados no banco de dados
            Documento doc = new Documento();
            doc.setTitulo(titulo);
            doc.setTipo(tipo);
            doc.setNomeArquivo(nomeFinal);
            doc.setUrlArquivo(caminhoCompleto.toString());
            doc.setFarm(farm);

            return documentoRepository.save(doc);

        } catch (IOException e) {
            throw new NegocioException("Erro ao salvar o arquivo no disco: " + e.getMessage());
        }
    }

    public List<Documento> listarPorFazenda(Long farmId) {
        farmService.buscarPorId(farmId);
        return documentoRepository.findByFarmId(farmId);
    }

    public Resource buscarArquivo(Long id) {
        Documento doc = documentoRepository.findById(id)
                .orElseThrow(() -> new EntidadeNaoEncontradaException("Documento não encontrado."));

        try {
            Path caminho = Paths.get(doc.getUrlArquivo());
            Resource resource = new UrlResource(caminho.toUri());

            if (resource.exists() || resource.isReadable()) {
                return resource;
            } else {
                throw new NegocioException("Não foi possível ler o arquivo no disco.");
            }
        } catch (MalformedURLException e) {
            throw new NegocioException("Erro ao localizar o arquivo: " + e.getMessage());
        }
    }

    public Documento buscarPorIdNoBanco(Long id) {
        return documentoRepository.findById(id)
                .orElseThrow(() -> new EntidadeNaoEncontradaException("Documento não encontrado no banco."));
    }

    public Documento salvarArquivoComValidade(Long farmId, String titulo, String tipo, LocalDate dataValidade, MultipartFile arquivo) {
        // 1. Validar se a fazenda existe (usando o seu FarmService)
        var farm = farmService.buscarPorId(farmId);

        try {
            // 2. Garantir que o diretório de upload existe
            Path diretorioPath = Paths.get(uploadDir);
            if (!Files.exists(diretorioPath)) {
                Files.createDirectories(diretorioPath);
            }

            // 3. Gerar o nome único para o arquivo (UUID + nome original)
            String nomeOriginal = arquivo.getOriginalFilename();
            String nomeFinal = UUID.randomUUID().toString() + "_" + nomeOriginal;
            Path caminhoCompleto = diretorioPath.resolve(nomeFinal);

            // 4. Salvar o arquivo físico no disco
            Files.copy(arquivo.getInputStream(), caminhoCompleto);

            // 5. Criar o objeto Documento com todos os dados para o banco
            Documento doc = new Documento();
            doc.setTitulo(titulo);
            doc.setTipo(tipo);
            doc.setDataValidade(dataValidade);
            doc.setNomeArquivo(nomeFinal); // Agora a variável nomeFinal existe aqui!
            doc.setUrlArquivo(caminhoCompleto.toString()); // Agora a variável caminhoCompleto existe aqui!
            doc.setFarm(farm); // Agora a variável farm existe aqui!

            return documentoRepository.save(doc);

        } catch (IOException e) {
            throw new NegocioException("Erro ao gravar arquivo: " + e.getMessage());
        }
    }

    public Page<Documento> listarAlertasPaginados(Long farmId, Pageable pageable) {
        // Definimos o limite de 30 dias para os alertas
        LocalDate limiteFuturo = LocalDate.now().plusDays(30);

        farmService.buscarPorId(farmId);

        return documentoRepository.buscarAlertasPaginados(farmId, limiteFuturo, pageable);
    }

}