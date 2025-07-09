package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;


import db.DB;
import db.DbException;
import model.entities.Department;

public class DepartmentDaoJDBC implements DepartmentDao{

	private Connection co = null;
	
	public DepartmentDaoJDBC(Connection conn) {
		co = conn;
	}

	@Override
	public void insert(Department obj) {
		PreparedStatement pst = null;
		ResultSet rs = null;
		try {
			pst = co.prepareStatement(
					"insert into department "
					+ "(Name) "
					+ "values "
					+ "(?);",Statement.RETURN_GENERATED_KEYS);
			
			pst.setString(1, obj.getName());
			
			int successInsert = pst.executeUpdate();
			
			if(successInsert > 0) {
				System.out.println("Department: '" + obj.getName() +"' succesfully inserted!");
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
	public void update(Department obj) {
		PreparedStatement pst = null;
		
		try {
			pst = co.prepareStatement(
					"UPDATE Department "
					+ "SET Name = ? "
					+ "WHERE Id = ?");
			
			pst.setString(1, obj.getName());
			pst.setInt(2, obj.getId());
			
			int successUpdate = pst.executeUpdate();
			
			if(successUpdate > 0) {
				System.out.println("Department: '" + obj.getName() +"' succesfully updated!");
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
					"delete from department "
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
	public Department findById(Integer id) {
		PreparedStatement pst = null;
		ResultSet rs = null;
		
		try {
			pst = co.prepareStatement(
					 "SELECT * FROM department "
					+ "WHERE department.id = ? ");
			
			pst.setInt(1, id);
			
			rs = pst.executeQuery();
			
			if(rs.next())return instantiateDepartment(rs);
			return null;
	
		} catch (SQLException e) {
			throw new DbException(e.getMessage());
		}finally {
			DB.closeResultSet(rs);
			DB.closeStatement(pst);
		}
	}

	@Override
	public List<Department> findAll() {
		PreparedStatement pst = null;
		ResultSet rs = null;
		List<Department> list = new ArrayList<>();
		
		try {
			pst = co.prepareStatement(
					"select * from department ");
			
			rs = pst.executeQuery();
			
			
			while(rs.next()) {
				list.add(instantiateDepartment(rs));	
			}
			
			return list;
	
		} catch (SQLException e) {
			throw new DbException(e.getMessage());
		}finally {
			DB.closeResultSet(rs);
			DB.closeStatement(pst);
		}
	}

	private Department instantiateDepartment(ResultSet rs) throws SQLException {
		return new Department(
				rs.getInt("Id"),
				rs.getString("Name"));
	}

}
