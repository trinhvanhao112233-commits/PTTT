package vn.edu.restman.dao;

import vn.edu.restman.model.Dish4;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class DishDAO4 extends DAO {
    private static final String BASE_SELECT = """
            SELECT d.id, d.dish_code, d.name, d.category, d.unit,
                   d.sale_price, d.serving_status, d.description,
                   CASE WHEN c.dish_id IS NULL THEN 'Món lẻ' ELSE 'Combo' END AS dish_type
              FROM dishes d
              LEFT JOIN combos c ON c.dish_id = d.id
            """;

    public List<Dish4> searchDishesByName(String keyword) {
        if (keyword == null || keyword.isBlank()) {
            return List.of();
        }
        String sql = BASE_SELECT + " WHERE LOWER(d.name) LIKE LOWER(?) ORDER BY d.id";
        List<Dish4> dishes = new ArrayList<>();
        try (Connection connection = openConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, "%" + keyword.trim() + "%");
            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    dishes.add(mapDish(resultSet));
                }
            }
            return dishes;
        } catch (SQLException exception) {
            throw new DataAccessException4("Khong the tim kiem mon an", exception);
        }
    }

    public Dish4 getDishDetails(String dishCode) {
        if (dishCode == null || dishCode.isBlank()) {
            return null;
        }
        String sql = BASE_SELECT + " WHERE d.dish_code = ?";
        try (Connection connection = openConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, dishCode.trim());
            try (ResultSet resultSet = statement.executeQuery()) {
                return resultSet.next() ? mapDish(resultSet) : null;
            }
        } catch (SQLException exception) {
            throw new DataAccessException4("Khong the lay chi tiet mon an", exception);
        }
    }

    static Dish4 mapDish(ResultSet resultSet) throws SQLException {
        return new Dish4(
                resultSet.getInt("id"),
                resultSet.getString("dish_code"),
                resultSet.getString("name"),
                resultSet.getString("category"),
                resultSet.getString("unit"),
                resultSet.getBigDecimal("sale_price"),
                resultSet.getString("serving_status"),
                resultSet.getString("description"),
                resultSet.getString("dish_type")
        );
    }
}
