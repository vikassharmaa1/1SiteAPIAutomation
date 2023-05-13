package productEnrichment.searchindex.response.pojo;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties (ignoreUnknown = true)
public class SearchIndexRoot {

	public Integer recordSetCount;
	public MetaData metaData;
	public List<CatalogEntryView> catalogEntryView;
	public Integer recordSetStartNumber;
	public CategoryView categoryView;
	public Integer recordSetTotal;
	public List<Object> facetView;

	public int getRecordSetCount() {
		return recordSetCount;
	}

	public void setRecordSetCount(int recordSetCount) {
		this.recordSetCount = recordSetCount;
	}

	public MetaData getMetaData() {
		return metaData;
	}

	public void setMetaData(MetaData metaData) {
		this.metaData = metaData;
	}

	public List<CatalogEntryView> getCatalogEntryView() {
		return catalogEntryView;
	}

	public void setCatalogEntryView(List<CatalogEntryView> catalogEntryView) {
		this.catalogEntryView = catalogEntryView;
	}

	public int getRecordSetStartNumber() {
		return recordSetStartNumber;
	}

	public void setRecordSetStartNumber(int recordSetStartNumber) {
		this.recordSetStartNumber = recordSetStartNumber;
	}

	public CategoryView getCategoryView() {
		return categoryView;
	}

	public void setCategoryView(CategoryView categoryView) {
		this.categoryView = categoryView;
	}

	public int getRecordSetTotal() {
		return recordSetTotal;
	}

	public void setRecordSetTotal(int recordSetTotal) {
		this.recordSetTotal = recordSetTotal;
	}

	public List<Object> getFacetView() {
		return facetView;
	}

	public void setFacetView(List<Object> facetView) {
		this.facetView = facetView;
	}

}
