package com.japinha.todolist.service;

import com.japinha.todolist.exception.TarefaNaoEncontradaException;
import com.japinha.todolist.exception.ValidacaoException;
import com.japinha.todolist.model.Categoria;
import com.japinha.todolist.model.StatusPrioridade;
import com.japinha.todolist.model.Tarefa;
import com.japinha.todolist.repository.TarefaRepository;

import java.util.List;
import java.util.Optional;

public class TarefaService {

    private final TarefaRepository repository;

    public TarefaService(TarefaRepository repository) {
        this.repository = repository;
    }


    public Tarefa criarTarefa(String titulo, String descricao, StatusPrioridade prioridade, Categoria categoria){
        if(titulo == null || titulo.isBlank()){
            throw new ValidacaoException("Tarefa nao preenchida");
        }

        Tarefa nova = new Tarefa(titulo, descricao, prioridade, categoria);

        return repository.salvar(nova);
    }

    public Tarefa concluirTarefa(Long id){
        Optional<Tarefa> tarefa = repository.buscarPorId(id);

        if(tarefa.isPresent()){
            tarefa.get().concluir();
            return repository.salvar(tarefa.get());
        }

        else {
            throw new TarefaNaoEncontradaException("Tarefa nao encontrada");
        }
    }

    public Tarefa editarTarefa(Long id, String novoTitulo, String novaDescricao, StatusPrioridade novaPrioridade, Categoria novaCategoria){
        Optional<Tarefa> tarefa = repository.buscarPorId(id);

        if (tarefa.isPresent()){
            tarefa.get().setTitulo(novoTitulo);
            tarefa.get().setDescricao(novaDescricao);
            tarefa.get().setStatusPrioridade(novaPrioridade);
            tarefa.get().setCategoria(novaCategoria);
            return repository.salvar(tarefa.get());
        }
        else{
            throw new TarefaNaoEncontradaException("Tareda nao encontrada!");
        }
    }

    public void excluirTarefa(Long id){
        repository.deletar(id);
    }

    public List<Tarefa> listarTarefas(){
        return repository.listarTodas();
    }

}
