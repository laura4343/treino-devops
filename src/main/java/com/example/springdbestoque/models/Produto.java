package com.example.springdbestoque.models;

    import jakarta.persistence.*;
    import jakarta.validation.constraints.Min;
    import jakarta.validation.constraints.NotNull;
    import jakarta.validation.constraints.Size;
    import org.springframework.beans.factory.annotation.Value;

import java.util.Random;


@Entity



public class Produto {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @NotNull(message="O nome não deve ser nulo!")
    @Size(max=100, message="O nome deve ter no máximo 100 caracteres!")
    private String nome;

    @NotNull(message="A descrição não deve ser nula!")
    private String descricao;

    @NotNull(message="O preco não deve ser nulo!")
    @Min(value=1, message = "O preço mínimo é R$1,00")

    private double preco;



    @Min(value = 0, message="A quantidade deve ser pelo menos 0")
    @Column(name = "quantidadeestoque")
    private int quantidadeEstoque;


    public Produto(int id, String nome, String decricao, double preco, int quantidadeEstoque) {
        this.id = id;
        this.nome = nome;
        this.descricao = decricao;
        this.preco = preco;
        this.quantidadeEstoque = quantidadeEstoque;
    }

    public Produto(){}

    public long getId() {
        return id;
    }

    public String getNome() {
        return nome;
    }

    public String getDecricao() {
        return descricao;
    }

    public double getPreco() {
        return preco;
    }

    public int getQuantidadeEstoque() {
        return quantidadeEstoque;
    }

    public void setId(long id) {
        this.id = id;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public void setDecricao(String decricao) {
        this.descricao = decricao;
    }

    public void setPreco(double preco) {
        this.preco = preco;
    }

    public void setQuantidadeEstoque(int quantidadeEstoque) {
        this.quantidadeEstoque = quantidadeEstoque;
    }


    @Override
    public String toString() {
        return "-=-=-=-=- PRODUTO -=-=-=-=" +
                "Nome: " + this.nome + " | Descrição: " + this.descricao + " Quantidade: " + this.quantidadeEstoque +
                " Preço: " + this.preco ;
    }


}
