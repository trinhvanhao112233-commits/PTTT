package vn.edu.restman.dao;

import vn.edu.restman.model.Bill4;
import vn.edu.restman.model.BillItem4;
import vn.edu.restman.model.Customer4;
import vn.edu.restman.model.DiningTable4;
import vn.edu.restman.model.Dish4;
import vn.edu.restman.model.SalesStaff4;
import vn.edu.restman.model.TableReservation4;
import vn.edu.restman.model.TableVisit4;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class BillDAO4 extends DAO {
    public static final String STATUS_UNPAID = "Chưa thanh toán";
    public static final String STATUS_PAID = "Đã thanh toán";

    public Bill4 getBillByTableVisit(int visitId) {
        if (visitId <= 0) return null;
        String billSql = """
                SELECT b.id AS bill_id, b.confirmed_at, b.status AS bill_status,
                       tv.id AS visit_id, tv.arrival_time, tv.guest_count AS visit_guest_count,
                       tv.status AS visit_status,
                       dt.id AS table_id, dt.table_code, dt.area, dt.capacity, dt.status AS table_status,
                       tr.id AS reservation_id, tr.expected_arrival_time,
                       tr.guest_count AS reservation_guest_count, tr.status AS reservation_status,
                       cu.id AS customer_id, cu.full_name AS customer_name, cu.username AS customer_username,
                       cu.password_hash AS customer_password_hash, cu.phone AS customer_phone,
                       cu.email AS customer_email, c.customer_code,
                       su.id AS confirmer_id, su.full_name AS confirmer_name, su.username AS confirmer_username,
                       su.password_hash AS confirmer_password_hash, su.phone AS confirmer_phone,
                       su.email AS confirmer_email, st.staff_code AS confirmer_staff_code
                  FROM bills b
                  JOIN table_visits tv ON tv.id = b.table_visitsid
                  JOIN dining_tables dt ON dt.id = tv.dining_tablesid
                  LEFT JOIN table_reservations tr ON tr.id = tv.table_reservationsid
                  LEFT JOIN customers c ON c.user_id = tr.customersuser_id
                  LEFT JOIN users cu ON cu.id = c.user_id
                  LEFT JOIN sales_staff ss ON ss.staff_id = b.sales_staffstaff_id
                  LEFT JOIN staff st ON st.user_id = ss.staff_id
                  LEFT JOIN users su ON su.id = st.user_id
                 WHERE b.table_visitsid = ?
                """;
        try (Connection connection = openConnection();
             PreparedStatement billStatement = connection.prepareStatement(billSql)) {
            billStatement.setInt(1, visitId);
            Bill4 bill;
            try (ResultSet resultSet = billStatement.executeQuery()) {
                if (!resultSet.next()) return null;
                bill = mapBill(resultSet);
            }
            List<BillItem4> items = loadItems(connection, bill.getId());
            bill.setItems(items);
            bill.setTotalAmount(items.stream()
                    .map(BillItem4::getLineAmount)
                    .reduce(BigDecimal.ZERO, BigDecimal::add));
            return bill;
        } catch (SQLException exception) {
            throw new DataAccessException4("Khong the lay hoa don cua luot dung ban", exception);
        }
    }

    public boolean confirmPayment(int billId, int staffId) {
        if (billId <= 0) {
            throw new PaymentException4(PaymentException4.Reason.NOT_FOUND, "Không tìm thấy hóa đơn");
        }
        if (staffId <= 0) {
            throw new PaymentException4(PaymentException4.Reason.INVALID_STAFF, "Phiên đăng nhập không có nhân viên bán hàng hợp lệ");
        }
        Connection connection = null;
        try {
            connection = openConnection();
            connection.setAutoCommit(false);
            lockAndValidateBill(connection, billId);
            validateSalesStaff(connection, staffId);
            validateBillHasItems(connection, billId);

            String updateSql = """
                    UPDATE bills
                       SET status = ?, confirmed_at = CURRENT_TIMESTAMP, sales_staffstaff_id = ?
                     WHERE id = ? AND status = ? AND confirmed_at IS NULL
                    """;
            try (PreparedStatement statement = connection.prepareStatement(updateSql)) {
                statement.setString(1, STATUS_PAID);
                statement.setInt(2, staffId);
                statement.setInt(3, billId);
                statement.setString(4, STATUS_UNPAID);
                if (statement.executeUpdate() != 1) {
                    throw new PaymentException4(
                            PaymentException4.Reason.ALREADY_CONFIRMED,
                            "Hóa đơn đã được xác nhận trước đó"
                    );
                }
            }
            connection.commit();
            return true;
        } catch (PaymentException4 exception) {
            rollbackQuietly(connection);
            throw exception;
        } catch (SQLException exception) {
            rollbackQuietly(connection);
            throw new DataAccessException4("Khong the xac nhan thanh toan", exception);
        } finally {
            closeQuietly(connection);
        }
    }

    private void lockAndValidateBill(Connection connection, int billId) throws SQLException {
        String sql = "SELECT status, confirmed_at FROM bills WHERE id = ? FOR UPDATE";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, billId);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (!resultSet.next()) {
                    throw new PaymentException4(PaymentException4.Reason.NOT_FOUND, "Không tìm thấy hóa đơn");
                }
                String status = resultSet.getString("status");
                Timestamp confirmedAt = resultSet.getTimestamp("confirmed_at");
                if (confirmedAt != null || STATUS_PAID.equals(status)) {
                    throw new PaymentException4(
                            PaymentException4.Reason.ALREADY_CONFIRMED,
                            "Hóa đơn đã được xác nhận trước đó"
                    );
                }
                if (!STATUS_UNPAID.equals(status)) {
                    throw new PaymentException4(PaymentException4.Reason.NOT_OPEN, "Hóa đơn không ở trạng thái chờ thanh toán");
                }
            }
        }
    }

    private void validateSalesStaff(Connection connection, int staffId) throws SQLException {
        try (PreparedStatement statement = connection.prepareStatement("SELECT 1 FROM sales_staff WHERE staff_id = ?")) {
            statement.setInt(1, staffId);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (!resultSet.next()) {
                    throw new PaymentException4(
                            PaymentException4.Reason.INVALID_STAFF,
                            "Tài khoản hiện tại không phải nhân viên bán hàng"
                    );
                }
            }
        }
    }

    private void validateBillHasItems(Connection connection, int billId) throws SQLException {
        try (PreparedStatement statement = connection.prepareStatement("SELECT COUNT(*) FROM bill_items WHERE billsid = ?")) {
            statement.setInt(1, billId);
            try (ResultSet resultSet = statement.executeQuery()) {
                resultSet.next();
                if (resultSet.getInt(1) == 0) {
                    throw new PaymentException4(PaymentException4.Reason.EMPTY_BILL, "Chưa có món để thanh toán");
                }
            }
        }
    }

    private Bill4 mapBill(ResultSet resultSet) throws SQLException {
        DiningTable4 table = new DiningTable4(
                resultSet.getInt("table_id"), resultSet.getString("table_code"),
                resultSet.getString("area"), resultSet.getInt("capacity"), resultSet.getString("table_status")
        );
        TableReservation4 reservation = null;
        Integer reservationId = (Integer) resultSet.getObject("reservation_id");
        if (reservationId != null) {
            Customer4 customer = null;
            Integer customerId = (Integer) resultSet.getObject("customer_id");
            if (customerId != null) {
                customer = new Customer4(
                        customerId, resultSet.getString("customer_name"), resultSet.getString("customer_username"),
                        resultSet.getString("customer_password_hash"), resultSet.getString("customer_phone"),
                        resultSet.getString("customer_email"), resultSet.getString("customer_code")
                );
            }
            reservation = new TableReservation4(
                    reservationId,
                    TableVisitDAO4.toLocalDateTime(resultSet.getTimestamp("expected_arrival_time")),
                    resultSet.getInt("reservation_guest_count"), resultSet.getString("reservation_status"),
                    customer, null
            );
        }
        TableVisit4 visit = new TableVisit4(
                resultSet.getInt("visit_id"),
                TableVisitDAO4.toLocalDateTime(resultSet.getTimestamp("arrival_time")),
                resultSet.getInt("visit_guest_count"), resultSet.getString("visit_status"), table, reservation
        );
        SalesStaff4 confirmer = null;
        Integer confirmerId = (Integer) resultSet.getObject("confirmer_id");
        if (confirmerId != null) {
            confirmer = new SalesStaff4(
                    confirmerId, resultSet.getString("confirmer_name"), resultSet.getString("confirmer_username"),
                    resultSet.getString("confirmer_password_hash"), resultSet.getString("confirmer_phone"),
                    resultSet.getString("confirmer_email"), resultSet.getString("confirmer_staff_code")
            );
        }
        LocalDateTime confirmedAt = TableVisitDAO4.toLocalDateTime(resultSet.getTimestamp("confirmed_at"));
        return new Bill4(
                resultSet.getInt("bill_id"), resultSet.getString("bill_status"), confirmedAt,
                BigDecimal.ZERO, visit, confirmer, List.of()
        );
    }

    private List<BillItem4> loadItems(Connection connection, int billId) throws SQLException {
        String sql = """
                SELECT bi.id AS bill_item_id, bi.quantity, bi.unit_price,
                       d.id, d.dish_code, d.name, d.category, d.unit, d.sale_price,
                       d.serving_status, d.description,
                       CASE WHEN c.dish_id IS NULL THEN 'Món lẻ' ELSE 'Combo' END AS dish_type
                  FROM bill_items bi
                  JOIN dishes d ON d.id = bi.dishesid
                  LEFT JOIN combos c ON c.dish_id = d.id
                 WHERE bi.billsid = ?
                 ORDER BY bi.id
                """;
        List<BillItem4> items = new ArrayList<>();
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, billId);
            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    Dish4 dish = DishDAO4.mapDish(resultSet);
                    int quantity = resultSet.getInt("quantity");
                    BigDecimal unitPrice = resultSet.getBigDecimal("unit_price");
                    items.add(new BillItem4(
                            resultSet.getInt("bill_item_id"), dish, quantity, unitPrice,
                            unitPrice.multiply(BigDecimal.valueOf(quantity))
                    ));
                }
            }
        }
        return items;
    }

    private void rollbackQuietly(Connection connection) {
        if (connection == null) return;
        try { connection.rollback(); } catch (SQLException ignored) { }
    }

    private void closeQuietly(Connection connection) {
        if (connection == null) return;
        try { connection.close(); } catch (SQLException ignored) { }
    }
}
