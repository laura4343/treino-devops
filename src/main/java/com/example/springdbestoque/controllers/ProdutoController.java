package com.example.springdbestoque.controllers;


import com.example.springdbestoque.models.Produto;
import com.example.springdbestoque.services.ProdutoService;
import jakarta.validation.Valid;

import org.apache.coyote.Response;
import org.springframework.validation.Validator;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import org.springframework.validation.BindingResult;
import org.springframework.validation.DataBinder;
import org.springframework.validation.FieldError;

import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.ResponseExtractor;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


@RestController //rest --> indica que o corpo será um json | controller --> indica que é o controlador
@RequestMapping("/api/produtos")
public class ProdutoController {
    private final ProdutoService produtoService;
    private final Validator validator;

    public ProdutoController(ProdutoService produtoService, Validator validator) {
        this.produtoService = produtoService;
        this.validator = validator;


    }


    //selecionar
    @GetMapping("/selecionar")
    public List<Produto> listarProdutos(){
        return produtoService.buscarTodosProdutos();
    }

    @GetMapping("/selecionarPorNome/{nome}")
    public ResponseEntity<?> buscarPorNome(@PathVariable String nome){

        List <Produto> produtos = produtoService.buscarPorNome(nome);
        if(produtos.isEmpty()){
            return  ResponseEntity.status(HttpStatus.NOT_FOUND).body("Nenhum produto encontrado!");
        }else{
            StringBuilder produtosString = new StringBuilder();

            for (Produto produto : produtos) {
                produtosString.append(produto.toString()).append("------------------------------------------------------------");
            }

            return ResponseEntity.ok(produtosString.toString());
        }

    }

//    @GetMapping("/selecionarPorNome/")
//    public ResponseEntity<?> buscarPorNomeRP(@RequestParam String nome){
//
//        List <Produto> produtos = produtoService.buscarPorNome(nome);
//        if(produtos.isEmpty()){
//            return  ResponseEntity.status(HttpStatus.NOT_FOUND).body("Nenhum produto encontrado!");
//        }else{
//            StringBuilder produtosString = new StringBuilder();
//
//            for (Produto produto : produtos) {
//                produtosString.append(produto.toString()).append("    |    ");
//            }
//
//            return ResponseEntity.ok(produtosString.toString());
//        }
//
//    }

    //inserção
    @PostMapping("/inserir")
    public ResponseEntity<String> inserirProduto(@Valid @RequestBody Produto produto, BindingResult result) {


        if (result.hasErrors()) {

            Map<String, String> errors = new HashMap<>();
            for (FieldError error : result.getFieldErrors()
            ) {

                errors.put(error.getField(), error.getDefaultMessage());

            }

            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body(errors.toString());
        } else {
            System.out.println(produto.toString());
            produtoService.salvarProduto(produto);
            return ResponseEntity.ok("Produto inserido com sucesso!");


        }
    }



    @DeleteMapping("/excluir/{id}")
    public ResponseEntity<String> excluirProdutoController(@PathVariable Long id){

       try{
            produtoService.deletarProdutoPorId(id);
           return ResponseEntity.ok("Produto excluído com sucesso!");
       }catch (RuntimeException re){
           return ResponseEntity
                   .status(HttpStatus.NOT_FOUND)
                   .body("Produto não achado.");
       }

    }



//    @DeleteMapping("/excluirPorQuantidade/{quantidade}")
//    public ResponseEntity<?> deleteByQuantidadeEstoqueIsLessThanEqual(@RequestParam int quantidade){
//
//    }

    @PutMapping("/alterar/{id}")
    public ResponseEntity<String> atualizarProduto(@PathVariable Long id, @Valid @RequestBody Produto produtoAtualizado, BindingResult result){

        if(result.hasErrors()){

            Map<String, String> errors = new HashMap<>();
            int contador = 1;
            for (FieldError error: result.getFieldErrors()) {
                errors.put("ERRO " + contador + " :", error.getDefaultMessage());
                contador++;

            }

            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body(errors.toString().trim());
        }else {

            Produto produto = produtoService.buscarPorId(id);


            produto.setNome(produtoAtualizado.getNome());
            produto.setDecricao(produtoAtualizado.getDecricao());
            produto.setPreco(produtoAtualizado.getPreco());
            produto.setQuantidadeEstoque(produtoAtualizado.getQuantidadeEstoque());
            produtoService.salvarProduto(produto);

            return ResponseEntity.ok("Produto atualizado com sucesso!");
        }
    }

    @PatchMapping("/atualizarParcial/{id}")
        public ResponseEntity<String> atualizarParcialmente(@PathVariable Long id, @RequestBody Map<String, Object> updates){


        try{

            Produto produto = produtoService.buscarPorId(id);



            if(updates.containsKey("nome")){
                produto.setNome((String) updates.get("nome"));
            }
            if(updates.containsKey("descricao")){
                produto.setDecricao((String) updates.get("descricao"));
            }

            if(updates.containsKey("preco")){
                produto.setPreco((Double) updates.get("preco"));
            }

            if(updates.containsKey("quantidadeEstoque")){
                produto.setQuantidadeEstoque((Integer) updates.get("quantidadeEstoque"));
            }

            DataBinder databinder = new DataBinder(produto);
            databinder.setValidator(validator);
            databinder.validate();
            BindingResult resultado = databinder.getBindingResult();

            if(resultado.hasErrors()){
                Map<String, String> erros = validarProduto(resultado);
                return ResponseEntity.badRequest().body(erros.toString());
            }
            Produto produtoSalvo = produtoService.salvarProduto(produto);
            return ResponseEntity.ok(produtoSalvo.toString());


        }catch(RuntimeException re){
            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .body("Produto não achado.");
        }
    }

    public Map<String, String> validarProduto(BindingResult resultado){
        Map<String, String> erros = new HashMap<>();
        int contador = 0;
        for(FieldError erro : resultado.getFieldErrors()){
            erros.put("ERRO " + contador + " :", erro.getDefaultMessage());
            contador++;
        }
        return erros;
    }
}
