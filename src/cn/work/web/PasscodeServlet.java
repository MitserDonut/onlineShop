package cn.work.web;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Servlet implementation class PasscodeServlet
 */
public class PasscodeServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public PasscodeServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		String passcode = request.getParameter("passcode");
		if(passcode != null){
			String varify = (String) request.getSession().getAttribute("code_session");
			if((varify !=null) &&(varify.equalsIgnoreCase(passcode) )){
				String json = "{\"passcode\":"+true+"}";
				response.getWriter().write(json);
			}
			else{
				String json = "{\"passcode\":"+false+"}";
				response.getWriter().write(json);
			}
		}
		else{
			String json = "{\"passcode\":"+false+"}";
			response.getWriter().write(json);
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
