package cn.work.web;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Servlet implementation class BaseServlet
 */
public class BaseServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String methodName = request.getParameter("method");
		if(methodName!=null && methodName.length()>0){
			try {
				//根据获取的字符串取执行与字符串相同的方法  index -->index(request, response);\\
				//1.获取方法名
				
				//2.得到servlet请求的字节码  user-->UserServlet  product-->ProductServlet
				Class clazz = this.getClass();
				//3.根据字节码（servlet）和方法名获取方法
				//所有要执行的方法都需要HttpServletRequest和HttpServletResponse参数
				Method method = clazz.getMethod(methodName, HttpServletRequest.class,HttpServletResponse.class);
				//4.调用invoke方法取执行所需的方法
				method.invoke(this, request,response);
			} catch (NoSuchMethodException | SecurityException | IllegalAccessException | IllegalArgumentException
					| InvocationTargetException e) {
				e.printStackTrace();
			} 
		}
		
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}

}
