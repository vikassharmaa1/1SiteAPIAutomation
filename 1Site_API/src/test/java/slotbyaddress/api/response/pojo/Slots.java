package slotbyaddress.api.response.pojo;

import java.util.ArrayList;
import com.fasterxml.jackson.annotation.JsonProperty;

public class Slots{
    @JsonProperty("HD") 
    public ArrayList<HD> hD;

	public ArrayList<HD> gethD() {
		return hD;
	}

	public void sethD(ArrayList<HD> hD) {
		this.hD = hD;
	}
}
