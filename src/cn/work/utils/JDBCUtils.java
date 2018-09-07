package cn.work.utils;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javax.sql.DataSource;

import com.mchange.v2.c3p0.ComboPooledDataSource;

public class JDBCUtils {

	// ����c3p0�����ӳ�
	static ComboPooledDataSource cpds = new ComboPooledDataSource();

	// �������
	public static Connection getConnection() throws Exception {
		// �������
		return cpds.getConnection();
	}
	
	public static DataSource getDataSource() {
		
		return cpds;
	}

	// �ͷ���Դ
	public static void release(Connection conn, Statement stmt, ResultSet rs) {
		try {
			if (rs != null) {
				rs.close();
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		rs = null;

		release(conn, stmt);
	}

	// �ͷ���Դ
	public static void release(Connection conn, Statement stmt) {
		try {
			if (stmt != null) {
				stmt.close();
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		stmt = null;

		try {
			if (conn != null) {
				conn.close();
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		conn = null;
	}
}
