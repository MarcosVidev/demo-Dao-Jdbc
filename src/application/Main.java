package application;

import model.dao.DaoFactory;
import model.dao.SellerDao;
import model.entities.Department;
import model.entities.Seller;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
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
        System.out.println("======TEST 3: seller find all=======");
        List<Seller> list2 = sellerDao.findAll();
        for (Seller seller2: list2){
            System.out.println(seller2);
        }
        System.out.println("=====TEST 4: insert seller=====");
        Seller seller2 = new Seller(null, "vinicius", "vinicius@gmail.com", new Date(), 2000.0, new Department(1, "Computers"));
        sellerDao.insert(seller2);
        System.out.println("Inserted! new id: " + seller2.getId());
        System.out.println("======TEST 5: Update seller======");
        Seller seller3 = new Seller(8, "Gabriel", "gabriel@gmail.com", new Date(), 3000.0, new Department(2, "Eletronics"));
        sellerDao.update(seller3);
        System.out.println("Seller updated! ");
        System.out.println("======TEST 6: Deleted seller======");
        System.out.print("Insert the seller id: ");
        int id = sc.nextInt();
        sellerDao.deleteById(id);
        System.out.println("Seller deleted successfully");
    }
}