package com.example.springdbestoque.repository;

import com.example.springdbestoque.models.Produto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ProdutoRepository extends JpaRepository<Produto,Long> {

    @Modifying
    @Query("DELETE FROM Produto prod where :id = prod.id")
    void deleteById(Long id);

    int countByQuantidadeEstoqueIsLessThanEqual(Long quant);

    void deleteByQuantidadeEstoqueIsLessThanEqual(Long quant);

    List<Produto> findByNomeLikeIgnoreCase(String nome);


}
