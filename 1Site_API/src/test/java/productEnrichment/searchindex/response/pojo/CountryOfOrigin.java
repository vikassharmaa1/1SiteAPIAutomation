package productEnrichment.searchindex.response.pojo;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties (ignoreUnknown = true)
public class CountryOfOrigin {
	public String cooallystmt;
	public String coopercentage;
	public String coostdsind;
	public String coologorq;
	public String cooref_id;
	public String coostmt;
	public String coobcrq;
	public String coocountry;

	public String getCooallystmt() {
		return cooallystmt;
	}

	public void setCooallystmt(String cooallystmt) {
		this.cooallystmt = cooallystmt;
	}

	public String getCoopercentage() {
		return coopercentage;
	}

	public void setCoopercentage(String coopercentage) {
		this.coopercentage = coopercentage;
	}

	public String getCoostdsind() {
		return coostdsind;
	}

	public void setCoostdsind(String coostdsind) {
		this.coostdsind = coostdsind;
	}

	public String getCoologorq() {
		return coologorq;
	}

	public void setCoologorq(String coologorq) {
		this.coologorq = coologorq;
	}

	public String getCooref_id() {
		return cooref_id;
	}

	public void setCooref_id(String cooref_id) {
		this.cooref_id = cooref_id;
	}

	public String getCoostmt() {
		return coostmt;
	}

	public void setCoostmt(String coostmt) {
		this.coostmt = coostmt;
	}

	public String getCoobcrq() {
		return coobcrq;
	}

	public void setCoobcrq(String coobcrq) {
		this.coobcrq = coobcrq;
	}

	public String getCoocountry() {
		return coocountry;
	}

	public void setCoocountry(String coocountry) {
		this.coocountry = coocountry;
	}

}
