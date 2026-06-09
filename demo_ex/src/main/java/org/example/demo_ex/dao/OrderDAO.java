package org.example.demo_ex.dao;

import org.example.demo_ex.model.Order;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class OrderDAO {

    public List<Order> getAll() {
        List<Order> list = new ArrayList<>();
        String sql = "SELECT * FROM orders ORDER BY OrderID";
        try (Connection conn = DBConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                list.add(mapOrder(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    public Order getById(int id) {
        String sql = "SELECT * FROM orders WHERE OrderID=?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return mapOrder(rs);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public void insert(Order order) {
        String sql = "INSERT INTO orders (Article, Status, PickupPoint, OrderDate, ReceiveDate) VALUES (?, ?, ?, ?, ?)";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, order.getArticle());
            ps.setString(2, order.getStatus());
            ps.setString(3, order.getPickupPoint());
            ps.setDate(4, order.getOrderDate() != null ? Date.valueOf(order.getOrderDate()) : null);
            ps.setDate(5, order.getReceiveDate() != null ? Date.valueOf(order.getReceiveDate()) : null);
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void update(Order order) {
        String sql = "UPDATE orders SET Article=?, Status=?, PickupPoint=?, OrderDate=?, ReceiveDate=? WHERE OrderID=?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, order.getArticle());
            ps.setString(2, order.getStatus());
            ps.setString(3, order.getPickupPoint());
            ps.setDate(4, order.getOrderDate() != null ? Date.valueOf(order.getOrderDate()) : null);
            ps.setDate(5, order.getReceiveDate() != null ? Date.valueOf(order.getReceiveDate()) : null);
            ps.setInt(6, order.getOrderId());
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void delete(int id) {
        String sql = "DELETE FROM orders WHERE OrderID=?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private Order mapOrder(ResultSet rs) throws SQLException {
        Order o = new Order();
        o.setOrderId(rs.getInt("OrderID"));
        o.setArticle(rs.getString("Article"));
        o.setStatus(rs.getString("Status"));
        o.setPickupPoint(rs.getString("PickupPoint"));
        Date od = rs.getDate("OrderDate");
        if (od != null) o.setOrderDate(od.toLocalDate());
        Date rd = rs.getDate("ReceiveDate");
        if (rd != null) o.setReceiveDate(rd.toLocalDate());
        return o;
    }
}
