package application;

import java.util.ArrayList;
import java.util.List;

import dao.DaoFactory;
import dao.DepartmentDao;
import db.DB;
import model.entities.Department;

public class Program2 {

	public static void main(String[] args) {
		
		DepartmentDao dd = DaoFactory.createDepartmentDao();
		List<Department> list = new ArrayList<>();
		
		System.out.println("=========Test FindById =========");
		int FindById_value = 4;
		Department dep =  dd.findById(FindById_value);
		if(dep == null) {
			System.out.println("Nenhum resultado encontrado com Id: "+FindById_value);
		}else{
			System.out.println(dep.toString());
		};
		
		System.out.println("\n\n=========Test FindAll =========");	
		list = dd.findAll();
		for(Department d : list)System.out.println(d.toString());

		System.out.println("\n\n=========Test Insert =========");
		Department dep2 = new Department(null,"Dev");
		dd.insert(dep2);
		
		System.out.println("\n\n=========Test Update =========");
		dep.setName("Compiuter");
		dd.update(dep);
		
		System.out.println("\n\n=========Test deleteById =========");
		dd.deleteById(12);
		
		DB.closeConnection();
		
	}

}
