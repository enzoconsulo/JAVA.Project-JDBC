package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import db.DB;
import db.DbException;
import model.entities.Department;
import model.entities.Seller;

public class SellerDaoJDBC implements SellerDao{
	
	private Connection co;

	public SellerDaoJDBC(Connection conn) {
		co = conn;
	}

	@Override
	public void insert(Seller obj) {
		
	}

	@Override
	public void update(Seller obj) {
		
	}

	@Override
	public void deleteById(Integer id) {
		
	}

	@Override
	public Seller findById(Integer id) {
		
		PreparedStatement pst = null;
		ResultSet rs = null;
		
		try {
			pst = co.prepareStatement(
						"select seller.*,department.Name as DepName " 
						+ "from seller inner join department "
						+ "on seller.DepartmentId = department.Id "
						+ "where seller.Id = ?; ");
		
			pst.setInt(1, id);
			
			rs = pst.executeQuery();
			
			if (rs.next()) {
				
				return instantiateSeller(rs, instantiateDepartment(rs));// Id encontrado retorna Obj Seller
				
			}
			return null; // Id nao encontrado retorna null
			
		} catch (SQLException e) {
			throw new DbException(e.getMessage());
		}finally {
			DB.closeResultSet(rs);
			DB.closeStatement(pst);
		}
		
	}

	@Override
	public List<Seller> findAll() {
		return null;
	}

	private Seller instantiateSeller(ResultSet rs,Department dep) throws SQLException {
		return new Seller(
				rs.getInt("Id"),
				rs.getString("Name"),
				rs.getString("Email"),  
				rs.getDate("BirthDate"),
				rs.getDouble("BaseSalary"),
				dep);			
	}
	
	private Department instantiateDepartment(ResultSet rs) throws SQLException {
		return new Department(
				rs.getInt("DepartmentId"),
				rs.getString("DepName"));
	}
	
}
