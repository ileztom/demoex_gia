package org.example.demo_ex.controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import org.example.demo_ex.dao.ProductDAO;
import org.example.demo_ex.model.Product;
import org.example.demo_ex.util.AlertUtil;
import org.example.demo_ex.util.UserSession;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class ProductListController {

    @FXML private TableView<Product> productTable;
    @FXML private TextField txtSearch;
    @FXML private ComboBox<String> cmbSort;
    @FXML private ComboBox<String> cmbFilter;
    @FXML private HBox filterPanel;
    @FXML private Button btnAddProduct;
    @FXML private Button btnDeleteProduct;
    @FXML private Button btnOrders;
    @FXML private Button btnLogout;
    @FXML private Label lblUser;

    private ProductDAO productDAO = new ProductDAO();
    private ObservableList<Product> allProducts = FXCollections.observableArrayList();
    private static boolean editWindowOpened = false;

    @FXML
    private void initialize() {
        setupColumns();
        setupUserInterface();
        loadSuppliers();
        setupListeners();
        loadProducts();
    }

    private void setupColumns() {
        ((TableColumn<Product, String>) productTable.getColumns().get(0)).setCellFactory(col -> new TableCell<Product, String>() {
            private final ImageView imageView = new ImageView();
            {
                imageView.setFitWidth(80);
                imageView.setFitHeight(60);
            }
            @Override
            protected void updateItem(String imagePath, boolean empty) {
                super.updateItem(imagePath, empty);
                if (empty || getTableRow() == null || getTableRow().getItem() == null) {
                    setGraphic(null);
                    return;
                }
                Product p = getTableRow().getItem();
                Image img = null;
                if (p.getImagePath() != null && !p.getImagePath().isEmpty()) {
                    try {
                        img = new Image("file:" + p.getImagePath(), 80, 60, true, true);
                    } catch (Exception e) {
                        img = null;
                    }
                }
                if (img == null || img.isError()) {
                    img = new Image(getClass().getResourceAsStream("/org/example/demo_ex/images/picture.png"), 80, 60, true, true);
                }
                imageView.setImage(img);
                setGraphic(imageView);
            }
        });

        productTable.setRowFactory(tv -> new TableRow<Product>() {
            @Override
            protected void updateItem(Product item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setStyle("");
                } else {
                    if (item.getQuantity() == 0) {
                        setStyle("-fx-background-color: lightblue;");
                    } else if (item.getDiscount() > 15) {
                        setStyle("-fx-background-color: #2E8B57;");
                    } else {
                        setStyle("");
                    }
                }
            }
        });

        productTable.setOnMouseClicked(event -> {
            if (event.getClickCount() == 2 && UserSession.getInstance().isAdmin()) {
                Product selected = productTable.getSelectionModel().getSelectedItem();
                if (selected != null) {
                    openEditProduct(selected);
                }
            }
        });

        TableColumn<Product, Number> priceCol = (TableColumn<Product, Number>) productTable.getColumns().get(6);
        priceCol.setCellFactory(col -> new TableCell<Product, Number>() {
            @Override
            protected void updateItem(Number price, boolean empty) {
                super.updateItem(price, empty);
                if (empty || getTableRow() == null || getTableRow().getItem() == null) {
                    setGraphic(null);
                    return;
                }
                Product p = getTableRow().getItem();
                if (p.getDiscount() > 0) {
                    Label oldPrice = new Label(String.format("%.2f", p.getPrice()));
                    oldPrice.setStyle("-fx-strikethrough: true; -fx-text-fill: red;");
                    Label finalPrice = new Label(String.format("%.2f", p.getFinalPrice()));
                    finalPrice.setStyle("-fx-text-fill: black;");
                    HBox box = new HBox(5, oldPrice, finalPrice);
                    setGraphic(box);
                } else {
                    setText(String.format("%.2f", p.getPrice()));
                    setGraphic(null);
                }
            }
        });

        productTable.setItems(allProducts);
    }

    private void setupUserInterface() {
        String role = "Гость";
        String name = "";
        boolean canFilter = false;

        if (UserSession.getInstance().isGuest()) {
            role = "Гость";
            canFilter = false;
        } else {
            role = UserSession.getInstance().getCurrentUser().getRole();
            name = UserSession.getInstance().getCurrentUser().getFullName();
            canFilter = UserSession.getInstance().isManager() || UserSession.getInstance().isAdmin();
        }

        lblUser.setText(name.isEmpty() ? role : name + " (" + role + ")");
        btnAddProduct.setVisible(UserSession.getInstance().isAdmin());
        btnDeleteProduct.setVisible(UserSession.getInstance().isAdmin());
        btnOrders.setVisible(UserSession.getInstance().isManager() || UserSession.getInstance().isAdmin());
        filterPanel.setVisible(canFilter);
        filterPanel.setManaged(canFilter);
    }

    private void loadSuppliers() {
        List<String> suppliers = productDAO.getAllSuppliers();
        cmbFilter.getItems().clear();
        cmbFilter.getItems().add("Все поставщики");
        cmbFilter.getItems().addAll(suppliers);
        cmbFilter.getSelectionModel().select(0);

        cmbSort.getItems().clear();
        cmbSort.getItems().addAll("Без сортировки", "По возрастанию", "По убыванию");
        cmbSort.getSelectionModel().select(0);
    }

    private void setupListeners() {
        txtSearch.textProperty().addListener((obs, old, val) -> applyFilters());
        cmbSort.setOnAction(e -> applyFilters());
        cmbFilter.setOnAction(e -> applyFilters());
    }

    private void loadProducts() {
        allProducts.setAll(productDAO.getAll());
        applyFilters();
    }

    private void applyFilters() {
        String search = txtSearch.getText().toLowerCase().trim();
        String selectedSupplier = cmbFilter.getSelectionModel().getSelectedItem();
        String sortMode = cmbSort.getSelectionModel().getSelectedItem();

        List<Product> filtered = allProducts.stream()
                .filter(p -> search.isEmpty()
                        || p.getName().toLowerCase().contains(search)
                        || (p.getDescription() != null && p.getDescription().toLowerCase().contains(search))
                        || (p.getManufacturer() != null && p.getManufacturer().toLowerCase().contains(search))
                        || (p.getSupplier() != null && p.getSupplier().toLowerCase().contains(search))
                        || (p.getCategory() != null && p.getCategory().toLowerCase().contains(search)))
                .filter(p -> selectedSupplier == null || selectedSupplier.equals("Все поставщики")
                        || (p.getSupplier() != null && p.getSupplier().equals(selectedSupplier)))
                .collect(Collectors.toList());

        if (sortMode != null) {
            switch (sortMode) {
                case "По возрастанию":
                    filtered.sort(Comparator.comparingInt(Product::getQuantity));
                    break;
                case "По убыванию":
                    filtered.sort(Comparator.comparingInt(Product::getQuantity).reversed());
                    break;
            }
        }

        productTable.setItems(FXCollections.observableArrayList(filtered));
    }

    @FXML
    private void onSortChanged() {
        applyFilters();
    }

    @FXML
    private void onFilterChanged() {
        applyFilters();
    }

    @FXML
    private void onAddProduct() {
        if (!UserSession.getInstance().isAdmin()) return;
        if (editWindowOpened) {
            AlertUtil.warning("Окно редактирования уже открыто");
            return;
        }
        editWindowOpened = true;
        openEditProduct(null);
    }

    @FXML
    private void onDeleteProduct() {
        if (!UserSession.getInstance().isAdmin()) return;
        Product selected = productTable.getSelectionModel().getSelectedItem();
        if (selected == null) {
            AlertUtil.warning("Выберите товар для удаления");
            return;
        }
        if (productDAO.isInOrder(selected.getId())) {
            AlertUtil.warning("Товар \"" + selected.getName() + "\" присутствует в заказе и не может быть удален");
            return;
        }
        if (AlertUtil.confirm("Удалить товар \"" + selected.getName() + "\"?")) {
            productDAO.delete(selected.getId());
            AlertUtil.info("Товар успешно удален");
            loadProducts();
        }
    }

    private void openEditProduct(Product product) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/org/example/demo_ex/view/ProductEdit.fxml"));
            Parent root = loader.load();
            ProductEditController controller = loader.getController();
            controller.setProduct(product);
            controller.setOnCloseCallback(() -> {
                editWindowOpened = false;
                loadProducts();
            });

            Stage stage = new Stage();
            stage.setTitle(product == null ? "Добавление товара" : "Редактирование товара");
            stage.setScene(new Scene(root));
            stage.setOnHidden(e -> {
                editWindowOpened = false;
                loadProducts();
            });
            stage.show();
        } catch (Exception e) {
            editWindowOpened = false;
            e.printStackTrace();
        }
    }

    @FXML
    private void onOrders() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/org/example/demo_ex/view/OrderList.fxml"));
            Parent root = loader.load();
            Stage stage = (Stage) btnOrders.getScene().getWindow();
            stage.setTitle("Список заказов");
            stage.setScene(new Scene(root));
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void onLogout() {
        UserSession.getInstance().logout();
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/org/example/demo_ex/view/Login.fxml"));
            Parent root = loader.load();
            Stage stage = (Stage) btnLogout.getScene().getWindow();
            stage.setTitle("Авторизация");
            stage.setScene(new Scene(root));
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
