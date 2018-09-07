package cn.work.web;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import cn.work.dao.AdminDao;
import cn.work.domain.Admin;
import cn.work.domain.Product;
import cn.work.service.UserService;

/**
 * Servlet implementation class AdminServlet
 */
public class AdminServlet extends BaseServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public AdminServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	public void adLogin(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
			String username = null;
			String password = null;
			
				username = request.getParameter("adusername");
				password = request.getParameter("adpassword");
				if((username!=null)&&(password!=null)){//存在账号密码
					//数据库验证
					UserService ls = new UserService();
					Admin admin = new Admin();
					admin.setAdname(username);
					admin.setAdpassword(password);
					Admin gAdmin = ls.adLogin(admin);
					//验证成功
					if(gAdmin!=null){
							HttpSession hs = request.getSession();
							hs.setAttribute("admin",gAdmin );
							response.sendRedirect("admin/home.jsp");
							return;
						
					}
					//验证失败
					else{
						request.setAttribute("msg", "用户名或密码错误");
						request.getRequestDispatcher("error.jsp").forward(request, response);
					}
				}
				else{
					request.setAttribute("msg", "用户名或密码错误");
					request.getRequestDispatcher("error.jsp").forward(request, response);
				}
			}
		
		public void adminProductInfo(HttpServletRequest request, HttpServletResponse response)
				throws ServletException, IOException {
			List<Product> product = new ArrayList<>();
			
			AdminDao dao = new AdminDao();
			product = dao.query(product);
			System.out.println(product.toString());
			request.setAttribute("productList", product);
			request.getRequestDispatcher("admin/product/list.jsp").forward(request, response);
		}
}



