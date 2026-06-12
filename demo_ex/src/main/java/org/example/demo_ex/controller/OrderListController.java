package org.example.demo_ex.controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import org.example.demo_ex.dao.OrderDAO;
import org.example.demo_ex.model.Order;
import org.example.demo_ex.util.AlertUtil;
import org.example.demo_ex.util.UserSession;

public class OrderListController {

    @FXML private TableView<Order> orderTable;
    @FXML private Button btnAddOrder;
    @FXML private Button btnDeleteOrder;
    @FXML private Label lblUser;

    private OrderDAO orderDAO = new OrderDAO();
    private ObservableList<Order> orderList = FXCollections.observableArrayList();

    @FXML
    private void initialize() {
        setupTable();
        setupUserInterface();
        loadOrders();
    }

    private void setupTable() {
        orderTable.setItems(orderList);

        orderTable.setOnMouseClicked(event -> {
            if (event.getClickCount() == 2 && UserSession.getInstance().isAdmin()) {
                Order selected = orderTable.getSelectionModel().getSelectedItem();
                if (selected != null) {
                    openEditOrder(selected);
                }
            }
        });
    }

    private void setupUserInterface() {
        boolean isAdmin = UserSession.getInstance().isAdmin();
        if (UserSession.getInstance().getCurrentUser() != null) {
            lblUser.setText(UserSession.getInstance().getCurrentUser().getFullName());
        }
        btnAddOrder.setVisible(isAdmin);
        btnDeleteOrder.setVisible(isAdmin);
    }

    private void loadOrders() {
        orderList.setAll(orderDAO.getAll());
    }

    @FXML
    private void onAddOrder() {
        if (!UserSession.getInstance().isAdmin()) return;
        openEditOrder(null);
    }

    @FXML
    private void onDeleteOrder() {
        if (!UserSession.getInstance().isAdmin()) return;
        Order selected = orderTable.getSelectionModel().getSelectedItem();
        if (selected == null) {
            AlertUtil.warning("Выберите заказ для удаления");
            return;
        }
        if (AlertUtil.confirm("Удалить заказ \"" + selected.getArticle() + "\"?")) {
            orderDAO.delete(selected.getOrderId());
            AlertUtil.info("Заказ успешно удален");
            loadOrders();
        }
    }

    private void openEditOrder(Order order) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/org/example/demo_ex/view/OrderEdit.fxml"));
            Parent root = loader.load();
            OrderEditController controller = loader.getController();
            controller.setOrder(order);
            controller.setOnCloseCallback(this::loadOrders);

            Stage stage = new Stage();
            stage.setTitle(order == null ? "Добавление заказа" : "Редактирование заказа");
            stage.setScene(new Scene(root));
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void onBack() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/org/example/demo_ex/view/ProductList.fxml"));
            Parent root = loader.load();
            Stage stage = (Stage) orderTable.getScene().getWindow();
            stage.setTitle("Список товаров");
            stage.setScene(new Scene(root));
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
