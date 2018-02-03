package vo;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.TimeZone;

import misc.Utils;

public class InvoiceDetails {
	private String _id;
	private long invoiceTime;
	private String customerName;
	private String shippingAddress;
	private String shippingState;
	private String customerPhone;
	private String customerEmail;
	private List<InvoiceItem> myCartManual;
	private List<InvoiceItem>  myCart;
	private long invoiceNo;
	private String modeOfSale = "OE";//E = ecomerce, OE = other that Ecommerce
	private String ecommerceGSTN;
	
	
	public long getInvoiceTime() {
		return invoiceTime;
	}
	public void setInvoiceTime(long invoiceTime) {
		this.invoiceTime = invoiceTime;
	}
	public String getCustomerName() {
		return customerName;
	}
	public void setCustomerName(String customerName) {
		this.customerName = customerName;
	}
	public String getShippingAddress() {
		return shippingAddress;
	}
	public void setShippingAddress(String shippingAddress) {
		this.shippingAddress = shippingAddress;
	}
	public String getShippingState() {
		return shippingState;
	}
	public void setShippingState(String shippingState) {
		this.shippingState = shippingState;
	}
	public List<InvoiceItem> getMyCartManual() {
		return myCartManual;
	}
	public void setMyCartManual(List<InvoiceItem> myCartManual) {
		this.myCartManual = myCartManual;
	}
	public List<InvoiceItem> getMyCart() {
		return myCart;
	}
	public void setMyCart(List<InvoiceItem> myCart) {
		this.myCart = myCart;
	}
	public long getInvoiceNo() {
		return invoiceNo;
	}
	public void setInvoiceNo(long invoiceNo) {
		this.invoiceNo = invoiceNo;
	}
	public String get_id() {
		return _id;
	}
	public void set_id(String _id) {
		this._id = _id;
	}
	public String getInvoiceDateFormatted() {
		Calendar cal = Utils.getCalender();
		Utils.setTimeZone();
		cal.setTimeInMillis(invoiceTime);
		return Utils.dateFormatDDMonYYYYhm.format(cal.getTimeInMillis());
	}
	public void setInvoiceDateFormatted(String invoiceDateFormatted) {
		//this.invoiceDateFormatted = invoiceDateFormatted;
	}
	public String getCustomerPhone() {
		if (null == customerPhone){
			customerPhone = "";
		}
		return customerPhone;
	}
	public void setCustomerPhone(String customerPhone) {
		this.customerPhone = customerPhone;
	}
	public String getCustomerEmail() {
		if (null == customerEmail){
			customerEmail = "";
		}
		return customerEmail;
	}
	public void setCustomerEmail(String customerEmail) {
		this.customerEmail = customerEmail;
	}
	public String getModeOfSale() {
		return modeOfSale;
	}
	public void setModeOfSale(String modeOfSale) {
		if (null == modeOfSale){
			modeOfSale = "OE";
		}
		this.modeOfSale = modeOfSale;
	}
	public String getEcommerceGSTN() {
		if (null == ecommerceGSTN){
			ecommerceGSTN = "";
		}
		return ecommerceGSTN;
	}
	public void setEcommerceGSTN(String ecommerceGSTN) {
		this.ecommerceGSTN = ecommerceGSTN;
	}
}
