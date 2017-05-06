/**
 * 
 */
package ReadFromJSON;

import java.io.InputStreamReader;
import java.io.Reader;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * @author Kareem
 *
 */
public class Main {

	private static String readAll(Reader rd) throws IOException {
		StringBuilder sb = new StringBuilder();
		int cp;
		while ((cp = rd.read()) != -1) {
			sb.append((char) cp);
		}
		return sb.toString();
	}

	public static JSONObject readJsonFromUrl(String url) throws IOException, JSONException {
		InputStream is = new URL(url).openStream();
		try {
			BufferedReader rd = new BufferedReader(new InputStreamReader(is, Charset.forName("UTF-8")));
			String jsonText = readAll(rd);
			JSONObject json = new JSONObject(jsonText);
			return json;
		} finally {
			is.close();
		}
	}

	public static Order buildOrder(JSONObject j) throws JSONException {
		// Create an Order object from the json param
		Order newOrder = new Order((Integer) j.get("id"), (String) j.get("customer_email"),
				(Boolean) j.get("fulfilled"), buildProducts(j.getJSONArray("products")));
		return newOrder;
	}

	private static ArrayList<JSONObject> JSONArray_to_ArrayList(JSONArray a) throws JSONException {
		// Converts JSONArray to ArrayList for easy iteration
		ArrayList<JSONObject> jAData = new ArrayList<JSONObject>();
		if (a != null) {
			for (int i = 0; i < a.length(); i++) {
				jAData.add(a.getJSONObject(i));
			}
		}

		return jAData;
	}

	public static ArrayList<Product> buildProducts(JSONArray jA) throws JSONException {
		// Create an empty result array to store products
		ArrayList<Product> retList = new ArrayList<Product>();

		// Converts JSONArray to ArrayList for easy iteration
		ArrayList<JSONObject> jAData = JSONArray_to_ArrayList(jA);

		// Loop through json objects
		for (JSONObject obj : jAData) {
			Product newProduct = new Product((String) obj.get("title"), (Integer) obj.get("amount"));
			retList.add(newProduct);
		}
		return retList;
	}

	private static ArrayList<Order> eligeableOrders(ArrayList<Order> orders, int available_cookies){
		//Set temporary empty list of eligeable orders, will add later on
		ArrayList<Order> eligeable_orders = new ArrayList<Order>();
		//Iterate through all orders
		for (Order o : orders){
			//Check if it is not already fulfilled
			if (!o.isFulfilled()){
				//Iterate through the products of the unfulfilled order
				for (Product p : o.getProducts()){
					//Check if the products include cookies
					if (p.getTitle().equals("Cookie")){
						//Check if the cookie amount isn't higher than the initial available ones
						if (p.getAmount() <= available_cookies){
							//If all this is true, add to eligeable order list
							eligeable_orders.add(o);
						}
					}
				}
			}
		}
		//Return eligeable order list
		return eligeable_orders;
	}
	
	private static Product getCookie(ArrayList<Product> products){
		for (Product p : products){
			if (p.getTitle().equals("Cookie")){
				return p;
			}
		}
		return null;
	}
	
	public static void main(String[] args) throws IOException, JSONException {

		// Create temp non-empty arraylist to start while loop
		JSONObject obj = readJsonFromUrl(Constants.leadingJsonURL + Constants.URLPageParam);
		JSONArray orders = obj.getJSONArray("orders");

		// Create a list for orders
		ArrayList<Order> parsedOrders = new ArrayList<Order>();

		// Loop through json pages until orders on the proceeding page is empty
		int i = 1;
		while (orders.length() != 0) {
			// Retrieve json from current page
			obj = readJsonFromUrl(Constants.leadingJsonURL + Constants.URLPageParam + i);
			// set order list to ones on current page
			orders = obj.getJSONArray("orders");
			ArrayList<JSONObject> jAData = JSONArray_to_ArrayList(orders);
			for (JSONObject j : jAData) {
				parsedOrders.add(buildOrder(j));
			}
			// Set next obj as next JSON url page
			i += 1;
			obj = readJsonFromUrl(Constants.leadingJsonURL + Constants.URLPageParam + (i));
		}

		// Set the temporary return values, this will be updated
		HashMap<String, Integer> retLineOne = new HashMap<String, Integer>();
		HashMap<String, ArrayList<Integer>> retLineTwo = new HashMap<String, ArrayList<Integer>>();
		
		Integer available_cookies = new Integer((Integer) obj.get("available_cookies"));
		// Set line one as the total remaining cookies at the beginning
		retLineOne.put("remaining_cookies", available_cookies);
		
		// Set line two as all impossible orders (i.e more cookies than inventory) that are seen at first glance
		// will add more unfulfillable orders later
		ArrayList<Integer> unfulfilled_orders = new ArrayList<Integer>();
		for (Order or : parsedOrders){
			if (!or.isFulfilled()){
				for (Product p : or.getProducts()){
					if (p.getTitle().equals("Cookie")){
						if (getCookie(or.getProducts()).getAmount() > available_cookies){
							unfulfilled_orders.add(or.getId());
						}
					}
				}
			}
		}
		retLineTwo.put("unfulfilled_orders", unfulfilled_orders);
		//Compress the list of orders to only the ones that are eligeable for this algorithm
		ArrayList<Order> ordersToCheck = eligeableOrders(parsedOrders, (Integer) obj.get("available_cookies"));
		
		//Sort by cookie amount descending (level 1) and by id ascending (level 2)
		Collections.sort(ordersToCheck, new Comparator<Order>() {
			@Override
			public int compare(Order o1, Order o2) {
				if (getCookie(o1.getProducts()).getAmount() - getCookie(o2.getProducts()).getAmount() == 0){
					return (o1.getId() - o2.getId());
				}
				else {
					return (getCookie(o2.getProducts()).getAmount() - getCookie(o1.getProducts()).getAmount());
				}
			}
	    });
		
		// Now for the final step. Iterate through these last few orders to determine which cannot be fulfilled
		for (Order order : ordersToCheck){
			//Check if number of cookies in curr order is less than or equal to available cookies left
			if (getCookie(order.getProducts()).getAmount() <= available_cookies){
				//If so add to sum and decrement available cookies
				available_cookies -= getCookie(order.getProducts()).getAmount();
			}
			else {
				unfulfilled_orders.add(order.getId());
			}
		}
		
	}
}