package dao;

import java.sql.SQLException;
import java.util.List;

/**
 * GenericRepository - declares basic CRUD operations.
 * Implementations should manage PreparedStatements and ResultSets.
 */
public interface GenericRepository<T> {
    T create(T t) throws SQLException;
    T findById(int id) throws SQLException;
    List<T> findAll() throws SQLException;
    boolean update(T t) throws SQLException;
    boolean delete(int id) throws SQLException;
}
