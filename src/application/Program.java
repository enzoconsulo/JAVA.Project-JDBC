package application;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.ParseException;
import java.text.SimpleDateFormat;

import db.DB;
import db.DbIntegrityException;

public class Program {

	public static void main(String[] args) {
		
		Connection co = null;
		PreparedStatement pst = null;
		
		try {
			co = DB.getConnection();
			
			co.setAutoCommit(false);
			
			pst = co.prepareStatement("update seller "
					+ "set BaseSalary = 2090 "
					+ "where (DepartmentId = 1)");
			
			int Department1 = pst.executeUpdate();
			
			//int x= 1;
			//if (x>0) {
			//	throw new SQLException("Purpoused Error");
			//}
			
			pst = co.prepareStatement("update seller "
					+ "set BaseSalary = 3090 "
					+ "where (DepartmentId = 2) "
					+ "");
			
			int Department2 = pst.executeUpdate();
			
			System.out.println("Department 1 : "+Department1
							+  "\nDepartment 2 : "+Department2);

			co.commit();
			
		}catch(SQLException e) {
			try {
				co.rollback();
				throw new DbIntegrityException("Error in Operation. Rolling Back..."
						+ "\n Cause By : "+ e.getMessage());
				
			} catch (SQLException e1) {
				throw new DbIntegrityException("Error in RollBack."
						+ "\n Cause By: " + e1.getMessage());
			}

		}finally {
			DB.closeStatement(pst);
			DB.closeConnection();
		}
		
	}

}
