package application;

import java.util.Date;
import java.util.List;

import dao.DaoFactory;
import dao.SellerDao;
import model.entities.Department;
import model.entities.Seller;

public class Program {

	public static void main(String[] args) {
		
		SellerDao sd = DaoFactory.createSellerDao();
		
		System.out.println("=========Test FindById =========");
		int FindById_value = 4;
		Seller seller =  sd.findById(FindById_value);
		if(seller == null) {
			System.out.println("Nenhum resultado encontrado com Id: "+FindById_value);
		}else{
			System.out.println(seller.toString());
		};
		
		
		System.out.println("\n\n=========Test FindByDepartment =========");
		int findByDepartment_value = 1;
		List<Seller> list = sd.findByDepartment(findByDepartment_value);
		if(list.isEmpty())System.out.println("Nenhum resultado encontrado com DepartmentId: "+findByDepartment_value);
		for(Seller s : list) System.out.println(s.toString());
		list.clear();

		System.out.println("\n\n=========Test FindAll =========");
		list = sd.findAll();
		for(Seller s : list)System.out.println(s.toString());
		
		System.out.println("\n\n=========Test Insert =========");
		seller = new Seller(null,"teste","teste@gmail.com",new Date(),2000.0,new Department(1,"dev"));
		sd.insert(seller);
		
		
		
		
	}
}
