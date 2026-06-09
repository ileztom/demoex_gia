-- H2 schema for shoe store (auto-run at startup)

CREATE TABLE IF NOT EXISTS users (
    UserID INT AUTO_INCREMENT PRIMARY KEY,
    Login VARCHAR(50) NOT NULL UNIQUE,
    Password VARCHAR(100) NOT NULL,
    Role VARCHAR(20) NOT NULL DEFAULT 'Клиент',
    FullName VARCHAR(100) NOT NULL
);

CREATE TABLE IF NOT EXISTS products (
    ProductID INT AUTO_INCREMENT PRIMARY KEY,
    Name VARCHAR(200) NOT NULL,
    Category VARCHAR(100),
    Description TEXT,
    Manufacturer VARCHAR(100),
    Supplier VARCHAR(100),
    Price DECIMAL(10, 2) NOT NULL DEFAULT 0.00,
    Unit VARCHAR(20) DEFAULT 'шт',
    Quantity INT NOT NULL DEFAULT 0,
    Discount DECIMAL(5, 2) DEFAULT 0.00,
    ImagePath VARCHAR(500)
);

CREATE TABLE IF NOT EXISTS orders (
    OrderID INT AUTO_INCREMENT PRIMARY KEY,
    Article VARCHAR(100) NOT NULL,
    Status VARCHAR(50) NOT NULL DEFAULT 'Новый',
    PickupPoint VARCHAR(200),
    OrderDate DATE NOT NULL,
    ReceiveDate DATE
);

CREATE TABLE IF NOT EXISTS order_product (
    OrderID INT NOT NULL,
    ProductID INT NOT NULL,
    PRIMARY KEY (OrderID, ProductID),
    FOREIGN KEY (OrderID) REFERENCES orders(OrderID) ON DELETE CASCADE,
    FOREIGN KEY (ProductID) REFERENCES products(ProductID)
);

-- Seed data (merge to avoid duplicates on restart)
MERGE INTO users (UserID, Login, Password, Role, FullName) VALUES
(1, 'admin', 'admin123', 'Администратор', 'Иванов Иван Иванович'),
(2, 'manager', 'manager123', 'Менеджер', 'Петров Петр Петрович'),
(3, 'client', 'client123', 'Клиент', 'Сидорова Анна Сергеевна');

MERGE INTO products (ProductID, Name, Category, Description, Manufacturer, Supplier, Price, Unit, Quantity, Discount, ImagePath) VALUES
(1, 'Кроссовки Nike Air Max', 'Кроссовки', 'Удобные кроссовки для бега', 'Nike', 'СпортМастер', 8500.00, 'шт', 50, 10.00, NULL),
(2, 'Ботинки Timberland', 'Ботинки', 'Классические ботинки из кожи', 'Timberland', 'БотинКо', 12000.00, 'шт', 30, 20.00, NULL),
(3, 'Туфли мужские', 'Туфли', 'Классические туфли', 'Baldinini', 'МодаОбувь', 9500.00, 'шт', 0, 15.00, NULL),
(4, 'Сапоги женские', 'Сапоги', 'Зимние сапоги на меху', 'Ralf Ringer', 'СтильОбувь', 14500.00, 'шт', 25, 5.00, NULL),
(5, 'Кеды Converse', 'Кеды', 'Легкие кеды на каждый день', 'Converse', 'СпортМастер', 5500.00, 'шт', 100, 30.00, NULL),
(6, 'Сандалии', 'Сандалии', 'Летние сандалии из кожи', 'Ecco', 'ОбувнойРай', 3200.00, 'шт', 15, 0.00, NULL);

MERGE INTO orders (OrderID, Article, Status, PickupPoint, OrderDate, ReceiveDate) VALUES
(1, 'Заказ-001', 'Новый', 'ул. Ленина, 15', '2026-06-01', NULL),
(2, 'Заказ-002', 'Подтвержден', 'пр. Мира, 42', '2026-06-03', NULL),
(3, 'Заказ-003', 'Получен', 'ул. Советская, 7', '2026-05-20', '2026-05-25');

MERGE INTO order_product (OrderID, ProductID) VALUES
(1, 1), (1, 5), (2, 2), (3, 4), (3, 6);
