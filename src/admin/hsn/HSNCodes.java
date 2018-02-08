package admin.hsn;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import db.MangoDB;
import vo.Product;
import vo.Registration;

/**
 * Servlet implementation class HSNCodes
 */
@WebServlet("/HSNCodes")
public class HSNCodes extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public HSNCodes() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		Registration registrationDetails = (Registration)request.getSession().getAttribute("registrationDetails");
		if (null == registrationDetails){
			response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
		}else {
			Gson  json = new Gson();
			String products = json.toJson(registrationDetails.getProducts(), new TypeToken<List<Product>>() {}.getType());
			response.getWriter().append(products);
		}
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPut(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		Registration registrationDetails = (Registration)request.getSession().getAttribute("registrationDetails");
		if (null == registrationDetails){
			response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
		}else {
			String productsStr =  request.getParameter("products");
			Gson  json = new Gson();
			List<Product> products = json.fromJson(productsStr, new TypeToken<List<Product>>() {}.getType());
			registrationDetails.setProducts(products);
			String registrationDetailsJson = json.toJson(registrationDetails, new TypeToken<Registration>() {}.getType());
			MangoDB.insertOrUpdateData("gst-registration", "gst-registration",registrationDetailsJson,MangoDB.mlabKeySonu,registrationDetails.get_id());
			response.getWriter().append("Done");
		}
	}

}
