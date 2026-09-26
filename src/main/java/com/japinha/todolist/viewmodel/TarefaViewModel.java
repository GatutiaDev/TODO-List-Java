package com.japinha.todolist.viewmodel;

import com.japinha.todolist.model.Categoria;
import com.japinha.todolist.model.StatusPrioridade;
import com.japinha.todolist.model.Tarefa;
import com.japinha.todolist.service.TarefaService;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class TarefaViewModel {

    private final TarefaService service;
    private final ObservableList<Tarefa> tarefas = FXCollections.observableArrayList();

    public TarefaViewModel(TarefaService service) {
        this.service = service;
        carregarTarefas();
    }

    public ObservableList<Tarefa> getTarefas() {
        return tarefas;
    }

    public void carregarTarefas() {
        tarefas.setAll(service.listarTarefas());
    }

    public void criarTarefa(String titulo, String descricao, StatusPrioridade prioridade, Categoria categoria) {
        service.criarTarefa(titulo, descricao, prioridade, categoria);
        carregarTarefas();
    }

    public void concluirTarefa(Long id) {
        service.concluirTarefa(id);
        carregarTarefas();
    }

    public void editarTarefa(Long id, String novoTitulo, String novaDescricao, StatusPrioridade novaPrioridade, Categoria novaCategoria) {
        service.editarTarefa(id, novoTitulo, novaDescricao, novaPrioridade, novaCategoria);
        carregarTarefas();
    }

    public void excluirTarefa(Long id) {
        service.excluirTarefa(id);
        carregarTarefas();
    }
}
