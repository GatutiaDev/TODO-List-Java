package com.japinha.todolist.repository;

import com.japinha.todolist.exception.ValidacaoException;
import com.japinha.todolist.model.Categoria;
import com.japinha.todolist.model.StatusPrioridade;
import com.japinha.todolist.model.StatusTarefa;
import com.japinha.todolist.model.Tarefa;
import com.japinha.todolist.persistence.Banco;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class TarefaRepositorySQL implements TarefaRepository{

    private final Connection conn;

    public TarefaRepositorySQL(){
        conn = Banco.conexao();
    }

    @Override
    public Tarefa salvar(Tarefa tarefa) {
        if (tarefa.getId() == null) {
            return inserir(tarefa);
        }
        return atualizar(tarefa);
    }

    private Tarefa inserir(Tarefa tarefa) {
        String sql = """
                INSERT INTO tarefas (titulo, descricao, status_tarefa, status_prioridade, categoria, data_criacao, data_concluido)
                VALUES (?, ?, ?, ?, ?, ?, ?)
                """;

        try (PreparedStatement statement = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            preencherParametros(statement, tarefa);
            statement.executeUpdate();

            try (ResultSet chavesGeradas = statement.getGeneratedKeys()) {
                chavesGeradas.next();
                long novoId = chavesGeradas.getLong(1);

                return new Tarefa(
                        novoId,
                        tarefa.getTitulo(),
                        tarefa.getDescricao(),
                        tarefa.getStatusTarefa(),
                        tarefa.getStatusPrioridade(),
                        tarefa.getCategoria(),
                        tarefa.getDataCriacao(),
                        tarefa.getDataConcluido()
                );
            }
        } catch (SQLException e) {
            throw new RuntimeException("Não foi possível salvar a tarefa", e);
        }
    }

    private Tarefa atualizar(Tarefa tarefa) {
        String sql = """
                UPDATE tarefas
                SET titulo = ?, descricao = ?, status_tarefa = ?, status_prioridade = ?, categoria = ?, data_criacao = ?, data_concluido = ?
                WHERE id = ?
                """;

        try (PreparedStatement statement = conn.prepareStatement(sql)) {
            preencherParametros(statement, tarefa);
            statement.setLong(8, tarefa.getId());
            statement.executeUpdate();

            return tarefa;
        } catch (SQLException e) {
            throw new RuntimeException("Não foi possível atualizar a tarefa", e);
        }
    }

    private void preencherParametros(PreparedStatement statement, Tarefa tarefa) throws SQLException {
        statement.setString(1, tarefa.getTitulo());
        statement.setString(2, tarefa.getDescricao());
        statement.setString(3, tarefa.getStatusTarefa().name());
        statement.setString(4, tarefa.getStatusPrioridade().name());
        statement.setString(5, tarefa.getCategoria().name());
        statement.setString(6, tarefa.getDataCriacao().toString());
        statement.setString(7, tarefa.getDataConcluido() != null ? tarefa.getDataConcluido().toString() : null);
    }

    @Override
    public Optional<Tarefa> buscarPorId(Long id) {
        if(id == null){
            throw new ValidacaoException("Nao tem id");
        }
            return sqlBuscaPorId(id);
    }

    private Optional<Tarefa> sqlBuscaPorId(Long id) {
        String sql = """
                SELECT * FROM tarefas
                WHERE id = ?
                """;

        try(PreparedStatement statement = conn.prepareStatement(sql)){
            statement.setLong(1, id);
            ResultSet dados = statement.executeQuery();

            if(dados.next()){
                return Optional.of(new Tarefa(dados.getLong(1),
                        dados.getString("titulo"),
                        dados.getString(3),
                        StatusTarefa.valueOf( dados.getString(4)),
                        StatusPrioridade.valueOf(dados.getString(5)),
                        Categoria.valueOf(dados.getString(6)),
                        LocalDate.parse(dados.getString(7)),
                        dados.getString(8) != null ? LocalDate.parse(dados.getString(8)) : null
                        )
                );
            }
            else {
                return Optional.empty();
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }

    @Override
    public List<Tarefa> listarTodas() {
        String sql = """
                SELECT * FROM tarefas
                """;

        try(PreparedStatement statement = conn.prepareStatement(sql)){

            ResultSet lista = statement.executeQuery();
            List<Tarefa> listaDados = new ArrayList<>();

            while(lista.next()){
                listaDados.add(new Tarefa(lista.getLong(1),
                        lista.getString("titulo"),
                        lista.getString(3),
                        StatusTarefa.valueOf( lista.getString(4)),
                        StatusPrioridade.valueOf(lista.getString(5)),
                        Categoria.valueOf(lista.getString(6)),
                        LocalDate.parse(lista.getString(7)),
                        lista.getString(8) != null ? LocalDate.parse(lista.getString(8)) : null
                )
                );
            }
            return listaDados;

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void deletar(Long id) {
        String sql = """
                DELETE FROM tarefas
                WHERE id = ?
                """;
        if(id == null){
            throw new IllegalArgumentException("nao tem id");
        }

        try(PreparedStatement statement = conn.prepareStatement(sql)){
            statement.setLong(1,id);
            statement.executeUpdate();

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
