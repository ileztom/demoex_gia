package org.example.demo_ex.controller;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.text.Text;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import org.example.demo_ex.dao.ProductDAO;
import org.example.demo_ex.model.Product;
import org.example.demo_ex.util.AlertUtil;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;

public class ProductEditController {

    @FXML private Text titleText;
    @FXML private ImageView imageView;
    @FXML private TextField txtId, txtName, txtSupplier, txtPrice, txtUnit, txtQuantity, txtDiscount;
    @FXML private ComboBox<String> cmbCategory, cmbManufacturer;
    @FXML private TextArea txtDescription;
    @FXML private Button btnLoadImage;

    private ProductDAO productDAO = new ProductDAO();
    private Product currentProduct;
    private String selectedImagePath;
    private Runnable onCloseCallback;

    private static final int MAX_WIDTH = 300;
    private static final int MAX_HEIGHT = 200;

    @FXML
    private void initialize() {
        cmbCategory.getItems().addAll("Кроссовки", "Ботинки", "Туфли", "Сапоги", "Кеды", "Сандалии", "Кеди", "Лоферы", "Мокасины");
        cmbManufacturer.getItems().addAll("Nike", "Adidas", "Puma", "Reebok", "Timberland", "Baldinini", "Ralf Ringer", "Converse", "Ecco");
        loadDefaultImage();
    }

    public void setProduct(Product product) {
        this.currentProduct = product;
        if (product == null) {
            titleText.setText("Добавление товара");
            txtId.setText("(авто)");
        } else {
            titleText.setText("Редактирование товара");
            txtId.setText(String.valueOf(product.getId()));
            txtName.setText(product.getName());
            cmbCategory.setValue(product.getCategory());
            txtDescription.setText(product.getDescription());
            cmbManufacturer.setValue(product.getManufacturer());
            txtSupplier.setText(product.getSupplier());
            txtPrice.setText(String.valueOf(product.getPrice()));
            txtUnit.setText(product.getUnit());
            txtQuantity.setText(String.valueOf(product.getQuantity()));
            txtDiscount.setText(String.valueOf(product.getDiscount()));
            selectedImagePath = product.getImagePath();
            loadImage(selectedImagePath);
        }
    }

    public void setOnCloseCallback(Runnable callback) {
        this.onCloseCallback = callback;
    }

    @FXML
    private void onLoadImage() {
        FileChooser chooser = new FileChooser();
        chooser.getExtensionFilters().add(
                new FileChooser.ExtensionFilter("Изображения", "*.png", "*.jpg", "*.jpeg", "*.bmp", "*.gif"));
        File file = chooser.showOpenDialog(btnLoadImage.getScene().getWindow());
        if (file == null) return;

        try {
            BufferedImage buf = ImageIO.read(file);
            if (buf == null) {
                AlertUtil.error("Не удалось прочитать изображение");
                return;
            }
            if (buf.getWidth() > MAX_WIDTH || buf.getHeight() > MAX_HEIGHT) {
                AlertUtil.error("Размер изображения не должен превышать " + MAX_WIDTH + "x" + MAX_HEIGHT + " пикселей");
                return;
            }

            File destDir = new File("products_images");
            if (!destDir.exists()) destDir.mkdirs();

            String ext = file.getName().substring(file.getName().lastIndexOf('.'));
            String destName = System.currentTimeMillis() + ext;
            File dest = new File(destDir, destName);

            Files.copy(file.toPath(), dest.toPath(), StandardCopyOption.REPLACE_EXISTING);

            if (selectedImagePath != null && !selectedImagePath.isEmpty() && currentProduct != null) {
                File oldFile = new File(selectedImagePath);
                if (oldFile.exists()) {
                    oldFile.delete();
                }
            }

            selectedImagePath = dest.getAbsolutePath();
            loadImage(selectedImagePath);
        } catch (IOException e) {
            AlertUtil.error("Ошибка загрузки изображения: " + e.getMessage());
        }
    }

    @FXML
    private void onSave() {
        try {
            String name = txtName.getText().trim();
            if (name.isEmpty()) {
                AlertUtil.error("Введите наименование товара");
                return;
            }

            double price;
            try {
                price = Double.parseDouble(txtPrice.getText().trim());
                if (price < 0) throw new NumberFormatException();
            } catch (NumberFormatException e) {
                AlertUtil.error("Цена должна быть положительным числом");
                return;
            }

            int quantity;
            try {
                quantity = Integer.parseInt(txtQuantity.getText().trim());
                if (quantity < 0) throw new NumberFormatException();
            } catch (NumberFormatException e) {
                AlertUtil.error("Количество должно быть неотрицательным целым числом");
                return;
            }

            double discount = 0;
            try {
                if (!txtDiscount.getText().trim().isEmpty()) {
                    discount = Double.parseDouble(txtDiscount.getText().trim());
                    if (discount < 0 || discount > 100) throw new NumberFormatException();
                }
            } catch (NumberFormatException e) {
                AlertUtil.error("Скидка должна быть числом от 0 до 100");
                return;
            }

            if (currentProduct == null) {
                currentProduct = new Product();
            }

            currentProduct.setName(name);
            currentProduct.setCategory(cmbCategory.getValue());
            currentProduct.setDescription(txtDescription.getText());
            currentProduct.setManufacturer(cmbManufacturer.getValue());
            currentProduct.setSupplier(txtSupplier.getText());
            currentProduct.setPrice(price);
            currentProduct.setUnit(txtUnit.getText().isEmpty() ? "шт" : txtUnit.getText());
            currentProduct.setQuantity(quantity);
            currentProduct.setDiscount(discount);
            currentProduct.setImagePath(selectedImagePath);

            if (txtId.getText().equals("(авто)")) {
                productDAO.insert(currentProduct);
                AlertUtil.info("Товар успешно добавлен");
            } else {
                productDAO.update(currentProduct);
                AlertUtil.info("Товар успешно обновлен");
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

    private void loadDefaultImage() {
        Image img = new Image(getClass().getResourceAsStream("/org/example/demo_ex/images/picture.png"), 200, 150, true, true);
        imageView.setImage(img);
    }

    private void loadImage(String path) {
        if (path == null || path.isEmpty()) {
            loadDefaultImage();
            return;
        }
        try {
            Image img = new Image("file:" + path, 200, 150, true, true);
            if (img.isError()) {
                loadDefaultImage();
            } else {
                imageView.setImage(img);
            }
        } catch (Exception e) {
            loadDefaultImage();
        }
    }
}
