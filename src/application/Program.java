package application;

import java.util.List;

import dao.DaoFactory;
import dao.SellerDao;
import model.entities.Seller;

public class Program {

	public static void main(String[] args) {
		
		SellerDao sd = DaoFactory.createSellerDao();
		
		System.out.println("=========Test FindById =========\n\n");
		Seller seller =  sd.findById(1);
		System.out.println(seller.toString());
		
		System.out.println("=========Test FindByDepartment =========\n\n");
		List<Seller> list = sd.findByDepartment(1);
		for(Seller s : list) {
			System.out.println(s.toString());
		}

		
	}
}
