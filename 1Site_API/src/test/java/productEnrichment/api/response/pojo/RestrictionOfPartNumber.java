package productEnrichment.api.response.pojo;

public class RestrictionOfPartNumber {
	public Boolean tobaccoAgeRestricted = null;
	public Boolean liquorAgeRestricted = null;

	public Boolean isTobaccoAgeRestricted() {
		return tobaccoAgeRestricted;
	}

	public void setTobaccoAgeRestricted(Boolean tobaccoAgeRestricted) {
		this.tobaccoAgeRestricted = tobaccoAgeRestricted;
	}

	public Boolean isLiquorAgeRestricted() {
		return liquorAgeRestricted;
	}

	public void setLiquorAgeRestricted(Boolean liquorAgeRestricted) {
		this.liquorAgeRestricted = liquorAgeRestricted;
	}

}
