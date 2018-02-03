package invoice;

import java.io.IOException;
import java.io.OutputStream;
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
import vo.InvoiceItem;
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
			
			String query = "&s=%7B%22invoiceNo%22:-1%7D";
			int count =10;
			if (request.getParameter("count") != null){
				count = Integer.parseInt(request.getParameter("count"));
				query += "&l="+count; // sort by invoice desc and limit 1 record
			}
			
			if (request.getParameter("month") != null){
				month = Integer.parseInt(request.getParameter("month"));
			}
			
			if (request.getParameter("year") != null){
				year = Integer.parseInt(request.getParameter("year"));
			}
			
			String invoicesStr = "["+MangoDB.getDocumentWithQuery(registrationDetails.getMdbInvoiceStore()+"-"+year, ""+month, null, MangoDB.mlabKeySonu,query)+"]";
			
			if ("csv".equalsIgnoreCase(request.getParameter("format") )){
				Gson  json = new Gson();
				List<InvoiceDetails> invoiceList= json.fromJson(invoicesStr, new TypeToken<List<InvoiceDetails>>() {}.getType());
				response.setContentType("text/csv");
				response.setHeader("Content-Disposition", "attachment; filename=\""+year+"_"+month+".csv\"");
				OutputStream outputStream = response.getOutputStream();
				String header = "Type,Place Of Supply,Rate,Taxable Value,Cess Amount,E-Commerce GSTIN\n";
				outputStream.write(header.getBytes());
				for (InvoiceDetails invoice: invoiceList){
					String commonFields = invoice.getModeOfSale()+","+invoice.getShippingState()+",";
					List<InvoiceItem> items = invoice.getMyCart();
					items.addAll(invoice.getMyCartManual());
					for (InvoiceItem item: items){
						String lineItem = commonFields +(item.getCgst()+item.getSgst()+item.getIgst())+","+item.getTaxableValue()+","+invoice.getEcommerceGSTN()+"\n";
						outputStream.write(lineItem.getBytes());
					}
					 
				}
				outputStream.flush();
		        outputStream.close();
				
			}else {
				response.getWriter().append(invoicesStr);
			}
			
			
			
			
			
			
			
			
			
		}else {
			response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
		}
		
	}
	
	

	

}
