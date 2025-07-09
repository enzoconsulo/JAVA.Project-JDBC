package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
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
		// TODO Auto-generated method stub
		
	}

	@Override
	public void update(Department obj) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void deleteById(Integer id) {
		// TODO Auto-generated method stub
		
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
