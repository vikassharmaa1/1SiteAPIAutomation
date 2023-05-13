package orderFullSummary.pojo;

public class OrderItem {


    public Number unitPrice;
    public Number itemTotal;
    public int itemSaving;
    public String orderItemId;
    public int qty;
    public String partNumber;
    public int multibuyPromoMinQty;
    public String multibuyPromoId;
    public String multibuyPromoType;
    public double multibuyPromoReward;
    public String multibuyDesc;
    
	public Number getUnitPrice() {
		return unitPrice;
	}
	public void setUnitPrice(Number unitPrice) {
		this.unitPrice = unitPrice;
	}
	public Number getItemTotal() {
		return itemTotal;
	}
	public void setItemTotal(Number itemTotal) {
		this.itemTotal = itemTotal;
	}
	public int getItemSaving() {
		return itemSaving;
	}
	public void setItemSaving(int itemSaving) {
		this.itemSaving = itemSaving;
	}
	public String getOrderItemId() {
		return orderItemId;
	}
	public void setOrderItemId(String orderItemId) {
		this.orderItemId = orderItemId;
	}
	public int getQty() {
		return qty;
	}
	public void setQty(int qty) {
		this.qty = qty;
	}
	public String getPartNumber() {
		return partNumber;
	}
	public void setPartNumber(String partNumber) {
		this.partNumber = partNumber;
	}
	public int getMultibuyPromoMinQty() {
		return multibuyPromoMinQty;
	}
	public void setMultibuyPromoMinQty(int multibuyPromoMinQty) {
		this.multibuyPromoMinQty = multibuyPromoMinQty;
	}
	public String getMultibuyPromoId() {
		return multibuyPromoId;
	}
	public void setMultibuyPromoId(String multibuyPromoId) {
		this.multibuyPromoId = multibuyPromoId;
	}
	public String getMultibuyPromoType() {
		return multibuyPromoType;
	}
	public void setMultibuyPromoType(String multibuyPromoType) {
		this.multibuyPromoType = multibuyPromoType;
	}
	public double getMultibuyPromoReward() {
		return multibuyPromoReward;
	}
	public void setMultibuyPromoReward(double multibuyPromoReward) {
		this.multibuyPromoReward = multibuyPromoReward;
	}
	public String getMultibuyDesc() {
		return multibuyDesc;
	}
	public void setMultibuyDesc(String multibuyDesc) {
		this.multibuyDesc = multibuyDesc;
	}

    
}
