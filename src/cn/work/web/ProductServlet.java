package cn.work.web;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.ResourceBundle;
import java.util.UUID;

import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.beanutils.BeanUtils;

import cn.work.domain.Cart;
import cn.work.domain.CartItem;
import cn.work.domain.Category;
import cn.work.domain.Order;
import cn.work.domain.OrderItem;
import cn.work.domain.PageBean;
import cn.work.domain.Product;
import cn.work.domain.User;
import cn.work.service.ProductService;
import cn.work.utils.PaymentUtil;

/**
 * Servlet implementation class ProductServlet
 */
public class ProductServlet extends BaseServlet {
	
//	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
//		String method = request.getParameter("method");
//		if(method.equals("index")){
//			this.index(request, response);
//		}else if(method.equals("product_list")){
//			this.product_list(request, response);
//		}else if(method.equals("productInfo")){
//			this.productInfo(request, response);
//		}
//	}

	
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public void index(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		ProductService service = new ProductService();
		//1.调用service获取最新商品和热门商品
		List<Product> hot_products = service.getHotProducts();
		List<Product> new_products = service.getNewProducts();
		//2.把2种商品request域中
		request.setAttribute("hot_products", hot_products);
		request.setAttribute("new_products", new_products);
		//获取商品的分类信息
		List<Category> categories = service.getCategoryies();
		//把分类信息放在session域中，一个用户访问所有页面都可以获取分类信息
		request.getSession().setAttribute("categories", categories);
		request.getRequestDispatcher("index.jsp").forward(request, response);
	
	}
	
	
	public  void product_list(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		//获取当前页
		String currentPage = request.getParameter("currentPage");
		int now_page = 0;
		if(currentPage == null) {
			now_page = 1;
		}else{
			now_page = Integer.parseInt(currentPage);
		}
		//每页显示条数
		int currentCount = 12;
		
		//1.获取分类id
		String cid = request.getParameter("cid");
		System.out.println(cid);
		//2.调用service层，根据分类id来获取商品
		ProductService service = new ProductService();
		PageBean<Product> pageBean = service.findPorductListCid(cid,now_page,currentCount);
		request.setAttribute("pageBean", pageBean);
		
		//把分类id放入request域中
		request.setAttribute("cid", cid);
		//3.跳转到product_list.jsp页面，显示商品
		
		//显示浏览记录
		//通过cookie获取
		Cookie[]  cookies = request.getCookies();
		//定义一个接收历史浏览记录的列表
		List<Product> historyList = new ArrayList<>();
		if(cookies != null){
			for (Cookie cookie : cookies) {
				if("pids".equals(cookie.getName())){
					String pids = cookie.getValue();//（2,1,3）
					String[] pids_arr = pids.split(",");
					for (String pid : pids_arr) {
						//根据产品编号查找产品
						Product product = service.findProductById(pid);
						historyList.add(product);
					}
				}
			}
		}
		request.setAttribute("historyList",historyList);
		//
		request.getRequestDispatcher("product_list.jsp").forward(request, response);
	
	}
	
	public void productInfo(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		//1.接收商品id
		String pid = request.getParameter("pid");
		//2.调用service获取商品详细信息
		ProductService service = new ProductService();
		Product p = service.findProductById(pid);
		//3.把商品信息返回给页面并显示
		request.setAttribute("product", p);
		request.setAttribute("history", p);
		//把商品id放入cookie
		//定义一个商品id的value，放入cookie
		String pids = pid;
		//获取所有的cookie
		Cookie[] cookies = request.getCookies();
		if(cookies!=null){
			for (Cookie cookie : cookies) {
				if("pids".equals(cookie.getName())){
					//保存浏览记录商品的id 1,2,3    2
					pids = cookie.getValue();
					String[] strs = pids.split(",");
					//把数组转换成list来操作
					List<String> arrList = Arrays.asList(strs);
					//
					LinkedList<String> list = new LinkedList<>(arrList);
					if(list.contains(pid)){
						//移除存在的id
						list.remove(pid);
					}
					//判断链表是否等于7
					if(list.size()== 7)
					{
						list.remove(6);
					}
					//无论重不重复都要添加在起始位置
					list.addFirst(pid);
					//把list中数据做成字符串，以逗号分隔  2，1，3,
					StringBuffer sb = new StringBuffer();
					for(int i=0;i<list.size();i++){
						sb.append(list.get(i));
						sb.append(",");
					}
					//去除最后一个逗号
					sb.substring(0, sb.length()-1);
					pids = sb.toString();
				}
			}
		}
		
		//创建cookie，把pids的值返回给浏览器
		Cookie cookie = new Cookie("pids", pids);
		response.addCookie(cookie);
		//把分类id，当前页放入request域中
		request.setAttribute("cid",request.getParameter("cid"));
		request.setAttribute("currentPage", request.getParameter("currentPage"));
		request.getRequestDispatcher("product_info.jsp").forward(request, response);
	}
	
	public  void addCart(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		//1.接收商品id和数量
		String pid = request.getParameter("pid");
		String num = request.getParameter("buyNum");
		//2.获取商品
		ProductService service = new ProductService();
		Product product = service.findProductById(pid);
		//3.判断传入的数量
		int buyNum = 1;
		if(num != null){
			buyNum = Integer.parseInt(num);
			if(buyNum < 0){
				request.setAttribute("msg", "请输入正确的数量后提交");
				request.getRequestDispatcher("error.jsp").forward(request, response);
				return;
			}
		}
		//传入的数量合法
		//计算小计
		double subTotal = buyNum * product.getShop_price();
		//把信息放入cartitem
		CartItem item = new CartItem(product, buyNum, subTotal);
		//把商品放入购物车
		HttpSession session = request.getSession();
		Cart cart = (Cart) session.getAttribute("cart");
		if(cart == null){
			cart = new Cart();
		}
		
		
		//判断商品是否存在于购物车，如果存在，把商品数量增加
		Map<String, CartItem> cartItems = cart.getCartItems();
		if(cartItems.containsKey(pid)){
			CartItem item2 = cartItems.get(pid);
			int new_num = 0;
			new_num = item2.getBuyNum() + buyNum;
			//新的小计
			double new_subTotal = item2.getSubTotal()+subTotal;
			//把数量和小计放入当前cartitem
			cartItems.get(pid).setBuyNum(new_num);
			cartItems.get(pid).setSubTotal(new_subTotal);
		}else{
			//如果session中有购物车，把商品放入购物车
			cart.getCartItems().put(pid, item);
		}
		
		
		//总计= 原来的值+小计
		cart.setTotal(cart.getTotal()+subTotal);
		//再次存回到session
		session.setAttribute("cart", cart);
		//跳转到购物车页面,为了避免刷新页面时重复提交数据，使用sendRedirect重定向
		response.sendRedirect(request.getContextPath()+"/cart.jsp");
		
	}
	
	public void delProduct(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		HttpSession session = request.getSession();
		//在session中删除一个商品
		String pid = request.getParameter("pid");
		Cart cart = (Cart) session.getAttribute("cart");
		if(cart!=null){
			Map<String, CartItem> list = cart.getCartItems();
			//从新计算总计
			cart.setTotal(cart.getTotal() - list.get(pid).getSubTotal());
			list.remove(pid);
		}
		//再次存回到session
		session.setAttribute("cart", cart);
		//跳转到购物车页面,为了避免刷新页面时重复提交数据，使用sendRedirect重定向
		response.sendRedirect(request.getContextPath()+"/cart.jsp");
	}
	
	//提交订单方法
	public void submitOrder(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		//1.判断用户是否登录
		HttpSession session = request.getSession();
		User user = (User) session.getAttribute("user");
		if(user == null){
			//跳转到登录页面
			response.sendRedirect(request.getContextPath()+"/login.jsp");
		}else{
			//2.往订单表和订单明细表插入购物车数据
			//创建Order对象
			Order order = new Order();
			order.setOid(UUID.randomUUID().toString());
			order.setOrdertime(new Date());
			//从session中获取购物车
			Cart cart = (Cart) session.getAttribute("cart");
			order.setTotal(cart.getTotal());//总金额
			order.setState(0);
			order.setUser(user);
			
			//把cartItem转换成orderItem
			Map<String, CartItem> list = cart.getCartItems();
			for (Entry<String, CartItem> entry : list.entrySet()) {
				//得到每个购物项
				CartItem cartItem = entry.getValue();
				//新建OrderItem
				OrderItem orderItem = new OrderItem();
				orderItem.setItemid(UUID.randomUUID().toString());
				orderItem.setCount(cartItem.getBuyNum());
				orderItem.setSubtotal(cartItem.getSubTotal());
				orderItem.setProduct(cartItem.getProduct());
				orderItem.setOrder(order);
				order.getOrderItems().add(orderItem);
			}
			//调用service层
			ProductService service = new ProductService();
			service.submitOrder(order);
			//返回结果
			session.setAttribute("order", order);
			response.sendRedirect(request.getContextPath()+"/order_info.jsp");
		}
	}
	
//	public  void confirmOrder(HttpServletRequest request, HttpServletResponse response)
//			throws ServletException, IOException {
//		//接收页面参数
//		//从session中获取order对象
//		HttpSession session = request.getSession();
//		Order order = (Order) session.getAttribute("order");
//		//从页面封装数据到order对象
//		try {
//			BeanUtils.populate(order, request.getParameterMap());
//			System.out.println("address:"+order.getAddress());
//			//在orders表中更新用户的收货信息
//			ProductService service = new ProductService();
//			service.updateOrderInfo(order);
//		} catch (IllegalAccessException | InvocationTargetException e) {
//			e.printStackTrace();
//		}
//	}
	
	//确认订单
		public void confirmOrder(HttpServletRequest request, HttpServletResponse response)
				throws ServletException, IOException {
			ProductService service = new ProductService();
			HttpSession session = request.getSession();
			// 表单信息,从session域中获取
			Order order =(Order)session.getAttribute("order");
			
				try {
					BeanUtils.populate(order, request.getParameterMap());
					// 1.更新用户的收货信息
					service.updateOrderInfo(order);
				} catch (IllegalAccessException | InvocationTargetException e) {
					
					e.printStackTrace();
				}
				

				 // 2.完成支付功能
				// 获得 支付必须基本数据
				Order order1 = new Order();
				String orderid = order1.getOid();
				//String money = order.getTotal()+"";
				String money = "0.01";
				System.out.println(money);
				// 银行
				//String pd_FrpId = request.getParameter("pd_FrpId");
				String pd_FrpId ="ABC-NET";
				// 发给支付公司需要哪些数据
				String p0_Cmd = "Buy";
				String p1_MerId = ResourceBundle.getBundle("merchantInfo").getString("p1_MerId");
				String p2_Order = orderid;
				String p3_Amt = money;
				String p4_Cur = "CNY";
				String p5_Pid = "";
				String p6_Pcat = "";
				String p7_Pdesc = "";
				// 支付成功回调地址 ---- 第三方支付公司会访问、用户访问
				// 第三方支付可以访问网址
				String p8_Url = ResourceBundle.getBundle("merchantInfo").getString("callback");
				String p9_SAF = "";
				String pa_MP = "";
				String pr_NeedResponse = "1";
				// 加密hmac 需要密钥
				String keyValue = ResourceBundle.getBundle("merchantInfo").getString("keyValue");
				String hmac = PaymentUtil.buildHmac(p0_Cmd, p1_MerId, p2_Order, p3_Amt, p4_Cur, p5_Pid, p6_Pcat, p7_Pdesc,
						p8_Url, p9_SAF, pa_MP, pd_FrpId, pr_NeedResponse, keyValue);
				String url = "https://www.yeepay.com/app-merchant-proxy/node?pd_FrpId=" + pd_FrpId + "&p0_Cmd=" + p0_Cmd
						+ "&p1_MerId=" + p1_MerId + "&p2_Order=" + p2_Order + "&p3_Amt=" + p3_Amt + "&p4_Cur=" + p4_Cur
						+ "&p5_Pid=" + p5_Pid + "&p6_Pcat=" + p6_Pcat + "&p7_Pdesc=" + p7_Pdesc + "&p8_Url=" + p8_Url
						+ "&p9_SAF=" + p9_SAF + "&pa_MP=" + pa_MP + "&pr_NeedResponse=" + pr_NeedResponse + "&hmac=" + hmac;
				// 重定向到第三方支付平台
				System.out.println(url);
				response.sendRedirect(url);

		}
		
		public void myOrders(HttpServletRequest request, HttpServletResponse response)
				throws ServletException, IOException {
			HttpSession session = request.getSession();
			// 验证用户是否登录
			User user = (User) session.getAttribute("user");
			if (user == null) {
				// 未登录状态
				response.sendRedirect(request.getContextPath() + "/login.jsp");
				return;
			}	
			//用户肯定登录了
			ProductService service = new ProductService();
			//查找该用户下的所有订单集合
			List<Order> orderList = service.findAllOrders(user.getUid());
			if(orderList!=null)
			{
				//遍历集合
				for(Order order : orderList)
				{
					//获取每一个Order对象,根据oid查找对应的List<OrderItem>订单项的集合
					List<Map<String, Object>> listMap = service.findAllOrderItems(order.getOid());
					
					//组装订单下的所有订单项数据
					for(Map<String, Object> map : listMap)
					{
						try {
							OrderItem orderItem = new OrderItem();
							//orderItem.setCount(Integer.parseInt(map.get("count").toString()));//手动单个转换
							//映射orderitem对象
							BeanUtils.populate(orderItem, map);
							Product product = new Product();
							///映射product对象
							BeanUtils.populate(product, map);//pimage,pname,shop_price
							//关联两者之间的关系
							orderItem.setProduct(product);
							//orderItem存入order的List<OrderItem>订单项的集合
							order.getOrderItems().add(orderItem);
						} catch (IllegalAccessException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						} catch (InvocationTargetException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}//count,subtotal
					}
				}
			}
			//将存入
			request.setAttribute("orderList", orderList);
			//跳转到前台页面
			request.getRequestDispatcher("/order_list.jsp").forward(request, response);
	}
		
	
	//清空购物车方法
			public void clearCart(HttpServletRequest request, HttpServletResponse response)
					throws ServletException, IOException {
				HttpSession session = request.getSession();
				//清除
				session.removeAttribute("cart");
				//跳转
				//页面的跳转
				response.sendRedirect(request.getContextPath()+"/cart.jsp");
			}

	

}
