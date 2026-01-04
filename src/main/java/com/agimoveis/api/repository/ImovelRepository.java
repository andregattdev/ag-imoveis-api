package com.agimoveis.api.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.agimoveis.api.model.Imovel;
import com.agimoveis.api.model.enums.Finalidade;

public interface ImovelRepository extends JpaRepository<Imovel, Long> {
	List<Imovel> findByAtivoTrue();
	List<Imovel> findByFinalidadeAndAtivoTrue(Finalidade finalidade);
	// Filtro por preço máximo (Ex: Casas até R$ 500.000)
    List<Imovel> findByPrecoLessThanEqualAndAtivoTrue(Double preco);
    
    // Filtro por número de quartos (Ex: Apartamentos com 3 quartos ou mais)
    List<Imovel> findByQuartosGreaterThanEqualAndAtivoTrue(Integer quartos);
    
    // Busca por cidade ignorando maiúsculas/minúsculas
    List<Imovel> findByCidadeContainingIgnoreCaseAndAtivoTrue(String cidade);

	List<Imovel> findByCidadeContainingIgnoreCaseAndPrecoLessThanEqualAndAtivoTrue(String cidade, Double preco);
    
}
    
