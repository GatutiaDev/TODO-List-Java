package com.japinha.todolist;

import com.japinha.todolist.repository.TarefaRepository;
import com.japinha.todolist.repository.TarefaRepositorySQL;
import com.japinha.todolist.service.TarefaService;
import com.japinha.todolist.view.MainController;
import com.japinha.todolist.viewmodel.TarefaViewModel;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class App extends Application {

    @Override
    public void start(Stage stage) throws IOException {
        TarefaRepository repository = new TarefaRepositorySQL();
        TarefaService service = new TarefaService(repository);
        TarefaViewModel viewModel = new TarefaViewModel(service);

        FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/main-view.fxml"));
        Parent raiz = loader.load();

        MainController controller = loader.getController();
        controller.setViewModel(viewModel);

        stage.setTitle("TODO List");
        stage.setScene(new Scene(raiz));
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
