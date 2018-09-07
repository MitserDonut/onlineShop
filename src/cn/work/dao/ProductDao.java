package cn.work.dao;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.BeanHandler;
import org.apache.commons.dbutils.handlers.BeanListHandler;
import org.apache.commons.dbutils.handlers.MapListHandler;
import org.apache.commons.dbutils.handlers.ScalarHandler;

import cn.work.domain.Category;
import cn.work.domain.Order;
import cn.work.domain.OrderItem;
import cn.work.domain.Product;
import cn.work.utils.DataSourceUtils;

public class ProductDao {
	private QueryRunner qr = new QueryRunner(DataSourceUtils.getDataSource());
	private String sql = "";
	
	//获取热门商品
	public List<Product> getHotProducts() {
		try {
			sql = "select * from product where is_hot = ? limit ?,?";
			return qr.query(sql, new BeanListHandler<>(Product.class), 1,0,9);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}
	//获取最新商品
	public List<Product> getNewProducts() {
		try {
			sql = "select * from product order by pdate desc limit ?,?";
			return qr.query(sql, new BeanListHandler<>(Product.class), 0,9);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}
	public List<Category> getCategoryies() {
		try {
			sql = "select * from category";
			return qr.query(sql, new BeanListHandler<>(Category.class));
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}
	//获取总记录数
	public int getTotal(String cid) {
		sql = "select count(*) from product where cid = ?";
		Long rows = 0L;
		//ScalarHandler() 能够返回一行一列的数据
		try {
			rows =  (Long) qr.query(sql, new ScalarHandler(), cid);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return rows.intValue();
	}
	
	
	public List<Product> findProdutListByCid(String cid, int startIndex, int currentCount) {
		sql = "select * from product where cid = ? limit ?,?";
		try {
			return qr.query(sql, new BeanListHandler<>(Product.class), cid,startIndex,currentCount);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}
	public Product findProductById(String pid) { 
		sql = "select * from product where pid = ?";
		Product product = null;
		try {
			product = qr.query(sql, new BeanHandler<>(Product.class), pid);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return product;
	}
	public void addOrder(Order order) {
		try {
			QueryRunner qr1 = new QueryRunner();
			sql = "insert into orders values (?,?,?,?,?,?,?,?)";
			//这种方式获取的连接在service层是绑定在线程中的同一个连接
			Connection conn = DataSourceUtils.getConnection();
			qr1.update(conn, sql, order.getOid(),order.getOrdertime(),order.getTotal(),
					order.getState(),order.getAddress(),order.getName(),
					order.getTelephone(),order.getUser().getUid());
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	public void addOrderItems(Order order) {
		try {
			QueryRunner qr1 = new QueryRunner();
			//这种方式获取的连接在service层是绑定在线程中的同一个连接
			Connection conn = DataSourceUtils.getConnection();
			for (OrderItem item : order.getOrderItems()) {
				sql = "insert into orderitem values (?,?,?,?,?)";
				qr1.update(conn, sql, item.getItemid(),item.getCount(),
						item.getSubtotal(),item.getProduct().getPid(),order.getOid());
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
	}
	public void updateOrderInfo(Order order) {
		try {
			sql = "update orders set address = ?,name=?,telephone=? where oid =?";
			qr.update(sql, order.getAddress(),order.getName(),order.getTelephone(),order.getOid());
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	//findAllOrder根据用户ID查找所有订单数据--单表查询
			public List<Order> findAllOrders(String uid) throws SQLException {
				QueryRunner runner = new QueryRunner(DataSourceUtils.getDataSource());
				String sql = "select * from orders where uid=?";
				return runner.query(sql, new BeanListHandler<Order>(Order.class), uid);
			}
			
		//findAllOrderItem根据订单ID查找orderitem和product两张表数据
			public List<Map<String, Object>> findAllOrderItems(String oid) throws SQLException {
				QueryRunner runner = new QueryRunner(DataSourceUtils.getDataSource());
				String sql = "select i.count,i.subtotal,p.pimage,p.pname,p.shop_price from "
						+ "orderitem i,product p where i.pid=p.pid and i.oid=?";
				List<Map<String, Object>> listMap = runner.query(sql, new MapListHandler(), oid);
				return listMap;
			}
			public void updateOrderState(String oid) throws SQLException {
				QueryRunner runner = new QueryRunner(DataSourceUtils.getDataSource());
				String sql = "update orders set state=? where oid=?";
				runner.update(sql,1,oid);
		}

}
