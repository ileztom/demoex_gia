package org.example.demo_ex.model;

public class Product {
    private int id;
    private String name;
    private String category;
    private String description;
    private String manufacturer;
    private String supplier;
    private double price;
    private String unit;
    private int quantity;
    private double discount;
    private String imagePath;

    public Product() {}

    public Product(int id, String name, String category, String description, String manufacturer,
                   String supplier, double price, String unit, int quantity, double discount, String imagePath) {
        this.id = id;
        this.name = name;
        this.category = category;
        this.description = description;
        this.manufacturer = manufacturer;
        this.supplier = supplier;
        this.price = price;
        this.unit = unit;
        this.quantity = quantity;
        this.discount = discount;
        this.imagePath = imagePath;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getCategory() { return category; }
    public void setCategory(String category) { this.category = category; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    public String getManufacturer() { return manufacturer; }
    public void setManufacturer(String manufacturer) { this.manufacturer = manufacturer; }
    public String getSupplier() { return supplier; }
    public void setSupplier(String supplier) { this.supplier = supplier; }
    public double getPrice() { return price; }
    public void setPrice(double price) { this.price = price; }
    public String getUnit() { return unit; }
    public void setUnit(String unit) { this.unit = unit; }
    public int getQuantity() { return quantity; }
    public void setQuantity(int quantity) { this.quantity = quantity; }
    public double getDiscount() { return discount; }
    public void setDiscount(double discount) { this.discount = discount; }
    public String getImagePath() { return imagePath; }
    public void setImagePath(String imagePath) { this.imagePath = imagePath; }

    public double getFinalPrice() {
        return price - price * discount / 100.0;
    }

    public boolean hasDiscount() {
        return discount > 0;
    }
}
