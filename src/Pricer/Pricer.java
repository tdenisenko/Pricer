package Pricer;

import java.util.Calendar;

public class Pricer extends Object {

	long timestamp;
	String message;
	static int NUMBER_OF_ORDERS = 0;
	String orderID;
	String side;
	double price;
	int size;

	// 5 constructors:
	// Add order with ID (6 param)
	public Pricer(long timestamp, String message, String orderID, String side,
			double price, int size) {
		this.timestamp = timestamp;
		this.message = message;
		this.orderID = orderID;
		Pricer.NUMBER_OF_ORDERS++;
		this.side = side;
		this.price = price;
		this.size = size;
	}

	// Add order with timestamp (5 param)
	public Pricer(long timestamp, String message, String side, double price,
			int size) {
		this.timestamp = timestamp;
		this.message = message;
		this.orderID = String.valueOf(100 + Pricer.NUMBER_OF_ORDERS);
		Pricer.NUMBER_OF_ORDERS++;
		this.side = side;
		this.price = price;
		this.size = size;
	}

	// Add order without timestamp (4 param)
	public Pricer(String message, String side, double price, int size) {
		Calendar c = Calendar.getInstance();
		long now = c.getTimeInMillis();
		c.set(Calendar.HOUR_OF_DAY, 0);
		c.set(Calendar.MINUTE, 0);
		c.set(Calendar.SECOND, 0);
		c.set(Calendar.MILLISECOND, 0);
		this.timestamp = now - c.getTimeInMillis();
		this.message = message;
		this.orderID = String.valueOf(100 + Pricer.NUMBER_OF_ORDERS);
		Pricer.NUMBER_OF_ORDERS++;
		this.side = side;
		this.price = price;
		this.size = size;
	}

	// Reduce order with timestamp (3 param)
	public Pricer(long timestamp, String message, String orderID, int size) {
		this.timestamp = timestamp;
		this.message = message;
		this.orderID = orderID;
		Pricer.NUMBER_OF_ORDERS++;
		this.size = size;
	}

	// Reduce order without timestamp (2 param)
	public Pricer(String message, String orderID, int size) {
		Calendar c = Calendar.getInstance();
		long now = c.getTimeInMillis();
		c.set(Calendar.HOUR_OF_DAY, 0);
		c.set(Calendar.MINUTE, 0);
		c.set(Calendar.SECOND, 0);
		c.set(Calendar.MILLISECOND, 0);
		this.timestamp = now - c.getTimeInMillis();
		this.message = message;
		this.orderID = orderID;
		Pricer.NUMBER_OF_ORDERS++;
		this.size = size;
	}

	//Overridden equals method for comparison with orders' orderID
	public boolean equals(Object p) {
		if (this.orderID == ((Pricer) p).orderID) {
			return true;
		} else {
			return false;
		}
	}

	//toString method for optimized printing.
	//Format: timestamp ID buy/sell
	public String toString() {
		String s = "";
		s += this.timestamp + "\t";
		if (this.message.equals("A") && this.side.equals("B")) {
			s += this.orderID + "\t" + this.price + "\t" + this.size;
		} else if (this.message.equals("A") && this.side.equals("S")) {
			s += this.orderID + "\t\t\t\t" + this.price + "\t" + this.size;
		}
		return s;
	}
}