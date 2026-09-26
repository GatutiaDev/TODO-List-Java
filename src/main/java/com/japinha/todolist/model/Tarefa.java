package com.japinha.todolist.model;

import java.time.LocalDate;

public class Tarefa {

    private Long id;

    private String titulo;

    private String descricao;

    private StatusTarefa statusTarefa;

    private StatusPrioridade statusPrioridade;

    private Categoria categoria;

    private LocalDate dataCriacao;

    private LocalDate dataConcluido;

    public Tarefa(Long id, String titulo, String descricao, StatusTarefa statusTarefa, StatusPrioridade statusPrioridade, Categoria categoria, LocalDate dataCriacao, LocalDate dataConcluido) {
        this.id = id;
        this.titulo = titulo;
        this.descricao = descricao;
        this.statusTarefa = statusTarefa;
        this.statusPrioridade = statusPrioridade;
        this.categoria = categoria;
        this.dataCriacao = dataCriacao;
        this.dataConcluido = dataConcluido;
    }

    public Tarefa(String titulo, String descricao, StatusPrioridade statusPrioridade, Categoria categoria) {
        this.titulo = titulo;
        this.descricao = descricao;
        this.statusTarefa = StatusTarefa.PENDENTE;
        this.statusPrioridade = statusPrioridade;
        this.dataCriacao = LocalDate.now();
        this.categoria = categoria;
    }

    public String getTitulo() {
        return titulo;
    }

    public String getDescricao() {
        return descricao;
    }

    public StatusTarefa getStatusTarefa() {
        return statusTarefa;
    }

    public StatusPrioridade getStatusPrioridade() {
        return statusPrioridade;
    }

    public LocalDate getDataCriacao() {
        return dataCriacao;
    }

    public LocalDate getDataConcluido() {
        return dataConcluido;
    }

    public Long getId() {
        return id;
    }

    public Categoria getCategoria() {
        return categoria;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public void setStatusPrioridade(StatusPrioridade statusPrioridade) {
        this.statusPrioridade = statusPrioridade;
    }

    public void setCategoria(Categoria categoria) {
        this.categoria = categoria;
    }

    public void concluir(){
        this.statusTarefa = StatusTarefa.CONCLUIDO;
        this.dataConcluido = LocalDate.now();
    }
}
