package vn.edu.restman.dao;

import vn.edu.restman.config.DatabaseConfig4;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DAO {
    protected Connection con;

    public DAO() {
        try {
            Class.forName(DatabaseConfig4.getDriver());
        } catch (ClassNotFoundException exception) {
            throw new IllegalStateException("Khong nap duoc JDBC driver", exception);
        }
    }

    protected Connection openConnection() throws SQLException {
        con = DriverManager.getConnection(DatabaseConfig4.getUrl(), DatabaseConfig4.getUser(), DatabaseConfig4.getPassword());
        return con;
    }
}
