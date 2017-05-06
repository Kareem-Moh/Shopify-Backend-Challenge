package ReadFromJSON;

import java.util.ArrayList;

/**
 * @author Kareem
 * Class to represent an order placed by customer
 */
public class Order {
	//Each order contains their unique id
	private int id;
	//Customer 'id' tied to the order
	private String customer_email;
	//Determines weather completed or not (True = completed, False = not completed)
	private boolean fulfilled;
	//All products that belong to the order
	private ArrayList<Product> products;
	
	/**
	 * @param id
	 * @param customer_email
	 * @param fulfilled
	 * @param products
	 */
	public Order(Integer id, String customer_email, boolean fulfilled, ArrayList<Product> products) {
		super();
		this.id = id;
		this.customer_email = customer_email;
		this.fulfilled = fulfilled;
		this.products = products;
	}

	/**
	 * @return the id
	 */
	public int getId() {
		return id;
	}

	/**
	 * @param id the id to set
	 */
	public void setId(int id) {
		this.id = id;
	}

	/**
	 * @return the customer_email
	 */
	public String getCustomer_email() {
		return customer_email;
	}

	/**
	 * @param customer_email the customer_email to set
	 */
	public void setCustomer_email(String customer_email) {
		this.customer_email = customer_email;
	}

	/**
	 * @return the fulfilled
	 */
	public boolean isFulfilled() {
		return fulfilled;
	}

	/**
	 * @param fulfilled the fulfilled to set
	 */
	public void setFulfilled(boolean fulfilled) {
		this.fulfilled = fulfilled;
	}

	/**
	 * @return the products
	 */
	public ArrayList<Product> getProducts() {
		return products;
	}

	/**
	 * @param products the products to set
	 */
	public void setProducts(ArrayList<Product> products) {
		this.products = products;
	}
	
}