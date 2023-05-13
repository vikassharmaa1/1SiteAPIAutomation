package slotbyaddress.api.response.pojo;


public class Root{
    public Slots slots;
    public boolean isColesPlusMember;
    public SlotBagging slotBagging;
	public Slots getSlots() {
		return slots;
	}
	public void setSlots(Slots slots) {
		this.slots = slots;
	}
	public boolean isColesPlusMember() {
		return isColesPlusMember;
	}
	public void setColesPlusMember(boolean isColesPlusMember) {
		this.isColesPlusMember = isColesPlusMember;
	}
	public SlotBagging getSlotBagging() {
		return slotBagging;
	}
	public void setSlotBagging(SlotBagging slotBagging) {
		this.slotBagging = slotBagging;
	}
}