package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
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
		PreparedStatement pst = null;
		ResultSet rs = null;
		try {
			pst = co.prepareStatement(
					"insert into seller "
					+ "(Name,Email,BirthDate,BaseSalary,DepartmentId) "
					+ "values "
					+ "(?,?,?,?,?);",Statement.RETURN_GENERATED_KEYS);
			
			pst.setString(1, obj.getName());
			pst.setString(2, obj.getEmail());
			pst.setDate(3, new java.sql.Date(obj.getBirthDate().getTime()));
			pst.setDouble(4, obj.getBaseSalary());
			pst.setInt(5, obj.getDepartment().getId());
			
			int successInsert = pst.executeUpdate();
			
			if(successInsert > 0) {
				System.out.println("Seller: '" + obj.getName() +"' succesfully inserted!");
				rs = pst.getGeneratedKeys();
				if(rs.next()) {
					obj.setId(rs.getInt(1));
				}
				
			}else {
				System.out.println("Insertion Failed !");
			}

		} catch (SQLException e) {
			throw new DbException(e.getMessage());
		}finally {
			DB.closeStatement(pst);
			DB.closeResultSet(rs);
		}
		
	}

	@Override
	public void update(Seller obj) {
		PreparedStatement pst = null;
		
		try {
			pst = co.prepareStatement(
					"UPDATE seller "
					+ "SET Name = ?, Email = ?, BirthDate = ?, BaseSalary = ?, DepartmentId = ? "
					+ "WHERE Id = ?");
			
			pst.setString(1, obj.getName());
			pst.setString(2, obj.getEmail());
			pst.setDate(3, new java.sql.Date(obj.getBirthDate().getTime()));
			pst.setDouble(4, obj.getBaseSalary());
			pst.setInt(5, obj.getDepartment().getId());
			pst.setInt(6, obj.getId());
			
			int successUpdate = pst.executeUpdate();
			
			if(successUpdate > 0) {
				System.out.println("Seller: '" + obj.getName() +"' succesfully updated!");
			}else {
				System.out.println("Update Failed !");
			}

		} catch (SQLException e) {
			throw new DbException(e.getMessage());
		}finally {
			DB.closeStatement(pst);
		}
	}

	@Override
	public void deleteById(Integer id) {
		PreparedStatement pst = null;
		
		try {
			pst = co.prepareStatement(
					"delete seller from seller "
					+ "where Id = ?; ");
			
			pst.setInt(1, id);
			
			int successDelete = pst.executeUpdate();
			
			if(successDelete >0) {
				System.out.println("Succesfull Deleted" );
			}else {
				System.out.println("Delete Failed !");
			}

		} catch (SQLException e) {
			throw new DbException(e.getMessage());
		}
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
		PreparedStatement pst = null;
		ResultSet rs = null;
		List<Seller> list = new ArrayList<>();
		
		try {
			pst = co.prepareStatement(
					"select seller.*,department.name as DepName "
					+ "from seller inner join department "
					+ "on DepartmentId = department.Id ");
			
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
		}finally {
			DB.closeResultSet(rs);
			DB.closeStatement(pst);
		}
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
			}finally {
				DB.closeResultSet(rs);
				DB.closeStatement(pst);
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
