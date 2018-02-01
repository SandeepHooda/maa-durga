package pdf;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
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
import com.itextpdf.text.DocumentException;

import db.MangoDB;
import vo.InvoiceDetails;
import vo.InvoiceItem;
import vo.Registration;

/**
 * Servlet implementation class Print
 */
@WebServlet("/Print")
public class Print extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public Print() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		ByteArrayOutputStream baos;
		try {
			Registration registrationDetails = (Registration)request.getSession().getAttribute("registrationDetails");
			long time = Long.parseLong(request.getParameter("time"));
			long invoiceNo = Long.parseLong(request.getParameter("invoiceNo"));
			if (null != registrationDetails){
				List<InvoiceItem> invoice = new ArrayList<InvoiceItem>();
				InvoiceItem item = new InvoiceItem();
				invoice.add(item);
				Calendar cal = new GregorianCalendar();
				cal.setTimeInMillis(time);
				int year = cal.get(Calendar.YEAR);
				int month = 1+cal.get(Calendar.MONTH);
				
				String invoicesStr = MangoDB.getDocumentWithQuery(registrationDetails.getMdbInvoiceStore()+"-"+year, ""+month, ""+invoiceNo, MangoDB.mlabKeySonu,null);
				
				Gson  json = new Gson();
				InvoiceDetails aInvoice= json.fromJson(invoicesStr, new TypeToken<InvoiceDetails>() {}.getType());
				
				baos = new InvoiceToPDF().getPdfBytes(registrationDetails,aInvoice);
				// setting some response headers
		        response.setHeader("Expires", "0");
		        response.setHeader("Cache-Control",
		            "must-revalidate, post-check=0, pre-check=0");
		        response.setHeader("Pragma", "public");
		        // setting the content type
		        response.setContentType("application/pdf");
		        // the contentlength
		        response.setContentLength(baos.size());
		     // write ByteArrayOutputStream to the ServletOutputStream
	            OutputStream os = response.getOutputStream();
	            baos.writeTo(os);
	            os.flush();
	            os.close();
			}else {
				response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
			}
			
		} catch (Exception e) {
			
			e.printStackTrace();
		}
		
		
	}

	

}
