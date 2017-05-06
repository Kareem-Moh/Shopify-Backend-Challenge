/**
 * 
 */
package ReadFromJSON;

/**
 * @author Kareem
 *
 */
public class Product {
	//Name of the product (ex. 'Cookie')
	private String title;
	//Amount of product, important when part of an order
	private int amount;
	
	
	/**
	 * @param title
	 * @param unit_price
	 * @param amount
	 */
	public Product(String title, int amount) {
		super();
		this.title = title;
		this.amount = amount;
	}

	/**
	 * @return the title
	 */
	public String getTitle() {
		return title;
	}


	/**
	 * @param title the title to set
	 */
	public void setTitle(String title) {
		this.title = title;
	}


	/**
	 * @return the amount
	 */
	public int getAmount() {
		return amount;
	}


	/**
	 * @param amount the amount to set
	 */
	public void setAmount(int amount) {
		this.amount = amount;
	}
	
	
	
}