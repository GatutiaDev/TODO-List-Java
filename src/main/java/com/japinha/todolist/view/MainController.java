package com.japinha.todolist.view;

import com.japinha.todolist.exception.TarefaNaoEncontradaException;
import com.japinha.todolist.exception.ValidacaoException;
import com.japinha.todolist.model.Categoria;
import com.japinha.todolist.model.StatusPrioridade;
import com.japinha.todolist.model.Tarefa;
import com.japinha.todolist.viewmodel.TarefaViewModel;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ListCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

public class MainController {

    @FXML
    private TextField campoTitulo;
    @FXML
    private TextArea campoDescricao;
    @FXML
    private ComboBox<StatusPrioridade> campoPrioridade;
    @FXML
    private ComboBox<Categoria> campoCategoria;

    @FXML
    private Button botaoSalvar;
    @FXML
    private Button botaoCancelarEdicao;
    @FXML
    private Button botaoEditar;
    @FXML
    private Button botaoConcluir;
    @FXML
    private Button botaoExcluir;

    @FXML
    private TableView<Tarefa> tabelaTarefas;
    @FXML
    private TableColumn<Tarefa, String> colunaTitulo;
    @FXML
    private TableColumn<Tarefa, String> colunaDescricao;
    @FXML
    private TableColumn<Tarefa, String> colunaStatus;
    @FXML
    private TableColumn<Tarefa, String> colunaPrioridade;
    @FXML
    private TableColumn<Tarefa, String> colunaCategoria;
    @FXML
    private TableColumn<Tarefa, String> colunaDataCriacao;
    @FXML
    private TableColumn<Tarefa, String> colunaDataConcluido;

    private TarefaViewModel viewModel;
    private Long idEmEdicao;

    public void setViewModel(TarefaViewModel viewModel) {
        this.viewModel = viewModel;
        tabelaTarefas.setItems(viewModel.getTarefas());
    }

    @FXML
    private void initialize() {
        campoPrioridade.setItems(FXCollections.observableArrayList(StatusPrioridade.values()));
        campoCategoria.setItems(FXCollections.observableArrayList(Categoria.values()));

        campoPrioridade.setButtonCell(criarCelulaComTextoPadrao("Prioridade"));
        campoCategoria.setButtonCell(criarCelulaComTextoPadrao("Categoria"));

        colunaTitulo.setCellValueFactory(dados -> new SimpleStringProperty(dados.getValue().getTitulo()));
        colunaDescricao.setCellValueFactory(dados -> new SimpleStringProperty(dados.getValue().getDescricao()));
        colunaStatus.setCellValueFactory(dados -> new SimpleStringProperty(dados.getValue().getStatusTarefa().name()));
        colunaPrioridade.setCellValueFactory(dados -> new SimpleStringProperty(dados.getValue().getStatusPrioridade().name()));
        colunaCategoria.setCellValueFactory(dados -> new SimpleStringProperty(dados.getValue().getCategoria().name()));
        colunaDataCriacao.setCellValueFactory(dados -> new SimpleStringProperty(dados.getValue().getDataCriacao().toString()));
        colunaDataConcluido.setCellValueFactory(dados -> new SimpleStringProperty(
                dados.getValue().getDataConcluido() != null ? dados.getValue().getDataConcluido().toString() : ""));
    }

    @FXML
    private void aoClicarSalvar() {
        String titulo = campoTitulo.getText();
        String descricao = campoDescricao.getText();
        StatusPrioridade prioridade = campoPrioridade.getValue();
        Categoria categoria = campoCategoria.getValue();

        try {
            if (idEmEdicao == null) {
                viewModel.criarTarefa(titulo, descricao, prioridade, categoria);
            } else {
                viewModel.editarTarefa(idEmEdicao, titulo, descricao, prioridade, categoria);
            }
            limparFormulario();
        } catch (ValidacaoException | TarefaNaoEncontradaException e) {
            mostrarErro(e.getMessage());
        }
    }

    @FXML
    private void aoClicarEditar() {
        Tarefa selecionada = tabelaTarefas.getSelectionModel().getSelectedItem();
        if (selecionada == null) {
            mostrarErro("Selecione uma tarefa para editar.");
            return;
        }

        idEmEdicao = selecionada.getId();
        campoTitulo.setText(selecionada.getTitulo());
        campoDescricao.setText(selecionada.getDescricao());
        campoPrioridade.setValue(selecionada.getStatusPrioridade());
        campoCategoria.setValue(selecionada.getCategoria());
    }

    @FXML
    private void aoClicarCancelarEdicao() {
        limparFormulario();
    }

    @FXML
    private void aoClicarConcluir() {
        Tarefa selecionada = tabelaTarefas.getSelectionModel().getSelectedItem();
        if (selecionada == null) {
            mostrarErro("Selecione uma tarefa para concluir.");
            return;
        }

        try {
            viewModel.concluirTarefa(selecionada.getId());
        } catch (TarefaNaoEncontradaException e) {
            mostrarErro(e.getMessage());
        }
    }

    @FXML
    private void aoClicarExcluir() {
        Tarefa selecionada = tabelaTarefas.getSelectionModel().getSelectedItem();
        if (selecionada == null) {
            mostrarErro("Selecione uma tarefa para excluir.");
            return;
        }

        viewModel.excluirTarefa(selecionada.getId());
        if (selecionada.getId().equals(idEmEdicao)) {
            limparFormulario();
        }
    }

    private void limparFormulario() {
        idEmEdicao = null;
        campoTitulo.clear();
        campoDescricao.clear();
        campoPrioridade.getSelectionModel().clearSelection();
        campoCategoria.getSelectionModel().clearSelection();
    }

    private void mostrarErro(String mensagem) {
        Alert alert = new Alert(Alert.AlertType.ERROR, mensagem);
        alert.showAndWait();
    }

    private <T> ListCell<T> criarCelulaComTextoPadrao(String textoPadrao) {
        return new ListCell<>() {
            @Override
            protected void updateItem(T item, boolean vazio) {
                super.updateItem(item, vazio);
                setText(vazio || item == null ? textoPadrao : item.toString());
            }
        };
    }
}
