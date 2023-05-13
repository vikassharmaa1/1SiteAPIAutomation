package slots.api.response.pojo;

import java.util.ArrayList;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Slots{
	@JsonProperty("CC") 
    public ArrayList<CC> cC;

	public ArrayList<CC> getcC() {
		return cC;
	}

	public void setcC(ArrayList<CC> cC) {
		this.cC = cC;
	}
	
	
}
