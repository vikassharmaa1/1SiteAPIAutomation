package slots.api.response.pojo;

import java.util.Date;

public class DisplayStartTime {
	
	public Date utc;
    public Date local;
	public Date getUtc() {
		return utc;
	}
	public void setUtc(Date utc) {
		this.utc = utc;
	}
	public Date getLocal() {
		return local;
	}
	public void setLocal(Date local) {
		this.local = local;
	}
    

}
