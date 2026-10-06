package model.dao.impl;

import db.DB;
import db.DbException;
import model.dao.SellerDao;
import model.entities.Seller;

import java.sql.*;
import java.util.List;

public class SellerDaoJDBC implements SellerDao {

    private Connection conn;

    public SellerDaoJDBC(Connection conn) {
        this.conn = conn;
    }

    @Override
    public void insert(Seller sel) {

    }

    @Override
    public void update(Seller dep) {

    }

    @Override
    public void deleteById(Integer id) {

    }

    @Override
    public Seller findById(Integer id) {
        PreparedStatement pst = null;
        ResultSet rs = null;
        try {
            conn = DB.getConnection();
            pst = conn.prepareStatement("SELECT seller.*, department.Name as depName "
                                            +"FROM seller INNER JOIN departmet "
                                            +"ON seller.DepartmentId = department.Id "
                                            +"WHERE seller.Id = ?");
            pst.setInt(1, id);
            rs = pst.executeQuery();

        } catch (SQLException e) {
            throw new DbException(e.getMessage());
        }
    }

    @Override
    public List<Seller> findAll() {
        return null;
    }
}
