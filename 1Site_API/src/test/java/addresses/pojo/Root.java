package addresses.pojo;

import java.util.List;

public class Root {
	
	 public List<AddressData> data;
	 
	 public Links links;

	public List<AddressData> getAddresses() {
		return data;
	}

	public void setAddresses(List<AddressData> data) {
		this.data = data;
	}

}
