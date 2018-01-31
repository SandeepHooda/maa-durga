package invoice;

import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;

import db.MangoDB;
import vo.InvoiceDetails;
import vo.InvoiceDetailsSort;
import vo.Registration;

/**
 * Servlet implementation class SubmitInvoice
 */
@WebServlet("/SubmitInvoice")
public class SubmitInvoice extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public SubmitInvoice() {
        super();
        // TODO Auto-generated constructor stub
    }

	

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		Gson  json = new Gson();
		String invoiceDetailsStr = request.getParameter("invoiceDetails");
		invoiceDetailsStr = invoiceDetailsStr.replaceAll("[^\\d. A-Za-z:\\\"\\-{},\\[\\]]", "");
		/*System.out.println(" invoiceDetailsStr ="+invoiceDetailsStr);
		JsonReader reader = new JsonReader(new StringReader(invoiceDetailsStr));
		reader.setLenient(true);*/
		InvoiceDetails invoiceDetails= json.fromJson(invoiceDetailsStr,  InvoiceDetails.class);
		
		Registration registrationDetails = (Registration)request.getSession().getAttribute("registrationDetails");
		if (null == registrationDetails){
			response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
		}else {
			long latestInvoiceNo = getLatestInvoive(registrationDetails, invoiceDetails.getInvoiceTime());
			if (latestInvoiceNo == 0){
				latestInvoiceNo = registrationDetails.getInvoiceStart();
			}else {
				latestInvoiceNo++;
			}
			invoiceDetails.setInvoiceNo(latestInvoiceNo);
			invoiceDetails.set_id(""+latestInvoiceNo);
			
			Calendar cal = new GregorianCalendar();
			cal.setTimeInMillis(invoiceDetails.getInvoiceTime());
			int year = cal.get(Calendar.YEAR);
			int month = 1+cal.get(Calendar.MONTH);
			/* This is a test code
			List<InvoiceDetails> list = new ArrayList<InvoiceDetails>();
			for (int i=0;i<1100;i++){
				
				InvoiceDetails invoiceDetailsNew = new InvoiceDetails();
				latestInvoiceNo++;
				invoiceDetailsNew.set_id(""+latestInvoiceNo);
				invoiceDetailsNew.setInvoiceNo(latestInvoiceNo);
				list.add(invoiceDetailsNew);
				
			}
			MangoDB.createNewCollectionWithData(""+month, registrationDetails.getMdbInvoiceStore()+"-"+year, json.toJson(list, new TypeToken<List<InvoiceDetails>>() {}.getType()), MangoDB.mlabKeySonu);
			This is a test code
			*/
			MangoDB.createNewCollectionWithData(""+month, registrationDetails.getMdbInvoiceStore()+"-"+year, json.toJson(invoiceDetails, InvoiceDetails.class), MangoDB.mlabKeySonu);
			response.getWriter().append(""+latestInvoiceNo);
		}
		
	}
	
	private long getLatestInvoive(Registration registrationDetails, long invoiceTimeStamp){
		
		Calendar cal = new GregorianCalendar();
		cal.setTimeInMillis(invoiceTimeStamp);
		cal.set(Calendar.DATE, 1); // We need current month and year don't care much about date but when we go back last last month have date as 1 helps
		int year = cal.get(Calendar.YEAR);
		int month = 1+cal.get(Calendar.MONTH);
		String query = "&s=%7B%22invoiceNo%22:-1%7D&l=1"; // sort by invoice desc and limit 1 record
		String invoicesStr = MangoDB.getDocumentWithQuery(registrationDetails.getMdbInvoiceStore()+"-"+year, ""+month, null, MangoDB.mlabKeySonu,query);
		String noDBMessage = "{ \"message\" : \"Database not found.\"}";
		if ("".equals(invoicesStr) || noDBMessage.equals(invoicesStr)){
			
			for (int i=1;i<=12;i++){
				System.out.println("No invoice for Year "+year+" month "+month+" lets check previous month ");
				cal.add(Calendar.MONTH, -1);
				year = cal.get(Calendar.YEAR);
				month = 1+cal.get(Calendar.MONTH);
				invoicesStr = MangoDB.getDocumentWithQuery(registrationDetails.getMdbInvoiceStore()+"-"+year, ""+month, null, MangoDB.mlabKeySonu,query);
				
				if (noDBMessage.equals(invoicesStr)){
					//No data data base created and no data found no need to look beyond
					return 0L;
				}else if("".equals(invoicesStr)) {
					//keep looking in last 12 months till you don't find "no db message"
					System.out.println(" continue looging previous month");
				}else {
					//Data found in last 12 months
					 return getLatestInvoiceNumber( invoicesStr);
				}
			}
			
		}else {
			//Data found in current month
			return getLatestInvoiceNumber( invoicesStr);
		}
		
		return 0L;
	}
	
	private long getLatestInvoiceNumber(String invoicesStr){
		Gson  json = new Gson();
		InvoiceDetails listOfInvoices= json.fromJson(invoicesStr, new TypeToken<InvoiceDetails>() {}.getType());
		return listOfInvoices.getInvoiceNo();
		
	}

}
