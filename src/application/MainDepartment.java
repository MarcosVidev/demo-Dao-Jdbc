package application;

import db.DB;
import model.dao.DaoFactory;
import model.dao.DepartmentDao;
import model.dao.impl.DepartmentDaoJDBC;
import model.entities.Department;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class MainDepartment {
    public static void main(String[] args) {
        Scanner sc =  new Scanner(System.in);
        DepartmentDao departmentDao = DaoFactory.creatDepartmentDao();
        Department dep = new Department(null, "Clothes");
        System.out.println("=====TEST 1 Insert departments====");
        //departmentDao.insert(dep);
        System.out.println("=====TEST 2 Update departments====");
        System.out.print("which department to update: ");
        //int depChoose = sc.nextInt();
        //Department dep2 = new Department(depChoose, "Animals");
        //departmentDao.update(dep2);
        System.out.println("=====TEST 3 Delete departments====");
        System.out.print("which department to delete: ");
        //depChoose = sc.nextInt();
        //departmentDao.deleteById(depChoose);
        System.out.println("=====TEST 4 find departments by id====");
        //System.out.print(departmentDao.findById(4));
        System.out.println("====TEST 5 find all departments====");
        List<Department> list = departmentDao.findAll();
        for (Department department: list){
            System.out.println(department);
        }

    }
}
