package application;

import dao.DaoFactory;
import dao.SellerDao;
import model.entities.Seller;

public class Program {

	public static void main(String[] args) {
		
		SellerDao sd = DaoFactory.createSellerDao();
		
		System.out.println("=========Test FindById =========");
		Seller seller =  sd.findById(1);
		System.out.println(seller.toString());
		
	}
}
