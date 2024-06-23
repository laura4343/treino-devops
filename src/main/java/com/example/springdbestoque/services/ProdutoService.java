package com.example.springdbestoque.services;

import com.example.springdbestoque.models.Produto;
import com.example.springdbestoque.repository.ProdutoRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
public class ProdutoService {

    private final ProdutoRepository produtoRepository;


    public ProdutoService(ProdutoRepository produtoRepository) {
        this.produtoRepository = produtoRepository;
    }


    // método de save (inserir)
    public Produto salvarProduto(Produto produto){
        return produtoRepository.save(produto);
    }

    // métodos de busca

    public List<Produto> buscarTodosProdutos(){
        return produtoRepository.findAll();
    }

    public List<Produto> buscarPorNome(String nome){

        return produtoRepository.findByNomeLikeIgnoreCase(nome);
    }

    public Produto buscarPorId(Long id){
        return produtoRepository.findById(id).orElseThrow(() -> {
            return new RuntimeException("Produto não encontrado");
        }

        );
    }

    // métodos de exclusão
    @Transactional
    public Produto deletarProdutoPorId(Long id){
        Produto produto = buscarPorId(id);
        produtoRepository.deleteById(id);
        return produto;
    }

//    @Transactional
//    public int deleteByQuantidadeEstoqueIsLessThanEqual(int quantidade){
//
//
//    }




}
