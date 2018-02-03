package invoice;

import java.io.IOException;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import db.MangoDB;
import misc.Utils;
import vo.InvoiceDetails;
import vo.Registration;

/**
 * Servlet implementation class RecentInvoices
 */
@WebServlet("/RecentInvoices")
public class RecentInvoices extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public RecentInvoices() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		Registration registrationDetails = (Registration)request.getSession().getAttribute("registrationDetails");
		if (null != registrationDetails){
			Calendar cal = Utils.getCalender();
			Utils.setTimeZone();
			int year = cal.get(Calendar.YEAR);
			int month = 1+cal.get(Calendar.MONTH);
			
			int count =10;
			if (request.getParameter("count") != null){
				count = Integer.parseInt(request.getParameter("count"));
			}
			String query = "&s=%7B%22invoiceNo%22:-1%7D&l="+count; // sort by invoice desc and limit 1 record
			String invoicesStr = MangoDB.getDocumentWithQuery(registrationDetails.getMdbInvoiceStore()+"-"+year, ""+month, null, MangoDB.mlabKeySonu,query);
			
			response.getWriter().append("["+invoicesStr+"]");
		}else {
			response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
		}
		
	}

	

}
