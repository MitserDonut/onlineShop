package cn.work.dao;

import java.sql.Connection;
import java.sql.SQLException;

import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.BeanHandler;

import cn.work.domain.Admin;
import cn.work.domain.User;
import cn.work.utils.DataSourceUtils;
import cn.work.utils.JDBCUtils;

public class RegistDao {

	public boolean regist(User user) {
		try {
			QueryRunner qr = new QueryRunner(DataSourceUtils.getDataSource());
			String sql = "insert into user values (?,?,?,?,?,?,?,?,?,?)";
			int resutl =qr.update(sql,user.getUid(),user.getUsername(),
					user.getPassword(),user.getName(),user.getEmail(),
					user.getTelephone(),user.getBirthday(),user.getSex(),
					user.getState(),user.getCode());
			return resutl > 0 ? true: false;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return false;
	}

	public boolean active(String code) {
		try {
			QueryRunner qr = new QueryRunner(DataSourceUtils.getDataSource());
			String sql ="update user set state = ? where code = ?";
			int result = qr.update(sql, 1,code);
			return result>0?true:false;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return false;
	}

	public boolean checkUsername(String username) {
		try {
			QueryRunner qr = new QueryRunner(DataSourceUtils.getDataSource());
			String sql ="select * from user where username = ?";
			User user = qr.query(sql, new BeanHandler<>(User.class), username);
			return user == null?false:true;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return false;
	}

	public User login(User user) {
		// TODO Auto-generated method stub
		try {
			Connection conn = JDBCUtils.getConnection();
			String sql = "select * from user where username = ? and password = ?";
			QueryRunner qr = new QueryRunner();
			user = qr.query(conn, sql, new BeanHandler<>(User.class),user.getUsername(),user.getPassword());
			return user;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	public boolean delete(User user) {
		try {
			QueryRunner qr = new QueryRunner(DataSourceUtils.getDataSource());
			String sql = "update user set state = 0 where username = ? and password = ?";
			System.out.println(user.getUsername()+user.getPassword());
			int u = qr.update(sql,user.getUsername(),user.getPassword());
			if(u>0){
				return true;
			}
			else{
				return false;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return false;	}

	public User infoChangeDao(User user) {
		// TODO Auto-generated method stub
		int count=0;
		User u= new User();
		String uid=user.getUid();
		QueryRunner qr = new QueryRunner(DataSourceUtils.getDataSource());
		try {
			String sql = "update user set username = ?,telephone=?,email=?,name=?,sex=?,birthday=? where uid=?";
			count = qr.update(sql,user.getUsername(),user.getTelephone(),user.getEmail(),user.getName(),user.getSex(),user.getBirthday(),uid);
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		if(count >0){
			String get ="select * from user where uid = ?";
			try {
				u = qr.query(get, new BeanHandler<>(User.class), uid);
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return u;
		}
		else{
			return null;
		}
	}

	public Admin adLogin(Admin admin) {
		// TODO Auto-generated method stub
		try {
			Connection conn = JDBCUtils.getConnection();
			String sql = "select * from admin where adname =? and adpassword =?";
			QueryRunner qr = new QueryRunner(); 
			Admin ad = null;
			ad = qr.query(conn, sql, new BeanHandler<>(Admin.class),admin.getAdname(),admin.getAdpassword());
			return ad;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

}
