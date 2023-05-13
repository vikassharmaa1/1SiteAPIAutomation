package slotbyaddress.api.response.pojo;

import com.fasterxml.jackson.annotation.JsonProperty;

public class SlotBaggingOption{
	@JsonProperty("BAGNAME") 
    public Object bAGNAME;
    @JsonProperty("BITVALUE") 
    public int bITVALUE;
    @JsonProperty("HEADERDISPLAY") 
    public String hEADERDISPLAY;
    @JsonProperty("DESCRIPTION") 
    public String dESCRIPTION;
    @JsonProperty("SUPPORTTEXT") 
    public Object sUPPORTTEXT;
    @JsonProperty("UNITPRICE") 
    public double uNITPRICE;
    @JsonProperty("MAXWEIGHT") 
    public double mAXWEIGHT;
    @JsonProperty("PARTNUMBER") 
    public String pARTNUMBER;
    public boolean isFlatFee;
    public String relationalSKU;
    public String relationalBagType;
    
	public Object getbAGNAME() {
		return bAGNAME;
	}
	public void setbAGNAME(Object bAGNAME) {
		this.bAGNAME = bAGNAME;
	}
	public int getbITVALUE() {
		return bITVALUE;
	}
	public void setbITVALUE(int bITVALUE) {
		this.bITVALUE = bITVALUE;
	}
	public String gethEADERDISPLAY() {
		return hEADERDISPLAY;
	}
	public void sethEADERDISPLAY(String hEADERDISPLAY) {
		this.hEADERDISPLAY = hEADERDISPLAY;
	}
	public String getdESCRIPTION() {
		return dESCRIPTION;
	}
	public void setdESCRIPTION(String dESCRIPTION) {
		this.dESCRIPTION = dESCRIPTION;
	}
	public Object getsUPPORTTEXT() {
		return sUPPORTTEXT;
	}
	public void setsUPPORTTEXT(Object sUPPORTTEXT) {
		this.sUPPORTTEXT = sUPPORTTEXT;
	}
	public double getuNITPRICE() {
		return uNITPRICE;
	}
	public void setuNITPRICE(double uNITPRICE) {
		this.uNITPRICE = uNITPRICE;
	}
	public double getmAXWEIGHT() {
		return mAXWEIGHT;
	}
	public void setmAXWEIGHT(double mAXWEIGHT) {
		this.mAXWEIGHT = mAXWEIGHT;
	}
	public String getpARTNUMBER() {
		return pARTNUMBER;
	}
	public void setpARTNUMBER(String pARTNUMBER) {
		this.pARTNUMBER = pARTNUMBER;
	}
	public boolean isFlatFee() {
		return isFlatFee;
	}
	public void setFlatFee(boolean isFlatFee) {
		this.isFlatFee = isFlatFee;
	}
	public String getRelationalSKU() {
		return relationalSKU;
	}
	public void setRelationalSKU(String relationalSKU) {
		this.relationalSKU = relationalSKU;
	}
	public String getRelationalBagType() {
		return relationalBagType;
	}
	public void setRelationalBagType(String relationalBagType) {
		this.relationalBagType = relationalBagType;
	}
}