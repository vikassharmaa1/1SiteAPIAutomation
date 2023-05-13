package productEnrichment.api.response.pojo;

public class PartNumber {

	public AttributesOfPartNumber attributesOfPartNumber;
	public TicketTypeOfPartNumber ticketTypeOfPartNumber;
	public PricesOfPartNumber pricesOfPartNumber;
	public RedemptionsOfPartNumber redemptionsOfPartNumber;
	public String partNumber;
	public Boolean temporarilyUnavailable;
	public PriceDisplayOfPartNumber priceDisplayOfPartNumber;
	public CooOfPartNumber cooOfPartNumber;
	public RestrictionOfPartNumber restrictionOfPartNumber;
	public MultiBuyPromoOfPartNumber multiBuyPromoOfPartNumber;

	public TicketTypeOfPartNumber getTicketTypeOfPartNumber() {
		return ticketTypeOfPartNumber;
	}

	public void setTicketTypeOfPartNumber(TicketTypeOfPartNumber ticketTypeOfPartNumber) {
		this.ticketTypeOfPartNumber = ticketTypeOfPartNumber;
	}

	public MultiBuyPromoOfPartNumber getMultiBuyPromoOfPartNumber() {
		return multiBuyPromoOfPartNumber;
	}

	public void setMultiBuyPromoOfPartNumber(MultiBuyPromoOfPartNumber multiBuyPromoOfPartNumber) {
		this.multiBuyPromoOfPartNumber = multiBuyPromoOfPartNumber;
	}

	public AttributesOfPartNumber getAttributesOfPartNumber() {
		return attributesOfPartNumber;
	}

	public void setAttributesOfPartNumber(AttributesOfPartNumber attributesOfPartNumber) {
		this.attributesOfPartNumber = attributesOfPartNumber;
	}

	public PricesOfPartNumber getPricesOfPartNumber() {
		return pricesOfPartNumber;
	}

	public void setPricesOfPartNumber(PricesOfPartNumber pricesOfPartNumber) {
		this.pricesOfPartNumber = pricesOfPartNumber;
	}

	public RedemptionsOfPartNumber getRedemptionsOfPartNumber() {
		return redemptionsOfPartNumber;
	}

	public void setRedemptionsOfPartNumber(RedemptionsOfPartNumber redemptionsOfPartNumber) {
		this.redemptionsOfPartNumber = redemptionsOfPartNumber;
	}

	public String getPartNumber() {
		return partNumber;
	}

	public void setPartNumber(String partNumber) {
		this.partNumber = partNumber;
	}

	public Boolean isTemporarilyUnavailable() {
		return temporarilyUnavailable;
	}

	public void setTemporarilyUnavailable(Boolean temporarilyUnavailable) {
		this.temporarilyUnavailable = temporarilyUnavailable;
	}

	public PriceDisplayOfPartNumber getPriceDisplayOfPartNumber() {
		return priceDisplayOfPartNumber;
	}

	public void setPriceDisplayOfPartNumber(PriceDisplayOfPartNumber priceDisplayOfPartNumber) {
		this.priceDisplayOfPartNumber = priceDisplayOfPartNumber;
	}

	public CooOfPartNumber getCooOfPartNumber() {
		return cooOfPartNumber;
	}

	public void setCooOfPartNumber(CooOfPartNumber cooOfPartNumber) {
		this.cooOfPartNumber = cooOfPartNumber;
	}

	public RestrictionOfPartNumber getRestrictionOfPartNumber() {
		return restrictionOfPartNumber;
	}

	public void setRestrictionOfPartNumber(RestrictionOfPartNumber restrictionOfPartNumber) {
		this.restrictionOfPartNumber = restrictionOfPartNumber;
	}

}
