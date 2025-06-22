package com.github.fadingafterglow.repository;

import com.github.fadingafterglow.entity.GoodsEntity;
import com.github.fadingafterglow.exception.DataBaseException;
import com.github.fadingafterglow.filter.GoodsFilter;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class GoodsRepository extends BaseRepository {

    private static final Map<String, String> FIELD_EXPRESSION_MAP = Map.of(
            "id", "id",
            "name", "name",
            "category", "category",
            "quantity", "quantity",
            "price", "price"
    );

    public Optional<GoodsEntity> getGoodsById(int id) {
        final String sql = "SELECT * FROM goods WHERE id = ?";
        try (PreparedStatement statement = transactionManager.currentTransaction().prepareStatement(sql)) {
            statement.setInt(1, id);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next())
                    return Optional.of(map(resultSet));
                return Optional.empty();
            }
        } catch (SQLException e) {
            throw new DataBaseException(e);
        }
    }

    public List<GoodsEntity> getGoodsByFilter(GoodsFilter filter) {
        String query = "SELECT * FROM goods";
        query = filter.addFilteringAndPagination(query, FIELD_EXPRESSION_MAP);
        try (PreparedStatement statement = transactionManager.currentTransaction().prepareStatement(query)) {
            filter.setParameters(statement);
            try (ResultSet resultSet = statement.executeQuery()) {
                List<GoodsEntity> result = new ArrayList<>();
                while (resultSet.next())
                    result.add(map(resultSet));
                return result;
            }
        } catch (SQLException e) {
            throw new DataBaseException(e);
        }
    }

    public long countGoodsByFilter(GoodsFilter filter) {
        String query = "SELECT COUNT(*) FROM goods";
        query = filter.addFiltering(query, FIELD_EXPRESSION_MAP);
        try (PreparedStatement statement = transactionManager.currentTransaction().prepareStatement(query)) {
            filter.setParameters(statement);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next())
                    return resultSet.getLong(1);
                return 0;
            }
        } catch (SQLException e) {
            throw new DataBaseException(e);
        }
    }

    public Integer createGoods(GoodsEntity goodsEntity) {
        final String sql = "INSERT INTO goods (name, category, quantity, price) VALUES (?, ?, ?, ?)";
        try (PreparedStatement statement = transactionManager.currentTransaction().prepareStatement(sql, true)){
            statement.setString(1, goodsEntity.getName());
            statement.setString(2, goodsEntity.getCategory());
            statement.setInt(3, goodsEntity.getQuantity());
            statement.setBigDecimal(4, goodsEntity.getPrice());
            statement.executeUpdate();
            try (ResultSet generatedKeys = statement.getGeneratedKeys()) {
                if (generatedKeys.next())
                    return generatedKeys.getInt(1);
                else
                    throw new DataBaseException("Cannot create goods");
            }
        }
        catch (SQLException e) {
            throw new DataBaseException(e);
        }
    }

    public boolean updateGoods(GoodsEntity goodsEntity) {
        final String sql = "UPDATE goods SET name = ?, category = ?, quantity = ?, price = ? WHERE id = ?";
        try (PreparedStatement statement = transactionManager.currentTransaction().prepareStatement(sql)) {
            statement.setString(1, goodsEntity.getName());
            statement.setString(2, goodsEntity.getCategory());
            statement.setInt(3, goodsEntity.getQuantity());
            statement.setBigDecimal(4, goodsEntity.getPrice());
            statement.setInt(5, goodsEntity.getId());
            return statement.executeUpdate() != 0;
        }
        catch (SQLException e) {
            throw new DataBaseException(e);
        }
    }

    public boolean deleteGoods(int id) {
        final String sql = "DELETE FROM goods WHERE id = ?";
        try (PreparedStatement statement = transactionManager.currentTransaction().prepareStatement(sql)) {
            statement.setInt(1, id);
            return statement.executeUpdate() != 0;
        }
        catch (SQLException e) {
            throw new DataBaseException(e);
        }
    }

    public Optional<Integer> findIdByName(String name) {
        final String sql = "SELECT id FROM goods WHERE name = ?";
        try (PreparedStatement statement = transactionManager.currentTransaction().prepareStatement(sql)) {
            statement.setString(1, name);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next())
                    return Optional.of(resultSet.getInt("id"));
                return Optional.empty();
            }
        } catch (SQLException e) {
            throw new DataBaseException(e);
        }
    }

    public long countAll() {
        final String sql = "SELECT COUNT(*) FROM goods";
        try (PreparedStatement statement = transactionManager.currentTransaction().prepareStatement(sql)) {
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next())
                    return resultSet.getLong(1);
                return 0;
            }
        } catch (SQLException e) {
            throw new DataBaseException(e);
        }
    }

    public void deleteAll() {
        final String sql = "DELETE FROM goods";
        try (PreparedStatement statement = transactionManager.currentTransaction().prepareStatement(sql)) {
            statement.executeUpdate();
        } catch (SQLException e) {
            throw new DataBaseException(e);
        }
    }

    private GoodsEntity map(ResultSet resultSet) throws SQLException {
        return GoodsEntity.builder()
                .id(resultSet.getInt("id"))
                .name(resultSet.getString("name"))
                .category(resultSet.getString("category"))
                .quantity(resultSet.getInt("quantity"))
                .price(resultSet.getBigDecimal("price"))
                .build();
    }
}
