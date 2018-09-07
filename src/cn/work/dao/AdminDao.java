package cn.work.dao;

import java.sql.Connection;
import java.util.List;

import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.BeanListHandler;

import cn.work.domain.Product;
import cn.work.utils.JDBCUtils;

public class AdminDao {

	public List<Product> query(List<Product> product) {
		// TODO Auto-generated method stub
		
		try {
			Connection conn = JDBCUtils.getConnection();
			
			QueryRunner qr = new QueryRunner();
			
			String sql = "select * from product where pflag=0";
			
			product = qr.query(conn,sql, new BeanListHandler<>(Product.class));
			
			
			return product;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		return null;
	}

}
