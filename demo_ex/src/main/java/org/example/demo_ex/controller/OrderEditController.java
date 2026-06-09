package org.example.demo_ex.controller;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import org.example.demo_ex.dao.OrderDAO;
import org.example.demo_ex.model.Order;
import org.example.demo_ex.util.AlertUtil;
import org.example.demo_ex.util.UserSession;

import java.time.LocalDate;

public class OrderEditController {

    @FXML private Text titleText;
    @FXML private TextField txtId, txtArticle, txtAddress;
    @FXML private ComboBox<String> cmbStatus;
    @FXML private DatePicker dpOrderDate, dpReceiveDate;

    private OrderDAO orderDAO = new OrderDAO();
    private Order currentOrder;
    private Runnable onCloseCallback;

    @FXML
    private void initialize() {
        cmbStatus.getItems().addAll("Новый", "Подтвержден", "Собран", "Доставлен", "Получен");
    }

    public void setOrder(Order order) {
        this.currentOrder = order;
        if (order == null) {
            titleText.setText("Добавление заказа");
            txtId.setText("(авто)");
            dpOrderDate.setValue(LocalDate.now());
        } else {
            titleText.setText("Редактирование заказа");
            txtId.setText(String.valueOf(order.getOrderId()));
            txtArticle.setText(order.getArticle());
            cmbStatus.setValue(order.getStatus());
            txtAddress.setText(order.getPickupPoint());
            dpOrderDate.setValue(order.getOrderDate());
            dpReceiveDate.setValue(order.getReceiveDate());
        }
    }

    public void setOnCloseCallback(Runnable callback) {
        this.onCloseCallback = callback;
    }

    @FXML
    private void onSave() {
        try {
            String article = txtArticle.getText().trim();
            if (article.isEmpty()) {
                AlertUtil.error("Введите артикул заказа");
                return;
            }

            String status = cmbStatus.getValue();
            if (status == null || status.isEmpty()) {
                AlertUtil.error("Выберите статус заказа");
                return;
            }

            String address = txtAddress.getText().trim();
            if (address.isEmpty()) {
                AlertUtil.error("Введите адрес пункта выдачи");
                return;
            }

            LocalDate orderDate = dpOrderDate.getValue();
            if (orderDate == null) {
                AlertUtil.error("Выберите дату заказа");
                return;
            }

            LocalDate receiveDate = dpReceiveDate.getValue();
            if (receiveDate != null && receiveDate.isBefore(orderDate)) {
                AlertUtil.error("Дата выдачи не может быть раньше даты заказа");
                return;
            }

            if (currentOrder == null) {
                currentOrder = new Order();
            }

            currentOrder.setArticle(article);
            currentOrder.setStatus(status);
            currentOrder.setPickupPoint(address);
            currentOrder.setOrderDate(orderDate);
            currentOrder.setReceiveDate(receiveDate);

            if (txtId.getText().equals("(авто)")) {
                orderDAO.insert(currentOrder);
                AlertUtil.info("Заказ успешно добавлен");
            } else {
                orderDAO.update(currentOrder);
                AlertUtil.info("Заказ успешно обновлен");
            }

            if (onCloseCallback != null) onCloseCallback.run();
            close();
        } catch (Exception e) {
            AlertUtil.error("Ошибка сохранения: " + e.getMessage());
        }
    }

    @FXML
    private void onCancel() {
        close();
    }

    private void close() {
        Stage stage = (Stage) txtId.getScene().getWindow();
        stage.close();
    }
}
