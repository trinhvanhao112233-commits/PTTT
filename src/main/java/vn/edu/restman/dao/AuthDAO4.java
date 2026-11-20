package vn.edu.restman.dao;

import vn.edu.restman.security.AuthenticatedUser4;
import vn.edu.restman.util.PasswordUtil4;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class AuthDAO4 extends DAO {
    private static final String AUTH_SQL = """
            SELECT u.id, u.full_name, u.username, u.password_hash,
                   CASE
                       WHEN ss.staff_id IS NOT NULL THEN 'SALES_STAFF'
                       WHEN c.user_id IS NOT NULL THEN 'CUSTOMER'
                       ELSE 'UNSUPPORTED'
                   END AS user_role,
                   ss.staff_id
            FROM users u
            LEFT JOIN sales_staff ss ON ss.staff_id = u.id
            LEFT JOIN customers c ON c.user_id = u.id
            WHERE u.username = ?
            """;

    public AuthenticatedUser4 authenticate(String username, String password) {
        if (username == null || username.isBlank() || password == null || password.isBlank()) return null;
        try (Connection connection = openConnection();
             PreparedStatement statement = connection.prepareStatement(AUTH_SQL)) {
            statement.setString(1, username.trim());
            try (ResultSet result = statement.executeQuery()) {
                if (!result.next() || !PasswordUtil4.matches(password, result.getString("password_hash"))) return null;
                String role = result.getString("user_role");
                if ("UNSUPPORTED".equals(role)) return null;
                int rawStaffId = result.getInt("staff_id");
                Integer staffId = result.wasNull() ? null : rawStaffId;
                return new AuthenticatedUser4(
                        result.getInt("id"), staffId, result.getString("full_name"),
                        result.getString("username"), role
                );
            }
        } catch (SQLException exception) {
            throw new IllegalStateException("Khong the dang nhap do loi co so du lieu", exception);
        }
    }
}
