package com.japinha.todolist.repository;

import com.japinha.todolist.model.Tarefa;

import java.util.List;
import java.util.Optional;

public interface TarefaRepository {
    Tarefa salvar(Tarefa tarefa);

    Optional<Tarefa> buscarPorId(Long id);

    List<Tarefa> listarTodas();

    void deletar(Long id);
}