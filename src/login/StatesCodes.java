package login;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.gson.Gson;

/**
 * Servlet implementation class StatesCodes
 */
@WebServlet("/StatesCodes")
public class StatesCodes extends HttpServlet {
	private static final long serialVersionUID = 1L;
	public static List<String> stateCodes;
	
    /**
     * @see HttpServlet#HttpServlet()
     */
    public StatesCodes() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		Gson  json = new Gson();
		response.addHeader("Cache-Control", "max-age=86400");//1 day
		response.getWriter().append(json.toJson(stateCodes));
	}

	static{
		stateCodes = new ArrayList<String>();
		stateCodes.add("01-Jammu & Kashmir");
		stateCodes.add("02-Himachal Pradesh");
		stateCodes.add("03-Punjab");
		stateCodes.add("04-Chandigarh");
		stateCodes.add("05-Uttarakhand");
		stateCodes.add("06-Haryana");
		stateCodes.add("07-Delhi");
		stateCodes.add("08-Rajasthan");
		stateCodes.add("09-Uttar Pradesh");
		stateCodes.add("10-Bihar");
		stateCodes.add("11-Sikkim");
		stateCodes.add("12-Arunachal Pradesh");
		stateCodes.add("13-Nagaland");
		stateCodes.add("14-Manipur");
		stateCodes.add("15-Mizoram");
		stateCodes.add("16-Tripura");
		stateCodes.add("17-Meghalaya");
		stateCodes.add("18-Assam");
		stateCodes.add("19-West Bengal");
		stateCodes.add("20-Jharkhand");
		stateCodes.add("21-Odisha");
		stateCodes.add("22-Chhattisgarh");
		stateCodes.add("23-Madhya Pradesh");
		stateCodes.add("24-Gujarat");
		stateCodes.add("25-Daman & Diu");
		stateCodes.add("26-Dadra & Nagar Haveli");
		stateCodes.add("27-Maharashtra");
		stateCodes.add("29-Karnataka");
		stateCodes.add("30-Goa");
		stateCodes.add("31-Lakshdweep");
		stateCodes.add("32-Kerala");
		stateCodes.add("33-Tamil Nadu");
		stateCodes.add("34-Puducherry");
		stateCodes.add("35-Andaman & Nicobar Islands");
		stateCodes.add("36-Telangana");
		stateCodes.add("37-Andhra Pradesh");
		stateCodes.add("97-Other Territory");

	}
       

}
