package model.dao.impl;

import db.DB;
import db.DbException;
import model.dao.SellerDao;
import model.entities.Department;
import model.entities.Seller;

import javax.swing.text.StyledEditorKit;
import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SellerDaoJDBC implements SellerDao {

    private Connection conn;

    public SellerDaoJDBC(Connection conn) {
        this.conn = conn;
    }

    @Override
    public void insert(Seller sel) {
        PreparedStatement pst = null;
        ResultSet rs = null;
        try {
            conn = DB.getConnection();
            pst = conn.prepareStatement(
                    "INSERT INTO seller "
                        +"(Name, Email, BirthDate, BaseSalary, DepartmentId) "
                        +"VALUES "
                        +"(?, ?, ?, ?, ?)",
                        + Statement.RETURN_GENERATED_KEYS
                        );
            pst.setString(1, sel.getName());
            pst.setString(2, sel.getEmail());
            pst.setDate(3, new java.sql.Date(sel.getBirthDate().getTime()));
            pst.setDouble(4, sel.getBaseSalary());
            pst.setInt(5, sel.getDep().getId());
            int rowsAffected = pst.executeUpdate();
            if (rowsAffected > 0){
                rs = pst.getGeneratedKeys();
                if (rs.next()){
                    int id = rs.getInt(1);
                    sel.setId(id);
                }
                DB.closeresultSet(rs);
            }else {
                throw new DbException("No rows affected");
            }
        } catch (SQLException e) {
            throw new DbException(e.getMessage());
        }finally {
            DB.closeStatement(pst);
        }
    }

    @Override
    public void update(Seller sel) {
        PreparedStatement pst = null;
        try {
            conn = DB.getConnection();
            pst = conn.prepareStatement(
                    "UPDATE seller "
                        +"SET Name = ?, Email = ?, BirthDate = ?, BaseSalary = ?, DepartmentId = ? "
                        +"WHERE id = ?"
                    );
            pst.setString(1, sel.getName());
            pst.setString(2, sel.getEmail());
            pst.setDate(3, new java.sql.Date(sel.getBirthDate().getTime()));
            pst.setDouble(4, sel.getBaseSalary());
            pst.setInt(5, sel.getDep().getId());
            pst.setInt(6, sel.getId());

            pst.executeUpdate();
        } catch (SQLException e) {
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
                    "DELETE FROM seller "
                        +"WHERE id = ? "
                        );
            pst.setInt(1 ,id);
            int rows = pst.executeUpdate();
            if (rows == 0){
                throw new DbException("There is no seller with this id!");
            }
        }catch (SQLException e){
            throw new DbException(e.getMessage());
        }finally {
            DB.closeStatement(pst);
        }
    }

    @Override
    public Seller findById(Integer id) {
        PreparedStatement pst = null;
        ResultSet rs = null;
        try {
            conn = DB.getConnection();
            pst = conn.prepareStatement(
                    "SELECT seller.*, department.Name as depName "
                    +"FROM seller INNER JOIN department "
                    +"ON seller.DepartmentId = department.Id "
                    +"WHERE seller.Id = ?"
            );
            pst.setInt(1, id);
            rs = pst.executeQuery();
            if (rs.next()) {
                Department dep = instantiateDepartment(rs);
                Seller seller = instantiateSeller(rs, dep);
                return seller;
            }
            return null;
        } catch (SQLException e) {
            throw new DbException(e.getMessage());
        } finally {
            DB.closeStatement(pst);
            DB.closeresultSet(rs);
        }
    }

    @Override
    public List<Seller> findAll() {
        ResultSet rs = null;
        PreparedStatement pst = null;
        List<Seller> list= new ArrayList<>();
        try{
            conn = DB.getConnection();
            pst = conn.prepareStatement(
                    "SELECT seller.*, department.Name as depName "
                            + "FROM seller INNER JOIN department "
                            + "ON seller.DepartmentId = department.Id "
                            + "ORDER BY Name"
                    );
            rs = pst.executeQuery();
            Map<Integer, Department> map = new HashMap<>();
                while(rs.next()) {
                    Department dep = map.get(rs.getInt("DepartmentId"));
                        if (dep == null){
                        dep = instantiateDepartment(rs);
                        map.put(rs.getInt("DepartmentId"), dep);
                    }
                    Seller seller = instantiateSeller(rs, dep);
                    list.add(seller);
                }
                return list;
        } catch (SQLException e) {
            throw new DbException(e.getMessage());
        }finally {
            DB.closeStatement(pst);
            DB.closeresultSet(rs);
        }
    }

    @Override
    public List<Seller> findByDepartment(Department department) {
        ResultSet rs = null;
        PreparedStatement pst = null;
        List<Seller> list= new ArrayList<>();
        try{
            conn = DB.getConnection();
            pst = conn.prepareStatement(
                    "SELECT seller.*, department.Name as depName "
                    + "FROM seller INNER JOIN department "
                    + "ON seller.DepartmentId = department.Id "
                    + "WHERE DepartmentId = ? "
                    + "ORDER BY Name");
            pst.setInt(1, department.getId());
            rs = pst.executeQuery();
            boolean rs1 = rs.next();
            if (rs1) {
                Department dep = instantiateDepartment(rs);
                while(rs1) {
                    Seller seller = instantiateSeller(rs, dep);
                    list.add(seller);
                    rs1 = rs.next();
                }
                return list;
            }
            return null;
        } catch (SQLException e) {
            throw new DbException(e.getMessage());
        }finally {
            DB.closeStatement(pst);
            DB.closeresultSet(rs);
        }
    }

    private Seller instantiateSeller(ResultSet rs, Department dep) throws SQLException {
        Seller seller = new Seller();
        seller.setId(rs.getInt("Id"));
        seller.setName(rs.getString("Name"));
        seller.setEmail(rs.getString("Email"));
        seller.setBirthDate(rs.getDate("BirthDate"));
        seller.setBaseSalary(rs.getDouble("BaseSalary"));
        seller.setDep(dep);
        return seller;
    }

    private Department instantiateDepartment(ResultSet rs) throws SQLException {
        Department dep = new Department();
        dep.setId(rs.getInt("DepartmentId"));
        dep.setName(rs.getString("depName"));
        return dep;
    }
}
