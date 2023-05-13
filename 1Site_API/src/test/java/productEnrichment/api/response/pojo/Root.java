package productEnrichment.api.response.pojo;

import java.util.List;

public class Root {

	public List<PartNumber> partNumbers;
	public int totalCount;

	public List<PartNumber> getPartNumbers() {
		return partNumbers;
	}

	public void setPartNumbers(List<PartNumber> partNumbers) {
		this.partNumbers = partNumbers;
	}

	public int getTotalCount() {
		return totalCount;
	}

	public void setTotalCount(int totalCount) {
		this.totalCount = totalCount;
	}

}
