package inventory;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.gson.Gson;

import db.MangoDB;
import login.StatesCodes;
import vo.InventortWithStateCodes;
import vo.Registration;

/**
 * Servlet implementation class Inventory
 */
@WebServlet("/Inventory")
public class Inventory extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public Inventory() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		Registration registrationDetails = (Registration)request.getSession().getAttribute("registrationDetails");
		if (null != registrationDetails){
			InventortWithStateCodes inventortWithStateCodes = new InventortWithStateCodes();
			inventortWithStateCodes.setStateCodes(StatesCodes.stateCodes);
			
			inventortWithStateCodes.setMyGstStateCode(registrationDetails.getGSTIN().substring(0,2));
			inventortWithStateCodes.setInventoryItems(registrationDetails.getInventory());
			Gson  json = new Gson();
			//response.addHeader("Cache-Control", "max-age=86400");//1 day
			response.getWriter().append(json.toJson(inventortWithStateCodes));
		}else {
			response.getWriter().append("");
		}
		
		
		
	}

	

}
