package vo;

public class Inventory {
	private String hsn;
	private String desc;
	private String modelNo;
	private int purchasePriceWithoutTax;
	private int salesPriceAmazonWithoutTax;
	private int salesPriceRegularWithoutTax;
	public String getHsn() {
		return hsn;
	}
	public void setHsn(String hsn) {
		this.hsn = hsn;
	}
	public String getDesc() {
		return desc;
	}
	public void setDesc(String desc) {
		this.desc = desc;
	}
	public String getModelNo() {
		return modelNo;
	}
	public void setModelNo(String modelNo) {
		this.modelNo = modelNo;
	}
	public int getPurchasePriceWithoutTax() {
		return purchasePriceWithoutTax;
	}
	public void setPurchasePriceWithoutTax(int purchasePriceWithoutTax) {
		this.purchasePriceWithoutTax = purchasePriceWithoutTax;
	}
	public int getSalesPriceAmazonWithoutTax() {
		return salesPriceAmazonWithoutTax;
	}
	public void setSalesPriceAmazonWithoutTax(int salesPriceAmazonWithoutTax) {
		this.salesPriceAmazonWithoutTax = salesPriceAmazonWithoutTax;
	}
	public int getSalesPriceRegularWithoutTax() {
		return salesPriceRegularWithoutTax;
	}
	public void setSalesPriceRegularWithoutTax(int salesPriceRegularWithoutTax) {
		this.salesPriceRegularWithoutTax = salesPriceRegularWithoutTax;
	}

}
