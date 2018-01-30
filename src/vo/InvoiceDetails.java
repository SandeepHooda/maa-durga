package vo;

import java.util.List;

public class InvoiceDetails {
	private String _id;
	private long invoiceTime;
	private String customerName;
	private String shippingAddress;
	private String shippingState;
	private List<InvoiceItem> myCartManual;
	private List<InvoiceItem>  myCart;
	private long invoiceNo;
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
}
