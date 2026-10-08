package model.dao.impl;

import db.DB;
import db.DbException;
import model.dao.DepartmentDao;
import model.entities.Department;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DepartmentDaoJDBC implements DepartmentDao {
    private Connection conn;

    public DepartmentDaoJDBC(Connection conn){
        this.conn = conn;
    }
    @Override
    public void insert(Department dep) {
        PreparedStatement pst = null;
        try {
            conn = DB.getConnection();
            pst = conn.prepareStatement(
                    "INSERT INTO department "
                        +"(Name) "
                        +"VALUES "
                        +"(?) ",
                        + Statement.RETURN_GENERATED_KEYS
                    );
            pst.setString(1, dep.getName());
            int rows = pst.executeUpdate();
            if (rows > 0){
                ResultSet rs = pst.getGeneratedKeys();
                if (rs.next()){
                    int id = rs.getInt(1);
                    dep.setId(id);
                }
                DB.closeresultSet(rs);
            }else {
                throw new DbException("rows not affected");
            }
        }catch (SQLException e){
            throw new DbException(e.getMessage());
        }finally {
            DB.closeStatement(pst);
        }
    }

    @Override
    public void update(Department dep) {
        PreparedStatement pst = null;
        try {
            conn = DB.getConnection();
            pst = conn.prepareStatement(
                    "UPDATE department "
                        +"SET Name = ? "
                        +"WHERE id = ?");
            pst.setString(1, dep.getName());
            pst.setInt(2, dep.getId());
            int rows = pst.executeUpdate();
            if (rows == 0){
                throw new DbException("no rows updated");
            }
        }catch (SQLException e){
            throw new DbException(e.getMessage());
        }finally {
            DB.closeStatement(pst);
        }
    }

    @Override
    public void deleteById(Integer id) {
        PreparedStatement pst = null;
        try {
            conn = DB.getConnection();
            pst = conn.prepareStatement(
                    "DELETE FROM department "
                        +"WHERE id = ?");
            pst.setInt(1, id);
            int rows = pst.executeUpdate();
            if(rows == 0){
                throw new DbException("no rows deleted");
            }
        }catch (SQLException e){
            throw new DbException(e.getMessage());
        }finally {
            DB.closeStatement(pst);
        }
    }

    @Override
    public Department findById(Integer id) {
        PreparedStatement pst = null;
        ResultSet rs = null;
        try {
            conn = DB.getConnection();
            pst = conn.prepareStatement(
                    "SELECT department.* "
                        +"FROM department "
                        +"WHERE id = ?");
            pst.setInt(1, id);
            rs = pst.executeQuery();
            if (rs.next()){
                Department dep = new Department(rs.getInt(1), rs.getString(2));
                return dep;
            }
            return null;
        }catch (SQLException e) {
            throw new DbException(e.getMessage());
        }finally {
            DB.closeStatement(pst);
            DB.closeresultSet(rs);
        }
    }

    @Override
    public List<Department> findAll() {
        PreparedStatement pst = null;
        ResultSet rs = null;
        List<Department> depList = new ArrayList<>();
        try {
            conn = DB.getConnection();
            pst = conn.prepareStatement(
                    "SELECT department.* "
                        +"FROM department "
                        +"ORDER BY Name");
            rs = pst.executeQuery();
            while (rs.next()){
                Department department = new Department(rs.getInt(1), rs.getString(2));
                depList.add(department);
            }
            return depList;
        }catch (SQLException e) {
            throw new DbException(e.getMessage());
        }finally {
            DB.closeStatement(pst);
            DB.closeresultSet(rs);
        }
    }
}
