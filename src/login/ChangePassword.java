package login;

import java.io.IOException;
import java.util.Date;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import db.MangoDB;
import vo.Registration;
import vo.ResetPassword;

/**
 * Servlet implementation class ChangePassword
 */
@WebServlet("/ChangePassword")
public class ChangePassword extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public ChangePassword() {
        super();
        // TODO Auto-generated constructor stub
    }

	

	protected void doPut(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		Gson  json = new Gson();
		String ref = request.getParameter("ref");
		String pwd = request.getParameter("pwd");
		ResetPassword resetPasswordvo = (ResetPassword)request.getSession().getAttribute(ref);
		//String resetPasswordvoStr = MangoDB.getDocumentWithQuery("gst-registration", "reset-pwd", ref, MangoDB.mlabKeySonu,null);
		//ResetPassword resetPasswordvo = json.fromJson(resetPasswordvoStr, new TypeToken<ResetPassword>() {}.getType());
		
		if (null != resetPasswordvo && null != pwd && pwd.length() >=4 ){
			long timegap =new Date().getTime() - resetPasswordvo.getGenerateTime();
			System.out.println(" timegap ="+timegap);
			if (timegap < 1000*60*resetPasswordvo.getValidTillMinutes()){
				
				
				String query = "&q=%7B%22email%22:%22"+resetPasswordvo.getEmail()+"%22%7D&l=1";//q={"email": "emailID"}
				String registrationDetailsJson = MangoDB.getDocumentWithQuery("gst-registration", "gst-registration", null, MangoDB.mlabKeySonu,query);
				Registration registrationDetails = json.fromJson(registrationDetailsJson, new TypeToken<Registration>() {}.getType());
				registrationDetails.setPwd(pwd);
				
				registrationDetailsJson = json.toJson(registrationDetails, new TypeToken<Registration>() {}.getType());
				MangoDB.insertOrUpdateData("gst-registration", "gst-registration",registrationDetailsJson,MangoDB.mlabKeySonu,registrationDetails.get_id());
			}else {
				System.out.println(" timegap ="+timegap);
				response.getWriter().append("Your link is expored");
				response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
			}
			
			
		}else {
			System.out.println(" resetPasswordvo ="+resetPasswordvo);
			response.getWriter().append("Please try from the same machine from where requested the link.");
			response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
		}
		
	}

}
