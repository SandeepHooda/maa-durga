package login;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import db.MangoDB;
import vo.Registration;

/**
 * Servlet implementation class Login
 */
@WebServlet("/Login")
public class Login extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public Login() {
        super();
       
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String userID = request.getParameter("userID");
		String pwd = request.getParameter("pwd");
		Registration registrationDetails = (Registration)request.getSession().getAttribute("registrationDetails");
		if (null == registrationDetails){
			String registrationDetailsJson = MangoDB.getADocument("gst-registration", "maa-durga-electronics", userID, MangoDB.mlabKeySonu);
			Gson  json = new Gson();
			registrationDetails = json.fromJson(registrationDetailsJson, new TypeToken<Registration>() {}.getType());
		}
		
		if (null != registrationDetails && registrationDetails.getPwd().equals(pwd)){
			request.getSession().setAttribute("registrationDetails",registrationDetails );
			response.getWriter().append(userID);
		}else {
			response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
		}
		
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		doGet(request, response);
	}

}
