package cn.work.web;

import java.io.IOException;
import java.io.PrintWriter;
import java.lang.reflect.InvocationTargetException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;

import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.beanutils.ConvertUtils;
import org.apache.commons.beanutils.Converter;

import cn.work.dao.RegistDao;
import cn.work.domain.User;
import cn.work.service.UserService;
import cn.work.utils.MD5Utils;
import cn.work.utils.MailUtils;

/**
 * Servlet implementation class UserServlet
 */
public class UserServlet extends BaseServlet {
	private static final long serialVersionUID = 1L;

	public void regist(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String username = request.getParameter("username");
		System.out.println(username);
		String code=request.getParameter("yzm");
		String cs=(String)request.getSession().getAttribute("code_session");
		if(code.equalsIgnoreCase(cs)){
			try {
				ConvertUtils.register(new Converter() {
					@SuppressWarnings("rawtypes")
					@Override
					public Object convert(Class arg0, Object arg1) {
						SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
						Date desc = null;
						try {
							desc = sdf.parse((String) arg1);
						} catch (ParseException e) {
							e.printStackTrace();
						}
						return desc;
					}
				}, Date.class);
				
				//1.接收注册页面参数
				User user = new User();
				BeanUtils.populate(user, request.getParameterMap());
				//往user对象中设置uid，state，code
				user.setUid(UUID.randomUUID().toString());
				user.setState(0);
				user.setCode(UUID.randomUUID().toString());//激活码
				System.out.println(user);
				//2.调用service层
				UserService service = new UserService();
				boolean flag = service.regist(user);
				//3.返回结果
				if(flag){
					//发送激活邮件
					String emailMsg="恭喜您注册成功，请点击下面的链接进行激活账户"
							+"<a href='http://localhost:8888/foodshop/user?method=active&code='"+user.getCode()+">"
							+"http://localhost:8888/foodshop/ActiveServlet?code="+user.getCode()+"</a>";
					MailUtils.sendMail(user.getEmail(), emailMsg);
					response.sendRedirect("registerSuccess.jsp");
				}else{
					response.sendRedirect("registerFail.jsp");
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}else{
		
			PrintWriter out=response.getWriter();
			out.print("<script> alert(\"验证码有误!\"); </script>");
			response.setHeader("refresh","1;URL=http://localhost:8888/IGeekShop/register.jsp");
		}
	}
	
	
	public void active(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		//1.接收激活码
		String code = request.getParameter("code");
		//2.修改状态
		UserService service = new UserService();
		boolean flag = service.active(code);
		if(flag){
			//激活成功
			//跳转到主页
			response.sendRedirect("default.jsp");
		}else{
			//激活失败
			response.sendRedirect("register.jsp");
			//注册界面
		}
	}
	
	public void checkUsername(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		//1.接收用户名
		String username = request.getParameter("username");
		//2.调用service检查用户名
		UserService service = new UserService();
		boolean isExist = service.checkUsername(username);
		//3.返回结果 json数据 {"isExist":true}
		String json = "{\"isExist\":"+isExist+"}";
		response.getWriter().write(json);
		
	}
	
	public  void login(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException, IllegalAccessException, InvocationTargetException {
		String username = null;
		String password = null;
		String autoLogin = request.getParameter("autoLogin");
		
		Cookie[] Cookies = request.getCookies();
		
		if(autoLogin!=null){
			if(Cookies!=null){ //存在缓存
				for(Cookie ck:Cookies){
					if(ck.getName().equals("username")){
						username = ck.getValue();
					}
					if(ck.getName().equals("password")){
						password = ck.getValue();
					}
				}
				if((username!=null&&username.length()!=0)&&(password!=null)&&password.length()!=0){//存在用户缓存
					//进行数据库验证
					UserService ls = new UserService();
					User u = new User();
					u.setUsername(username);
					u.setPassword(password);
					User user = ls.login(u);
					//成功
					if(user!=null){
						if(user.getState()==1){
							HttpSession hs = request.getSession();
							if(hs.getAttribute("user")==null||!hs.getAttribute("user").equals(user)){

								user.setPassword("");
								hs.setAttribute("user", user);
							}
							response.sendRedirect(request.getContextPath()+"/product?method=index");
						}
						else{
							request.setAttribute("msg", "用户已注销或未激活");
							request.getRequestDispatcher("/error.jsp").forward(request, response);
						}
					}
					//验证失败
					else{
						request.setAttribute("msg", "用户名或密码错误");
						request.getRequestDispatcher("error.jsp").forward(request, response);
					}
				}
				else{//不存在用户缓存 按未勾选自动登录处理
					username = request.getParameter("username1");
					password = request.getParameter("password1");
					
					System.out.println("输入密码:"+password);
					System.out.println("输入用户名:"+username);
					
					if((username!=null&&username.length()!=0)&&(password!=null)&&password.length()!=0){//存在账号密码
						//数据库验证
						UserService ls = new UserService();
						User u = new User();
						u.setUsername(username);
						u.setPassword(password);
						User user = ls.login(u);
						//验证成功

						String password_temp = new String(password);
						if((user!=null)&&(user.getState()!=0)){
							//新建cookie
							Cookie cookie = new Cookie("username",username);
							Cookie cookie2 = new Cookie("password",password_temp);
							response.addCookie(cookie);
							response.addCookie(cookie2);
							//user放入session
							HttpSession hs = request.getSession();
							user.setPassword("");
							hs.setAttribute("user", user);
							
							response.sendRedirect(request.getContextPath()+"/product?method=index");
						}
						//验证失败
						else if(user.getState()==0){
							request.setAttribute("msg", "用户已注销或未激活");
							request.getRequestDispatcher("/error.jsp").forward(request, response);
						}
						else{
							request.setAttribute("msg", "用户名或密码错误");
							request.getRequestDispatcher("error.jsp").forward(request, response);
						}
					}
					else{
						request.setAttribute("msg", "该浏览器没有账户缓存，请重新输入账号密码");
						request.getRequestDispatcher("error.jsp").forward(request, response);
					}
				}	
			}
		}
		else{//未勾选自动登录
			username = request.getParameter("username");
			password = request.getParameter("password");
			if((username!=null)&&(password!=null)){//存在账号密码
				//数据库验证
				UserService ls = new UserService();
				User u = new User();
				u.setUsername(username);
				u.setPassword(password);
				User user = ls.login(u);
				//验证成功
				if(user!=null){
					if(user.getState()==1){
						HttpSession hs = request.getSession();
						hs.setAttribute("user", user);
						response.sendRedirect(request.getContextPath()+"/product?method=index");
					}
					else{
						request.setAttribute("msg", "用户已注销");
						request.getRequestDispatcher("/error.jsp").forward(request, response);
					}
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
	}

	public void logout(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		HttpSession hs = request.getSession();
		hs.invalidate();
		response.sendRedirect(request.getContextPath()+"/product?method=index");
	}
	
	
	public void delete(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		User user = new User();
		try {
			BeanUtils.populate(user, request.getParameterMap());
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		String password=MD5Utils.md5(request.getParameter("password"));
        user.setPassword(password);
		RegistDao dao2=new RegistDao();
		boolean isSuccess=dao2.delete(user);
		if(isSuccess){
			PrintWriter out=response.getWriter();
			out.print("<script> alert(\"注销成功!\"); </script>");
			response.setHeader("refresh","1;URL=http://localhost:8888/foodshop/login.jsp ");
		}else{
			PrintWriter out=response.getWriter();
			out.print("<script> alert(\"用户名或者密码有误!\"); </script>");
			response.setHeader("refresh","1;URL=http://localhost:8888/foodshop/delete.jsp ");
		}
	}
	public void infoChange(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		HttpSession hs = request.getSession();
		
		if(hs!=null&&hs.getAttribute("user")!=null){
			User user = new User();
			User newUser =null;
			user.setUid(request.getParameter("uid"));
			user.setUsername(request.getParameter("username"));
			user.setName(request.getParameter("name"));
			user.setEmail(request.getParameter("email"));
			user.setSex(request.getParameter("sex"));
			user.setTelephone(request.getParameter("telephone"));
			
			String s = request.getParameter("birthday");
			
	        //注意：SimpleDateFormat构造函数的样式与strDate的样式必须相符
	        SimpleDateFormat simpleDateFormat=new SimpleDateFormat("yyyy-MM-dd");
	        //必须捕获异常
	        try {
	            Date date=simpleDateFormat.parse(s);
		        user.setBirthday(date);
	        } catch(ParseException px) {
	            px.printStackTrace();
	        }
			
			UserService rs = new UserService();
			newUser = rs.infoChangeService(user);
			if(newUser!=null){
				System.out.println(newUser.toString());
				newUser.setPassword("");
				hs.setAttribute("user", newUser);
				response.getWriter().println("<strong>已修改</strong>");
			}
			else{
				response.getWriter().println("<strong>出错！</strong>");
			}
		}
		
	}
	
}
