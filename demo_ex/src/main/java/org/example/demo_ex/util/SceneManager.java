package org.example.demo_ex.util;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;

public class SceneManager {

    public static void openWindow(String fxml, String title) {
        try {
            FXMLLoader loader = new FXMLLoader(SceneManager.class.getResource("/org/example/demo_ex/view/" + fxml));
            Parent root = loader.load();
            Stage stage = new Stage();
            stage.setTitle(title);
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            AlertUtil.error("Не удалось открыть окно: " + e.getMessage());
        }
    }

    public static void closeWindow(javafx.scene.Node node) {
        Stage stage = (Stage) node.getScene().getWindow();
        stage.close();
    }
}
