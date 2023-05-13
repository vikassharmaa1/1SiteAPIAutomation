package addresses.pojo;


import java.util.Date;
import java.util.List;

public class AddressData {

	 public String id;
	    public String nickname;
	    public String recipientFirstName;
	    public String recipientLastName;
	    public String addressType;
	    public String addressLine1;
	    public String addressLine2;
	    public String addressLine3;
	    public String postcode;
	    public String city;
	    public String state;
	    public String country;
	    public Location location;
	    public String verificationId;
	    public boolean verificationStatus;
	    public String customerNotes;
	    public List<Contact> contacts;
	    public Date created;
	    public Date modified;
		public String getId() {
			return id;
		}
		public void setId(String id) {
			this.id = id;
		}
		public String getNickname() {
			return nickname;
		}
		public void setNickname(String nickname) {
			this.nickname = nickname;
		}
		public String getRecipientFirstName() {
			return recipientFirstName;
		}
		public void setRecipientFirstName(String recipientFirstName) {
			this.recipientFirstName = recipientFirstName;
		}
		public String getRecipientLastName() {
			return recipientLastName;
		}
		public void setRecipientLastName(String recipientLastName) {
			this.recipientLastName = recipientLastName;
		}
		public String getAddressType() {
			return addressType;
		}
		public void setAddressType(String addressType) {
			this.addressType = addressType;
		}
		public String getAddressLine1() {
			return addressLine1;
		}
		public void setAddressLine1(String addressLine1) {
			this.addressLine1 = addressLine1;
		}
		public String getAddressLine2() {
			return addressLine2;
		}
		public void setAddressLine2(String addressLine2) {
			this.addressLine2 = addressLine2;
		}
		public String getAddressLine3() {
			return addressLine3;
		}
		public void setAddressLine3(String addressLine3) {
			this.addressLine3 = addressLine3;
		}
		public String getPostcode() {
			return postcode;
		}
		public void setPostcode(String postcode) {
			this.postcode = postcode;
		}
		public String getCity() {
			return city;
		}
		public void setCity(String city) {
			this.city = city;
		}
		public String getState() {
			return state;
		}
		public void setState(String state) {
			this.state = state;
		}
		public String getCountry() {
			return country;
		}
		public void setCountry(String country) {
			this.country = country;
		}
		public Location getLocation() {
			return location;
		}
		public void setLocation(Location location) {
			this.location = location;
		}
		public String getVerificationId() {
			return verificationId;
		}
		public void setVerificationId(String verificationId) {
			this.verificationId = verificationId;
		}
		public boolean isVerificationStatus() {
			return verificationStatus;
		}
		public void setVerificationStatus(boolean verificationStatus) {
			this.verificationStatus = verificationStatus;
		}
		public String getCustomerNotes() {
			return customerNotes;
		}
		public void setCustomerNotes(String customerNotes) {
			this.customerNotes = customerNotes;
		}
		public List<Contact> getContacts() {
			return contacts;
		}
		public void setContacts(List<Contact> contacts) {
			this.contacts = contacts;
		}
		public Date getCreated() {
			return created;
		}
		public void setCreated(Date created) {
			this.created = created;
		}
		public Date getModified() {
			return modified;
		}
		public void setModified(Date modified) {
			this.modified = modified;
		}
	    
}
