package productEnrichment.api.response.pojo;

public class MultiBuyPromoOfPartNumber {
	public Integer promoMinQty;
	public String promoDesc;
	public String promoType;
	public Integer promoReward;
	public String promoId;

	public Integer getPromoMinQty() {
		return promoMinQty;
	}

	public void setPromoMinQty(Integer promoMinQty) {
		this.promoMinQty = promoMinQty;
	}

	public String getPromoDesc() {
		return promoDesc;
	}

	public void setPromoDesc(String promoDesc) {
		this.promoDesc = promoDesc;
	}

	public String getPromoType() {
		return promoType;
	}

	public void setPromoType(String promoType) {
		this.promoType = promoType;
	}

	public Integer getPromoReward() {
		return promoReward;
	}

	public void setPromoReward(Integer promoReward) {
		this.promoReward = promoReward;
	}

	public String getPromoId() {
		return promoId;
	}

	public void setPromoId(String promoId) {
		this.promoId = promoId;
	}

}
