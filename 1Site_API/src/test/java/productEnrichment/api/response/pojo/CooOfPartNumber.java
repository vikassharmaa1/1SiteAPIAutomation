package productEnrichment.api.response.pojo;

public class CooOfPartNumber {

	public String cooStatement;
	public String cooDescription;
	public int cooPercentage;
	public Boolean isCooLogoRequired;
	public Boolean isCooDescriptionRequired;
	public Boolean isCooBarcodeRequired;
	public String cooCountry;

	public String getCooStatement() {
		return cooStatement;
	}

	public void setCooStatement(String cooStatement) {
		this.cooStatement = cooStatement;
	}

	public String getCooDescription() {
		return cooDescription;
	}

	public void setCooDescription(String cooDescription) {
		this.cooDescription = cooDescription;
	}

	public Integer getCooPercentage() {
		return cooPercentage;
	}

	public void setCooPercentage(int cooPercentage) {
		this.cooPercentage = cooPercentage;
	}

	public Boolean isCooLogoRequired() {
		return isCooLogoRequired;
	}

	public void setCooLogoRequired(Boolean isCooLogoRequired) {
		this.isCooLogoRequired = isCooLogoRequired;
	}

	public Boolean isCooDescriptionRequired() {
		return isCooDescriptionRequired;
	}

	public void setCooDescriptionRequired(Boolean isCooDescriptionRequired) {
		this.isCooDescriptionRequired = isCooDescriptionRequired;
	}

	public Boolean isCooBarcodeRequired() {
		return isCooBarcodeRequired;
	}

	public void setCooBarcodeRequired(Boolean isCooBarcodeRequired) {
		this.isCooBarcodeRequired = isCooBarcodeRequired;
	}

	public String getCooCountry() {
		return cooCountry;
	}

	public void setCooCountry(String cooCountry) {
		this.cooCountry = cooCountry;
	}

}
