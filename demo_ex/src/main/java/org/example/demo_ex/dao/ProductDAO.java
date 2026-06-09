package org.example.demo_ex.dao;

import org.example.demo_ex.model.Product;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ProductDAO {

    public List<Product> getAll() {
        List<Product> list = new ArrayList<>();
        String sql = "SELECT * FROM products ORDER BY ProductID";
        try (Connection conn = DBConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                list.add(mapProduct(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    public Product getById(int id) {
        String sql = "SELECT * FROM products WHERE ProductID=?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return mapProduct(rs);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public int getMaxId() {
        String sql = "SELECT MAX(ProductID) FROM products";
        try (Connection conn = DBConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            if (rs.next()) {
                return rs.getInt(1);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }

    public void insert(Product product) {
        String sql = "INSERT INTO products (Name, Category, Description, Manufacturer, Supplier, Price, Unit, Quantity, Discount, ImagePath) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, product.getName());
            ps.setString(2, product.getCategory());
            ps.setString(3, product.getDescription());
            ps.setString(4, product.getManufacturer());
            ps.setString(5, product.getSupplier());
            ps.setDouble(6, product.getPrice());
            ps.setString(7, product.getUnit());
            ps.setInt(8, product.getQuantity());
            ps.setDouble(9, product.getDiscount());
            ps.setString(10, product.getImagePath());
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void update(Product product) {
        String sql = "UPDATE products SET Name=?, Category=?, Description=?, Manufacturer=?, Supplier=?, " +
                "Price=?, Unit=?, Quantity=?, Discount=?, ImagePath=? WHERE ProductID=?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, product.getName());
            ps.setString(2, product.getCategory());
            ps.setString(3, product.getDescription());
            ps.setString(4, product.getManufacturer());
            ps.setString(5, product.getSupplier());
            ps.setDouble(6, product.getPrice());
            ps.setString(7, product.getUnit());
            ps.setInt(8, product.getQuantity());
            ps.setDouble(9, product.getDiscount());
            ps.setString(10, product.getImagePath());
            ps.setInt(11, product.getId());
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void delete(int id) {
        String sql = "DELETE FROM products WHERE ProductID=?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public boolean isInOrder(int productId) {
        String sql = "SELECT COUNT(*) FROM order_product WHERE ProductID=?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, productId);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getInt(1) > 0;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public List<String> getAllSuppliers() {
        List<String> suppliers = new ArrayList<>();
        String sql = "SELECT DISTINCT Supplier FROM products WHERE Supplier IS NOT NULL ORDER BY Supplier";
        try (Connection conn = DBConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                suppliers.add(rs.getString("Supplier"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return suppliers;
    }

    private Product mapProduct(ResultSet rs) throws SQLException {
        Product p = new Product();
        p.setId(rs.getInt("ProductID"));
        p.setName(rs.getString("Name"));
        p.setCategory(rs.getString("Category"));
        p.setDescription(rs.getString("Description"));
        p.setManufacturer(rs.getString("Manufacturer"));
        p.setSupplier(rs.getString("Supplier"));
        p.setPrice(rs.getDouble("Price"));
        p.setUnit(rs.getString("Unit"));
        p.setQuantity(rs.getInt("Quantity"));
        p.setDiscount(rs.getDouble("Discount"));
        p.setImagePath(rs.getString("ImagePath"));
        return p;
    }
}
