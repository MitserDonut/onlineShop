package cn.work.service;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import cn.work.dao.ProductDao;
import cn.work.domain.Category;
import cn.work.domain.Order;
import cn.work.domain.PageBean;
import cn.work.domain.Product;
import cn.work.utils.DataSourceUtils;

public class ProductService {
	
	private ProductDao dao = new ProductDao();

	public List<Product> getHotProducts() {
		return dao.getHotProducts();
	}

	public List<Product> getNewProducts() {
		return dao.getNewProducts();
	}

	public List<Category> getCategoryies() {
		return dao.getCategoryies();
	}

	public PageBean<Product> findPorductListCid(String cid,int currentPage,int currentCount) {
		PageBean<Product> pageBean = new PageBean<>();
		//显示第一页数据
		//int currentPage = 1;
		pageBean.setCurrentPage(currentPage);
		//int currentCount = 12;
		pageBean.setCurrentCount(currentCount);
		//获取总条数
		ProductDao dao = new ProductDao();
		int totalCount = dao.getTotal(cid);
		pageBean.setTotalCount(totalCount);
		//总页数
		int totalPage = (int)Math.ceil(totalCount*1.0 / currentCount);
		pageBean.setTotalPage(totalPage);
		//调用dao获取数据  select form product where cid = ? limit startIndex,currentCount
		int startIndex = (currentPage - 1) * currentCount;
		List<Product> list = dao.findProdutListByCid(cid,startIndex,currentCount);
		pageBean.setList(list);
		return pageBean;
	}

	public Product findProductById(String pid) {
		ProductDao dao = new ProductDao();
		return dao.findProductById(pid);
	}

	public void submitOrder(Order order) {
		try {
			//1.开启事务
			DataSourceUtils.startTransaction();
			//2.调用dao方法
			ProductDao dao = new ProductDao();
			dao.addOrder(order);
			dao.addOrderItems(order);
		} catch (Exception e) {
			//事务回滚
			try {
				DataSourceUtils.rollback();
			} catch (SQLException e1) {
				e1.printStackTrace();
			}
		}finally {
			//提交事务和释放资源
			try {
				DataSourceUtils.commitAndRelease();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}

	public void updateOrderInfo(Order order) {
		ProductDao dao = new ProductDao();
		dao.updateOrderInfo(order);
	}
	//findAllOrders方法根据用户ID查找所有订单
			public List<Order> findAllOrders(String uid) {
				List<Order> orderList = null;
				try {
					orderList = dao.findAllOrders(uid);
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				return orderList;
			}
		//findAllOrderItems方法根据订单ID查找订单下所有子项
			public List<Map<String, Object>> findAllOrderItems(String oid) {
				List<Map<String, Object>> itemList = null;
				try {
					itemList = dao.findAllOrderItems(oid);
				} catch (SQLException e) {
					e.printStackTrace();
				}
				return itemList;
			}
			public void updateOrderState(String oid) {
				// TODO Auto-generated method stub
				try {
					dao.updateOrderState(oid);
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}

}
