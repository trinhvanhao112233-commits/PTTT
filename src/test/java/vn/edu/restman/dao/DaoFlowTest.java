package vn.edu.restman.dao;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import vn.edu.restman.model.Bill4;
import vn.edu.restman.model.Dish4;
import vn.edu.restman.model.TableVisit4;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class DaoFlowTest {
    @BeforeEach
    void createDatabase() throws Exception {
        String url = "jdbc:h2:mem:restman_" + UUID.randomUUID() + ";MODE=MySQL;DATABASE_TO_LOWER=TRUE;DB_CLOSE_DELAY=-1";
        System.setProperty("restman.db.url", url);
        System.setProperty("restman.db.user", "sa");
        System.setProperty("restman.db.password", "test");
        System.setProperty("restman.db.driver", "org.h2.Driver");
        Class.forName("org.h2.Driver");
        try (Connection connection = DriverManager.getConnection(url, "sa", "test");
             Statement statement = connection.createStatement()) {
            statement.execute("""
                    CREATE TABLE users (
                        id INT PRIMARY KEY, full_name VARCHAR(150), username VARCHAR(50),
                        password_hash VARCHAR(255), phone VARCHAR(20), email VARCHAR(150)
                    )
                    """);
            statement.execute("CREATE TABLE staff (user_id INT PRIMARY KEY, staff_code VARCHAR(20))");
            statement.execute("CREATE TABLE sales_staff (staff_id INT PRIMARY KEY)");
            statement.execute("CREATE TABLE customers (user_id INT PRIMARY KEY, customer_code VARCHAR(20))");
            statement.execute("""
                    CREATE TABLE dishes (
                        id INT PRIMARY KEY, dish_code VARCHAR(20), name VARCHAR(150), category VARCHAR(100),
                        unit VARCHAR(30), sale_price DECIMAL(15,2), serving_status VARCHAR(30), description VARCHAR(500)
                    )
                    """);
            statement.execute("CREATE TABLE combos (dish_id INT PRIMARY KEY)");
            statement.execute("""
                    CREATE TABLE dining_tables (
                        id INT PRIMARY KEY, table_code VARCHAR(20), area VARCHAR(100), capacity INT, status VARCHAR(30)
                    )
                    """);
            statement.execute("""
                    CREATE TABLE table_reservations (
                        id INT PRIMARY KEY, expected_arrival_time TIMESTAMP, guest_count INT, status VARCHAR(30),
                        customersuser_id INT, sales_staffstaff_id INT
                    )
                    """);
            statement.execute("""
                    CREATE TABLE table_visits (
                        id INT PRIMARY KEY, arrival_time TIMESTAMP, guest_count INT, status VARCHAR(30),
                        dining_tablesid INT, table_reservationsid INT
                    )
                    """);
            statement.execute("""
                    CREATE TABLE bills (
                        id INT PRIMARY KEY, confirmed_at TIMESTAMP, status VARCHAR(30),
                        table_visitsid INT, sales_staffstaff_id INT
                    )
                    """);
            statement.execute("""
                    CREATE TABLE bill_items (
                        id INT PRIMARY KEY, quantity INT, unit_price DECIMAL(15,2), billsid INT, dishesid INT
                    )
                    """);

            statement.execute("INSERT INTO users VALUES (1, 'Nguyễn Văn B', 'khachhang', 'x', '0901', 'khach@example.com')");
            statement.execute("INSERT INTO users VALUES (2, 'Trần Minh An', 'nhanvien', 'x', '0902', 'staff@example.com')");
            statement.execute("INSERT INTO customers VALUES (1, 'KH01')");
            statement.execute("INSERT INTO staff VALUES (2, 'NV02')");
            statement.execute("INSERT INTO sales_staff VALUES (2)");

            statement.execute("INSERT INTO dishes VALUES (1, 'CB01', 'Combo Gà quay gia đình', 'Combo', 'Suất', 250000, 'Đang phục vụ', 'Combo gia đình')");
            statement.execute("INSERT INTO dishes VALUES (2, 'GQ01', 'Gà quay nguyên con', 'Món chính', 'Con', 150000, 'Tạm hết', 'Gà quay nguyên con, da giòn, ăn kèm rau thơm.')");
            statement.execute("INSERT INTO dishes VALUES (3, 'GR02', 'Gà rán phần vừa', 'Món chính', 'Phần', 50000, 'Đang phục vụ', 'Gà rán giòn')");
            statement.execute("INSERT INTO combos VALUES (1)");

            statement.execute("INSERT INTO dining_tables VALUES (5, 'B05', 'Phòng riêng', 6, 'Đang sử dụng')");
            statement.execute("INSERT INTO table_reservations VALUES (10, '2026-11-20 19:00:00', 4, 'Đã xác nhận', 1, 2)");
            statement.execute("INSERT INTO table_reservations VALUES (11, '2026-11-20 20:00:00', 2, 'Đã xác nhận', 1, 2)");
            statement.execute("INSERT INTO table_visits VALUES (20, '2026-11-20 19:15:00', 4, 'Đang phục vụ', 5, 10)");
            statement.execute("INSERT INTO table_visits VALUES (21, '2026-11-20 20:05:00', 2, 'Đang phục vụ', 5, 11)");
            statement.execute("INSERT INTO bills VALUES (101, NULL, 'Chưa thanh toán', 20, NULL)");
            statement.execute("INSERT INTO bills VALUES (102, NULL, 'Chưa thanh toán', 21, NULL)");
            statement.execute("INSERT INTO bill_items VALUES (201, 1, 150000, 101, 2)");
            statement.execute("INSERT INTO bill_items VALUES (202, 1, 50000, 101, 3)");
        }
    }

    @Test
    void searchesPartialDishNameAndDerivesDishType() {
        List<Dish4> dishes = new DishDAO4().searchDishesByName("Gà");
        assertEquals(List.of("CB01", "GQ01", "GR02"), dishes.stream().map(Dish4::getDishCode).toList());
        assertEquals("Combo", dishes.get(0).getDishType());

        Dish4 details = new DishDAO4().getDishDetails("GQ01");
        assertNotNull(details);
        assertEquals("Gà quay nguyên con", details.getName());
        assertEquals("Món lẻ", details.getDishType());
        assertEquals(new BigDecimal("150000.00"), details.getSalePrice());
    }

    @Test
    void findsReservedTableVisitsByPartialCustomerName() {
        List<TableVisit4> visits = new TableVisitDAO4().findTableVisitsByCustomerName("Văn B");
        assertEquals(2, visits.size());
        TableVisit4 visit20 = visits.stream().filter(visit -> visit.getId() == 20).findFirst().orElseThrow();
        assertEquals("Nguyễn Văn B", visit20.getCustomerName());
        assertEquals("B05", visit20.getTable().getTableCode());
        assertEquals("Phòng riêng", visit20.getTable().getArea());
        assertEquals("19:15", visit20.getArrivalTimeDisplay());
    }

    @Test
    void loadsBillUsingHistoricalItemPrices() {
        Bill4 bill = new BillDAO4().getBillByTableVisit(20);
        assertNotNull(bill);
        assertEquals(101, bill.getId());
        assertEquals(2, bill.getItems().size());
        assertEquals(new BigDecimal("200000.00"), bill.getTotalAmount());
        assertEquals("Nguyễn Văn B", bill.getCustomerName());
        assertEquals("B05", bill.getTableCode());
    }

    @Test
    void confirmsOnceAndRecordsStaffAndTime() {
        BillDAO4 dao = new BillDAO4();
        assertTrue(dao.confirmPayment(101, 2));

        Bill4 bill = dao.getBillByTableVisit(20);
        assertEquals("Đã thanh toán", bill.getStatus());
        assertNotNull(bill.getConfirmedAt());
        assertEquals(2, bill.getConfirmedBy().getId());

        PaymentException4 duplicate = assertThrows(PaymentException4.class, () -> dao.confirmPayment(101, 2));
        assertEquals(PaymentException4.Reason.ALREADY_CONFIRMED, duplicate.getReason());
    }

    @Test
    void rejectsBillWithoutItems() {
        PaymentException4 empty = assertThrows(PaymentException4.class, () -> new BillDAO4().confirmPayment(102, 2));
        assertEquals(PaymentException4.Reason.EMPTY_BILL, empty.getReason());
    }
}
