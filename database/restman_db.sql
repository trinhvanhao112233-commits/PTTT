-- RestMan - De 20
-- Script nay khoi tao lai cac bang trong rieng schema restman_db.
-- Khong chay tren schema dang chua du lieu can giu lai.

CREATE DATABASE IF NOT EXISTS restman_db
    CHARACTER SET utf8mb4
    COLLATE utf8mb4_0900_ai_ci;
USE restman_db;

SET FOREIGN_KEY_CHECKS = 0;
DROP TABLE IF EXISTS ingredient_receipt_items;
DROP TABLE IF EXISTS ingredient_receipts;
DROP TABLE IF EXISTS suppliers;
DROP TABLE IF EXISTS ingredients;
DROP TABLE IF EXISTS online_order_items;
DROP TABLE IF EXISTS online_orders;
DROP TABLE IF EXISTS bill_items;
DROP TABLE IF EXISTS bills;
DROP TABLE IF EXISTS combo_items;
DROP TABLE IF EXISTS combos;
DROP TABLE IF EXISTS dishes;
DROP TABLE IF EXISTS table_visits;
DROP TABLE IF EXISTS table_reservations;
DROP TABLE IF EXISTS dining_tables;
DROP TABLE IF EXISTS membership_cards;
DROP TABLE IF EXISTS warehouse_staff;
DROP TABLE IF EXISTS managers;
DROP TABLE IF EXISTS sales_staff;
DROP TABLE IF EXISTS customers;
DROP TABLE IF EXISTS staff;
DROP TABLE IF EXISTS users;
SET FOREIGN_KEY_CHECKS = 1;

CREATE TABLE users (
    id INT NOT NULL AUTO_INCREMENT,
    full_name VARCHAR(150) NOT NULL,
    username VARCHAR(50) NOT NULL,
    password_hash VARCHAR(255) NOT NULL,
    phone VARCHAR(20) NOT NULL,
    email VARCHAR(150) NOT NULL,
    PRIMARY KEY (id),
    UNIQUE KEY uq_users_username (username)
) ENGINE=InnoDB;

CREATE TABLE staff (
    user_id INT NOT NULL,
    staff_code VARCHAR(20) NOT NULL,
    PRIMARY KEY (user_id),
    CONSTRAINT fk_staff_user
        FOREIGN KEY (user_id) REFERENCES users (id)
        ON UPDATE CASCADE ON DELETE RESTRICT
) ENGINE=InnoDB;

CREATE TABLE customers (
    user_id INT NOT NULL,
    customer_code VARCHAR(20) NOT NULL,
    PRIMARY KEY (user_id),
    CONSTRAINT fk_customers_user
        FOREIGN KEY (user_id) REFERENCES users (id)
        ON UPDATE CASCADE ON DELETE RESTRICT
) ENGINE=InnoDB;

CREATE TABLE warehouse_staff (
    staff_id INT NOT NULL,
    PRIMARY KEY (staff_id),
    CONSTRAINT fk_warehouse_staff_staff
        FOREIGN KEY (staff_id) REFERENCES staff (user_id)
        ON UPDATE CASCADE ON DELETE RESTRICT
) ENGINE=InnoDB;

CREATE TABLE managers (
    staff_id INT NOT NULL,
    PRIMARY KEY (staff_id),
    CONSTRAINT fk_managers_staff
        FOREIGN KEY (staff_id) REFERENCES staff (user_id)
        ON UPDATE CASCADE ON DELETE RESTRICT
) ENGINE=InnoDB;

CREATE TABLE sales_staff (
    staff_id INT NOT NULL,
    PRIMARY KEY (staff_id),
    CONSTRAINT fk_sales_staff_staff
        FOREIGN KEY (staff_id) REFERENCES staff (user_id)
        ON UPDATE CASCADE ON DELETE RESTRICT
) ENGINE=InnoDB;

CREATE TABLE membership_cards (
    card_number VARCHAR(30) NOT NULL,
    issued_date DATE NOT NULL,
    customersuser_id INT NOT NULL,
    sales_staffstaff_id INT NOT NULL,
    PRIMARY KEY (card_number),
    UNIQUE KEY uq_membership_cards_customer (customersuser_id),
    CONSTRAINT fk_membership_cards_customer
        FOREIGN KEY (customersuser_id) REFERENCES customers (user_id)
        ON UPDATE CASCADE ON DELETE RESTRICT,
    CONSTRAINT fk_membership_cards_sales_staff
        FOREIGN KEY (sales_staffstaff_id) REFERENCES sales_staff (staff_id)
        ON UPDATE CASCADE ON DELETE RESTRICT
) ENGINE=InnoDB;

CREATE TABLE dining_tables (
    id INT NOT NULL AUTO_INCREMENT,
    table_code VARCHAR(20) NOT NULL,
    area VARCHAR(100) NOT NULL,
    capacity INT NOT NULL,
    status VARCHAR(30) NOT NULL,
    PRIMARY KEY (id)
) ENGINE=InnoDB;

CREATE TABLE table_reservations (
    id INT NOT NULL AUTO_INCREMENT,
    expected_arrival_time TIMESTAMP NOT NULL,
    guest_count INT NOT NULL,
    status VARCHAR(30) NOT NULL,
    customersuser_id INT NOT NULL,
    sales_staffstaff_id INT NULL,
    PRIMARY KEY (id),
    KEY idx_table_reservations_customer (customersuser_id),
    CONSTRAINT fk_table_reservations_customer
        FOREIGN KEY (customersuser_id) REFERENCES customers (user_id)
        ON UPDATE CASCADE ON DELETE RESTRICT,
    CONSTRAINT fk_table_reservations_sales_staff
        FOREIGN KEY (sales_staffstaff_id) REFERENCES sales_staff (staff_id)
        ON UPDATE CASCADE ON DELETE SET NULL
) ENGINE=InnoDB;

CREATE TABLE table_visits (
    id INT NOT NULL AUTO_INCREMENT,
    arrival_time TIMESTAMP NOT NULL,
    guest_count INT NOT NULL,
    status VARCHAR(30) NOT NULL,
    dining_tablesid INT NOT NULL,
    table_reservationsid INT NULL,
    PRIMARY KEY (id),
    UNIQUE KEY uq_table_visits_reservation (table_reservationsid),
    KEY idx_table_visits_table (dining_tablesid),
    CONSTRAINT fk_table_visits_table
        FOREIGN KEY (dining_tablesid) REFERENCES dining_tables (id)
        ON UPDATE CASCADE ON DELETE RESTRICT,
    CONSTRAINT fk_table_visits_reservation
        FOREIGN KEY (table_reservationsid) REFERENCES table_reservations (id)
        ON UPDATE CASCADE ON DELETE SET NULL
) ENGINE=InnoDB;

CREATE TABLE dishes (
    id INT NOT NULL AUTO_INCREMENT,
    dish_code VARCHAR(20) NOT NULL,
    name VARCHAR(150) NOT NULL,
    category VARCHAR(100) NOT NULL,
    unit VARCHAR(30) NOT NULL,
    sale_price DECIMAL(15, 2) NOT NULL,
    serving_status VARCHAR(30) NOT NULL,
    description VARCHAR(500) NULL,
    PRIMARY KEY (id),
    UNIQUE KEY uq_dishes_dish_code (dish_code)
) ENGINE=InnoDB;

CREATE TABLE combos (
    dish_id INT NOT NULL,
    PRIMARY KEY (dish_id),
    CONSTRAINT fk_combos_dish
        FOREIGN KEY (dish_id) REFERENCES dishes (id)
        ON UPDATE CASCADE ON DELETE CASCADE
) ENGINE=InnoDB;

CREATE TABLE combo_items (
    id INT NOT NULL AUTO_INCREMENT,
    quantity INT NOT NULL,
    combosdish_id INT NOT NULL,
    dishesid INT NOT NULL,
    PRIMARY KEY (id),
    KEY idx_combo_items_combo (combosdish_id),
    KEY idx_combo_items_dish (dishesid),
    CONSTRAINT fk_combo_items_combo
        FOREIGN KEY (combosdish_id) REFERENCES combos (dish_id)
        ON UPDATE CASCADE ON DELETE CASCADE,
    CONSTRAINT fk_combo_items_dish
        FOREIGN KEY (dishesid) REFERENCES dishes (id)
        ON UPDATE CASCADE ON DELETE RESTRICT
) ENGINE=InnoDB;

CREATE TABLE bills (
    id INT NOT NULL AUTO_INCREMENT,
    confirmed_at TIMESTAMP NULL,
    status VARCHAR(30) NOT NULL,
    table_visitsid INT NOT NULL,
    sales_staffstaff_id INT NULL,
    PRIMARY KEY (id),
    UNIQUE KEY uq_bills_table_visit (table_visitsid),
    CONSTRAINT fk_bills_table_visit
        FOREIGN KEY (table_visitsid) REFERENCES table_visits (id)
        ON UPDATE CASCADE ON DELETE RESTRICT,
    CONSTRAINT fk_bills_sales_staff
        FOREIGN KEY (sales_staffstaff_id) REFERENCES sales_staff (staff_id)
        ON UPDATE CASCADE ON DELETE SET NULL
) ENGINE=InnoDB;

CREATE TABLE bill_items (
    id INT NOT NULL AUTO_INCREMENT,
    quantity INT NOT NULL,
    unit_price DECIMAL(15, 2) NOT NULL,
    billsid INT NOT NULL,
    dishesid INT NOT NULL,
    PRIMARY KEY (id),
    KEY idx_bill_items_bill (billsid),
    KEY idx_bill_items_dish (dishesid),
    CONSTRAINT fk_bill_items_bill
        FOREIGN KEY (billsid) REFERENCES bills (id)
        ON UPDATE CASCADE ON DELETE CASCADE,
    CONSTRAINT fk_bill_items_dish
        FOREIGN KEY (dishesid) REFERENCES dishes (id)
        ON UPDATE CASCADE ON DELETE RESTRICT
) ENGINE=InnoDB;

CREATE TABLE online_orders (
    id INT NOT NULL AUTO_INCREMENT,
    created_at TIMESTAMP NOT NULL,
    status VARCHAR(30) NOT NULL,
    customersuser_id INT NOT NULL,
    sales_staffstaff_id INT NULL,
    PRIMARY KEY (id),
    KEY idx_online_orders_customer (customersuser_id),
    CONSTRAINT fk_online_orders_customer
        FOREIGN KEY (customersuser_id) REFERENCES customers (user_id)
        ON UPDATE CASCADE ON DELETE RESTRICT,
    CONSTRAINT fk_online_orders_sales_staff
        FOREIGN KEY (sales_staffstaff_id) REFERENCES sales_staff (staff_id)
        ON UPDATE CASCADE ON DELETE SET NULL
) ENGINE=InnoDB;

CREATE TABLE online_order_items (
    id INT NOT NULL AUTO_INCREMENT,
    quantity INT NOT NULL,
    unit_price DECIMAL(15, 2) NOT NULL,
    online_ordersid INT NOT NULL,
    dishesid INT NOT NULL,
    PRIMARY KEY (id),
    KEY idx_online_order_items_order (online_ordersid),
    KEY idx_online_order_items_dish (dishesid),
    CONSTRAINT fk_online_order_items_order
        FOREIGN KEY (online_ordersid) REFERENCES online_orders (id)
        ON UPDATE CASCADE ON DELETE CASCADE,
    CONSTRAINT fk_online_order_items_dish
        FOREIGN KEY (dishesid) REFERENCES dishes (id)
        ON UPDATE CASCADE ON DELETE RESTRICT
) ENGINE=InnoDB;

CREATE TABLE ingredients (
    id INT NOT NULL AUTO_INCREMENT,
    ingredient_code VARCHAR(20) NOT NULL,
    name VARCHAR(150) NOT NULL,
    unit VARCHAR(30) NOT NULL,
    PRIMARY KEY (id)
) ENGINE=InnoDB;

CREATE TABLE suppliers (
    id INT NOT NULL AUTO_INCREMENT,
    name VARCHAR(150) NOT NULL,
    phone VARCHAR(20) NOT NULL,
    address VARCHAR(255) NOT NULL,
    PRIMARY KEY (id)
) ENGINE=InnoDB;

CREATE TABLE ingredient_receipts (
    id INT NOT NULL AUTO_INCREMENT,
    received_at TIMESTAMP NOT NULL,
    suppliersid INT NOT NULL,
    warehouse_staffstaff_id INT NOT NULL,
    PRIMARY KEY (id),
    KEY idx_ingredient_receipts_supplier (suppliersid),
    KEY idx_ingredient_receipts_staff (warehouse_staffstaff_id),
    CONSTRAINT fk_ingredient_receipts_supplier
        FOREIGN KEY (suppliersid) REFERENCES suppliers (id)
        ON UPDATE CASCADE ON DELETE RESTRICT,
    CONSTRAINT fk_ingredient_receipts_warehouse_staff
        FOREIGN KEY (warehouse_staffstaff_id) REFERENCES warehouse_staff (staff_id)
        ON UPDATE CASCADE ON DELETE RESTRICT
) ENGINE=InnoDB;

CREATE TABLE ingredient_receipt_items (
    id INT NOT NULL AUTO_INCREMENT,
    quantity DECIMAL(12, 3) NOT NULL,
    unit_price DECIMAL(15, 2) NOT NULL,
    ingredient_receiptsid INT NOT NULL,
    ingredientsid INT NOT NULL,
    PRIMARY KEY (id),
    KEY idx_ingredient_receipt_items_receipt (ingredient_receiptsid),
    KEY idx_ingredient_receipt_items_ingredient (ingredientsid),
    CONSTRAINT fk_ingredient_receipt_items_receipt
        FOREIGN KEY (ingredient_receiptsid) REFERENCES ingredient_receipts (id)
        ON UPDATE CASCADE ON DELETE CASCADE,
    CONSTRAINT fk_ingredient_receipt_items_ingredient
        FOREIGN KEY (ingredientsid) REFERENCES ingredients (id)
        ON UPDATE CASCADE ON DELETE RESTRICT
) ENGINE=InnoDB;

-- Tai khoan mau. Mat khau duoc bam PBKDF2-HMAC-SHA256, 210000 vong.
INSERT INTO users (id, full_name, username, password_hash, phone, email) VALUES
    (1, 'Nguyễn Văn B', 'khachhang', 'pbkdf2_sha256$210000$cmVzdG1hbi1jdXN0b21lci12MQ==$EgwOm8HThd4GR1eRcuqZBbh6Uh05f+msIfEHocFZHV8=', '0901000001', 'khachhang@example.test'),
    (2, 'Trần Minh Nhân', 'nhanvien', 'pbkdf2_sha256$210000$cmVzdG1hbi1zdGFmZi12MQ==$72gp/sNBmebsPzEYFb54nMeX6KOBN3hWKHCZjzAnfI8=', '0901000002', 'nhanvien@example.test'),
    (3, 'Nguyễn Văn C', 'khachhang2', 'pbkdf2_sha256$210000$cmVzdG1hbi1jdXN0b21lci12MQ==$EgwOm8HThd4GR1eRcuqZBbh6Uh05f+msIfEHocFZHV8=', '0901000003', 'khachhang2@example.test'),
    (4, 'Khách Chưa Có Hóa Đơn', 'khachhang3', 'pbkdf2_sha256$210000$cmVzdG1hbi1jdXN0b21lci12MQ==$EgwOm8HThd4GR1eRcuqZBbh6Uh05f+msIfEHocFZHV8=', '0901000004', 'khachhang3@example.test'),
    (5, 'Khách Đã Thanh Toán', 'khachhang4', 'pbkdf2_sha256$210000$cmVzdG1hbi1jdXN0b21lci12MQ==$EgwOm8HThd4GR1eRcuqZBbh6Uh05f+msIfEHocFZHV8=', '0901000005', 'khachhang4@example.test'),
    (6, 'Lê Thị Kho', 'nhanvienkho', 'pbkdf2_sha256$210000$cmVzdG1hbi1zdGFmZi12MQ==$72gp/sNBmebsPzEYFb54nMeX6KOBN3hWKHCZjzAnfI8=', '0901000006', 'kho@example.test');

INSERT INTO staff (user_id, staff_code) VALUES (2, 'NVBH01'), (6, 'NVK01');
INSERT INTO sales_staff (staff_id) VALUES (2);
INSERT INTO warehouse_staff (staff_id) VALUES (6);
INSERT INTO customers (user_id, customer_code) VALUES
    (1, 'KH001'), (3, 'KH002'), (4, 'KH003'), (5, 'KH004');

INSERT INTO membership_cards (card_number, issued_date, customersuser_id, sales_staffstaff_id)
VALUES ('TV0001', '2026-11-01', 1, 2);

INSERT INTO dining_tables (id, table_code, area, capacity, status) VALUES
    (5, 'B05', 'Phòng riêng', 6, 'Đang phục vụ'),
    (6, 'B06', 'Sảnh chính', 4, 'Đang phục vụ'),
    (7, 'B07', 'Sảnh chính', 4, 'Đang phục vụ'),
    (8, 'B08', 'Ngoài trời', 4, 'Đang phục vụ');

INSERT INTO table_reservations
    (id, expected_arrival_time, guest_count, status, customersuser_id, sales_staffstaff_id) VALUES
    (10, '2026-11-20 19:00:00', 4, 'Đã xác nhận', 1, 2),
    (11, '2026-11-20 19:30:00', 2, 'Đã xác nhận', 3, 2),
    (12, '2026-11-20 20:00:00', 2, 'Đã xác nhận', 4, 2),
    (13, '2026-11-20 18:30:00', 3, 'Đã xác nhận', 5, 2);

INSERT INTO table_visits
    (id, arrival_time, guest_count, status, dining_tablesid, table_reservationsid) VALUES
    (20, '2026-11-20 19:15:00', 4, 'Đang phục vụ', 5, 10),
    (21, '2026-11-20 19:35:00', 2, 'Đang phục vụ', 6, 11),
    (22, '2026-11-20 20:05:00', 2, 'Đang phục vụ', 7, 12),
    (23, '2026-11-20 18:40:00', 3, 'Đã kết thúc', 8, 13);

INSERT INTO dishes
    (id, dish_code, name, category, unit, sale_price, serving_status, description) VALUES
    (1, 'CB01', 'Combo Gà quay gia đình', 'Combo gia đình', 'Suất', 250000.00, 'Đang phục vụ', 'Combo gà quay dùng cho gia đình.'),
    (2, 'GQ01', 'Gà quay nguyên con', 'Món chính', 'Con', 150000.00, 'Tạm hết', 'Gà quay nguyên con, da giòn, ăn kèm rau thơm.'),
    (3, 'GR02', 'Gà rán phần vừa', 'Món chính', 'Phần', 50000.00, 'Đang phục vụ', 'Gà rán giòn phần vừa.'),
    (4, 'RA01', 'Rau trộn theo mùa', 'Khai vị', 'Phần', 35000.00, 'Đang phục vụ', 'Rau tươi trộn theo mùa.');

INSERT INTO combos (dish_id) VALUES (1);
INSERT INTO combo_items (id, quantity, combosdish_id, dishesid) VALUES
    (1, 1, 1, 2),
    (2, 2, 1, 3),
    (3, 1, 1, 4);

INSERT INTO bills (id, confirmed_at, status, table_visitsid, sales_staffstaff_id) VALUES
    (101, NULL, 'Chưa thanh toán', 20, NULL),
    (102, NULL, 'Chưa thanh toán', 21, NULL),
    (103, '2026-11-20 19:30:00', 'Đã thanh toán', 23, 2);

INSERT INTO bill_items (id, quantity, unit_price, billsid, dishesid) VALUES
    (1001, 1, 150000.00, 101, 2),
    (1002, 1, 50000.00, 101, 3),
    (1003, 1, 50000.00, 103, 3);

INSERT INTO online_orders (id, created_at, status, customersuser_id, sales_staffstaff_id)
VALUES (201, '2026-11-19 10:00:00', 'Đã xác nhận', 1, 2);
INSERT INTO online_order_items (id, quantity, unit_price, online_ordersid, dishesid)
VALUES (2001, 1, 150000.00, 201, 2);

INSERT INTO ingredients (id, ingredient_code, name, unit) VALUES
    (1, 'NL01', 'Gà nguyên con', 'Con'),
    (2, 'NL02', 'Rau thơm', 'Kilôgam');
INSERT INTO suppliers (id, name, phone, address)
VALUES (1, 'Nhà cung cấp Thực phẩm An Tâm', '02873000001', 'Thành phố Hồ Chí Minh');
INSERT INTO ingredient_receipts (id, received_at, suppliersid, warehouse_staffstaff_id)
VALUES (301, '2026-11-18 08:00:00', 1, 6);
INSERT INTO ingredient_receipt_items
    (id, quantity, unit_price, ingredient_receiptsid, ingredientsid) VALUES
    (3001, 10.000, 80000.00, 301, 1),
    (3002, 5.000, 30000.00, 301, 2);

CREATE USER IF NOT EXISTS 'restman'@'127.0.0.1' IDENTIFIED BY 'restman123';
ALTER USER 'restman'@'127.0.0.1' IDENTIFIED BY 'restman123';
GRANT SELECT, INSERT, UPDATE, DELETE ON restman_db.* TO 'restman'@'127.0.0.1';
FLUSH PRIVILEGES;

