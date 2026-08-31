package model.dao.impl;

import db.DbException;
import model.dao.DepartmentDao;
import model.entities.Department;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DepartmentDaoJDBC implements DepartmentDao {
    private Connection conn;

    public DepartmentDaoJDBC(Connection conn) {this.conn = conn;}

    @Override
    public void insert(Department obj) {
        try(PreparedStatement st = conn.prepareStatement("INSERT INTO department " +
                        "(Name) " +
                        "VALUES " +
                        "(?)",
                Statement.RETURN_GENERATED_KEYS)) {

            st.setString(1, obj.getName());
            int rowsAffected = st.executeUpdate();

            if (rowsAffected > 0) {
                try(ResultSet rs = st.getGeneratedKeys()) {
                    if (rs.next()) {
                        int id = rs.getInt(1);
                        obj.setId(id);
                    }
                }
            } else {
                throw new DbException("Unexpected error! No rows affected!");
            }

        } catch(SQLException e) {
            throw new DbException(e.getMessage());
        }
    }

    @Override
    public void update(Department obj) {

    }

    @Override
    public void deleteById(Integer id) {
        try(PreparedStatement st = conn.prepareStatement("DELETE FROM department WHERE Id = ?")) {
            st.setInt(1, id);
            st.executeUpdate();
        } catch (SQLException e) {
            throw new DbException(e.getMessage());
        }
    }

    @Override
    public Department findById(Integer id) {
        try(PreparedStatement st = conn.prepareStatement("SELECT * FROM department WHERE Id = ?")) {
            st.setInt(1, id);
            try(ResultSet rs = st.executeQuery()) {
                if (rs.next()) {
                    Department dep = new Department();
                    dep.setId(rs.getInt("Id"));
                    dep.setName(rs.getString("Name"));
                    return dep;
                }
                return null;
            }
        } catch (SQLException e) {
            throw new DbException(e.getMessage());
        }
    }

    @Override
    public List<Department> findAll() {
        try(PreparedStatement st = conn.prepareStatement("SELECT * FROM department ORDER BY Name")) {
            try(ResultSet rs = st.executeQuery()) {
                List<Department> list = new ArrayList<>();

                while(rs.next()) {
                    Department dep = new Department();
                    dep.setId(rs.getInt("Id"));
                    dep.setName(rs.getString("Name"));
                    list.add(dep);
                }
                return list;
            }
        }catch(SQLException e) {
            throw new DbException(e.getMessage());
        }
    }
}
