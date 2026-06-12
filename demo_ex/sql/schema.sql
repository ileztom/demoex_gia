-- ============================================================
-- Полный скрипт инициализации БД для магазина обуви
-- СУБД: MySQL
-- Запустите этот скрипт в вашем MySQL перед запуском приложения
-- ============================================================

-- Создание базы данных
CREATE DATABASE IF NOT EXISTS shoes
    CHARACTER SET utf8mb4
    COLLATE utf8mb4_unicode_ci;

USE shoes;

-- -----------------------------------------------------------
-- Таблица пользователей
-- -----------------------------------------------------------
CREATE TABLE IF NOT EXISTS users (
    UserID INT AUTO_INCREMENT PRIMARY KEY,
    Login VARCHAR(50) NOT NULL UNIQUE,
    Password VARCHAR(100) NOT NULL,
    Role VARCHAR(20) NOT NULL DEFAULT 'Клиент',
    FullName VARCHAR(100) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- -----------------------------------------------------------
-- Таблица товаров
-- -----------------------------------------------------------
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
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- -----------------------------------------------------------
-- Таблица заказов
-- -----------------------------------------------------------
CREATE TABLE IF NOT EXISTS orders (
    OrderID INT AUTO_INCREMENT PRIMARY KEY,
    Article VARCHAR(100) NOT NULL,
    Status VARCHAR(50) NOT NULL DEFAULT 'Новый',
    PickupPoint VARCHAR(200),
    OrderDate DATE NOT NULL,
    ReceiveDate DATE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- -----------------------------------------------------------
-- Таблица связи заказов и товаров (многие ко многим)
-- -----------------------------------------------------------
CREATE TABLE IF NOT EXISTS order_product (
    OrderID INT NOT NULL,
    ProductID INT NOT NULL,
    PRIMARY KEY (OrderID, ProductID),
    FOREIGN KEY (OrderID) REFERENCES orders(OrderID) ON DELETE CASCADE,
    FOREIGN KEY (ProductID) REFERENCES products(ProductID)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- ============================================================
-- Начальные данные (seed)
-- ============================================================

-- Пользователи
INSERT INTO users (Login, Password, Role, FullName) VALUES
    ('admin',    'admin123',    'Администратор', 'Иванов Иван Иванович'),
    ('manager',  'manager123',  'Менеджер',       'Петров Петр Петрович'),
    ('client',   'client123',   'Клиент',         'Сидорова Анна Сергеевна');

-- Товары
INSERT INTO products (Name, Category, Description, Manufacturer, Supplier, Price, Unit, Quantity, Discount, ImagePath) VALUES
    ('Кроссовки Nike Air Max', 'Кроссовки', 'Удобные кроссовки для бега',               'Nike',       'СпортМастер', 8500.00,  'шт', 50,  10.00, NULL),
    ('Ботинки Timberland',     'Ботинки',   'Классические ботинки из кожи',             'Timberland', 'БотинКо',     12000.00, 'шт', 30,  20.00, NULL),
    ('Туфли мужские',          'Туфли',     'Классические туфли',                       'Baldinini',  'МодаОбувь',   9500.00,  'шт', 0,   15.00, NULL),
    ('Сапоги женские',         'Сапоги',    'Зимние сапоги на меху',                    'Ralf Ringer','СтильОбувь',  14500.00, 'шт', 25,  5.00,  NULL),
    ('Кеды Converse',          'Кеды',      'Легкие кеды на каждый день',               'Converse',   'СпортМастер', 5500.00,  'шт', 100, 30.00, NULL),
    ('Сандалии',               'Сандалии',  'Летние сандалии из кожи',                  'Ecco',       'ОбувнойРай',  3200.00,  'шт', 15,  0.00,  NULL);

-- Заказы
INSERT INTO orders (Article, Status, PickupPoint, OrderDate, ReceiveDate) VALUES
    ('Заказ-001', 'Новый',        'ул. Ленина, 15',   '2026-06-01', NULL),
    ('Заказ-002', 'Подтвержден',  'пр. Мира, 42',     '2026-06-03', NULL),
    ('Заказ-003', 'Получен',      'ул. Советская, 7', '2026-05-20', '2026-05-25');

-- Состав заказов
INSERT INTO order_product (OrderID, ProductID) VALUES
    (1, 1),
    (1, 5),
    (2, 2),
    (3, 4),
    (3, 6);
