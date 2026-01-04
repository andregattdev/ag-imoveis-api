package com.agimoveis.api.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.agimoveis.api.dto.EnderecoDTO;
import com.agimoveis.api.model.Imovel;
import com.agimoveis.api.repository.ImovelRepository;

@Service
public class ImovelService {

    @Autowired
    private ImovelRepository repository;

    /**
     * Método salvar um novo imóvel.
     * @param imovel
     * @return Imovel salvo
     */
    public Imovel salvar(Imovel imovel) {
    if (imovel.getCep() != null && !imovel.getCep().isEmpty()) {
        EnderecoDTO dadosEndereco = buscarCep(imovel.getCep());

        // LINHA DE TESTE:
        System.out.println("CEP pesquisado: " + imovel.getCep());
        System.out.println("Retorno da API: " + dadosEndereco);

        
        if (dadosEndereco != null && dadosEndereco.getLogradouro() != null) {
            // Preenche apenas o que vem da API
            imovel.setLogradouro(dadosEndereco.getLogradouro());
            imovel.setBairro(dadosEndereco.getBairro());
            imovel.setCidade(dadosEndereco.getLocalidade()); // Mapeia localidade para cidade
            imovel.setUf(dadosEndereco.getUf());
            
            
        }
    }

    imovel.setAtivo(true);
    return repository.save(imovel);
}

    /**
     * Método atualizar um imóvel existente.
     * @param id
     * @param imovelAtualizado
     * @return Imovel atualizado
     */
    public Imovel atualizar(Long id, Imovel imovelAtualizado) {
    return repository.findById(id).map(imovel -> {
        // Lógica de CEP: Se o CEP mudou, busca na ViaCEP
        if (imovelAtualizado.getCep() != null && !imovelAtualizado.getCep().equals(imovel.getCep())) {
            EnderecoDTO dadosEndereco = buscarCep(imovelAtualizado.getCep());
            if (dadosEndereco != null && dadosEndereco.getLogradouro() != null) {
                imovel.setCep(imovelAtualizado.getCep());
                imovel.setLogradouro(dadosEndereco.getLogradouro());
                imovel.setBairro(dadosEndereco.getBairro());
                imovel.setCidade(dadosEndereco.getLocalidade());
                imovel.setUf(dadosEndereco.getUf());
                // Nota: Número e Complemento geralmente o usuário digita manualmente
                imovel.setNumero(imovelAtualizado.getNumero());
                imovel.setComplemento(imovelAtualizado.getComplemento());
            }
        } else {
            // Se o CEP é o mesmo, apenas atualiza os dados de endereço que podem ter mudado manualmente
            imovel.setLogradouro(imovelAtualizado.getLogradouro());
            imovel.setBairro(imovelAtualizado.getBairro());
            imovel.setCidade(imovelAtualizado.getCidade());
            imovel.setUf(imovelAtualizado.getUf());
            imovel.setNumero(imovelAtualizado.getNumero());
            imovel.setComplemento(imovelAtualizado.getComplemento());
        }

        // Atualiza os campos de negócio do imóvel
        imovel.setTitulo(imovelAtualizado.getTitulo());
        imovel.setDescricao(imovelAtualizado.getDescricao());
        imovel.setPreco(imovelAtualizado.getPreco());
        imovel.setQuartos(imovelAtualizado.getQuartos());
        imovel.setBanheiros(imovelAtualizado.getBanheiros());
        imovel.setArea(imovelAtualizado.getArea());
        imovel.setTipo(imovelAtualizado.getTipo());
        imovel.setFinalidade(imovelAtualizado.getFinalidade());
        imovel.setUrlImagemPrincipal(imovelAtualizado.getUrlImagemPrincipal());

        return repository.save(imovel);
    }).orElseThrow(() -> new RuntimeException("Imóvel não encontrado com o ID: " + id));
}

    /**
     * Método listar todos os imóveis ativos.
     * @return Lista de imóveis ativos
     */
    public List<Imovel> listarAtivos() {
        return repository.findByAtivoTrue();
    }

    /**
     * Método buscar imóvel por ID.
     * @param id
     * @return Retorna um imóvel pelo seu ID.
     */
    public Optional<Imovel> buscarPorId(Long id) {
        return repository.findById(id);
    }

    /**
     * Método buscar imóveis com filtros de cidade e preço máximo.
     * @param cidade
     * @param precoMax
     * @return Lista de imóveis que atendem aos filtros.
     */
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
    /**
     * Método pesquisar imóveis por localidade (cidade ou bairro).
     * @param termo
     * @return Lista de imóveis que correspondem ao termo de pesquisa.
     */
    public List<Imovel> pesquisarPorLocalidade(String termo) {
        return repository.findByAtivoTrueAndCidadeContainingIgnoreCaseOrAtivoTrueAndBairroContainingIgnoreCase(termo,
                termo);
    }

    /**
     * Método excluir logicamente um imóvel (desativar).
     * @param id
     */
    public void excluirLogico(Long id) {
        Imovel imovel = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Imóvel não encontrado!"));

        imovel.setAtivo(false); // Apenas desativa, não remove do banco
        repository.save(imovel);
    }

    public EnderecoDTO buscarCep(String cep) {
        // Remove traços ou espaços do CEP
        String cepFormatado = cep.replaceAll("\\D", "");
        String url = "https://viacep.com.br/ws/" + cepFormatado + "/json/";

        RestTemplate restTemplate = new RestTemplate();
        return restTemplate.getForObject(url, EnderecoDTO.class);
    }
}