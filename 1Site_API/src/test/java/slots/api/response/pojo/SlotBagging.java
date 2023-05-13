package slots.api.response.pojo;

import java.util.ArrayList;

public class SlotBagging {

	 public ArrayList<SlotBaggingOption> slotBaggingOptions;
	    public int slotBaggingBitFlag;
		public ArrayList<SlotBaggingOption> getSlotBaggingOptions() {
			return slotBaggingOptions;
		}
		public void setSlotBaggingOptions(ArrayList<SlotBaggingOption> slotBaggingOptions) {
			this.slotBaggingOptions = slotBaggingOptions;
		}
		public int getSlotBaggingBitFlag() {
			return slotBaggingBitFlag;
		}
		public void setSlotBaggingBitFlag(int slotBaggingBitFlag) {
			this.slotBaggingBitFlag = slotBaggingBitFlag;
		}
	    
	    
}
