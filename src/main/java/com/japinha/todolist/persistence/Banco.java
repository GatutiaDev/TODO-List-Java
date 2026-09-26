package com.japinha.todolist.persistence;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class Banco {

    private static Connection conexao;

    private Banco() {
    }

    public static Connection conexao() {
        if (conexao == null) {
            conexao = criarConexao();
            criarTabelaSeNaoExistir(conexao);
        }
        return conexao;
    }

    private static Connection criarConexao() {
        try {
            String appData = System.getenv("APPDATA");
            Path pastaApp = Paths.get(appData, "todolist");
            Files.createDirectories(pastaApp);

            Path arquivoDb = pastaApp.resolve("todolist.db");
            String url = "jdbc:sqlite:" + arquivoDb;

            return DriverManager.getConnection(url);
        } catch (IOException | SQLException e) {
            throw new RuntimeException("Não foi possível conectar ao banco de dados", e);
        }
    }

    private static void criarTabelaSeNaoExistir(Connection conexao) {
        String sql = """
                CREATE TABLE IF NOT EXISTS tarefas (
                    id INTEGER PRIMARY KEY AUTOINCREMENT,
                    titulo TEXT NOT NULL,
                    descricao TEXT,
                    status_tarefa TEXT NOT NULL,
                    status_prioridade TEXT NOT NULL,
                    categoria TEXT NOT NULL,
                    data_criacao TEXT NOT NULL,
                    data_concluido TEXT
                )
                """;

        try (Statement statement = conexao.createStatement()) {
            statement.execute(sql);
        } catch (SQLException e) {
            throw new RuntimeException("Não foi possível criar a tabela de tarefas", e);
        }
    }
}
