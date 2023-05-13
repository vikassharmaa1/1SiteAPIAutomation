package slots.api.response.pojo;


public class Root {
	
	public Slots slots;
    public SlotBagging slotBagging;
    public boolean isColesPlusMember;
	public Slots getSlots() {
		return slots;
	}
	public void setSlots(Slots slots) {
		this.slots = slots;
	}
	public SlotBagging getSlotBagging() {
		return slotBagging;
	}
	public void setSlotBagging(SlotBagging slotBagging) {
		this.slotBagging = slotBagging;
	}
	public boolean isColesPlusMember() {
		return isColesPlusMember;
	}
	public void setColesPlusMember(boolean isColesPlusMember) {
		this.isColesPlusMember = isColesPlusMember;
	}
	

}
