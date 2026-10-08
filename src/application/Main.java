package application;

import model.dao.DaoFactory;
import model.dao.SellerDao;
import model.entities.Department;
import model.entities.Seller;

import java.util.ArrayList;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        SellerDao sellerDao = DaoFactory.creatSellerDao();
        System.out.println("====TEST 1: seller find by Id====");
        Seller seller = sellerDao.findById(3);
        System.out.println(seller);
        System.out.println("\n ====TEST 2: seller find by Department====");
        Department department = new Department(4, null);
        List<Seller> list = sellerDao.findByDepartment(department);
        for (Seller value : list) {
            System.out.println(value);
        }
        System.out.println("======TESTE 3: seller find all=======");
        List<Seller> list2 = sellerDao.findAll();
        for (Seller seller2: list2){
            System.out.println(seller2);
        }
    }
}