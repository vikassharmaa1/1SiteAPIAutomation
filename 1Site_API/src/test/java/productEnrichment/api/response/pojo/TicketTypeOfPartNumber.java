package productEnrichment.api.response.pojo;

import com.fasterxml.jackson.annotation.JsonProperty;

public class TicketTypeOfPartNumber {

	public String graphicCode;
	public int onlinePromo;
	public String ticketType;
	@JsonProperty("1stShout")
	public String _1stShout;
	@JsonProperty("2ndShout")
	public String _2ndShout;
	
	public String get_2ndShout() {
		return _2ndShout;
	}

	public void set_2ndShout(String _2ndShout) {
		this._2ndShout = _2ndShout;
	}

	public String getGraphicCode() {
		return graphicCode;
	}

	public void setGraphicCode(String graphicCode) {
		this.graphicCode = graphicCode;
	}

	public int getOnlinePromo() {
		return onlinePromo;
	}

	public void setOnlinePromo(int onlinePromo) {
		this.onlinePromo = onlinePromo;
	}

	public String getTicketType() {
		return ticketType;
	}

	public void setTicketType(String ticketType) {
		this.ticketType = ticketType;
	}

	public String get_1stShout() {
		return _1stShout;
	}

	public void set_1stShout(String _1stShout) {
		this._1stShout = _1stShout;
	}

}
