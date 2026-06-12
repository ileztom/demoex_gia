package org.example.demo_ex.controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import org.example.demo_ex.dao.UserDAO;
import org.example.demo_ex.model.User;
import org.example.demo_ex.util.UserSession;

public class LoginController {

    @FXML
    private TextField loginField;
    @FXML
    private PasswordField passwordField;
    @FXML
    private Label errorLabel;

    private UserDAO userDAO = new UserDAO();

    @FXML
    private void onLogin() {
        String login = loginField.getText().trim();
        String password = passwordField.getText().trim();

        if (login.isEmpty() || password.isEmpty()) {
            showError("Введите логин и пароль");
            return;
        }

        User user = userDAO.authorize(login, password);
        if (user == null) {
            showError("Неверный логин или пароль");
            return;
        }

        UserSession.getInstance().setCurrentUser(user);
        openProductList();
    }

    @FXML
    private void onGuest() {
        UserSession.getInstance().setCurrentUser(null);
        openProductList();
    }

    private void openProductList() {
        try {
            Stage stage = (Stage) loginField.getScene().getWindow();
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/org/example/demo_ex/view/ProductList.fxml"));
            Parent root = loader.load();
            stage.setTitle("Список товаров");
            stage.setScene(new Scene(root));
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void showError(String msg) {
        errorLabel.setText(msg);
        errorLabel.setVisible(true);
    }
}
