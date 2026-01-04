package com.agimoveis.api.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.agimoveis.api.model.Imovel;
import com.agimoveis.api.repository.ImovelRepository;

@Service
public class ImovelService {

    @Autowired
    private ImovelRepository repository;

    // CREATE
    public Imovel salvar(Imovel imovel) {
        // Garante que todo novo imóvel comece como ativo
        imovel.setAtivo(true);
        return repository.save(imovel);
    }

    // UPDATE
    public Imovel atualizar(Long id, Imovel imovelAtualizado) {
        return repository.findById(id).map(imovel -> {
            // Atualiza apenas os campos permitidos
            imovel.setTitulo(imovelAtualizado.getTitulo());
            imovel.setDescricao(imovelAtualizado.getDescricao());
            imovel.setPreco(imovelAtualizado.getPreco());
            imovel.setQuartos(imovelAtualizado.getQuartos());
            imovel.setBanheiros(imovelAtualizado.getBanheiros());
            imovel.setArea(imovelAtualizado.getArea());
            imovel.setCidade(imovelAtualizado.getCidade());
            imovel.setBairro(imovelAtualizado.getBairro());
            imovel.setLogradouro(imovelAtualizado.getLogradouro());
            imovel.setTipo(imovelAtualizado.getTipo());
            imovel.setFinalidade(imovelAtualizado.getFinalidade());
            imovel.setUrlImagemPrincipal(imovelAtualizado.getUrlImagemPrincipal());
            return repository.save(imovel);
        }).orElseThrow(() -> new RuntimeException("Imóvel não encontrado com o ID: " + id));
    }

    // READ (Todos ativos)
    public List<Imovel> listarAtivos() {
        return repository.findByAtivoTrue();
    }

    // READ (Por ID)
    public Optional<Imovel> buscarPorId(Long id) {
        return repository.findById(id);
    }

    // BUSCA COM FILTROS (Para o Front-end)
    public List<Imovel> buscarComFiltros(String cidade, Double precoMax) {
        if (cidade != null && precoMax != null) {
            return repository.findByCidadeContainingIgnoreCaseAndPrecoLessThanEqualAndAtivoTrue(cidade, precoMax);
        } else if (cidade != null) {
            return repository.findByCidadeContainingIgnoreCaseAndAtivoTrue(cidade);
        } else if (precoMax != null) {
            return repository.findByPrecoLessThanEqualAndAtivoTrue(precoMax);
        }
        return repository.findByAtivoTrue();
    }

    public List<Imovel> pesquisarPorLocalidade(String termo) {
        return repository.findByAtivoTrueAndCidadeContainingIgnoreCaseOrAtivoTrueAndBairroContainingIgnoreCase(termo,
                termo);
    }

    // DELETE (Soft Delete / Exclusão Lógica)
    public void excluirLogico(Long id) {
        Imovel imovel = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Imóvel não encontrado!"));

        imovel.setAtivo(false); // Apenas desativa, não remove do banco
        repository.save(imovel);
    }
}