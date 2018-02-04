package login;

import java.io.IOException;
import java.util.Date;
import java.util.UUID;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import db.MangoDB;
import email.MailService;
import vo.Registration;
import vo.ResetPassword;

/**
 * Servlet implementation class ResetLink
 */
@WebServlet("/ResetLink")
public class ResetLink extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public ResetLink() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String emailID = request.getParameter("email");
		String query = "&q=%7B%22email%22:%22"+emailID+"%22%7D&l=1";//q={"email": "emailID"}
		String registrationDetailsJson = MangoDB.getDocumentWithQuery("gst-registration", "gst-registration", null, MangoDB.mlabKeySonu,query);
		
		Gson  json = new Gson();
		Registration registrationDetails = json.fromJson(registrationDetailsJson, new TypeToken<Registration>() {}.getType());
		if (null != registrationDetails){
			String uuid = UUID.randomUUID().toString();
			ResetPassword resetPasswordvo = new ResetPassword();
			resetPasswordvo.setEmail(emailID);
			resetPasswordvo.setUuid(uuid);
			resetPasswordvo.setGenerateTime( new Date().getTime());
			request.getSession().setAttribute(uuid, resetPasswordvo);
			String resetPasswordvoStr = json.toJson(resetPasswordvo, new TypeToken<ResetPassword>() {}.getType());
			MangoDB.createNewCollectionWithData("gst-registration", "reset-pwd",resetPasswordvoStr,MangoDB.mlabKeySonu);
			System.out.println("uuid ="+uuid);
			String link = "Dear "+registrationDetails.getOwnerName()+","+
					"<br/><br/> On your request we have sent you a link to reset your password. Please click on the below link and reset your password. For security reasons the link is valid only for 10 minutes and the link will only work from the machine from where you request it."+
					"<br/><br/>"+
					"<a href='https://maa-durga-electronics.appspot.com/web/ChangePassword.html?ref="+uuid+"'> Reset password </a>"+
					"<br/><br/> Your user ID is "+registrationDetails.getUserID()+"."+
					"<br/><br/> Warm Regards";
			new MailService().sendMultipartMail(registrationDetails.getEmail(), null,registrationDetails.getbName(),null, "Reset your password", link);
		}
		
	}

	
}
