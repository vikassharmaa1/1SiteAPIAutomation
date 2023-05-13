package ps.orderSummary.api.tests;

import java.sql.SQLException;
import java.util.Objects;

import org.json.JSONArray;
import org.json.JSONObject;
import org.testng.ITest;
import org.testng.annotations.Test;

import com.rest.main.Base_Class_API;
import com.rest.main.FunLibrary;
import com.rest.main.RestLibrary;
import com.rest.utilities.DatabaseUtilities;

import io.restassured.response.Response;

public class PS_orderSummary extends Base_Class_API implements ITest {

	@Test(description = "Validate freeDelivery flag & Order Adjustment disc. value matches with db value")
	public void validateOrdDiscAndfreeDelivery() throws SQLException {
		RestLibrary restLibrary = new RestLibrary(Base_Class_API.BaseURI, FunLibrary.excelData.get("BasePath"),
				FunLibrary.excelData.get("RequestType"));
		restLibrary.addHeader(FunLibrary.excelData.get("Header1_Key"), FunLibrary.excelData.get("Header1_Value"));
		restLibrary.addHeader(FunLibrary.excelData.get("Header2_Key"), FunLibrary.excelData.get("Header2_Value"));
		restLibrary.addQueryParameter(FunLibrary.excelData.get("QueryParameterKey1"),
				FunLibrary.excelData.get("QueryParameterValue1"));
		restLibrary.addQueryParameter(FunLibrary.excelData.get("QueryParameterKey2"),
				FunLibrary.excelData.get("QueryParameterValue2"));
		Response response = restLibrary.executeAPI();
		restLibrary.getResponseBody(response);
		funLibrary.validateStatusCode(response, FunLibrary.excelData.get("StatusCode"));
		String order_id = response.jsonPath().getString("orderId");
		//getOrderAdjustments(response, "adjustments.orderAdjustments");
		if (response.jsonPath().getString("orderDiscount").equals("0")) {
			System.out.println("Order is ineligible for OrderDiscount Validation");
		} else {

			String promo_id = funLibrary.getJsonPathValue(response, "adjustments.otherAdjustments[0].promotionId");
			funLibrary.validateJSONPathValue_Equals(response, "adjustments.otherAdjustments[0].amount",
					getOrderDisc(order_id, promo_id));
		}
		
		String promotion_id = funLibrary.getJsonPathValue(response, "adjustments.otherAdjustments[0].promotionId");
		String freeDelivery = String.valueOf(isFreeDelivery(order_id, promotion_id));
		funLibrary.validateJSONPathValue_Equals(response, "freeDelivery", freeDelivery);
		funLibrary.Assert.assertAll();
	}

	public String getOrderAdjustments(Response response, String jsonPath) {

		JSONObject json = new JSONObject(response);
		JSONArray orderAdjustmentsArray = json.getJSONArray(jsonPath);
		if (orderAdjustmentsArray.length() > 0)
			for (int i = 0; i < orderAdjustmentsArray.length(); i++) {
				JSONObject objects = orderAdjustmentsArray.getJSONObject(i);
				objects.getString("orderAdjustments");
			}
		else {
			System.out.println("There are no OrderAdjustments for this order.");
		}

		return jsonPath;

	}

	public String getOrderDisc(String orderid, String promo_id) {
		DatabaseUtilities dbUtil = new DatabaseUtilities();
		String tableName = "orders o, ordadjust oa" + " LEFT JOIN"
				+ " calcodedsc ccd ON oa.calcode_id = ccd.calcode_id, calcode cc, clcdpromo ccp, px_promotion p, px_element pe, px_group pg";
		String condition = "oa.orders_id = '" + orderid + "'"
				+ " AND o.member_id in (select member_id from orders where orders_id='" + orderid + "')"
				+ " AND p.px_promotion_id ='" + promo_id + "' AND o.orders_id = oa.orders_id"
				+ " AND oa.calcode_id = cc.calcode_id" + " AND oa.calcode_id = ccp.calcode_id"
				+ " AND ccp.px_promotion_id = p.px_promotion_id" + " AND ccp.px_promotion_id = pe.px_promotion_id"
				+ " AND p.px_group_id = pg.px_group_id" + " AND pe.type =" + "'" + "PurchaseCondition" + "'";

		String value = dbUtil.getValues(tableName, "oa.AMOUNT", condition);
		// System.out.println("My Value= " + value);
		if (value.equalsIgnoreCase("No Record Found")) {
			value = "";
		}
		return value;

	}

	@Test(description = "Validate FreeDelivery")
	public boolean isFreeDelivery(String order_id, String promotion_id) throws SQLException {

		boolean freeDelivery = false;
		String deliveryFee = getDeliveryFee(order_id);
		String shippingFee = getShippingFee(order_id, promotion_id);

		if (deliveryFee.equals("0")) {
			freeDelivery = true;
		} else if (!deliveryFee.equals("0")) {
			if (Integer.parseInt(deliveryFee.replaceAll("\\..*", ""))
					+ Integer.parseInt(shippingFee.replaceAll("\\..*", "")) == 0) {

				freeDelivery = true;
			} else {
				freeDelivery = false;
			}
		}
		return freeDelivery;
	}

	public String getDeliveryFee(String orderid) {
		DatabaseUtilities dbUtil = new DatabaseUtilities();
		String value = dbUtil.getValues("orders", "TOTALSHIPPING", "orders_id='" + orderid + "'");
		// System.out.println("My Value= " + value);
		if (value.equalsIgnoreCase("No Record Found")) {
			value = "";
		}
		return value;

	}

	public String getShippingFee(String orderid, String promo_id) {
		DatabaseUtilities dbUtil = new DatabaseUtilities();
		String tableName = "orders o, ordadjust oa" + " LEFT JOIN"
				+ " calcodedsc ccd ON oa.calcode_id = ccd.calcode_id, calcode cc, clcdpromo ccp, px_promotion p, px_element pe, px_group pg";
		String condition = "oa.orders_id = '" + orderid + "'"
				+ " AND o.member_id in (select member_id from orders where orders_id='" + orderid + "')"
				+ " AND p.px_promotion_id ='" + promo_id + "' AND o.orders_id = oa.orders_id"
				+ " AND oa.calcode_id = cc.calcode_id" + " AND oa.calcode_id = ccp.calcode_id"
				+ " AND ccp.px_promotion_id = p.px_promotion_id" + " AND ccp.px_promotion_id = pe.px_promotion_id"
				+ " AND p.px_group_id = pg.px_group_id" + " AND pe.type =" + "'" + "PurchaseCondition" + "'";
		String value = dbUtil.getValues(tableName, "oa.AMOUNT", condition);
		// System.out.println("My Value= " + value);
		if (value.equalsIgnoreCase("No Record Found")) {
			value = "";
		}
		return value;

	}

	@Override
	public String getTestName() {
		return testName.get();
	}

}
