package vn.edu.restman.dao;

import vn.edu.restman.model.Customer4;
import vn.edu.restman.model.DiningTable4;
import vn.edu.restman.model.TableReservation4;
import vn.edu.restman.model.TableVisit4;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class TableVisitDAO4 extends DAO {
    public List<TableVisit4> findTableVisitsByCustomerName(String customerName) {
        if (customerName == null || customerName.isBlank()) {
            return List.of();
        }
        String sql = """
                SELECT tv.id AS visit_id, tv.arrival_time, tv.guest_count AS visit_guest_count,
                       tv.status AS visit_status,
                       dt.id AS table_id, dt.table_code, dt.area, dt.capacity, dt.status AS table_status,
                       tr.id AS reservation_id, tr.expected_arrival_time,
                       tr.guest_count AS reservation_guest_count, tr.status AS reservation_status,
                       u.id AS customer_id, u.full_name AS customer_name, u.username AS customer_username,
                       u.password_hash AS customer_password_hash, u.phone AS customer_phone,
                       u.email AS customer_email, c.customer_code
                  FROM table_visits tv
                  JOIN dining_tables dt ON dt.id = tv.dining_tablesid
                  JOIN table_reservations tr ON tr.id = tv.table_reservationsid
                  JOIN customers c ON c.user_id = tr.customersuser_id
                  JOIN users u ON u.id = c.user_id
                 WHERE LOWER(u.full_name) LIKE LOWER(?)
                 ORDER BY tv.arrival_time DESC, tv.id DESC
                """;
        List<TableVisit4> visits = new ArrayList<>();
        try (Connection connection = openConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, "%" + customerName.trim() + "%");
            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    visits.add(mapVisit(resultSet));
                }
            }
            return visits;
        } catch (SQLException exception) {
            throw new DataAccessException4("Khong the tim luot dung ban", exception);
        }
    }

    private TableVisit4 mapVisit(ResultSet resultSet) throws SQLException {
        DiningTable4 table = new DiningTable4(
                resultSet.getInt("table_id"),
                resultSet.getString("table_code"),
                resultSet.getString("area"),
                resultSet.getInt("capacity"),
                resultSet.getString("table_status")
        );
        Customer4 customer = new Customer4(
                resultSet.getInt("customer_id"),
                resultSet.getString("customer_name"),
                resultSet.getString("customer_username"),
                resultSet.getString("customer_password_hash"),
                resultSet.getString("customer_phone"),
                resultSet.getString("customer_email"),
                resultSet.getString("customer_code")
        );
        TableReservation4 reservation = new TableReservation4(
                resultSet.getInt("reservation_id"),
                toLocalDateTime(resultSet.getTimestamp("expected_arrival_time")),
                resultSet.getInt("reservation_guest_count"),
                resultSet.getString("reservation_status"),
                customer,
                null
        );
        return new TableVisit4(
                resultSet.getInt("visit_id"),
                toLocalDateTime(resultSet.getTimestamp("arrival_time")),
                resultSet.getInt("visit_guest_count"),
                resultSet.getString("visit_status"),
                table,
                reservation
        );
    }

    static LocalDateTime toLocalDateTime(Timestamp timestamp) {
        return timestamp == null ? null : timestamp.toLocalDateTime();
    }
}
