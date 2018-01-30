package vo;

public class InvoiceItem {
	private String item;
	private String hsn;
	private String quantity;
	private String rate;
	private double taxablevalue;
	private double cgstApplied;
	private double sgstApplied;
	private double igstApplied;
	private double cessApplied;
	private double rowTotal;
	public String getItem() {
		return item;
	}
	public void setItem(String item) {
		this.item = item;
	}
	public String getHsn() {
		return hsn;
	}
	public void setHsn(String hsn) {
		this.hsn = hsn;
	}
	public String getQuantity() {
		return quantity;
	}
	public void setQuantity(String quantity) {
		this.quantity = quantity;
	}
	public String getRate() {
		return rate;
	}
	public void setRate(String rate) {
		this.rate = rate;
	}
	public double getTaxablevalue() {
		return taxablevalue;
	}
	public void setTaxablevalue(double taxablevalue) {
		this.taxablevalue = taxablevalue;
	}
	public double getCgstApplied() {
		return cgstApplied;
	}
	public void setCgstApplied(double cgstApplied) {
		this.cgstApplied = cgstApplied;
	}
	public double getSgstApplied() {
		return sgstApplied;
	}
	public void setSgstApplied(double sgstApplied) {
		this.sgstApplied = sgstApplied;
	}
	public double getIgstApplied() {
		return igstApplied;
	}
	public void setIgstApplied(double igstApplied) {
		this.igstApplied = igstApplied;
	}
	public double getCessApplied() {
		return cessApplied;
	}
	public void setCessApplied(double cessApplied) {
		this.cessApplied = cessApplied;
	}
	public double getRowTotal() {
		return rowTotal;
	}
	public void setRowTotal(double rowTotal) {
		this.rowTotal = rowTotal;
	}
	

}
