package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
	
	@Override
	public List<Seller> findByDepartment(Integer departmentId) {
			PreparedStatement pst = null;
			ResultSet rs = null;
			List<Seller> list = new ArrayList<>();
			
			try {
				pst = co.prepareStatement(
						 "SELECT Seller.*,department.Name as DepName "
						+ "from seller inner join department "
						+ "on seller.DepartmentId = department.Id "
						+ "where department.id = ? "
						+ "order by seller.Name; ");
				
				pst.setInt(1, departmentId);
				
				rs = pst.executeQuery();
				Map<Integer, Department> map = new HashMap<>();
				
				while(rs.next()) {
					
					if(!map.containsKey(rs.getInt("DepartmentId"))) {
						map.put(rs.getInt("DepartmentId"), instantiateDepartment(rs));
						
					}
					
					list.add(instantiateSeller(rs, map.get(rs.getInt("DepartmentId"))));	
				}
				
				return list;
				
			} catch (SQLException e) {
				throw new DbException(e.getMessage());
			}		
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
