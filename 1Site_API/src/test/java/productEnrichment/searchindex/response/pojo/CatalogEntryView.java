package productEnrichment.searchindex.response.pojo;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties (ignoreUnknown = true)
public class CatalogEntryView {

	public String unitPrice;
	public String thumbnail;
	public String coocountry_def;
	public String seoToken;
	public AttributesMap attributesMap;
	public String promo_id;
	public String singleSKUCatalogEntryID;
	public String manufacturer;
	public String promo_min_qty;
	public String promo_reward;
	public String promo_type;
	public Price price;
	public String name;
	public String promo_desc;
	public String partNumber;
	public CountryOfOrigin countryOfOrigin;
	public String tickettype;
	public String purchaseLimit;
	public String uniqueID;
	public Boolean available;
	public String shortDescription;
	public String downDate;
	public String rating;
	public String reviews;
	
	private String getRating() {
		return rating;
	}

	private void setRating(String rating) {
		this.rating = rating;
	}

	private String getReviews() {
		return reviews;
	}

	private void setReviews(String reviews) {
		this.reviews = reviews;
	}

	private Boolean getAvailable() {
		return available;
	}

	public String getDownDate() {
		return downDate;
	}

	public void setDownDate(String downDate) {
		this.downDate = downDate;
	}

	public String getShortDescription() {
		return shortDescription;
	}

	public void setShortDescription(String shortDescription) {
		this.shortDescription = shortDescription;
	}

	public Boolean isAvailable() {
		return available;
	}

	public void setAvailable(Boolean available) {
		this.available = available;
	}

	public String getUnitPrice() {
		return unitPrice;
	}

	public void setUnitPrice(String unitPrice) {
		this.unitPrice = unitPrice;
	}

	public String getThumbnail() {
		return thumbnail;
	}

	public void setThumbnail(String thumbnail) {
		this.thumbnail = thumbnail;
	}

	public String getCoocountry_def() {
		return coocountry_def;
	}

	public void setCoocountry_def(String coocountry_def) {
		this.coocountry_def = coocountry_def;
	}

	public String getSeoToken() {
		return seoToken;
	}

	public void setSeoToken(String seoToken) {
		this.seoToken = seoToken;
	}

	public AttributesMap getAttributesMap() {
		return attributesMap;
	}

	public void setAttributesMap(AttributesMap attributesMap) {
		this.attributesMap = attributesMap;
	}

	public String getPromo_id() {
		return promo_id;
	}

	public void setPromo_id(String promo_id) {
		this.promo_id = promo_id;
	}

	public String getSingleSKUCatalogEntryID() {
		return singleSKUCatalogEntryID;
	}

	public void setSingleSKUCatalogEntryID(String singleSKUCatalogEntryID) {
		this.singleSKUCatalogEntryID = singleSKUCatalogEntryID;
	}

	public String getManufacturer() {
		return manufacturer;
	}

	public void setManufacturer(String manufacturer) {
		this.manufacturer = manufacturer;
	}

	public String getPromo_min_qty() {
		return promo_min_qty;
	}

	public void setPromo_min_qty(String promo_min_qty) {
		this.promo_min_qty = promo_min_qty;
	}

	public String getPromo_reward() {
		return promo_reward;
	}

	public void setPromo_reward(String promo_reward) {
		this.promo_reward = promo_reward;
	}

	public String getPromo_type() {
		return promo_type;
	}

	public void setPromo_type(String promo_type) {
		this.promo_type = promo_type;
	}

	public Price getPrice() {
		return price;
	}

	public void setPrice(Price price) {
		this.price = price;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getPromo_desc() {
		return promo_desc;
	}

	public void setPromo_desc(String promo_desc) {
		this.promo_desc = promo_desc;
	}

	public String getPartNumber() {
		return partNumber;
	}

	public void setPartNumber(String partNumber) {
		this.partNumber = partNumber;
	}

	public CountryOfOrigin getCountryOfOrigin() {
		return countryOfOrigin;
	}

	public void setCountryOfOrigin(CountryOfOrigin countryOfOrigin) {
		this.countryOfOrigin = countryOfOrigin;
	}

	public String getTickettype() {
		return tickettype;
	}

	public void setTickettype(String tickettype) {
		this.tickettype = tickettype;
	}

	public String getPurchaseLimit() {
		return purchaseLimit;
	}

	public void setPurchaseLimit(String purchaseLimit) {
		this.purchaseLimit = purchaseLimit;
	}

	public String getUniqueID() {
		return uniqueID;
	}

	public void setUniqueID(String uniqueID) {
		this.uniqueID = uniqueID;
	}

}
