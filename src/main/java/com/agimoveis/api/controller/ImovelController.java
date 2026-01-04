package com.agimoveis.api.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.agimoveis.api.model.Imovel;
import com.agimoveis.api.service.ImovelService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/imoveis")
@CrossOrigin(origins = "*") // Permite que o Angular (ou Postman) acesse sem erros de segurança
public class ImovelController {

    @Autowired
    private ImovelService service;

    // 1. LISTAR TODOS OS ATIVOS
    // Endpoint: GET http://localhost:8080/api/imoveis
    @GetMapping
    public List<Imovel> listarTodos() {
        return service.listarAtivos();
    }

    // 2. BUSCAR POR ID
    // Endpoint: GET http://localhost:8080/api/imoveis/1
    @GetMapping("/{id}")
    public ResponseEntity<Imovel> buscarPorId(@PathVariable Long id) {
        return service.buscarPorId(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // 3. BUSCA COM FILTROS (Cidade e Preço)
    // Endpoint: GET
    // http://localhost:8080/api/imoveis/busca?cidade=Santos&precoMax=500000
    @GetMapping("/busca")
    public List<Imovel> buscarComFiltros(
            @RequestParam(required = false) String cidade,
            @RequestParam(required = false) Double precoMax) {
        return service.buscarComFiltros(cidade, precoMax);
    }

    // 4. CADASTRAR NOVO IMÓVEL
    // Endpoint: POST http://localhost:8080/api/imoveis
    @PostMapping
    public ResponseEntity<Imovel> cadastrar(@Valid @RequestBody Imovel imovel) {
        Imovel novoImovel = service.salvar(imovel);
        return ResponseEntity.status(HttpStatus.CREATED).body(novoImovel);
    }

    // 5. ATUALIZAR IMÓVEL EXISTENTE
    // Endpoint: PUT http://localhost:8080/api/imoveis/1
    @PutMapping("/{id}")
    public ResponseEntity<Imovel> atualizar(@PathVariable Long id, @Valid @RequestBody Imovel imovel) {
        try {
            Imovel imovelAtualizado = service.atualizar(id, imovel);
            return ResponseEntity.ok(imovelAtualizado);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    // Endpoint: GET http://localhost:8080/api/imoveis/pesquisa?termo=Centro
    @GetMapping("/pesquisa")
    public List<Imovel> pesquisar(@RequestParam String termo) {
        return service.pesquisarPorLocalidade(termo);
    }

    // 6. EXCLUSÃO LÓGICA (SOFT DELETE)
    // Endpoint: DELETE http://localhost:8080/api/imoveis/1
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> excluir(@PathVariable Long id) {
        try {
            service.excluirLogico(id);
            return ResponseEntity.noContent().build(); // Retorna 204 No Content
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }
}