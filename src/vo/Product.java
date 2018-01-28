package vo;

public class Product {
	private String hsn;
	private int cgst;// center gst rate
	private int sgst;// State Gst rate
	private int igst;//integrated gst rate
	private int cess;//addition cess
	private String desc;//Product description
	public String getHsn() {
		return hsn;
	}
	public void setHsn(String hsn) {
		this.hsn = hsn;
	}
	public int getCgst() {
		return cgst;
	}
	public void setCgst(int cgst) {
		this.cgst = cgst;
	}
	public int getSgst() {
		return sgst;
	}
	public void setSgst(int sgst) {
		this.sgst = sgst;
	}
	public int getIgst() {
		return igst;
	}
	public void setIgst(int igst) {
		this.igst = igst;
	}
	public int getCess() {
		return cess;
	}
	public void setCess(int cess) {
		this.cess = cess;
	}
	public String getDesc() {
		return desc;
	}
	public void setDesc(String desc) {
		this.desc = desc;
	}
	

}
